package com.udacity.comicser.features.volumeslist;

import dagger.Subcomponent;

@VolumeScope
@Subcomponent
public interface VolumesComponent {

  VolumesPresenter presenter();

  void inject(VolumesFragment fragment);
}
