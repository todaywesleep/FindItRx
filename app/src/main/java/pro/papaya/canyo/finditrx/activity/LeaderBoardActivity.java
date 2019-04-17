package pro.papaya.canyo.finditrx.activity;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import pro.papaya.canyo.finditrx.R;

public class LeaderBoardActivity extends BaseActivity {
  @BindView(R.id.leader_board_toolbar)
  Toolbar toolbar;
  @BindView(R.id.leader_board_view_pager)
  ViewPager viewPager;
  @BindView(R.id.leader_board_tab_navigator)
  TabLayout tabLayout;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_leader_board);
    ButterKnife.bind(this);
    configureToolbar();
  }

  private void configureToolbar() {
    toolbar.setNavigationOnClickListener(v -> finish());
  }
}
