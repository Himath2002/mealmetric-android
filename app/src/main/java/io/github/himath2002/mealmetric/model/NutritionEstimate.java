package io.github.himath2002.mealmetric.model;

/** A structured, read-only estimate returned by the optional nutrition search. */
public final class NutritionEstimate {

    private final String name;
    private final double servingQuantity;
    private final String servingUnit;
    private final double calories;
    private final double totalFat;
    private final double protein;
    private final double carbohydrates;

    public NutritionEstimate(
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

    public String getName() {
        return name;
    }

    public double getServingQuantity() {
        return servingQuantity;
    }

    public String getServingUnit() {
        return servingUnit;
    }

    public double getCalories() {
        return calories;
    }

    public double getTotalFat() {
        return totalFat;
    }

    public double getProtein() {
        return protein;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }
}
