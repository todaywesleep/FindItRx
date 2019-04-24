package pro.papaya.canyo.finditrx.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.adapter.viewpager.LeaderBoardPagerAdapter;

public class LeaderBoardActivity extends BaseActivity {
  @BindView(R.id.leader_board_toolbar)
  Toolbar toolbar;
  @BindView(R.id.leader_board_view_pager)
  ViewPager viewPager;
  @BindView(R.id.leader_board_tab_navigator)
  TabLayout tabLayout;

  private LeaderBoardPagerAdapter pagerAdapter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_leader_board);
    ButterKnife.bind(this);
    configureToolbar();
    configureViewPager();
    setListeners();
  }

  private void configureViewPager() {
    pagerAdapter = new LeaderBoardPagerAdapter(getSupportFragmentManager());
    viewPager.setAdapter(pagerAdapter);
  }

  private void selectPage(int idx) {
    viewPager.setCurrentItem(idx, true);
  }

  private void setListeners() {
    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      }

      @Override
      public void onPageSelected(int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);

        if (tab != null) {
          tab.select();
        }
      }

      @Override
      public void onPageScrollStateChanged(int state) {
      }
    });

    tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        selectPage(tab.getPosition());
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {
      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {
      }
    });
  }

  private void configureToolbar() {
    toolbar.setNavigationOnClickListener(v -> finish());
  }
}
