package pro.papaya.canyo.finditrx.adapter.viewpager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import pro.papaya.canyo.finditrx.activity.MainActivity;
import pro.papaya.canyo.finditrx.fragment.main.ActionFragment;
import pro.papaya.canyo.finditrx.fragment.main.ProfileFragment;
import pro.papaya.canyo.finditrx.fragment.main.QuestsFragment;
import pro.papaya.canyo.finditrx.model.view.MainViewPagerModel;

public class MainPagerAdapter extends FragmentPagerAdapter {
  private ProfileFragment profileFragment;
  private ActionFragment actionFragment;
  private QuestsFragment questFragment;

  public MainPagerAdapter(FragmentManager fm,
                          MainActivity fragmentsCallback) {
    super(fm);
    this.profileFragment = ProfileFragment.getNewInstance(fragmentsCallback);
    this.actionFragment = ActionFragment.getNewInstance(fragmentsCallback);
    this.questFragment = QuestsFragment.getNewInstance(fragmentsCallback);
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
