package pro.papaya.canyo.finditrx;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class AuthActivity extends AppCompatActivity {
  @BindView(R.id.auth_et_password)
  TextInputEditText etPassword;

  @BindView(R.id.auth_bth_login)
  Button btnLogin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_auth);
    ButterKnife.bind(this);

    btnLogin.setOnClickListener(v -> {
      Timber.d("Sample message");
    });
  }
}
