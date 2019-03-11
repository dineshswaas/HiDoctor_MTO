package com.swaas.hd.mto.RetrofitBuilder;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;
import com.swaas.hd.mto.Utils.Constants;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class RetrofitAPIBuilder {

    static Retrofit retrofit = null;

    public static synchronized Retrofit getInstance() {


        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(300, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(300, TimeUnit.SECONDS);
        okHttpClient.networkInterceptors().add(new StethoInterceptor());

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }

        return retrofit;
    }
}
