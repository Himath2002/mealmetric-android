package io.github.himath2002.mealmetric.data.remote;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import io.github.himath2002.mealmetric.BuildConfig;

/** Creates the configured client for MealMetric's optional nutrition service. */
public final class NutritionClient {

    private static final String BASE_URL = "https://trackapi.nutritionix.com/";
    private static final long NETWORK_TIMEOUT_SECONDS = 15;

    private static volatile Retrofit retrofit;

    private NutritionClient() {
    }

    /** Returns a service contract backed by the shared Retrofit instance. */
    public static NutritionApi createApi() {
        return getRetrofit().create(NutritionApi.class);
    }

    private static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (NutritionClient.class) {
                if (retrofit == null) {
                    retrofit = createRetrofit();
                }
            }
        }
        return retrofit;
    }

    private static Retrofit createRetrofit() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            httpClient.addInterceptor(logging);
        }

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
