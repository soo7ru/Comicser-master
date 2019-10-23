package com.udacity.comicser.data.source.local.dagger;

import com.udacity.comicser.data.source.local.LocalDataScope;
import com.udacity.comicser.data.source.local.dagger.modules.ComicLocalDataModule;
import com.udacity.comicser.features.issuedetails.IssueDetailsComponent;
import com.udacity.comicser.features.issueslist.IssuesComponent;
import com.udacity.comicser.features.issuesmanager.OwnedIssuesComponent;
import com.udacity.comicser.features.sync.ComicSyncAdapter;
import com.udacity.comicser.features.volumedetails.VolumeDetailsComponent;
import dagger.Subcomponent;

/**
 * Local data component, provides local data helper and preferences injections,
 * depends on remote data component.
 */


@LocalDataScope
@Subcomponent(modules = {ComicLocalDataModule.class})
public interface ComicLocalDataComponent {

  IssuesComponent plusIssuesComponent();

  IssueDetailsComponent plusIssueDetailsComponent();

  VolumeDetailsComponent plusVolumeDetailsComponent();

  OwnedIssuesComponent plusOwnedIssuesComponent();

  void inject(ComicSyncAdapter adapter);
}
