package io.github.himath2002.mealmetric;

final class FoodItem {

    private final String name;
    private final double servingQuantity;
    private final String servingUnit;
    private final double calories;
    private final double totalFat;
    private final double protein;
    private final double carbohydrates;

    FoodItem(
            String name,
            double servingQuantity,
            String servingUnit,
            double calories,
            double totalFat,
            double protein,
            double carbohydrates
    ) {
        this.name = name;
        this.servingQuantity = servingQuantity;
        this.servingUnit = servingUnit;
        this.calories = calories;
        this.totalFat = totalFat;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
    }

    String getName() {
        return name;
    }

    double getServingQuantity() {
        return servingQuantity;
    }

    String getServingUnit() {
        return servingUnit;
    }

    double getCalories() {
        return calories;
    }

    double getTotalFat() {
        return totalFat;
    }

    double getProtein() {
        return protein;
    }

    double getCarbohydrates() {
        return carbohydrates;
    }
}
