package com.udacity.comicser.data.source.local.dagger;

import com.udacity.comicser.data.source.local.LocalDataScope;
import com.udacity.comicser.data.source.local.dagger.modules.ComicLocalDataModule;
import com.udacity.comicser.features.widget.ComicWidgetFactory;
import dagger.Subcomponent;

@LocalDataScope
@Subcomponent(modules = {ComicLocalDataModule.class})
public interface ComicWidgetComponent {

  void inject(ComicWidgetFactory factory);
}
