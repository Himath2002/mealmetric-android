package io.github.himath2002.mealmetric;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

final class MealRepository {

    private final MealDao mealDao;

    MealRepository(Application application) {
        mealDao = MealDatabase.getInstance(application).mealDao();
    }

    LiveData<List<Meal>> observeMealsForDate(String date) {
        return mealDao.observeMealsForDate(date);
    }

    void insert(Meal meal) {
        MealDatabase.databaseWriteExecutor.execute(() -> mealDao.insert(meal));
    }
}
