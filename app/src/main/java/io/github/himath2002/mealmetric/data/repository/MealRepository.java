package io.github.himath2002.mealmetric.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.github.himath2002.mealmetric.data.local.MealDao;
import io.github.himath2002.mealmetric.data.local.MealDatabase;
import io.github.himath2002.mealmetric.model.Meal;

/**
 * Provides the ViewModel with one persistence boundary for observed meals and writes.
 */
public final class MealRepository {

    private final MealDao mealDao;

    public MealRepository(Application application) {
        mealDao = MealDatabase.getInstance(application).mealDao();
    }

    public LiveData<List<Meal>> observeMealsForDate(String date) {
        return mealDao.observeMealsForDate(date);
    }

    public void insert(Meal meal) {
        MealDatabase.executeWrite(() -> mealDao.insert(meal));
    }
}
