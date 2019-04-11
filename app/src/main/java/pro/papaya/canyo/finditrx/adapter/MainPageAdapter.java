package pro.papaya.canyo.finditrx.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import pro.papaya.canyo.finditrx.fragment.ActionFragment;
import pro.papaya.canyo.finditrx.fragment.ProfileFragment;
import pro.papaya.canyo.finditrx.fragment.QuestsFragment;
import pro.papaya.canyo.finditrx.model.view.MainViewPagerModel;

public class MainPageAdapter extends FragmentPagerAdapter {
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
