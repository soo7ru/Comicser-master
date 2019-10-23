package com.udacity.comicser.features.volumeslist;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.udacity.comicser.R;
import com.udacity.comicser.data.model.ComicImages;
import com.udacity.comicser.data.model.ComicPublisherInfo;
import com.udacity.comicser.data.model.ComicVolumeInfoList;
import com.udacity.comicser.features.volumeslist.VolumesAdapter.VolumeViewHolder;
import com.udacity.comicser.utils.ImageUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("WeakerAccess")
class VolumesAdapter extends RecyclerView.Adapter<VolumeViewHolder> {

  private List<ComicVolumeInfoList> volumes;
  final OnVolumeClickListener listener;

  VolumesAdapter(OnVolumeClickListener listener) {
    volumes = new ArrayList<>(0);
    this.listener = listener;
  }

  @Override
  public VolumeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_volumes_list_item, parent, false);

    return new VolumeViewHolder(view);
  }

  @Override
  public void onBindViewHolder(VolumeViewHolder holder, int position) {
    holder.bindTo(volumes.get(position));
  }

  @Override
  public int getItemCount() {
    return volumes == null ? 0 : volumes.size();
  }

  List<ComicVolumeInfoList> getVolumes() {
    return volumes;
  }

  void setVolumes(List<ComicVolumeInfoList> volumes) {
    this.volumes = volumes;
  }

  class VolumeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private long currentVolumeId;

    @BindView(R.id.volume_image_layout)
    FrameLayout imageLayout;
    @BindView(R.id.volume_cover)
    ImageView volumeCover;
    @BindView(R.id.volume_cover_progressbar)
    ProgressBar progressBar;
    @BindView(R.id.volume_name)
    TextView volumeName;
    @BindView(R.id.volume_publisher)
    TextView volumePublisher;
    @BindString(R.string.volumes_publisher_text)
    String publisherFormat;
    @BindView(R.id.volume_issues_count)
    TextView issuesCount;
    @BindString(R.string.volumes_count_text)
    String issuesCountFormat;

    VolumeViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      itemView.setOnClickListener(this);
    }

    void bindTo(ComicVolumeInfoList volume) {

      currentVolumeId = volume.id();

      ComicImages image = volume.image();
      if (image != null) {
        String url = image.small_url();
        ImageUtils.loadImageWithProgress(volumeCover, url, progressBar);
      } else {
        imageLayout.setVisibility(View.GONE);
      }

      volumeName.setText(volume.name());

      ComicPublisherInfo publisher = volume.publisher();

      if (publisher != null) {
        String publisherName = String.format(Locale.US, publisherFormat, publisher.name());
        volumePublisher.setText(publisherName);
      } else {
        volumePublisher.setVisibility(View.GONE);
      }

      String yearCount = String.format(Locale.US, issuesCountFormat, volume.count_of_issues());
      issuesCount.setText(yearCount);
    }

    @Override
    public void onClick(View v) {
      listener.volumeClicked(currentVolumeId);
    }
  }

  interface OnVolumeClickListener {

    void volumeClicked(long volumeId);
  }
}
