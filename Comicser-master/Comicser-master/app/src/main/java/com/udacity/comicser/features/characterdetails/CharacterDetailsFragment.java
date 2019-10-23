package com.udacity.comicser.features.characterdetails;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;
import com.udacity.comicser.ComicserApp;
import com.udacity.comicser.R;
import com.udacity.comicser.base.BaseLceFragment;
import com.udacity.comicser.data.model.ComicCharacterInfo;
import com.udacity.comicser.data.model.ComicImages;
import com.udacity.comicser.data.model.ComicOriginInfoShort;
import com.udacity.comicser.data.source.remote.dagger.modules.ComicRemoteDataModule;
import com.udacity.comicser.utils.HtmlUtils;
import com.udacity.comicser.utils.ImageUtils;

@SuppressWarnings("WeakerAccess")
@FragmentWithArgs
public class CharacterDetailsFragment extends
    BaseLceFragment<CardView, ComicCharacterInfo, CharacterDetailsView, CharacterDetailsPresenter>
    implements CharacterDetailsView {

  @Arg
  long characterId;

  @BindView(R.id.character_details_screen)
  ImageView characterPoster;
  @BindView(R.id.character_details_name)
  TextView characterName;
  @BindView(R.id.character_detail_real_name)
  TextView characterRealName;
  @BindView(R.id.character_detail_aliases)
  TextView characterAliases;
  @BindView(R.id.character_detail_birthdate)
  TextView characterBirthdate;
  @BindView(R.id.character_detail_origin)
  TextView characterOrigin;
  @BindView(R.id.character_detail_gender)
  TextView characterGender;
  @BindView(R.id.character_details_description)
  TextView characterDescription;

  private CharacterDetailsComponent characterDetailsComponent;
  private ComicCharacterInfo currentCharacterInfo;

  // --- FRAGMENT LIFECYCLE ---

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    setRetainInstance(true);

    if (savedInstanceState != null) {
      loadData(false);
    }
  }

  // --- BASE LCE FRAGMENT ---

  @Override
  protected int getLayoutRes() {
    return R.layout.fragment_character_details;
  }

  @NonNull
  @Override
  public CharacterDetailsPresenter createPresenter() {
    return characterDetailsComponent.presenter();
  }

  @Override
  protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
    return e.getMessage();
  }

  @Override
  protected void injectDependencies() {
    characterDetailsComponent = ComicserApp.getAppComponent()
        .plusRemoteComponent(new ComicRemoteDataModule())
        .plusCharacterDetailsComponent();

    characterDetailsComponent.inject(this);
  }

  // --- MVP VIEW STATE ---

  @Override
  public ComicCharacterInfo getData() {
    return currentCharacterInfo;
  }

  @NonNull
  @Override
  public LceViewState<ComicCharacterInfo, CharacterDetailsView> createViewState() {
    return new RetainingLceViewState<>();
  }

  // --- MVP VIEW ---


  @Override
  public void showContent() {
    contentView.setVisibility(View.VISIBLE);
    errorView.setVisibility(View.GONE);
    loadingView.setVisibility(View.GONE);
  }

  @Override
  public void showError(Throwable e, boolean pullToRefresh) {
    errorView.setText(R.string.error_issue_not_available);
    contentView.setVisibility(View.GONE);
    loadingView.setVisibility(View.GONE);
    errorView.setVisibility(View.VISIBLE);
  }

  @Override
  public void showLoading(boolean pullToRefresh) {
    if (pullToRefresh) {
      contentView.setVisibility(View.GONE);
      errorView.setVisibility(View.GONE);
      loadingView.setVisibility(View.VISIBLE);
    } else {
      contentView.setVisibility(View.VISIBLE);
      errorView.setVisibility(View.GONE);
      loadingView.setVisibility(View.GONE);
    }
  }

  @Override
  public void setData(ComicCharacterInfo data) {
    currentCharacterInfo = data;
    bindVolumeToUi(currentCharacterInfo);
  }

  @Override
  public void loadData(boolean pullToRefresh) {
    presenter.loadCharacterDetails(characterId);
  }

  // --- UI BINDING UTILS ---

  private void bindVolumeToUi(ComicCharacterInfo character) {
    loadHeaderImage(characterPoster, character.image());
    setUsualText(characterName, character.name());
    setUsualText(characterRealName, character.real_name());
    setUsualText(characterAliases, character.aliases());
    setUsualText(characterBirthdate, character.birth());
    setOrigin(characterOrigin, character.origin());
    setGender(characterGender, character.gender());
    setDescription(characterDescription, character.description());
  }

  private void loadHeaderImage(ImageView header, ComicImages image) {
    if (image != null) {
      String imageUrl = image.small_url();
      ImageUtils.loadImageWithTopCrop(header, imageUrl);
    } else {
      header.setVisibility(View.GONE);
    }
  }

  private void setUsualText(TextView textView, String text) {
    if (text != null) {
      textView.setText(text);
    } else {
      textView.setText("-");
    }
  }

  private void setOrigin(TextView textView, ComicOriginInfoShort origin) {
    if (origin != null) {
      textView.setText(origin.name());
    } else {
      textView.setText("-");
    }
  }

  private void setGender(TextView textView, int gender) {
    if (gender == 1 ) {
      textView.setText(R.string.character_details_gender_male);
    } else if (gender == 2){
      textView.setText(R.string.character_details_gender_female);
    } else {
      textView.setText(R.string.character_details_gender_unknown);
    }
  }

  private void setDescription(TextView textView, String description) {
    if (description != null) {
      textView.setText(HtmlUtils.parseHtmlText(description));
    } else {
      textView.setVisibility(View.GONE);
    }
  }
}
