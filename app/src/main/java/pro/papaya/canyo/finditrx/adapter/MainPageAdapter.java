package pro.papaya.canyo.finditrx.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import pro.papaya.canyo.finditrx.fragment.ActionPageFragment;
import pro.papaya.canyo.finditrx.fragment.ProfilePageFragment;
import pro.papaya.canyo.finditrx.fragment.StatsPageFragment;
import pro.papaya.canyo.finditrx.model.view.MainViewPagerModel;

public class MainPageAdapter extends FragmentPagerAdapter {
  public MainPageAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int position) {
    MainViewPagerModel model = MainViewPagerModel.values()[position];
    switch (model) {
      case PROFILE_PAGE: {
        return ProfilePageFragment.getInstance();
      }

      case CAMERA_PAGE: {
        return ActionPageFragment.getInstance();
      }

      case STATS_PAGE: {
        return StatsPageFragment.getInstance();
      }
    }

    return null;
  }

  @Override
  public int getCount() {
    return MainViewPagerModel.values().length;
  }
}
