package com.udacity.comicser;

import com.udacity.comicser.data.source.local.dagger.ComicDbHelperComponent;
import com.udacity.comicser.data.source.local.dagger.ComicWidgetComponent;
import com.udacity.comicser.data.source.local.dagger.modules.ComicDbHelperModule;
import com.udacity.comicser.data.source.remote.dagger.ComicRemoteDataComponent;
import com.udacity.comicser.data.source.remote.dagger.modules.ComicRemoteDataModule;
import com.udacity.comicser.features.navigation.NavigationActivity;
import com.udacity.comicser.features.preferences.ComicPreferencesHelperModule;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {ComicserAppModule.class, ComicPreferencesHelperModule.class})
public interface ComicserAppComponent {

  ComicDbHelperComponent plusDbHelperComponent(ComicDbHelperModule module);

  ComicRemoteDataComponent plusRemoteComponent(ComicRemoteDataModule module);

  ComicWidgetComponent plusWidgetComponent();

  void inject(NavigationActivity activity);
}
