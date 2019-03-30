package pro.papaya.canyo.finditrx.activity;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import pro.papaya.canyo.finditrx.utils.Constants;
import timber.log.Timber;

public abstract class BaseActivity extends AppCompatActivity {
  protected void logDebug(String message, Object... args) {
    Timber.d(Constants.LOG_OPENED_BRACKET
            .concat(getClass().getSimpleName())
            .concat(Constants.LOG_CLOSED_BRACKET)
            .concat(Constants.LOG_TAG_DIVIDER)
            .concat(message),
        args);
  }

  protected void showSnackBar(String message) {
    Snackbar.make(
        getWindow().getDecorView().findViewById(android.R.id.content),
        message,
        Snackbar.LENGTH_LONG
    ).show();
  }
}
