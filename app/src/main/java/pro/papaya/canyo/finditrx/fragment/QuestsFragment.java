package pro.papaya.canyo.finditrx.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.model.firebase.ItemModel;
import pro.papaya.canyo.finditrx.viewmodel.QuestsViewModel;
import timber.log.Timber;

public class QuestsFragment extends BaseFragment {
  public static QuestsFragment INSTANCE = null;

  private QuestsViewModel questsViewModel;

  public static QuestsFragment getInstance() {
    if (INSTANCE == null) {
      QuestsFragment fragment = new QuestsFragment();
      Bundle arguments = new Bundle();
      fragment.setArguments(arguments);
      INSTANCE = fragment;
    }

    return INSTANCE;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.main_fragment_stats, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    questsViewModel = ViewModelProviders.of(this).get(QuestsViewModel.class);
    questsViewModel.getAllLabels()
        .subscribe(new Observer<List<ItemModel>>() {
          @Override
          public void onSubscribe(Disposable d) {

          }

          @Override
          public void onNext(List<ItemModel> itemModels) {
            Timber.d("TEST: %s", itemModels.toString());
          }

          @Override
          public void onError(Throwable e) {
            showSnackBar(e.getLocalizedMessage());
            logError(e);
          }

          @Override
          public void onComplete() {

          }
        });
  }
}
