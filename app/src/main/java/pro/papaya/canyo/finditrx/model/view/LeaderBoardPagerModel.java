package pro.papaya.canyo.finditrx.model.view;

import pro.papaya.canyo.finditrx.R;

public enum LeaderBoardPagerModel {
  LEVEL_PAGE(R.layout.main_fragment_profile),
  EXPERIENCE_PAGE(R.layout.main_fragment_action),
  MONEY_PAGE(R.layout.main_fragment_stats),
  NEW_SUBJECTS_PAGE(R.layout.main_fragment_stats);

  private int layoutRes;

  LeaderBoardPagerModel(int layoutRes) {
    this.layoutRes = layoutRes;
  }

  public int getLayoutRes() {
    return layoutRes;
  }
}
