package com.mystrimz.android.http;

import com.mystrimz.android.http.requests.BasicAuthInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by amit rai on 23/12/16.
 */

public class ApiClient {
    private static ApiClient apiBuilder;
    private Retrofit retrofit = null;
    //  private OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private Retrofit.Builder builder = null;
    private int TIME_OUT = 60;
    private static OkHttpClient.Builder okHttpClient =
            new OkHttpClient.Builder();


    private ApiClient() {

    }

    /**
     * Factory method of {@link ApiClient}
     *
     * @return instance of {@link ApiClient}
     */
    public static ApiClient getInstance() {
        if (apiBuilder == null) {
            apiBuilder = new ApiClient();

        }
        return apiBuilder;
    }

    /**
     * @return instance of {@link Retrofit.Builder}
     */
    private Retrofit.Builder getRetrofitBuilder() {
        if (builder == null) {

            builder = new Retrofit.Builder().baseUrl(ServerUrl.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient.connectTimeout(TIME_OUT, TimeUnit.SECONDS).readTimeout(TIME_OUT, TimeUnit.SECONDS)
                            .addInterceptor(new BasicAuthInterceptor("Valigar", "Valigar12"))
                            .build());
                    

        }
        return builder;
    }

    /**
     * @param serviceClass
     * @param <S>
     * @return
     */
    public <S> S createService(Class<S> serviceClass) {
        setLogInterCeptor();
        if (builder == null) {
            builder = getRetrofitBuilder();
        }

        //  retrofit = builder.client(httpClient.build()).build();
        retrofit = builder.client(okHttpClient.build()).build();
        return retrofit.create(serviceClass);
    }

    private Retrofit getClient() {
        if (retrofit == null) {
            retrofit = getRetrofitBuilder()
                    .build();
        }
        return retrofit;
    }

    /**
     * set log interceptor for logging the network response
     */
    private static void setLogInterCeptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient.addInterceptor(interceptor).build();
    }

}
