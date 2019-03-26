package pro.papaya.canyo.finditrx.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import pro.papaya.canyo.finditrx.R;
import timber.log.Timber;

public class AuthActivity extends BaseActivity {
  @BindView(R.id.auth_et_password)
  TextInputEditText etPassword;

  @BindView(R.id.auth_et_repeat_password_layout)
  TextInputLayout etRepeatPasswordLayout;
  @BindView(R.id.auth_et_repeat_password)
  TextInputEditText etRepeatPassword;

  @BindView(R.id.auth_bth_login)
  Button btnLogin;

  @BindView(R.id.auth_bth_register)
  Button btnRegister;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_auth);
    ButterKnife.bind(this);
    setListeners();
  }

  private void setListeners() {
    btnLogin.setOnClickListener(v -> {
      if (isActivityInRegistrationMode()) {
        setActivityMode(false);
      }
    });

    btnRegister.setOnClickListener(v -> {
      if (!isActivityInRegistrationMode()) {
        setActivityMode(true);
      }
    });
  }

  private boolean isActivityInRegistrationMode() {
    return etRepeatPasswordLayout.getVisibility() == View.VISIBLE;
  }

  private void setActivityMode(boolean toRegistration) {
    String newLoginButtonText = getString(toRegistration
        ? R.string.activity_auth_to_login
        : R.string.activity_auth_login);
    String newRegisterButtonText = getString(toRegistration
        ? R.string.activity_auth_register
        : R.string.activity_auth_to_registration);
    int newRepeatPasswordLabelVisibility = toRegistration
        ? View.VISIBLE
        : View.GONE;

    btnRegister.setText(newRegisterButtonText);
    btnLogin.setText(newLoginButtonText);
    etRepeatPasswordLayout.setVisibility(newRepeatPasswordLabelVisibility);
  }
}
