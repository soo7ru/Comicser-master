package com.udacity.comicser.features.issueslist;

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;
import com.udacity.comicser.data.model.ComicIssueInfoList;
import java.util.List;

interface IssuesView extends MvpLceView<List<ComicIssueInfoList>> {

//  Already defined by Mosby:
//  void showLoading(boolean pullToRefresh);
//  void showContent();
//  void showError(Throwable e, boolean pullToRefresh);
//  void setData(List<ComicIssueInfoList> data);
//  void loadData(boolean pullToRefresh);

  void setTitle(String title);

  void showEmptyView(boolean show);

  void showErrorToast(String message);

  void choseDateAndLoadData();

  void loadDataForChosenDate(String date);

  void loadDataFiltered(String filter);
}
