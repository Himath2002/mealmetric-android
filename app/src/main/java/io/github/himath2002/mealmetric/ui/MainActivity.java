package io.github.himath2002.mealmetric.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.github.himath2002.mealmetric.BuildConfig;
import io.github.himath2002.mealmetric.R;
import io.github.himath2002.mealmetric.data.remote.NutritionApi;
import io.github.himath2002.mealmetric.data.remote.NutritionClient;
import io.github.himath2002.mealmetric.data.remote.NutritionSearchRequest;
import io.github.himath2002.mealmetric.data.remote.NutritionSearchResponse;
import io.github.himath2002.mealmetric.databinding.ActivityMainBinding;
import io.github.himath2002.mealmetric.databinding.DialogMealBinding;
import io.github.himath2002.mealmetric.model.Meal;
import io.github.himath2002.mealmetric.model.NutritionEstimate;
import io.github.himath2002.mealmetric.viewmodel.MealViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Hosts MealMetric's single-screen journal and coordinates user-initiated UI flows.
 *
 * <p>Persistent state remains behind {@link MealViewModel}; this activity only owns
 * short-lived presentation state such as an open meal dialog or in-flight search.</p>
 */
public final class MainActivity extends AppCompatActivity {

    private static final String TAG = "MealMetric";
    private static final String DATABASE_DATE_PATTERN = "yyyy-MM-dd";

    private ActivityMainBinding binding;
    private MealViewModel mealViewModel;
    private MealAdapter mealAdapter;
    private ActivityResultLauncher<String[]> photoPickerLauncher;
    private DialogMealBinding activeMealDialogBinding;
    private Uri selectedMealPhoto;
    private Call<NutritionSearchResponse> activeSearchCall;
    private String currentDateKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureSystemBars();
        registerPhotoPicker();
        configureMealList();
        configureHeader();
        configureActions();
        observeTodayMeals();
    }

    private void configureSystemBars() {
        int initialLeft = binding.getRoot().getPaddingLeft();
        int initialTop = binding.getRoot().getPaddingTop();
        int initialRight = binding.getRoot().getPaddingRight();
        int initialBottom = binding.getRoot().getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (view, windowInsets) -> {
            Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(
                    initialLeft + systemBars.left,
                    initialTop + systemBars.top,
                    initialRight + systemBars.right,
                    initialBottom + systemBars.bottom
            );
            return windowInsets;
        });
        ViewCompat.requestApplyInsets(binding.getRoot());
    }

    private void registerPhotoPicker() {
        photoPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                this::handleSelectedPhoto
        );
    }

    private void configureMealList() {
        mealAdapter = new MealAdapter();
        binding.mealList.setLayoutManager(new LinearLayoutManager(this));
        binding.mealList.setAdapter(mealAdapter);
        binding.mealList.setNestedScrollingEnabled(false);
    }

    private void configureHeader() {
        Date now = new Date();
        currentDateKey = new SimpleDateFormat(DATABASE_DATE_PATTERN, Locale.US).format(now);
        binding.currentDateText.setText(
                DateFormat.getDateInstance(DateFormat.FULL).format(now)
        );
    }

    private void configureActions() {
        binding.addMealButton.setOnClickListener(view -> showMealDialog(null));
        binding.searchButton.setOnClickListener(view -> startFoodSearch());
        binding.foodSearchInput.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                startFoodSearch();
                return true;
            }
            return false;
        });
    }

    private void observeTodayMeals() {
        mealViewModel = new ViewModelProvider(this).get(MealViewModel.class);
        mealViewModel.observeMealsForDate(currentDateKey).observe(this, meals -> {
            List<Meal> safeMeals = meals == null ? Collections.emptyList() : meals;
            mealAdapter.submitList(new ArrayList<>(safeMeals));
            updateDailySummary(safeMeals);
        });
    }

    private void updateDailySummary(List<Meal> meals) {
        int totalCalories = 0;
        for (Meal meal : meals) {
            totalCalories += meal.getCalories();
        }

        binding.totalCaloriesText.setText(
                getString(R.string.calorie_total_value, totalCalories)
        );
        binding.mealCountText.setText(
                getResources().getQuantityString(R.plurals.meal_count, meals.size(), meals.size())
        );

        boolean isEmpty = meals.isEmpty();
        binding.emptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        binding.mealList.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    private void startFoodSearch() {
        String query = binding.foodSearchInput.getText() == null
                ? ""
                : binding.foodSearchInput.getText().toString().trim();

        if (query.isEmpty()) {
            binding.foodSearchLayout.setError(getString(R.string.food_search_required));
            return;
        }
        binding.foodSearchLayout.setError(null);

        if (!isNutritionSearchConfigured()) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.search_configuration_title)
                    .setMessage(R.string.search_configuration_message)
                    .setPositiveButton(R.string.action_ok, null)
                    .show();
            return;
        }

        setSearchInProgress(true);
        NutritionApi api = NutritionClient.createApi();
        activeSearchCall = api.searchFood(
                BuildConfig.NUTRITIONIX_APP_ID,
                BuildConfig.NUTRITIONIX_APP_KEY,
                new NutritionSearchRequest(query)
        );
        activeSearchCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<NutritionSearchResponse> call,
                    @NonNull Response<NutritionSearchResponse> response
            ) {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                activeSearchCall = null;
                setSearchInProgress(false);
                NutritionSearchResponse body = response.body();
                if (!response.isSuccessful() || body == null || body.getFoods() == null) {
                    showSearchFailure(response.code());
                    return;
                }

                List<NutritionEstimate> estimates = new ArrayList<>();
                for (NutritionSearchResponse.Food food : body.getFoods()) {
                    estimates.add(food.toEstimate());
                }

                if (estimates.isEmpty()) {
                    Toast.makeText(
                            MainActivity.this,
                            R.string.food_search_empty,
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                showFoodSelectionDialog(estimates);
            }

            @Override
            public void onFailure(
                    @NonNull Call<NutritionSearchResponse> call,
                    @NonNull Throwable throwable
            ) {
                if (call.isCanceled() || isFinishing() || isDestroyed()) {
                    return;
                }
                activeSearchCall = null;
                setSearchInProgress(false);
                Log.e(TAG, "Nutrition search failed", throwable);
                Toast.makeText(
                        MainActivity.this,
                        R.string.food_search_network_error,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private boolean isNutritionSearchConfigured() {
        return !BuildConfig.NUTRITIONIX_APP_ID.trim().isEmpty()
                && !BuildConfig.NUTRITIONIX_APP_KEY.trim().isEmpty();
    }

    private void showSearchFailure(int statusCode) {
        int message = statusCode == 401 || statusCode == 403
                ? R.string.food_search_credentials_error
                : R.string.food_search_service_error;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void setSearchInProgress(boolean inProgress) {
        binding.searchProgress.setVisibility(inProgress ? View.VISIBLE : View.GONE);
        binding.searchButton.setEnabled(!inProgress);
        binding.foodSearchInput.setEnabled(!inProgress);
    }

    private void showFoodSelectionDialog(List<NutritionEstimate> estimates) {
        CharSequence[] options = new CharSequence[estimates.size()];
        NumberFormat numberFormat = NumberFormat.getNumberInstance();

        for (int index = 0; index < estimates.size(); index++) {
            NutritionEstimate estimate = estimates.get(index);
            options[index] = getString(
                    R.string.food_search_result,
                    estimate.getName(),
                    numberFormat.format(estimate.getServingQuantity()),
                    estimate.getServingUnit(),
                    Math.round(estimate.getCalories())
            );
        }

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.food_search_results_title)
                .setItems(options, (dialog, selectedIndex) ->
                        showMealDialog(estimates.get(selectedIndex)))
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }

    private void showMealDialog(@Nullable NutritionEstimate suggestedFood) {
        DialogMealBinding dialogBinding = DialogMealBinding.inflate(getLayoutInflater());
        activeMealDialogBinding = dialogBinding;
        selectedMealPhoto = null;

        ArrayAdapter<String> mealTypeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.meal_types)
        );
        dialogBinding.mealTypeInput.setAdapter(mealTypeAdapter);
        dialogBinding.mealTypeInput.setText(
                getResources().getStringArray(R.array.meal_types)[0],
                false
        );
        dialogBinding.mealPhotoPreview.setImageResource(R.drawable.meal_placeholder);
        dialogBinding.choosePhotoButton.setOnClickListener(view ->
                photoPickerLauncher.launch(new String[]{"image/*"}));

        if (suggestedFood != null) {
            dialogBinding.mealNameInput.setText(suggestedFood.getName());
            dialogBinding.mealCaloriesInput.setText(
                    String.valueOf(Math.round(suggestedFood.getCalories()))
            );
            dialogBinding.nutritionPreviewText.setText(
                    getString(
                            R.string.macronutrient_summary,
                            suggestedFood.getProtein(),
                            suggestedFood.getCarbohydrates(),
                            suggestedFood.getTotalFat()
                    )
            );
            dialogBinding.nutritionPreviewText.setVisibility(View.VISIBLE);
        }

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.log_meal_title)
                .setView(dialogBinding.getRoot())
                .setNegativeButton(R.string.action_cancel, null)
                .setPositiveButton(R.string.save_meal_action, null)
                .create();

        dialog.setOnShowListener(ignored -> dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(view -> saveMeal(dialogBinding, dialog)));
        dialog.setOnDismissListener(ignored -> {
            if (activeMealDialogBinding == dialogBinding) {
                activeMealDialogBinding = null;
                selectedMealPhoto = null;
            }
        });
        dialog.show();
    }

    private void saveMeal(DialogMealBinding dialogBinding, AlertDialog dialog) {
        String name = dialogBinding.mealNameInput.getText() == null
                ? ""
                : dialogBinding.mealNameInput.getText().toString().trim();
        String calorieText = dialogBinding.mealCaloriesInput.getText() == null
                ? ""
                : dialogBinding.mealCaloriesInput.getText().toString().trim();
        String mealType = dialogBinding.mealTypeInput.getText().toString().trim();

        dialogBinding.mealNameLayout.setError(name.isEmpty()
                ? getString(R.string.meal_name_required)
                : null);
        dialogBinding.mealCaloriesLayout.setError(calorieText.isEmpty()
                ? getString(R.string.meal_calories_required)
                : null);
        dialogBinding.mealTypeLayout.setError(mealType.isEmpty()
                ? getString(R.string.meal_type_required)
                : null);

        if (name.isEmpty() || calorieText.isEmpty() || mealType.isEmpty()) {
            return;
        }

        final int calories;
        try {
            calories = Integer.parseInt(calorieText);
        } catch (NumberFormatException exception) {
            dialogBinding.mealCaloriesLayout.setError(
                    getString(R.string.meal_calories_invalid)
            );
            return;
        }

        if (calories <= 0) {
            dialogBinding.mealCaloriesLayout.setError(
                    getString(R.string.meal_calories_invalid)
            );
            return;
        }

        Meal meal = new Meal();
        meal.setName(name);
        meal.setCalories(calories);
        meal.setMealType(mealType);
        meal.setPhotoUri(selectedMealPhoto == null ? "" : selectedMealPhoto.toString());
        meal.setDate(currentDateKey);
        mealViewModel.addMeal(meal);

        binding.foodSearchInput.setText("");
        dialog.dismiss();
        Toast.makeText(this, R.string.meal_saved_message, Toast.LENGTH_SHORT).show();
    }

    private void handleSelectedPhoto(@Nullable Uri uri) {
        if (uri == null || activeMealDialogBinding == null) {
            return;
        }

        try {
            getContentResolver().takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );
        } catch (SecurityException exception) {
            Log.w(TAG, "The selected provider did not grant persistent access", exception);
        }

        selectedMealPhoto = uri;
        activeMealDialogBinding.mealPhotoPreview.setImageURI(uri);
        activeMealDialogBinding.mealPhotoPreview.setContentDescription(
                getString(R.string.selected_meal_photo_description)
        );
    }

    @Override
    protected void onDestroy() {
        if (activeSearchCall != null) {
            activeSearchCall.cancel();
        }
        super.onDestroy();
    }
}
