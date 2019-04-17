package pro.papaya.canyo.finditrx.fragment.leaderboard;

import java.util.List;

import io.reactivex.Observer;
import pro.papaya.canyo.finditrx.listener.ShortObserver;
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
    return new ShortObserver<List<UserModel>>() {
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
    };
  }
}
