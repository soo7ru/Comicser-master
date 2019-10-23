package com.udacity.comicser.utils;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.udacity.comicser.R;
import com.udacity.comicser.utils.custom.GlideCustomCropTransformation;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ImageUtils {

  /**
   * External image loading function with custom image positioning (top crop)
   *
   * @param view Target imageView
   * @param url Image url
   */
  public static void loadImageWithTopCrop(ImageView view, String url) {

    Glide.with(view.getContext())
        .load(url)
            .transition(withCrossFade())
        .transform(new GlideCustomCropTransformation(view.getContext(), 0, 0))
        .error(R.drawable.placeholder_error)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(view);
  }

  /**
   * External image loading function which handles ProgressBar as loading placeholder
   *
   * @param view Target imageView
   * @param url Image url
   * @param progressBar ProgressBar view which shown while image loading
   */
  public static void loadImageWithProgress(ImageView view, String url, ProgressBar progressBar) {

    Glide.with(view.getContext())
        .load(url)
            .transition(withCrossFade())
        .listener(new RequestListener<Drawable>() {
          @Override
          public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target,
                                     boolean isFirstResource) {
            progressBar.setVisibility(View.GONE);
            return false;
          }

          @Override
          public boolean onResourceReady(Drawable resource, Object model,
                                         Target<Drawable> target,
                                         DataSource isFromMemoryCache, boolean isFirstResource) {
            progressBar.setVisibility(View.GONE);
            return false;
          }
        })
        .error(R.drawable.placeholder_error)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(view);
  }

  /**
   * External image loading function which runs custom callback on any loading result
   *
   * @param view Target imageView
   * @param url Image url
   * @param callback Callback which runs when image loading finished with any result
   */
  public static void loadImageWithCallback(ImageView view, String url,
      ImageLoadingCallback callback) {

    Glide.with(view.getContext())
        .load(url)
            .transition(withCrossFade())
        .listener(new RequestListener<Drawable>() {
          @Override
          public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target,
              boolean isFirstResource) {
            callback.onFinish(false);
            return false;
          }

          @Override
          public boolean onResourceReady(Drawable resource, Object model,
              Target<Drawable> target,
              DataSource isFromMemoryCache, boolean isFirstResource) {
            callback.onFinish(true);
            return false;
          }
        })
        .error(R.drawable.placeholder_error)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(view);
  }

  interface ImageLoadingCallback {

    void onFinish(boolean successful);
  }
}
