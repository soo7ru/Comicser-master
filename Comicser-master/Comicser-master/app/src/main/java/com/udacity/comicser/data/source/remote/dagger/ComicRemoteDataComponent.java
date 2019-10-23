package com.udacity.comicser.data.source.remote.dagger;

import com.udacity.comicser.data.source.local.dagger.ComicLocalDataComponent;
import com.udacity.comicser.data.source.local.dagger.modules.ComicLocalDataModule;
import com.udacity.comicser.data.source.remote.RemoteDataScope;
import com.udacity.comicser.data.source.remote.dagger.modules.ComicRemoteDataModule;
import com.udacity.comicser.features.characterdetails.CharacterDetailsComponent;
import com.udacity.comicser.features.characterslist.CharactersComponent;
import com.udacity.comicser.features.volumeslist.VolumesComponent;
import dagger.Subcomponent;

/**
 * Remote data component. Provides remote data helper injection, depends on App component.
 */

@RemoteDataScope
@Subcomponent(modules = {ComicRemoteDataModule.class})
public interface ComicRemoteDataComponent {

  ComicLocalDataComponent plusLocalComponent(ComicLocalDataModule module);

  VolumesComponent plusVolumesComponent();

  CharactersComponent plusCharactersComponent();

  CharacterDetailsComponent plusCharacterDetailsComponent();
}
