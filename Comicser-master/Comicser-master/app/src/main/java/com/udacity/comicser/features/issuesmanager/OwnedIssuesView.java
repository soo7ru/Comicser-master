package com.udacity.comicser.features.issuesmanager;

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;
import com.udacity.comicser.data.model.ComicIssueInfoList;
import java.util.List;

interface OwnedIssuesView extends MvpLceView<List<ComicIssueInfoList>> {

//  Already defined by Mosby:
//  void showLoading(boolean pullToRefresh);
//  void showContent();
//  void showError(Throwable e, boolean pullToRefresh);
//  void setData(List<ComicIssueInfoList> data);
//  void loadData(boolean pullToRefresh);

  void setTitle(String title);

  void showEmptyView(boolean show);

  void loadDataFiltered(String filter);
}
