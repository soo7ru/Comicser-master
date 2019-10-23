package com.udacity.comicser.features.issueslist;

import dagger.Subcomponent;

@IssueScope
@Subcomponent
public interface IssuesComponent {

  IssuesPresenter presenter();

  void inject(IssuesFragment fragment);
}
