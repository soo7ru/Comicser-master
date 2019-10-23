package com.udacity.comicser.features.volumedetails;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindBool;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;
import com.udacity.comicser.ComicserApp;
import com.udacity.comicser.R;
import com.udacity.comicser.base.BaseLceFragment;
import com.udacity.comicser.data.model.ComicImages;
import com.udacity.comicser.data.model.ComicIssueInfoShort;
import com.udacity.comicser.data.model.ComicPublisherInfo;
import com.udacity.comicser.data.model.ComicVolumeInfo;
import com.udacity.comicser.data.source.local.dagger.modules.ComicLocalDataModule;
import com.udacity.comicser.data.source.remote.dagger.modules.ComicRemoteDataModule;
import com.udacity.comicser.features.issuedetails.IssueDetailsActivity;
import com.udacity.comicser.features.issuedetails.IssueDetailsFragmentBuilder;
import com.udacity.comicser.features.volumedetails.VolumeDetailsIssueAdapter.IssuesAdapterCallbacks;
import com.udacity.comicser.utils.FragmentUtils;
import com.udacity.comicser.utils.HtmlUtils;
import com.udacity.comicser.utils.ImageUtils;
import com.udacity.comicser.utils.ViewUtils;
import java.util.List;

@SuppressWarnings("WeakerAccess")
@FragmentWithArgs
public class VolumeDetailsFragment extends
    BaseLceFragment<LinearLayout, ComicVolumeInfo, VolumeDetailsView, VolumeDetailsPresenter>
    implements VolumeDetailsView {

  @Arg
  long volumeId;

  @BindView(R.id.volume_details_screen)
  ImageView volumeCover;
  @BindView(R.id.volume_details_name)
  TextView volumeName;
  @BindView(R.id.volume_details_publisher)
  TextView volumePublisher;
  @BindView(R.id.volume_details_year)
  TextView volumeYear;
  @BindView(R.id.volume_details_description)
  TextView volumeDescription;
  @BindView(R.id.volume_details_issues_card)
  CardView issuesView;
  @BindView(R.id.volume_details_issues_view)
  RecyclerView issuesRecyclerView;

  @BindString(R.string.msg_tracked)
  String messageTracked;
  @BindString(R.string.msg_track_removed)
  String messageUntracked;
  @BindBool(R.bool.is_tablet_layout)
  boolean twoPaneMode;

  private VolumeDetailsComponent volumeDetailsComponent;
  private ComicVolumeInfo currentVolumeInfo;
  private VolumeDetailsIssueAdapter issuesAdapter;
  private Menu currentMenu;

  // --- FRAGMENT LIFECYCLE ---

  @Override
  public void onResume() {
    super.onResume();
    // Force recyclerView update so it can display actual bookmarks
    issuesAdapter.notifyDataSetChanged();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    setRetainInstance(true);
    setHasOptionsMenu(true);

    issuesAdapter = new VolumeDetailsIssueAdapter(new IssuesAdapterCallbacks() {
      @Override
      public void issueClicked(long issueId) {
        if (twoPaneMode) {
          FragmentManager manager = getActivity().getSupportFragmentManager();
          Fragment fragment = new IssueDetailsFragmentBuilder(issueId).build();

          FragmentUtils.replaceFragmentIn(
              manager, fragment, R.id.content_frame, "IssueDetailsFragment", true);
        } else {
          startActivity(IssueDetailsActivity.prepareIntent(getContext(), issueId));
        }
      }

      @Override
      public boolean isIssueTracked(long issueId) {
        return presenter.ifTargetIssueOwned(issueId);
      }
    });

    issuesAdapter.setHasStableIds(true);

    LinearLayoutManager manager =
        new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

    issuesRecyclerView.setLayoutManager(manager);
    issuesRecyclerView.setHasFixedSize(true);
    issuesRecyclerView.setNestedScrollingEnabled(false);
    issuesRecyclerView.setAdapter(issuesAdapter);

    issuesRecyclerView
        .addItemDecoration(new DividerItemDecoration(getContext(), manager.getOrientation()));

    if (savedInstanceState != null) {
      loadData(false);
    }
  }

  // --- OPTIONS MENU ---

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.fragment_volume_details, menu);

    currentMenu = menu;

    presenter.setUpTrackIconState(volumeId);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_track:
        onTrackingClick();
        break;
    }

    return super.onOptionsItemSelected(item);
  }

  // --- BASE LCE FRAGMENT ---

  @Override
  protected int getLayoutRes() {
    return R.layout.fragment_volume_details;
  }

  @NonNull
  @Override
  public VolumeDetailsPresenter createPresenter() {
    return volumeDetailsComponent.presenter();
  }

  @Override
  protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
    return e.getMessage();
  }

  @Override
  protected void injectDependencies() {
    volumeDetailsComponent = ComicserApp.getAppComponent()
        .plusRemoteComponent(new ComicRemoteDataModule())
        .plusLocalComponent(new ComicLocalDataModule())
        .plusVolumeDetailsComponent();

    volumeDetailsComponent.inject(this);
  }

  // --- MVP VIEW STATE ---

  @Override
  public ComicVolumeInfo getData() {
    return currentVolumeInfo;
  }

  @NonNull
  @Override
  public LceViewState<ComicVolumeInfo, VolumeDetailsView> createViewState() {
    return new RetainingLceViewState<>();
  }

  // --- MVP VIEW ---


  @Override
  public void showContent() {
    contentView.setVisibility(View.VISIBLE);
    errorView.setVisibility(View.GONE);
    loadingView.setVisibility(View.GONE);
  }

  @Override
  public void showError(Throwable e, boolean pullToRefresh) {
    errorView.setText(R.string.error_issue_not_available);
    contentView.setVisibility(View.GONE);
    loadingView.setVisibility(View.GONE);
    errorView.setVisibility(View.VISIBLE);
  }

  @Override
  public void showLoading(boolean pullToRefresh) {
    if (pullToRefresh) {
      contentView.setVisibility(View.GONE);
      errorView.setVisibility(View.GONE);
      loadingView.setVisibility(View.VISIBLE);
    } else {
      contentView.setVisibility(View.VISIBLE);
      errorView.setVisibility(View.GONE);
      loadingView.setVisibility(View.GONE);
    }
  }

  @Override
  public void setData(ComicVolumeInfo data) {
    currentVolumeInfo = data;
    bindVolumeToUi(currentVolumeInfo);
  }

  @Override
  public void loadData(boolean pullToRefresh) {
    presenter.loadVolumeDetails(volumeId);
  }

  @Override
  public void markAsTracked() {
    currentMenu.findItem(R.id.action_track).setIcon(R.drawable.ic_notifications_black_24dp);

    ViewUtils.tintMenuIcon(getContext(), currentMenu, R.id.action_track,
        R.color.material_color_white);
  }

  @Override
  public void unmarkAsTracked() {
    currentMenu.findItem(R.id.action_track).setIcon(R.drawable.ic_notifications_none_black_24dp);

    ViewUtils.tintMenuIcon(getContext(), currentMenu, R.id.action_track,
        R.color.material_color_white);
  }

  @Override
  public void onTrackingClick() {

    if (currentVolumeInfo == null) {
      return;
    }

    String message;

    boolean isTrackedNow = presenter.isCurrentVolumeTracked(volumeId);

    if (isTrackedNow) {
      presenter.removeTracking(volumeId);
      message = messageUntracked;
    } else {
      presenter.trackVolume(currentVolumeInfo);
      message = messageTracked;
    }

    presenter.setUpTrackIconState(volumeId);

    int parentLayoutId = (twoPaneMode) ?
        R.id.main_coordinator_layout :
        R.id.volume_details_activity_layout;

    Snackbar.make(
        ButterKnife.findById(getActivity(), parentLayoutId),
        message,
        Snackbar.LENGTH_SHORT)
        .show();
  }

  // --- UI BINDING UTILS ---

  private void bindVolumeToUi(ComicVolumeInfo volume) {

    loadHeaderImage(volumeCover, volume.image());

    String name = volume.name();
    volumeName.setText(name);

    int year = volume.start_year();
    volumeYear.setText(String.valueOf(year));

    ComicPublisherInfo publisher = volume.publisher();
    setUpPublisher(volumePublisher, publisher);
    setUpDescription(volumeDescription, volume.description());
    setUpIssuesList(issuesView, volume.issues());
  }

  private void loadHeaderImage(ImageView header, ComicImages image) {
    if (image != null) {
      String imageUrl = image.small_url();
      ImageUtils.loadImageWithTopCrop(header, imageUrl);
    } else {
      header.setVisibility(View.GONE);
    }
  }

  private void setUpPublisher(TextView textView, ComicPublisherInfo publisher) {
    if (publisher != null) {
      textView.setText(publisher.name());
    } else {
      textView.setText("-");
    }
  }

  private void setUpDescription(TextView textView, String description) {
    if (description != null) {
      textView.setText(HtmlUtils.parseHtmlText(description));
    } else {
      textView.setVisibility(View.GONE);
    }
  }

  private void setUpIssuesList(CardView parent, List<ComicIssueInfoShort> issues) {
    if (issues != null && !issues.isEmpty()) {
      issuesAdapter.setIssues(issues);
      issuesAdapter.notifyDataSetChanged();
    } else {
      parent.setVisibility(View.GONE);
    }
  }
}
