package com.udacity.comicser.data.source.local;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import android.content.ContentValues;
import android.database.Cursor;
import com.udacity.comicser.data.model.ComicImages;
import com.udacity.comicser.data.model.ComicIssueInfoList;
import com.udacity.comicser.data.model.ComicPublisherInfo;
import com.udacity.comicser.data.model.ComicVolumeInfoList;
import com.udacity.comicser.data.model.ComicVolumeInfoShort;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class TestUtils {

  static ComicIssueInfoList getDummyIssueInfo() {
    return ComicIssueInfoList.builder()
        .id(123)
        .issue_number(321)
        .name("name")
        .store_date("store_date")
        .cover_date("cover_date")
        .image(
            ComicImages.builder()
                .icon_url("icon_url")
                .medium_url("medium_url")
                .screen_url("screen_url")
                .small_url("small_url")
                .super_url("super_url")
                .thumb_url("thumb_url")
                .tiny_url("tiny_url")
                .build())
        .volume(
            ComicVolumeInfoShort.builder()
                .id(123)
                .name("name")
                .build()
        )
        .build();
  }

  static ComicVolumeInfoList getDummyVolumeInfo() {
    return ComicVolumeInfoList.builder()
        .count_of_issues(123)
        .id(321)
        .image(
            ComicImages.builder()
                .icon_url("icon_url")
                .medium_url("medium_url")
                .screen_url("screen_url")
                .small_url("small_url")
                .super_url("super_url")
                .thumb_url("thumb_url")
                .tiny_url("tiny_url")
                .build())
        .name("Tets issue")
        .publisher(ComicPublisherInfo.builder()
            .name("DC Comics")
            .id(11111)
            .build())
        .start_year(2003)
        .build();
  }

  static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
    assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
    validateCurrentRecord(error, valueCursor, expectedValues);
    valueCursor.close();
  }

  static void validateCurrentRecord(String error, Cursor valueCursor,
      ContentValues expectedValues) {
    Set<Entry<String, Object>> valueSet = expectedValues.valueSet();
    for (Map.Entry<String, Object> entry : valueSet) {
      String columnName = entry.getKey();
      int idx = valueCursor.getColumnIndex(columnName);
      assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
      String expectedValue = entry.getValue().toString();
      assertEquals("Value '" + entry.getValue().toString() +
          "' did not match the expected value '" +
          expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
    }
  }
}
