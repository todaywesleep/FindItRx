package pro.papaya.canyo.finditrx.fragment.leaderboard;

import java.util.List;

import io.reactivex.Observer;
import pro.papaya.canyo.finditrx.listener.ShortObserver;
import pro.papaya.canyo.finditrx.model.firebase.UserModel;

public class LevelFragment extends BaseLeaderBoardFragment {
  public static LevelFragment getNewInstance() {
    LevelFragment fragment = new LevelFragment();
    fragment.setRetainInstance(true);

    return fragment;
  }

  @Override
  protected Observer<List<UserModel>> getObserver() {
    return new ShortObserver<List<UserModel>>() {
      @Override
      public void onNext(List<UserModel> userModels) {
        adapter.setData(userModels);
      }

      @Override
      public void onError(Throwable e) {
        showSnackBar(e.getLocalizedMessage());
        logError(e);
      }
    };
  }
}
