package com.udacity.comicser;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.firebase.analytics.FirebaseAnalytics;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
class ComicserAppModule {

  @NonNull
  private final ComicserApp app;

  ComicserAppModule(@NonNull ComicserApp app) {
    this.app = app;
  }

  @Provides
  @NonNull
  @Singleton
  Context provideContext() {
    return app;
  }

  @Provides
  @NonNull
  @Singleton
  FirebaseAnalytics provideFirebaseAnalytics(Context context) {
    return FirebaseAnalytics.getInstance(context);
  }
}
