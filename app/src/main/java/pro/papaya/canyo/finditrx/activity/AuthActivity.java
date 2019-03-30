package pro.papaya.canyo.finditrx.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.firebase.FireBaseLoginManger;
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

  private Disposable authEventSubscription;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_auth);
    ButterKnife.bind(this);
    setListeners();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    authEventSubscription.dispose();
  }

  private void setListeners() {
    btnLogin.setOnClickListener(v -> {
      if (isActivityInRegistrationMode()) {
        setActivityMode(false);
      } else {

      }
    });

    btnRegister.setOnClickListener(v -> {
      if (!isActivityInRegistrationMode()) {
        setActivityMode(true);
      } else {
        signUp();
      }
    });

    etPassword.addTextChangedListener(new BaseTextWatcher() {
      @Override
      protected void onTextContentChanged(String s) {
        if (!s.isEmpty() && s.length() < Constants.MIN_PASSWORD_LENGTH) {
          etPasswordLayout.setError(getString(R.string.auth_password_length_error));
          setError(true);
        } else {
          etPasswordLayout.setErrorEnabled(false);
          setError(false);
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
          setError(true);
        } else {
          etRepeatPasswordLayout.setErrorEnabled(false);
          setError(false);
        }
      }
    });

    etEmail.addTextChangedListener(new BaseTextWatcher() {
      @Override
      protected void onTextContentChanged(String s) {
        if (isEmailValid(getEmailTextString())) {
          etEmailLayout.setError(getString(R.string.auth_email_error));
          setError(true);
        } else {
          etEmailLayout.setErrorEnabled(false);
          setError(false);
        }
      }
    });
  }

  private void setError(boolean isErrorExist) {
    if (isActivityInRegistrationMode()) {
      setRegisterButtonState(!isErrorExist);
    } else {
      setAuthButtonState(!isErrorExist);
    }
  }

  private String getEmailTextString() {
    return etEmail.getText() == null ? Constants.EMPTY_STRING : etEmail.getText()
        .toString();
  }

  private boolean isEmailValid(String email) {
    return !email.isEmpty() && !StringUtils.isEmailValid(email);
  }

  private boolean isRegistrationDataValid() {
    String email = etEmail.getText() == null
        ? Constants.EMPTY_STRING
        : etEmail.getText().toString();
    String password = etPassword.getText() == null
        ? Constants.EMPTY_STRING
        : etPassword.getText().toString();
    String repeatPassword = etRepeatPassword.getText() == null
        ? Constants.EMPTY_STRING
        : etRepeatPassword.getText().toString();
    return isEmailValid(email)
        && !password.isEmpty()
        && password.length() >= Constants.MIN_PASSWORD_LENGTH
        && repeatPassword.equals(password);
  }

  private boolean isAuthDataValid() {
    String email = etEmail.getText() == null
        ? Constants.EMPTY_STRING
        : etEmail.getText().toString();
    String password = etPassword.getText() == null
        ? Constants.EMPTY_STRING
        : etPassword.getText().toString();
    return isEmailValid(email)
        && !password.isEmpty()
        && password.length() >= Constants.MIN_PASSWORD_LENGTH;
  }

  private boolean isActivityInRegistrationMode() {
    return etRepeatPasswordLayout.getVisibility() == View.VISIBLE;
  }

  private void setActivityMode(boolean toRegistration) {
    String newLoginButtonText = getString(
        toRegistration ? R.string.auth_to_login : R.string.auth_login);
    String newRegisterButtonText = getString(
        toRegistration ? R.string.auth_register : R.string.auth_to_registration);
    int newRepeatPasswordLabelVisibility = toRegistration ? View.VISIBLE : View.GONE;

    btnRegister.setText(newRegisterButtonText);
    setRegisterButtonState(!toRegistration || isRegistrationDataValid());
    btnLogin.setText(newLoginButtonText);
    setAuthButtonState(toRegistration || isAuthDataValid());
    etRepeatPasswordLayout.setVisibility(newRepeatPasswordLabelVisibility);
  }

  private void setRegisterButtonState(boolean isEnabled){
    btnRegister.setEnabled(isEnabled);
  }

  private void setAuthButtonState(boolean isEnabled){
    btnLogin.setEnabled(isEnabled);
  }

  private void signUp() {
    authEventSubscription = FireBaseLoginManger.getInstance()
        .createRemoteUser(etEmail.getText()
                .toString(),
            etPassword.getText()
                .toString())
        .subscribe(fireBaseResponseModel -> {
          if (!fireBaseResponseModel.isResponseSuccessful()) {
            logDebug(
                "Registration failed with message: "
                    + fireBaseResponseModel.getMessage());
            showSnackBar(
                fireBaseResponseModel.getMessage());
          }
        });
  }
}
