package pro.papaya.canyo.finditrx.activity;

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
}
