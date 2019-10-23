package com.udacity.comicser.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.udacity.comicser.base.BaseLceFragment;

public class FragmentUtils {

  public static void addFragmentTo(@NonNull FragmentManager fragmentManager,
      @NonNull BaseLceFragment fragment, int frameId) {
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.add(frameId, fragment);
    transaction.commit();
  }

  public static void replaceFragmentIn(@NonNull FragmentManager fragmentManager,
      @NonNull Fragment fragment, int frameId, String tag, boolean addToBackstack) {
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.replace(frameId, fragment, tag);
    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
    if (addToBackstack) {
      transaction.addToBackStack(tag);
    }
    transaction.commit();
  }
}
