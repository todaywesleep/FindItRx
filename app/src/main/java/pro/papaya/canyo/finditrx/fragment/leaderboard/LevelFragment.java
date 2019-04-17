package pro.papaya.canyo.finditrx.fragment.leaderboard;

public class LevelFragment extends BaseLeaderBoardFragment {
  public static LevelFragment getNewInstance() {
    LevelFragment fragment = new LevelFragment();
    fragment.setRetainInstance(true);
//    Bundle arguments = new Bundle();
//    fragment.setArguments(arguments);

    return fragment;
  }
}
