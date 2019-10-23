package com.udacity.comicser.data.source.local.dagger.modules;

import android.content.Context;
import com.udacity.comicser.data.source.local.ComicDbHelper;
import com.udacity.comicser.data.source.local.LocalDataScope;
import dagger.Module;
import dagger.Provides;

@Module
public class ComicDbHelperModule {

  @Provides
  @LocalDataScope
  ComicDbHelper provideComicDbHelper(Context context) {
    return new ComicDbHelper(context);
  }
}
