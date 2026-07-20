package io.github.himath2002.mealmetric;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Meal.class}, version = 1, exportSchema = true)
public abstract class MealDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "mealmetric.db";
    private static final int WRITE_THREAD_COUNT = 2;

    private static volatile MealDatabase instance;

    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(WRITE_THREAD_COUNT);

    public abstract MealDao mealDao();

    static MealDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (MealDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    MealDatabase.class,
                                    DATABASE_NAME
                            )
                            .build();
                }
            }
        }
        return instance;
    }
}
