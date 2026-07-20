package io.github.himath2002.mealmetric;

import com.google.gson.annotations.SerializedName;

import java.util.List;

final class NutritionResponse {

    @SerializedName("foods")
    private List<Food> foods;

    List<Food> getFoods() {
        return foods;
    }

    static final class Food {

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

        FoodItem toFoodItem() {
            return new FoodItem(
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
