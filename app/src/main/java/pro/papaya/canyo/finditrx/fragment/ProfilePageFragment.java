package pro.papaya.canyo.finditrx.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import pro.papaya.canyo.finditrx.R;

public class ProfilePageFragment extends Fragment {
  public static ProfilePageFragment INSTANCE = null;

  public static ProfilePageFragment getInstance() {
    if (INSTANCE == null) {
      ProfilePageFragment fragment = new ProfilePageFragment();
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

    View view = inflater.inflate(R.layout.main_fragment_profile, container, false);
    return view;
  }
}
