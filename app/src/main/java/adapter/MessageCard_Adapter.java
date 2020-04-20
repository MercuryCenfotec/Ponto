package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Chat;
import com.cenfotec.ponto.data.model.Message;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MessageCard_Adapter extends RecyclerView.Adapter<MessageCard_Adapter.ViewHolder> {

  private Context context;
  private Chat chat;
  private String userId;
  private List<Message> messages;

  public MessageCard_Adapter() {
  }

  public MessageCard_Adapter(Context context, Chat chat, List<Message> messages, String userId) {

    this.context = context;
    this.chat = chat;
    this.messages = messages;
    this.userId = userId;
  }

  @NonNull
  @Override
  public MessageCard_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_message_card, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    Message message = messages.get(position);
    if (!message.getOwnerId().equals(userId)) {
      holder.cardSender.setVisibility(View.VISIBLE);
      if (chat.getBidderId().equals(userId) && !chat.getBidderImgUrl().equals("")) {
        Picasso.get().load(chat.getBidderImgUrl()).into(holder.imageProfileSender);
      } else if (chat.getPetitionerId().equals(userId) && !chat.getPetitionerImgUrl().equals("")) {
        Picasso.get().load(chat.getPetitionerImgUrl()).into(holder.imageProfileSender);
      }
      holder.txtSender.setText(message.getMessage());
      holder.txtTimeSender.setText(message.getDateTime());
    } else {
      holder.cardReceiver.setVisibility(View.VISIBLE);
      if (chat.getBidderId().equals(userId) && !chat.getBidderImgUrl().equals("")) {
        Picasso.get().load(chat.getBidderImgUrl()).into(holder.imageSeenReceiver);
      } else if (chat.getPetitionerId().equals(userId) && !chat.getPetitionerImgUrl().equals("")) {
        Picasso.get().load(chat.getPetitionerImgUrl()).into(holder.imageSeenReceiver);
      }
      holder.txtReceiver.setText(message.getMessage());
      holder.txtTimeReceiver.setText(message.getDateTime());
    }
  }

  @Override
  public int getItemCount() {
    return messages.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    ConstraintLayout cardSender, cardReceiver;
    ImageView imageProfileSender, imageSeenReceiver;
    TextView txtSender, txtTimeSender, txtReceiver, txtTimeReceiver;


    public ViewHolder(View itemView) {
      super(itemView);
      cardSender = itemView.findViewById(R.id.cardSender);
      cardReceiver = itemView.findViewById(R.id.cardReceiver);
      imageProfileSender = itemView.findViewById(R.id.imageProfileSender);
      imageSeenReceiver = itemView.findViewById(R.id.imageSeenReceiver);
      txtSender = itemView.findViewById(R.id.txtSender);
      txtTimeSender = itemView.findViewById(R.id.txtTimeSender);
      txtReceiver = itemView.findViewById(R.id.txtReceiver);
      txtTimeReceiver = itemView.findViewById(R.id.txtTimeReceiver);
    }
  }
}
