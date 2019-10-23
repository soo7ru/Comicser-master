package com.udacity.comicser.data.source.remote.dagger.modules;

import androidx.annotation.NonNull;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.udacity.comicser.data.source.remote.RemoteDataScope;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@SuppressWarnings("WeakerAccess")
@Module
public class OkHttpModule {

  @Provides
  @NonNull
  @RemoteDataScope
  OkHttpClient provideOkHttpClient() {
    return new OkHttpClient.Builder()
        .addNetworkInterceptor(new StethoInterceptor())
        .build();
  }
}
