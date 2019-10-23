package com.udacity.comicser.features.issuedetails;

import dagger.Subcomponent;

@IssueDetailsScope
@Subcomponent
public interface IssueDetailsComponent {

  IssueDetailsPresenter presenter();

  void inject(IssueDetailsFragment fragment);
}
