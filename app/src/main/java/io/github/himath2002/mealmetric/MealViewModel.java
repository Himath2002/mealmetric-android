package io.github.himath2002.mealmetric;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public final class MealViewModel extends AndroidViewModel {

    private final MealRepository repository;

    public MealViewModel(@NonNull Application application) {
        super(application);
        repository = new MealRepository(application);
    }

    LiveData<List<Meal>> observeMealsForDate(String date) {
        return repository.observeMealsForDate(date);
    }

    void addMeal(Meal meal) {
        repository.insert(meal);
    }
}
