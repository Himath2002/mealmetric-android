package io.github.himath2002.mealmetric.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "meals", indices = {@Index("date")})
/** A locally persisted meal entry scoped to one calendar date. */
public final class Meal {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String name = "";

    private int calories;

    @NonNull
    private String mealType = "";

    @NonNull
    private String photoUri = "";

    @NonNull
    private String date = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    @NonNull
    public String getMealType() {
        return mealType;
    }

    public void setMealType(@NonNull String mealType) {
        this.mealType = mealType;
    }

    @NonNull
    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(@NonNull String photoUri) {
        this.photoUri = photoUri;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }
}
