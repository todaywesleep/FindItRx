package pro.papaya.canyo.finditrx.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import pro.papaya.canyo.finditrx.activity.MainActivity;
import pro.papaya.canyo.finditrx.fragment.main.ActionFragment;
import pro.papaya.canyo.finditrx.fragment.main.ProfileFragment;
import pro.papaya.canyo.finditrx.fragment.main.QuestsFragment;
import pro.papaya.canyo.finditrx.model.view.LeaderBoardPagerModel;
import pro.papaya.canyo.finditrx.model.view.MainViewPagerModel;

public class LeaderBoardAdapter extends FragmentPagerAdapter {
  private ProfileFragment profileFragment;
  private ActionFragment actionFragment;
  private QuestsFragment questFragment;

  public LeaderBoardAdapter(FragmentManager fm,
                            MainActivity fragmentsCallback) {
    super(fm);
    this.profileFragment = ProfileFragment.getNewInstance(fragmentsCallback);
    this.actionFragment = ActionFragment.getNewInstance(fragmentsCallback);
    this.questFragment = QuestsFragment.getNewInstance(fragmentsCallback);
  }

  @Override
  public Fragment getItem(int position) {
    LeaderBoardPagerModel model = LeaderBoardPagerModel.values()[position];
    switch (model) {
      case LEVEL_PAGE: {
        return profileFragment;
      }

      case EXPERIENCE_PAGE: {
        return actionFragment;
      }

      case MONEY_PAGE: {
        return questFragment;
      }

      case NEW_SUBJECTS_PAGE: {
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
