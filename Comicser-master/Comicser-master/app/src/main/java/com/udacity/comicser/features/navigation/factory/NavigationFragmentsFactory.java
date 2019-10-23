package com.udacity.comicser.features.navigation.factory;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.udacity.comicser.features.characterslist.CharactersFragment;
import com.udacity.comicser.features.characterslist.CharactersFragmentBuilder;
import com.udacity.comicser.features.issueslist.IssuesFragment;
import com.udacity.comicser.features.issueslist.IssuesFragmentBuilder;
import com.udacity.comicser.features.issuesmanager.OwnedIssuesFragment;
import com.udacity.comicser.features.issuesmanager.OwnedIssuesFragmentBuilder;
import com.udacity.comicser.features.preferences.ComicPreferencesFragment;
import com.udacity.comicser.features.preferences.ComicPreferencesFragmentBuilder;
import com.udacity.comicser.features.volumeslist.VolumesFragment;
import com.udacity.comicser.features.volumeslist.VolumesFragmentBuilder;
import com.udacity.comicser.features.volumestracker.VolumesTrackerFragment;
import com.udacity.comicser.features.volumestracker.VolumesTrackerFragmentBuilder;

public class NavigationFragmentsFactory {

  public static Fragment getFragment(FragmentManager manager, @AppNavigation.Section int type) {

    Fragment fragment = manager.findFragmentByTag(getFragmentTag(type));

    if (fragment != null) {
      return fragment;
    }

    switch (type) {
      case AppNavigation.ISSUES:
        return new IssuesFragmentBuilder().build();
      case AppNavigation.VOLUMES:
        return new VolumesFragmentBuilder().build();
      case AppNavigation.CHARACTERS:
        return new CharactersFragmentBuilder().build();
      case AppNavigation.COLLECTION:
        return new OwnedIssuesFragmentBuilder().build();
      case AppNavigation.TRACKER:
        return new VolumesTrackerFragmentBuilder().build();
      case AppNavigation.SETTINGS:
        return new ComicPreferencesFragmentBuilder().build();
      default:
        return null;
    }
  }

  public static String getFragmentTag(@AppNavigation.Section int type) {
    switch (type) {
      case AppNavigation.ISSUES:
        return IssuesFragment.class.getSimpleName();
      case AppNavigation.VOLUMES:
        return VolumesFragment.class.getSimpleName();
      case AppNavigation.CHARACTERS:
        return CharactersFragment.class.getSimpleName();
      case AppNavigation.COLLECTION:
        return OwnedIssuesFragment.class.getSimpleName();
      case AppNavigation.TRACKER:
        return VolumesTrackerFragment.class.getSimpleName();
      case AppNavigation.SETTINGS:
        return ComicPreferencesFragment.class.getSimpleName();
      default:
        return "";
    }
  }
}
