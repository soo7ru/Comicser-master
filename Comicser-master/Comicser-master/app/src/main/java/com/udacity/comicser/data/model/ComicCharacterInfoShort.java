package com.udacity.comicser.data.model;

import androidx.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 *  Short character info
 */

@AutoValue
public abstract class ComicCharacterInfoShort {
  public abstract long id();
  @Nullable public abstract String name();

  public static TypeAdapter<ComicCharacterInfoShort> typeAdapter(Gson gson) {
    return new AutoValue_ComicCharacterInfoShort.GsonTypeAdapter(gson);
  }
}
