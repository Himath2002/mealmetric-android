package io.github.himath2002.mealmetric.data.remote;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.github.himath2002.mealmetric.model.NutritionEstimate;

/** Minimal Nutritionix response mapped into MealMetric's domain model. */
public final class NutritionSearchResponse {

    @SerializedName("foods")
    private List<Food> foods;

    public List<Food> getFoods() {
        return foods;
    }

    public static final class Food {

        @SerializedName("food_name")
        private String name;

        @SerializedName("serving_qty")
        private double servingQuantity;

        @SerializedName("serving_unit")
        private String servingUnit;

        @SerializedName("nf_calories")
        private double calories;

        @SerializedName("nf_total_fat")
        private double totalFat;

        @SerializedName("nf_protein")
        private double protein;

        @SerializedName("nf_total_carbohydrate")
        private double carbohydrates;

        public NutritionEstimate toEstimate() {
            return new NutritionEstimate(
                    name,
                    servingQuantity,
                    servingUnit,
                    calories,
                    totalFat,
                    protein,
                    carbohydrates
            );
        }
    }
}
