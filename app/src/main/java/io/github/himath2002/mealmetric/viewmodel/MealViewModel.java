package io.github.himath2002.mealmetric.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.github.himath2002.mealmetric.data.repository.MealRepository;
import io.github.himath2002.mealmetric.model.Meal;

/** Lifecycle-aware state holder for the currently selected journal date. */
public final class MealViewModel extends AndroidViewModel {

    private final MealRepository repository;

    public MealViewModel(@NonNull Application application) {
        super(application);
        repository = new MealRepository(application);
    }

    public LiveData<List<Meal>> observeMealsForDate(String date) {
        return repository.observeMealsForDate(date);
    }

    public void addMeal(Meal meal) {
        repository.insert(meal);
    }
}
