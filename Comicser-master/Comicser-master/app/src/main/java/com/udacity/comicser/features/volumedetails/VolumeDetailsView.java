package com.udacity.comicser.features.volumedetails;

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;
import com.udacity.comicser.data.model.ComicVolumeInfo;

interface VolumeDetailsView extends MvpLceView<ComicVolumeInfo> {
//  Already defined by Mosby:
//  void showLoading(boolean pullToRefresh);
//  void showContent();
//  void showError(Throwable e, boolean pullToRefresh);
//  void setData(List<ComicIssueInfoList> data);
//  void loadData(boolean pullToRefresh);

  void markAsTracked();

  void unmarkAsTracked();

  void onTrackingClick();
}
