package pro.papaya.canyo.finditrx.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import pro.papaya.canyo.finditrx.R;

public class ActionPageFragment extends Fragment {
  public static ActionPageFragment INSTANCE = null;

  public static ActionPageFragment getInstance() {
    if (INSTANCE == null) {
      ActionPageFragment fragment = new ActionPageFragment();
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
    View view = inflater.inflate(R.layout.main_fragment_action, container, false);
    return view;
  }
}
