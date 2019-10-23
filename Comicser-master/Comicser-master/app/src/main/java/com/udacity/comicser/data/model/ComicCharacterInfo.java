package com.udacity.comicser.data.model;

import androidx.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 *  Full character info:
 *
 *  Sample request:
 *  /character
 *  /4005-42443
 *  ?api_key=API_KEY
 *  &format=json
 *
 *  4005 - constant record type, 42443 - character id
 */

@AutoValue
public abstract class ComicCharacterInfo {
  @Nullable public abstract String aliases();
  @Nullable public abstract String birth();
  public abstract int count_of_issue_appearances();
  @Nullable public abstract String description();
  public abstract int gender();
  public abstract long id();
  @Nullable public abstract ComicImages image();
  @Nullable public abstract String name();
  @Nullable public abstract ComicOriginInfoShort origin();
  @Nullable public abstract String real_name();

  public static TypeAdapter<ComicCharacterInfo> typeAdapter(Gson gson) {
    return new AutoValue_ComicCharacterInfo.GsonTypeAdapter(gson);
  }
}
