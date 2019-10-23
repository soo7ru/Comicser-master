package com.udacity.comicser.data.model;

import androidx.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 *  Short origin info.
 *
 */

@AutoValue
public abstract class ComicOriginInfoShort {
  public abstract long id();
  @Nullable public abstract String name();

  public static TypeAdapter<ComicOriginInfoShort> typeAdapter(Gson gson) {
    return new AutoValue_ComicOriginInfoShort.GsonTypeAdapter(gson);
  }
}
