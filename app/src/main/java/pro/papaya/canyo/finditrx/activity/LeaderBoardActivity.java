package pro.papaya.canyo.finditrx.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.adapter.viewpager.LeaderBoardPagerAdapter;
import timber.log.Timber;

public class LeaderBoardActivity extends BaseActivity {
  @BindView(R.id.leader_board_view_pager)
  ViewPager viewPager;
  @BindView(R.id.leader_board_tab_navigator)
  TabLayout tabLayout;
  @BindView(R.id.leader_board_back)
  ImageButton back;

  private LeaderBoardPagerAdapter pagerAdapter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_leader_board);
    ButterKnife.bind(this);
//    TODO Uncomment to setup toolbar
//    configureToolbar();
    setUpViews();
    configureViewPager();
    setListeners();
  }

  private void setUpViews(){
    back.setOnClickListener(v -> {
      finish();
    });
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
}
