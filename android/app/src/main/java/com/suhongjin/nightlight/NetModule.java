package com.suhongjin.nightlight;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {

  @Provides
  @Singleton
  SharedPreferences providesSharedPreferences(Application application) {
    return application.getSharedPreferences(
        application.getString(R.string.preferences_file),
        Context.MODE_PRIVATE);
  }

  @Provides
  @Singleton
  Cache provideOkHttpCache(Application application) {
    int cacheSize = 10 * 1024 * 1024;
    return new Cache(application.getCacheDir(), cacheSize);
  }

  @Provides
  @Singleton
  Gson provideGson() {
    return new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();
  }

  @Provides
  @Singleton
  OkHttpClient provideOkHttpClient(Cache cache) {
    return new OkHttpClient.Builder()
        .cache(cache)
        .build();
  }

  @Provides
  @Singleton
  Retrofit.Builder provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
    return new Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient);
  }
}
