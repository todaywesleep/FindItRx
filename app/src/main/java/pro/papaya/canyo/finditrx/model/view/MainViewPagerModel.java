package pro.papaya.canyo.finditrx.model.view;

import pro.papaya.canyo.finditrx.R;

public enum MainViewPagerModel {
  PROFILE_PAGE(R.string.main_profile_title, R.layout.main_fragment_profile),
  CAMERA_PAGE(R.string.main_photo_title, R.layout.main_fragment_action),
  QUESTS_PAGE(R.string.main_quests_title, R.layout.main_fragment_stats);

  private int stringRes;
  private int layoutRes;

  MainViewPagerModel(int stringRes, int layoutRes){
    this.stringRes = stringRes;
    this.layoutRes = layoutRes;
  }

  public int getStringRes(){
    return stringRes;
  }

  public int getLayoutRes(){
    return layoutRes;
  }
}
