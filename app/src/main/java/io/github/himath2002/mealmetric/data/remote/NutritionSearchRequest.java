package io.github.himath2002.mealmetric.data.remote;

/** Request body for one natural-language nutrition search. */
public final class NutritionSearchRequest {

    private final String query;

    public NutritionSearchRequest(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
