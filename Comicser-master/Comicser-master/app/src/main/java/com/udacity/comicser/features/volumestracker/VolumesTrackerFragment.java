package com.udacity.comicser.features.volumestracker;

import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;
import butterknife.BindBool;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.udacity.comicser.R;
import com.udacity.comicser.base.BaseFragment;
import com.udacity.comicser.data.source.local.ComicContract.TrackedVolumeEntry;
import com.udacity.comicser.features.navigation.NavigationActivity;
import com.udacity.comicser.features.volumedetails.VolumeDetailsActivity;
import com.udacity.comicser.features.volumedetails.VolumeDetailsFragmentBuilder;
import com.udacity.comicser.utils.FragmentUtils;

import org.jetbrains.annotations.NotNull;

// LOADERS USED BECAUSE OF RUBRIC REQUIREMENT
@FragmentWithArgs
public class VolumesTrackerFragment extends BaseFragment
    implements LoaderManager.LoaderCallbacks<Cursor> {

  private static final int TRACKER_LOADER_ID = 1234;

  @BindView(R.id.contentView)
  RecyclerView contentView;
  @BindView(R.id.emptyView)
  TextView emptyView;

  @BindString(R.string.msg_no_volumes_tracked)
  String emptyViewText;
  @BindInt(R.integer.grid_columns_count)
  int gridColumnsCount;
  @BindBool(R.bool.is_tablet_layout)
  boolean twoPaneMode;

  private VolumesTrackerAdapter adapter;

  @Override
  public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    adapter = new VolumesTrackerAdapter(volumeId -> {
      if (twoPaneMode) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        Fragment fragment = new VolumeDetailsFragmentBuilder(volumeId).build();

        FragmentUtils.replaceFragmentIn(
            manager, fragment, R.id.content_frame, "VolumeDetailsFragment", true);
      } else {
        startActivity(VolumeDetailsActivity.prepareIntent(getContext(), volumeId));
      }
    });

    emptyView.setText(emptyViewText);

    adapter.setHasStableIds(true);

    StaggeredGridLayoutManager manager =
        new StaggeredGridLayoutManager(gridColumnsCount, StaggeredGridLayoutManager.VERTICAL);

    contentView.setLayoutManager(manager);
    contentView.setHasFixedSize(true);
    contentView.setAdapter(adapter);

    ActionBar supportActionBar = ((NavigationActivity) getActivity()).getSupportActionBar();

    if (supportActionBar != null) {
      supportActionBar.setTitle(R.string.volumes_tracker_fragment_title);
    }

    if (savedInstanceState == null) {
      hideToolbar();
      startToolbarAnimation();
    }

    getActivity().getSupportLoaderManager().initLoader(TRACKER_LOADER_ID, null, this);
  }

  @Override
  public void onResume() {
    super.onResume();
    getActivity().getSupportLoaderManager().restartLoader(TRACKER_LOADER_ID, null, this);
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.fragment_volumes_tracker;
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new AsyncTaskLoader<Cursor>(getContext()) {

      Cursor data = null;

      @Override
      protected void onStartLoading() {

        if (data != null) {
          deliverResult(data);
        } else {
          forceLoad();
        }
      }

      @Override
      public Cursor loadInBackground() {
        return getActivity().getContentResolver()
            .query(TrackedVolumeEntry.CONTENT_URI_TRACKED_VOLUMES,
                null, null, null, null);
      }

      @Override
      public void deliverResult(Cursor cursor) {
        data = cursor;
        super.deliverResult(cursor);
      }
    };
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    if (data.getCount() > 0) {
      emptyView.setVisibility(View.GONE);
    } else {
      emptyView.setVisibility(View.VISIBLE);
    }

    adapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    adapter.swapCursor(null);
  }
}
