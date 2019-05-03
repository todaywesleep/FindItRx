package pro.papaya.canyo.finditrx.fragment.leaderboard;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import pro.papaya.canyo.finditrx.model.firebase.UserModel;

public class LevelFragment extends BaseLeaderBoardFragment {
  public static LevelFragment getNewInstance() {
    LevelFragment fragment = new LevelFragment();
    fragment.setRetainInstance(true);

    return fragment;
  }

  @Override
  protected Observer<List<UserModel>> getObserver() {
    return new Observer<List<UserModel>>() {
      @Override
      public void onSubscribe(Disposable d) {
        disposable = d;
      }

      @Override
      public void onNext(List<UserModel> userModels) {
        adapter.setData(userModels);
        logDebug("Level models here");
      }

      @Override
      public void onError(Throwable e) {
        showSnackBar(e.getLocalizedMessage());
        logError(e);
      }

      @Override
      public void onComplete() {

      }
    };
  }
}
