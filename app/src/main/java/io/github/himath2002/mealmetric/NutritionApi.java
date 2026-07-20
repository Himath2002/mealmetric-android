package io.github.himath2002.mealmetric;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

interface NutritionApi {

    @Headers("Content-Type: application/json")
    @POST("v2/natural/nutrients")
    Call<NutritionResponse> searchFood(
            @Header("x-app-id") String applicationId,
            @Header("x-app-key") String applicationKey,
            @Body SearchRequest request
    );
}
