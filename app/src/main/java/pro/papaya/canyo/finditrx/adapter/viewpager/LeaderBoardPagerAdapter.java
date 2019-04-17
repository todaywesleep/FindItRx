package pro.papaya.canyo.finditrx.adapter.viewpager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import pro.papaya.canyo.finditrx.fragment.leaderboard.ExperienceFragment;
import pro.papaya.canyo.finditrx.fragment.leaderboard.MoneyFragment;
import pro.papaya.canyo.finditrx.fragment.leaderboard.NewSubjectsFragment;
import pro.papaya.canyo.finditrx.model.view.LeaderBoardPagerModel;

public class LeaderBoardPagerAdapter extends FragmentPagerAdapter {
  private ExperienceFragment experienceFragment;
  private MoneyFragment moneyFragment;
  private NewSubjectsFragment newSubjectsFragment;

  public LeaderBoardPagerAdapter(FragmentManager fm) {
    super(fm);
    this.experienceFragment = ExperienceFragment.getNewInstance();
    this.moneyFragment = MoneyFragment.getNewInstance();
    this.newSubjectsFragment = NewSubjectsFragment.getNewInstance();
  }

  @Override
  public Fragment getItem(int position) {
    LeaderBoardPagerModel model = LeaderBoardPagerModel.values()[position];
    switch (model) {
      case EXPERIENCE_PAGE: {
        return experienceFragment;
      }

      case MONEY_PAGE: {
        return moneyFragment;
      }

      case NEW_SUBJECTS_PAGE: {
        return newSubjectsFragment;
      }
    }

    return null;
  }

  @Override
  public int getCount() {
    return LeaderBoardPagerModel.values().length;
  }
}
