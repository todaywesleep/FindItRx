package pro.papaya.canyo.finditrx.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.utils.BaseTextWatcher;
import pro.papaya.canyo.finditrx.utils.Constants;

public class ChangeNicknameDialog extends Dialog {
  public interface ChangeNicknameDialogCallback {
    void onPayClick(String newNickName);
  }

  @BindView(R.id.dialog_change_button)
  Button changeButton;
  @BindView(R.id.dialog_close_button)
  Button closeButton;
  @BindView(R.id.dialog_change_nick_label)
  TextInputLayout editNicknameLayout;
  @BindView(R.id.dialog_change_nick)
  TextInputEditText editNickname;

  private String userName;
  private ChangeNicknameDialogCallback callback;

  public ChangeNicknameDialog(@NonNull Context context,
                              String userName,
                              ChangeNicknameDialogCallback callback) {
    super(context);
    this.userName = userName;
    this.callback = callback;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getWindow() != null) {
      getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
    setContentView(R.layout.dialog_change_nickname);
    ButterKnife.bind(this);
    changeButton.setEnabled(false);

    setListeners();
  }

  private boolean isDataValid() {
    boolean hasErrors = false;

    if (editNickname.getText() != null) {
      String newNick = editNickname.getText().toString();
      if (newNick.length() < Constants.MIN_NICKNAME_LENGTH) {
        editNicknameLayout.setError(getContext().getString(R.string.auth_nickname_length_error));
        hasErrors = true;
      } else if (newNick.equals(userName)) {
        editNicknameLayout.setError(getContext().getString(R.string.auth_nickname_equals_error));
        hasErrors = true;
      }
    } else {
      hasErrors = true;
    }

    return !hasErrors;
  }

  public void setListeners() {
    changeButton.setText(getContext().getString(
        R.string.change_nick_change,
        Constants.PRICE_CHANGE_NICKNAME));

    editNickname.addTextChangedListener(new BaseTextWatcher() {
      @Override
      protected void onTextContentChanged(String s) {
        if (s.length() < Constants.MIN_NICKNAME_LENGTH) {
          editNicknameLayout.setError(getContext().getString(R.string.auth_nickname_length_error));
        } else if (s.equals(userName)) {
          editNicknameLayout.setError(getContext().getString(R.string.auth_nickname_equals_error));
        } else {
          editNicknameLayout.setErrorEnabled(false);
        }

        changeButton.setEnabled(!editNicknameLayout.isErrorEnabled());
      }
    });

    changeButton.setOnClickListener(v -> {
      if (callback != null) {
        if (isDataValid()) {
          dismiss();
          callback.onPayClick(Objects.requireNonNull(editNickname.getText()).toString());
        }
      }
    });
    closeButton.setOnClickListener(v -> dismiss());
  }
}
