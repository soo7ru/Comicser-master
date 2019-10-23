package com.udacity.comicser.features.volumeslist;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import butterknife.BindBool;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.evernote.android.state.State;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.udacity.comicser.ComicserApp;
import com.udacity.comicser.R;
import com.udacity.comicser.base.BaseLceFragment;
import com.udacity.comicser.data.model.ComicVolumeInfoList;
import com.udacity.comicser.data.source.remote.dagger.modules.ComicRemoteDataModule;
import com.udacity.comicser.features.navigation.NavigationActivity;
import com.udacity.comicser.features.volumedetails.VolumeDetailsActivity;
import com.udacity.comicser.features.volumedetails.VolumeDetailsFragmentBuilder;
import com.udacity.comicser.utils.FragmentUtils;
import com.udacity.comicser.utils.ViewUtils;
import java.util.List;

@SuppressWarnings("WeakerAccess")
@FragmentWithArgs
public class VolumesFragment extends
    BaseLceFragment<RecyclerView, List<ComicVolumeInfoList>, VolumesView, VolumesPresenter>
    implements VolumesView {

  @BindInt(R.integer.grid_columns_count)
  int gridColumnsCount;
  @BindString(R.string.volumes_fragment_title)
  String fragmentTitle;
  @BindString(R.string.msg_no_volumes_found)
  String emptyViewText;
  @BindString(R.string.msg_volumes_start)
  String initialViewText;
  @BindBool(R.bool.is_tablet_layout)
  boolean twoPaneMode;

  @BindView(R.id.initialView)
  TextView initialView;
  @BindView(R.id.emptyView)
  TextView emptyView;

  @State
  String chosenName;

  @State
  String title;

  private VolumesComponent volumesComponent;
  private VolumesAdapter adapter;
  private boolean pendingStartupAnimation;

  // --- FRAGMENTS LIFECYCLE ---

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    if (savedInstanceState == null) {
      pendingStartupAnimation = true;
    }

    adapter = new VolumesAdapter(volumeId -> {
      if (twoPaneMode) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        Fragment fragment = new VolumeDetailsFragmentBuilder(volumeId).build();

        FragmentUtils.replaceFragmentIn(
            manager, fragment, R.id.content_frame, "VolumeDetailsFragment", true);
      } else {
        startActivity(VolumeDetailsActivity.prepareIntent(getContext(), volumeId));
      }
    });

    adapter.setHasStableIds(true);

    StaggeredGridLayoutManager manager =
        new StaggeredGridLayoutManager(gridColumnsCount, StaggeredGridLayoutManager.VERTICAL);

    contentView.setLayoutManager(manager);
    contentView.setHasFixedSize(true);
    contentView.setAdapter(adapter);

    setHasOptionsMenu(true);

    if (chosenName != null && chosenName.length() > 0) {
      loadDataByName(chosenName);
      setTitle(chosenName);
    } else {
      setTitle(fragmentTitle);
      showInitialView(true);
    }
  }

  // --- OPTIONS MENU ---

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.fragment_volume_list, menu);

    ViewUtils.tintMenuIcon(getContext(), menu, R.id.action_search, R.color.material_color_white);

    setUpSearchItem(menu);

    if (pendingStartupAnimation) {
      hideToolbar();
      pendingStartupAnimation = false;
      startToolbarAnimation();
    }

    super.onCreateOptionsMenu(menu, inflater);
  }

  // --- BASE LCE FRAGMENT ---

  @Override
  protected int getLayoutRes() {
    return R.layout.fragment_volumes;
  }

  @Override
  protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
    return e.getMessage();
  }

  @NonNull
  @Override
  public VolumesPresenter createPresenter() {
    return volumesComponent.presenter();
  }

  @Override
  protected void injectDependencies() {

    volumesComponent = ComicserApp.getAppComponent()
        .plusRemoteComponent(new ComicRemoteDataModule())
        .plusVolumesComponent();
    volumesComponent.inject(this);
  }

  // --- MVP VIEW STATE ---

  @Override
  public List<ComicVolumeInfoList> getData() {
    return adapter == null ? null : adapter.getVolumes();
  }

  @NonNull
  @Override
  public LceViewState<List<ComicVolumeInfoList>, VolumesView> createViewState() {
    return new RetainingLceViewState<>();
  }

  // --- MVP VIEW ---

  @Override
  public void setData(List<ComicVolumeInfoList> data) {
    adapter.setVolumes(data);
    adapter.notifyDataSetChanged();
  }

  @Override
  public void loadData(boolean pullToRefresh) {
    showLoading(false);
    showInitialView(true);
  }

  @Override
  public void loadDataByName(String name) {
    presenter.loadVolumesData(name);
  }

  private void setUpSearchItem(Menu menu) {
    // Find items
    MaterialSearchView searchView = ButterKnife.findById(getActivity(), R.id.search_view);
    MenuItem menuItem = menu.findItem(R.id.action_search);

    // Tweaks
    searchView.setVoiceSearch(false);
    searchView.setMenuItem(menuItem);

    // Listeners
    searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        chosenName = query;

        if (chosenName.length() > 0) {
          loadDataByName(chosenName);
          setTitle(chosenName);
          presenter.logVolumeSearchEvent(chosenName);
        }
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        //Do some magic
        return false;
      }
    });
  }

  @Override
  public void showContent() {
    showInitialView(false);
    showEmptyView(false);
    super.showContent();
  }

  @Override
  public void showLoading(boolean pullToRefresh) {
    if (pullToRefresh) {
      errorView.setVisibility(View.GONE);
      contentView.setVisibility(View.GONE);
      loadingView.setVisibility(View.VISIBLE);
    } else {
      loadingView.setVisibility(View.GONE);
    }
  }

  @Override
  public void showInitialView(boolean show) {
    if (show) {
      initialView.setText(initialViewText);
      initialView.setVisibility(View.VISIBLE);
      contentView.setVisibility(View.GONE);
      errorView.setVisibility(View.GONE);
    } else {
      initialView.setVisibility(View.GONE);
    }
  }

  @Override
  public void showEmptyView(boolean show) {
    if (show) {
      emptyView.setText(emptyViewText);
      emptyView.setVisibility(View.VISIBLE);
      contentView.setVisibility(View.GONE);
      errorView.setVisibility(View.GONE);
    } else {
      emptyView.setVisibility(View.GONE);
    }
  }

  @Override
  public void setTitle(String title) {
    this.title = title;
    updateTitle();
  }

  private void updateTitle() {
    ActionBar supportActionBar = ((NavigationActivity) getActivity()).getSupportActionBar();

    if (supportActionBar != null) {
      supportActionBar.setTitle(title);
    }
  }
}
