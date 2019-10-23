package com.udacity.comicser.features.volumedetails;

import dagger.Subcomponent;

@VolumeDetailsScope
@Subcomponent
public interface VolumeDetailsComponent {

  VolumeDetailsPresenter presenter();

  void inject(VolumeDetailsFragment fragment);
}
