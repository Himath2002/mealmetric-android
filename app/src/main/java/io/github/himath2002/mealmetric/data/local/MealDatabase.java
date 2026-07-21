package io.github.himath2002.mealmetric.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.himath2002.mealmetric.model.Meal;

/** Process-wide Room database for MealMetric's private on-device journal. */
@Database(entities = {Meal.class}, version = 1, exportSchema = true)
public abstract class MealDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "mealmetric.db";
    private static final int WRITE_THREAD_COUNT = 2;

    private static volatile MealDatabase instance;

    private static final ExecutorService DATABASE_WRITE_EXECUTOR =
            Executors.newFixedThreadPool(WRITE_THREAD_COUNT);

    public abstract MealDao mealDao();

    /** Returns the lazily created application-scoped database instance. */
    public static MealDatabase getInstance(Context context) {
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

    /** Schedules one database mutation away from the main thread. */
    public static void executeWrite(Runnable operation) {
        DATABASE_WRITE_EXECUTOR.execute(operation);
    }
}
