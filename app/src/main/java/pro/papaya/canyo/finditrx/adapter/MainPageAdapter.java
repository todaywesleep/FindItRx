package pro.papaya.canyo.finditrx.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import pro.papaya.canyo.finditrx.fragment.ActionPageFragment;
import pro.papaya.canyo.finditrx.fragment.ProfileFragment;
import pro.papaya.canyo.finditrx.fragment.QuestsFragment;
import pro.papaya.canyo.finditrx.model.view.MainViewPagerModel;

public class MainPageAdapter extends FragmentPagerAdapter {
  private ActionPageFragment.ActionPageCallback actionPageCallback;
  private QuestsFragment.QuestFragmentCallback fragmentCallback;

  public MainPageAdapter(FragmentManager fm,
                         ActionPageFragment.ActionPageCallback actionPageCallback,
                         QuestsFragment.QuestFragmentCallback fragmentCallback) {
    super(fm);
    this.actionPageCallback = actionPageCallback;
    this.fragmentCallback = fragmentCallback;
  }

  @Override
  public Fragment getItem(int position) {
    MainViewPagerModel model = MainViewPagerModel.values()[position];
    switch (model) {
      case PROFILE_PAGE: {
        return ProfileFragment.getInstance();
      }

      case CAMERA_PAGE: {
        return ActionPageFragment.getInstance(actionPageCallback);
      }

      case QUESTS_PAGE: {
        return QuestsFragment.getInstance(fragmentCallback);
      }
    }

    return null;
  }

  @Override
  public int getCount() {
    return MainViewPagerModel.values().length;
  }

  public void refreshActionFragment() {
    ActionPageFragment.getInstance(actionPageCallback).refresh();
  }
}
