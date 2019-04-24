package pro.papaya.canyo.finditrx.model.view;

import pro.papaya.canyo.finditrx.R;

public enum LeaderBoardPagerModel {
  LEVEL_PAGE(R.layout.main_fragment_action),
  BALANCE_PAGE(R.layout.main_fragment_quests),
  NEW_SUBJECTS_PAGE(R.layout.main_fragment_quests);

  private int layoutRes;

  LeaderBoardPagerModel(int layoutRes) {
    this.layoutRes = layoutRes;
  }

  public int getLayoutRes() {
    return layoutRes;
  }
}
