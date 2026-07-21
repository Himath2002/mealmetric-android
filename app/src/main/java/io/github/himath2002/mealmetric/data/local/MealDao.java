package io.github.himath2002.mealmetric.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.github.himath2002.mealmetric.model.Meal;

/** Room access contract for date-scoped meal journal operations. */
@Dao
public interface MealDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Meal meal);

    @Query("SELECT * FROM meals WHERE date = :date ORDER BY id DESC")
    LiveData<List<Meal>> observeMealsForDate(String date);
}
