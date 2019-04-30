package pro.papaya.canyo.finditrx.adapter.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.model.firebase.UserModel;
import pro.papaya.canyo.finditrx.model.view.LeaderBoardPagerModel;
import pro.papaya.canyo.finditrx.utils.Constants;
import pro.papaya.canyo.finditrx.utils.PixelUtils;
import timber.log.Timber;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder> {
  public interface LeaderBoardAdapterCallback {
    void onUserFounded(String username, String place);
  }

  private final static int FIRST_PLACE_IDX = 0;
  private final static int SECOND_PLACE_IDX = 1;
  private final static int THIRD_PLACE_IDX = 2;
  private final static int CARD_STROKE_WIDTH = 2;
  private final static int CARD_STROKE_WIDTH_BIG = 4;

  private List<UserModel> data = new ArrayList<>();
  private LeaderBoardPagerModel model;
  private String userId;
  private Context context;
  private LeaderBoardAdapterCallback callback;

  public LeaderBoardAdapter(LeaderBoardPagerModel model,
                            Context context,
                            LeaderBoardAdapterCallback callback,
                            String userId) {
    this.model = model;
    this.context = context;
    this.userId = userId;
    this.callback = callback;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View rootView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_leader_board_user, parent, false);

    return new ViewHolder(rootView);
  }

  @Override
  @SuppressLint("SetTextI18n")
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    UserModel model = data.get(position);
    holder.userIdx.setText(Integer.toString(position + 1).concat(Constants.DOT));
    holder.userName.setText(model.getNickName());
    holder.score.setText(getRequiredScore(model));
    applyCardStyle(holder, position);
    holder.setOnClickListener(v -> {
      Timber.d("TEST item %s clicked", model.getNickName());
    });
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  public void setData(List<UserModel> newData) {
    data.clear();
    data.addAll(newData);
    lookForUser(data);

    notifyDataSetChanged();
  }

  private void lookForUser(List<UserModel> data) {
    for (UserModel user : data) {
      if (user.getId().equals(userId)) {
        if (callback != null) {
          callback.onUserFounded(
              user.getNickName(),
              Integer.toString(data.indexOf(user) + 1)
          );
        }

        break;
      }
    }
  }

  private void applyCardStyle(@NonNull ViewHolder holder, int position) {
    GradientDrawable drawable = (GradientDrawable) holder.rootView.getBackground();
    int strokeColorResource = R.color.rating_common;
    int widthDp = CARD_STROKE_WIDTH;

    switch (position) {
      case FIRST_PLACE_IDX: {
        holder.scoreImage.setImageResource(R.drawable.ic_gold_medal);
        strokeColorResource = R.color.rating_gold;
        widthDp = CARD_STROKE_WIDTH_BIG;
        break;
      }

      case SECOND_PLACE_IDX: {
        holder.scoreImage.setImageResource(R.drawable.ic_silver_medal);
        strokeColorResource = R.color.rating_silver;
        widthDp = CARD_STROKE_WIDTH_BIG;
        break;
      }

      case THIRD_PLACE_IDX: {
        holder.scoreImage.setImageResource(R.drawable.ic_bronze_medal);
        strokeColorResource = R.color.rating_bronze;
        widthDp = CARD_STROKE_WIDTH_BIG;
        break;
      }

      default: {
        holder.userIdx.setVisibility(View.VISIBLE);
        holder.scoreImage.setVisibility(View.GONE);
        break;
      }
    }

    drawable.setStroke(PixelUtils.dipToPx(context, widthDp), ContextCompat.getColor(
        context, strokeColorResource
    ));
  }

  private String getRequiredScore(UserModel model) {
    switch (this.model) {
      case LEVEL_PAGE: {
        return Integer.toString(model.getLevel());
      }
      case NEW_SUBJECTS_PAGE: {
        return Integer.toString(model.getFoundedSubjects());
      }
      default: {
        return Long.toString(model.getBalance());
      }
    }
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    View rootView;
    @BindView(R.id.item_leader_board_user_idx)
    TextView userIdx;
    @BindView(R.id.item_leader_board_user_name)
    TextView userName;
    @BindView(R.id.item_leader_board_score)
    TextView score;
    @BindView(R.id.item_leader_board_user_idx_img)
    ImageView scoreImage;

    public void setOnClickListener(View.OnClickListener onClickListener) {
      rootView.setOnClickListener(onClickListener);
    }

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      rootView = itemView;
      ButterKnife.bind(this, itemView);
    }
  }
}
