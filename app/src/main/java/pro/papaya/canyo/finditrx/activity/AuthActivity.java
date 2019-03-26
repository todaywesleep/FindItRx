package pro.papaya.canyo.finditrx.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.utils.BaseTextWatcher;
import pro.papaya.canyo.finditrx.utils.Constants;
import pro.papaya.canyo.finditrx.utils.StringUtils;

public class AuthActivity extends BaseActivity {
  @BindView(R.id.auth_et_email_layout)
  TextInputLayout etEmailLayout;
  @BindView(R.id.auth_et_email)
  TextInputEditText etEmail;

  @BindView(R.id.auth_et_password_layout)
  TextInputLayout etPasswordLayout;
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

    etPassword.addTextChangedListener(new BaseTextWatcher() {
      @Override
      protected void onTextContentChanged(String s) {
        if (!s.isEmpty() && s.length() < Constants.MIN_PASSWORD_LENGTH) {
          etPasswordLayout.setError(getString(R.string.auth_password_length_error));
        } else {
          etPasswordLayout.setErrorEnabled(false);
        }
      }
    });

    etRepeatPassword.addTextChangedListener(new BaseTextWatcher() {
      @Override
      protected void onTextContentChanged(String s) {
        String password = etPassword.getText() != null
            ? etPassword.getText().toString()
            : Constants.EMPTY_STRING;

        if (!s.isEmpty() && !s.equals(password)) {
          etRepeatPasswordLayout.setError(getString(R.string.auth_repeat_password_error));
        } else {
          etRepeatPasswordLayout.setErrorEnabled(false);
        }
      }
    });

    etEmail.addTextChangedListener(new BaseTextWatcher() {
      @Override
      protected void onTextContentChanged(String s) {
        if (!s.isEmpty() && !StringUtils.isEmailValid(s)) {
          etEmailLayout.setError(getString(R.string.auth_email_error));
        } else {
          etEmailLayout.setErrorEnabled(false);
        }
      }
    });
  }

  private boolean isActivityInRegistrationMode() {
    return etRepeatPasswordLayout.getVisibility() == View.VISIBLE;
  }

  private void setActivityMode(boolean toRegistration) {
    String newLoginButtonText = getString(toRegistration
        ? R.string.auth_to_login
        : R.string.auth_login);
    String newRegisterButtonText = getString(toRegistration
        ? R.string.auth_register
        : R.string.auth_to_registration);
    int newRepeatPasswordLabelVisibility = toRegistration
        ? View.VISIBLE
        : View.GONE;

    btnRegister.setText(newRegisterButtonText);
    btnLogin.setText(newLoginButtonText);
    etRepeatPasswordLayout.setVisibility(newRepeatPasswordLabelVisibility);
  }
}
