package com.udacity.comicser.features.volumedetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import com.evernote.android.state.State;
import com.udacity.comicser.R;
import com.udacity.comicser.base.BaseActivity;
import com.udacity.comicser.utils.FragmentUtils;

@SuppressWarnings("WeakerAccess")
public class VolumeDetailsActivity extends BaseActivity {

  private static final String EXTRA_VOLUME_ID_ARG = "current_volume_id";

  @State
  long chosenVolumeId;

  @BindView(R.id.volume_details_toolbar)
  Toolbar toolbar;

  public static Intent prepareIntent(Context context, long volumeId) {
    Intent intent = new Intent(context, VolumeDetailsActivity.class);
    intent.putExtra(EXTRA_VOLUME_ID_ARG, volumeId);
    return intent;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_volume_details);

    setSupportActionBar(toolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    Bundle extras = getIntent().getExtras();
    chosenVolumeId = getIdFromExtras(extras);

    VolumeDetailsFragment fragment =
        (VolumeDetailsFragment) getSupportFragmentManager()
            .findFragmentById(R.id.volume_details_container);

    if (fragment == null) {
      fragment = new VolumeDetailsFragmentBuilder(chosenVolumeId).build();
      FragmentUtils.addFragmentTo(getSupportFragmentManager(), fragment,
          R.id.volume_details_container);
    }
  }

  private long getIdFromExtras(Bundle extras) {
    if (extras != null && extras.containsKey(EXTRA_VOLUME_ID_ARG)) {
      return extras.getLong(EXTRA_VOLUME_ID_ARG);
    }
    return 1L;
  }
}
