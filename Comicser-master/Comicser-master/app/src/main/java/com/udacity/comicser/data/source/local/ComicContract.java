package com.udacity.comicser.data.source.local;

import android.net.Uri;
import android.provider.BaseColumns;

@SuppressWarnings("WeakerAccess")
public class ComicContract {

  public static final String CONTENT_AUTHORITY = "com.sedsoftware.comicser";
  static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

  // Data paths
  static final String PATH_TODAY_ISSUES = "today_issues";
  static final String PATH_OWNED_ISSUES = "owned_issues";
  static final String PATH_TRACKED_VOLUMES = "tracked_volumes";

  // Issue info record
  public static final class IssueEntry implements BaseColumns {

    public static final Uri CONTENT_URI_TODAY_ISSUES = BASE_CONTENT_URI.buildUpon()
        .appendPath(PATH_TODAY_ISSUES)
        .build();

    static final String TABLE_NAME_TODAY_ISSUES = "today_issues";
    public static final String COLUMN_ISSUE_ID = "issue_id";
    public static final String COLUMN_ISSUE_NAME = "issue_name";
    public static final String COLUMN_ISSUE_NUMBER = "issue_number";
    public static final String COLUMN_ISSUE_STORE_DATE = "store_date";
    public static final String COLUMN_ISSUE_COVER_DATE = "cover_date";
    public static final String COLUMN_ISSUE_SMALL_IMAGE = "small_url";
    public static final String COLUMN_ISSUE_MEDIUM_IMAGE = "medium_url";
    public static final String COLUMN_ISSUE_HD_IMAGE = "super_url";
    public static final String COLUMN_ISSUE_VOLUME_ID = "volume_id";
    public static final String COLUMN_ISSUE_VOLUME_NAME = "volume_name";

    // Owned issue record (same columns)
    static final String TABLE_NAME_OWNED_ISSUES = "owned_issues";

    static final Uri CONTENT_URI_OWNED_ISSUES = BASE_CONTENT_URI.buildUpon()
        .appendPath(PATH_OWNED_ISSUES)
        .build();
  }

  // Tracked volume info record
  public static final class TrackedVolumeEntry implements BaseColumns {

    public static final Uri CONTENT_URI_TRACKED_VOLUMES = BASE_CONTENT_URI.buildUpon()
        .appendPath(PATH_TRACKED_VOLUMES)
        .build();

    static final String TABLE_NAME_TRACKED_VOLUMES = "tracked_volumes";
    public static final String COLUMN_VOLUME_ID = "volume_id";
    public static final String COLUMN_VOLUME_NAME = "volume_name";
    public static final String COLUMN_VOLUME_ISSUES_COUNT = "volume_issues_count";
    public static final String COLUMN_VOLUME_PUBLISHER_NAME = "volume_publisher_name";
    public static final String COLUMN_VOLUME_START_YEAR = "volume_start_year";
    public static final String COLUMN_VOLUME_SMALL_IMAGE = "small_url";
    public static final String COLUMN_VOLUME_MEDIUM_IMAGE = "medium_url";
    public static final String COLUMN_VOLUME_HD_IMAGE = "super_url";
  }
}