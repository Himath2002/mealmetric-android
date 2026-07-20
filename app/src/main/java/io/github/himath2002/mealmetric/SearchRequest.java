package io.github.himath2002.mealmetric;

final class SearchRequest {

    private final String query;

    SearchRequest(String query) {
        this.query = query;
    }

    String getQuery() {
        return query;
    }
}
