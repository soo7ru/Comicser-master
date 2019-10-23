package com.udacity.comicser.data.source.local.dagger;

import com.udacity.comicser.data.source.local.ComicProvider;
import com.udacity.comicser.data.source.local.LocalDataScope;
import com.udacity.comicser.data.source.local.dagger.modules.ComicDbHelperModule;
import dagger.Subcomponent;

/**
 * Separate component for Content Provider injection.
 */

@LocalDataScope
@Subcomponent(modules = {ComicDbHelperModule.class})
public interface ComicDbHelperComponent {
  void inject(ComicProvider provider);
}
