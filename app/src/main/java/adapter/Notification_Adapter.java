package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Notification;
import com.cenfotec.ponto.entities.notification.NotificationFactory;

import java.util.List;

import customfonts.MyTextView_SF_Pro_Display_Medium;
import customfonts.TextViewSFProDisplayRegular;

public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.ViewHolder> {
  private Context context;
  private String userId;
  private List<Notification> notifications;

  public Notification_Adapter() {
  }

  public Notification_Adapter(Context context, List<Notification> notifications, String userId) {

    this.context = context;
    this.notifications = notifications;
    this.userId = userId;
  }

  @NonNull
  @Override
  public Notification_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_notification_card, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    Notification notification = notifications.get(position);
    holder.name.setText(notification.getTitle());
    holder.name.setText(notification.getDetail());
    holder.linear.setOnClickListener(NotificationFactory.createNotificationOnClick(context, notification));
  }

  @Override
  public int getItemCount() {
    return notifications.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    TextViewSFProDisplayRegular name, time;
    MyTextView_SF_Pro_Display_Medium message;
    LinearLayout linear;
    ImageView image;

    public ViewHolder(View itemView) {
      super(itemView);
      image = itemView.findViewById(R.id.image);
      message = itemView.findViewById(R.id.message);
      name = itemView.findViewById(R.id.name);
      time = itemView.findViewById(R.id.time);
      linear = itemView.findViewById(R.id.linear);
    }
  }
}