package com.udacity.comicser.features.navigation;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.evernote.android.state.State;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.analytics.FirebaseAnalytics.Event;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.udacity.comicser.ComicserApp;
import com.udacity.comicser.R;
import com.udacity.comicser.base.BaseMvpActivity;
import com.udacity.comicser.features.navigation.factory.AppNavigation;
import com.udacity.comicser.features.navigation.factory.NavigationFragmentsFactory;
import com.udacity.comicser.features.preferences.ComicPreferencesHelper;
import com.udacity.comicser.features.sync.ComicSyncManager;
import com.udacity.comicser.utils.FragmentUtils;
import javax.inject.Inject;

public class NavigationActivity extends
    BaseMvpActivity<NavigationActivityView, NavigationActivityPresenter>
    implements NavigationActivityView, OnNavigationItemSelectedListener {

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.nav_view)
  NavigationView navigationView;
  @Nullable
  @BindView(R.id.drawer_layout)
  DrawerLayout drawer;

  @BindBool(R.bool.is_tablet_layout)
  boolean isTabletLayout;

  @State
  @AppNavigation.Section
  int currentSection;

  @Inject
  FirebaseAnalytics firebaseAnalytics;

  @Inject
  ComicPreferencesHelper comicPreferencesHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_navigation_view);

    ComicserApp
        .getAppComponent()
        .inject(this);

    setSupportActionBar(toolbar);
    setUpNavigationDrawerParams();

    if (savedInstanceState == null) {
      navigateToCurrentSection();
    }

    String defaultSyncPeriod = comicPreferencesHelper.getSyncPeriod();
    ComicSyncManager.createSyncAccount(this, Integer.parseInt(defaultSyncPeriod));
  }

  private void setUpNavigationDrawerParams() {

    if (!isTabletLayout) {
      ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
          this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
      drawer.addDrawerListener(toggle);
      toggle.syncState();
    }

    navigationView.setNavigationItemSelectedListener(this);
    navigationView.setCheckedItem(R.id.nav_issues);
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();
    handleChosenNavigationMenuItem(id);

    if (!isTabletLayout) {
      drawer.closeDrawer(GravityCompat.START);
    }

    return true;
  }

  @NonNull
  @Override
  public NavigationActivityPresenter createPresenter() {
    return new NavigationActivityPresenter();
  }

  @Override
  public void handleChosenNavigationMenuItem(int chosenMenuItem) {

    if (chosenMenuItem == R.id.nav_issues) {
      currentSection = AppNavigation.ISSUES;
    } else if (chosenMenuItem == R.id.nav_volumes) {
      currentSection = AppNavigation.VOLUMES;
    } else if (chosenMenuItem == R.id.nav_characters) {
      currentSection = AppNavigation.CHARACTERS;
    } else if (chosenMenuItem == R.id.nav_collection) {
      currentSection = AppNavigation.COLLECTION;
    } else if (chosenMenuItem == R.id.nav_tracker) {
      currentSection = AppNavigation.TRACKER;
    } else if (chosenMenuItem == R.id.nav_settings) {
      currentSection = AppNavigation.SETTINGS;
    }

    navigateToCurrentSection();
  }

  @Override
  public void navigateToCurrentSection() {

    logChosenNavigationSection(currentSection);

    FragmentManager manager = getSupportFragmentManager();

    Fragment fragment = NavigationFragmentsFactory.getFragment(manager, currentSection);

    FragmentUtils.replaceFragmentIn(
        manager, fragment, R.id.content_frame,
        NavigationFragmentsFactory.getFragmentTag(currentSection), false);

    restoreAppBarState();
  }

  private void logChosenNavigationSection(@AppNavigation.Section int section) {

    Bundle bundle = new Bundle();
    bundle.putInt(Param.ITEM_ID, section);
    bundle.putString(Param.ITEM_NAME, getChosenSectionName(section));
    firebaseAnalytics.logEvent(Event.SELECT_CONTENT, bundle);
  }

  private String getChosenSectionName(@AppNavigation.Section int section) {

    String chosenSection;

    switch (section) {
      case AppNavigation.ISSUES:
        chosenSection = "issues";
        break;
      case AppNavigation.VOLUMES:
        chosenSection = "volumes";
        break;
      case AppNavigation.CHARACTERS:
        chosenSection = "characters";
        break;
      case AppNavigation.COLLECTION:
        chosenSection = "collection";
        break;
      case AppNavigation.TRACKER:
        chosenSection = "tracker";
        break;
      case AppNavigation.SETTINGS:
        chosenSection = "settings";
        break;
      default:
        chosenSection = "unknown";
        break;
    }
    return chosenSection;
  }

  private void restoreAppBarState() {

    CoordinatorLayout coordinator = ButterKnife.findById(this, R.id.main_coordinator_layout);
    AppBarLayout appbar = ButterKnife.findById(this, R.id.appbar);
    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
    AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();

    if (behavior != null) {
      behavior.onNestedFling(coordinator, appbar, null, 0, -1000, true);
    }
  }
}
