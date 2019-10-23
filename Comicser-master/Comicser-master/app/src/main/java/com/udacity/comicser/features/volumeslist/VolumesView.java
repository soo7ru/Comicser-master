package com.udacity.comicser.features.volumeslist;

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;
import com.udacity.comicser.data.model.ComicVolumeInfoList;
import java.util.List;

interface VolumesView extends MvpLceView<List<ComicVolumeInfoList>> {

//  Already defined by Mosby:
//  void showLoading(boolean pullToRefresh);
//  void showContent();
//  void showError(Throwable e, boolean pullToRefresh);
//  void setData(List<ComicIssueInfoList> data);
//  void loadData(boolean pullToRefresh);

  void loadDataByName(String name);

  void showInitialView(boolean show);

  void showEmptyView(boolean show);

  void setTitle(String title);
}
