package pro.papaya.canyo.finditrx.fragment.leaderboard;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import pro.papaya.canyo.finditrx.model.firebase.UserModel;

public class BalanceFragment extends BaseLeaderBoardFragment {
  public static BalanceFragment getNewInstance() {
    BalanceFragment fragment = new BalanceFragment();
    fragment.setRetainInstance(true);
//    Bundle arguments = new Bundle();
//    fragment.setArguments(arguments);

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
        logDebug("Balance models here");
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
