package pro.papaya.canyo.finditrx.activity;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.utils.Constants;
import pro.papaya.canyo.finditrx.view.LoadingDialog;
import timber.log.Timber;

public abstract class BaseActivity extends AppCompatActivity {
  protected RelativeLayout progressContainer;
  protected FrameLayout contentContainer;
  protected LoadingDialog loadingDialog;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    loadingDialog = new LoadingDialog(BaseActivity.this);
  }

  @Override
  public void setContentView(int layoutResID) {
    super.setContentView(R.layout.activity_base);
    contentContainer = findViewById(R.id.base_container);
    contentContainer.addView(getLayoutInflater().inflate(layoutResID, null));
    ButterKnife.bind(this);
  }

  protected void setLoading(boolean isLoading) {
    if (isLoading) {
      loadingDialog.show(this, getString(R.string.loading));
    } else {
      loadingDialog.dismiss();
    }
  }

  protected void logDebug(String message, Object... args) {
    Timber.d(Constants.LOG_OPENED_BRACKET
            .concat(getClass().getSimpleName())
            .concat(Constants.LOG_CLOSED_BRACKET)
            .concat(Constants.LOG_TAG_DIVIDER)
            .concat(message),
        args);
  }

  protected void logError(Throwable e) {
    Timber.e(Constants.LOG_OPENED_BRACKET
        .concat(getClass().getSimpleName())
        .concat(Constants.LOG_CLOSED_BRACKET)
        .concat(Constants.LOG_TAG_DIVIDER)
        .concat(e.getLocalizedMessage()));
  }

  protected void showSnackBar(String message) {
    Snackbar.make(
        getWindow().getDecorView().findViewById(android.R.id.content),
        message,
        Snackbar.LENGTH_LONG
    ).show();
  }
}
