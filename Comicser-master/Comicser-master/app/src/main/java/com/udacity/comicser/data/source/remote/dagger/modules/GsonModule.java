package com.udacity.comicser.data.source.remote.dagger.modules;

import androidx.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.udacity.comicser.data.source.remote.RemoteDataScope;
import com.udacity.comicser.utils.custom.MyGsonAdapterFactory;
import dagger.Module;
import dagger.Provides;

@Module
public class GsonModule {

  @Provides
  @NonNull
  @RemoteDataScope
  Gson provideGson() {
    return new GsonBuilder()
        .registerTypeAdapterFactory(MyGsonAdapterFactory.create())
        .create();
  }
}
