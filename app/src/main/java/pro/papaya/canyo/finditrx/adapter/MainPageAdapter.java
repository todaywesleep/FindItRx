package pro.papaya.canyo.finditrx.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import pro.papaya.canyo.finditrx.fragment.ActionFragment;
import pro.papaya.canyo.finditrx.fragment.ProfileFragment;
import pro.papaya.canyo.finditrx.fragment.QuestsFragment;
import pro.papaya.canyo.finditrx.model.view.MainViewPagerModel;
import timber.log.Timber;

public class MainPageAdapter extends FragmentStatePagerAdapter {
  private ProfileFragment profileFragment;
  private ActionFragment actionFragment;
  private QuestsFragment questFragment;

  public MainPageAdapter(FragmentManager fm,
                         ActionFragment.ActionFragmentCallback actionFragmentCallback,
                         QuestsFragment.QuestFragmentCallback questFragmentCallback) {
    super(fm);
    this.profileFragment = ProfileFragment.getNewInstance();
    this.actionFragment = ActionFragment.getNewInstance(actionFragmentCallback);
    this.questFragment = QuestsFragment.getNewInstance(questFragmentCallback);
  }

  public void update(ActionFragment.ActionFragmentCallback actionFragmentCallback,
                     QuestsFragment.QuestFragmentCallback questFragmentCallback){
    actionFragment.setCallback(actionFragmentCallback);
    questFragment.setCallback(questFragmentCallback);
  }

  @Override
  public Fragment getItem(int position) {
    MainViewPagerModel model = MainViewPagerModel.values()[position];
    switch (model) {
      case PROFILE_PAGE: {
        return profileFragment;
      }

      case CAMERA_PAGE: {
        return actionFragment;
      }

      case QUESTS_PAGE: {
        return questFragment;
      }
    }

    return null;
  }

  @Override
  public int getCount() {
    return MainViewPagerModel.values().length;
  }

  public void refreshActionFragment() {
    actionFragment.refresh();
  }
}
