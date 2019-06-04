package pro.papaya.canyo.finditrx.adapter.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.model.firebase.NotificationModel;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {
  private List<NotificationModel> data = new ArrayList<>();

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View rootView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_notification, parent, false);

    return new ViewHolder(rootView);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    NotificationModel currentModel = data.get(position);
    holder.creator.setText(currentModel.getCreator());
    holder.date.setText(currentModel.getDateTime());
    holder.content.setText(currentModel.getContent());
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  public void updateData(List<NotificationModel> newData) {
    this.data.clear();
    this.data.addAll(newData);
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    View rootView;
    @BindView(R.id.notification_creator)
    TextView creator;
    @BindView(R.id.notification_date_time)
    TextView date;
    @BindView(R.id.notification_content)
    TextView content;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      rootView = itemView.findViewById(R.id.item_user_quest_root);
      ButterKnife.bind(this, itemView);
    }
  }
}
