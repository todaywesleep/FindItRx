package pro.papaya.canyo.finditrx.fragment;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.view.LoadingDialog;

public class BaseFragment extends Fragment {
  private LoadingDialog loadingDialog;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    loadingDialog = new LoadingDialog(getContext());
  }

  protected void showSnackBar(String message) {
    View view = getView();
    View rootView = null;
    if (view != null) {
      rootView = view.getRootView();
    }

    if (rootView != null && getContext() != null) {
      Snackbar snackbar = Snackbar.make(
          rootView,
          message,
          Snackbar.LENGTH_LONG
      );
      snackbar.setActionTextColor(ContextCompat.getColor(
          getContext(),
          R.color.snackBarText
      ));
      snackbar.show();
    }
  }

  protected void setLoading(boolean isLoading) {
    if (isLoading && getContext() != null) {
      loadingDialog.show(getContext(), getString(R.string.loading));
    } else {
      loadingDialog.dismiss();
    }
  }
}
