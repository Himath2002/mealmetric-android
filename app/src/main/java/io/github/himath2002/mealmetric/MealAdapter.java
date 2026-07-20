package io.github.himath2002.mealmetric;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import io.github.himath2002.mealmetric.databinding.MealItemBinding;

final class MealAdapter extends ListAdapter<Meal, MealAdapter.MealViewHolder> {

    private static final DiffUtil.ItemCallback<Meal> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull Meal oldItem, @NonNull Meal newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Meal oldItem, @NonNull Meal newItem) {
                    return oldItem.getCalories() == newItem.getCalories()
                            && oldItem.getName().equals(newItem.getName())
                            && oldItem.getMealType().equals(newItem.getMealType())
                            && oldItem.getPhotoUri().equals(newItem.getPhotoUri())
                            && oldItem.getDate().equals(newItem.getDate());
                }
            };

    MealAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MealItemBinding binding = MealItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new MealViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static final class MealViewHolder extends RecyclerView.ViewHolder {

        private final MealItemBinding binding;

        MealViewHolder(MealItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Meal meal) {
            binding.mealNameText.setText(meal.getName());
            binding.mealTypeText.setText(meal.getMealType());
            binding.mealCaloriesText.setText(
                    binding.getRoot().getContext().getString(
                            R.string.calorie_value,
                            meal.getCalories()
                    )
            );
            binding.mealPhoto.setContentDescription(
                    binding.getRoot().getContext().getString(
                            R.string.meal_photo_description,
                            meal.getName()
                    )
            );

            if (meal.getPhotoUri().isEmpty()) {
                binding.mealPhoto.setImageResource(R.drawable.meal_placeholder);
                return;
            }

            try {
                binding.mealPhoto.setImageURI(Uri.parse(meal.getPhotoUri()));
            } catch (RuntimeException exception) {
                binding.mealPhoto.setImageResource(R.drawable.meal_placeholder);
            }
        }
    }
}
