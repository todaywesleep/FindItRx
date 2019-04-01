package pro.papaya.canyo.finditrx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import pro.papaya.canyo.finditrx.model.view.MainViewPagerModel;

public class MainPageAdapter extends PagerAdapter {
  private Context context;

  public MainPageAdapter(Context context) {
    this.context = context;
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    MainViewPagerModel modelObject = MainViewPagerModel.values()[position];
    LayoutInflater inflater = LayoutInflater.from(context);
    ViewGroup layout = (ViewGroup) inflater.inflate(
        modelObject.getLayoutRes(),
        container,
        false
    );
    container.addView(layout);
    return layout;
  }

  @Override
  public int getCount() {
    return MainViewPagerModel.values().length;
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
    return view == object;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    container.removeView((View) object);
  }
}
