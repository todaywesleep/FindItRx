package pro.papaya.canyo.finditrx.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import pro.papaya.canyo.finditrx.fragment.ActionPageFragment;
import pro.papaya.canyo.finditrx.fragment.ProfilePageFragment;
import pro.papaya.canyo.finditrx.fragment.QuestsFragment;
import pro.papaya.canyo.finditrx.model.view.MainViewPagerModel;

public class MainPageAdapter extends FragmentPagerAdapter {
  private ActionPageFragment.ActionPageCallback actionPageCallback;

  public MainPageAdapter(FragmentManager fm,
                         ActionPageFragment.ActionPageCallback actionPageCallback) {
    super(fm);
    this.actionPageCallback = actionPageCallback;
  }

  @Override
  public Fragment getItem(int position) {
    MainViewPagerModel model = MainViewPagerModel.values()[position];
    switch (model) {
      case PROFILE_PAGE: {
        return ProfilePageFragment.getInstance();
      }

      case CAMERA_PAGE: {
        return ActionPageFragment.getInstance(actionPageCallback);
      }

      case STATS_PAGE: {
        return QuestsFragment.getInstance();
      }
    }

    return null;
  }

  @Override
  public int getCount() {
    return MainViewPagerModel.values().length;
  }

  public void refreshActionFragment(){
    ActionPageFragment.getInstance(actionPageCallback).refresh();
  }
}
