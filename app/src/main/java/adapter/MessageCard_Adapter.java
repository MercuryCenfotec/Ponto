package adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

  @RequiresApi(api = Build.VERSION_CODES.O)
  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    Message message = messages.get(position);


    if (!message.getOwnerId().equals(userId)) {
      holder.cardSender.setVisibility(View.VISIBLE);
      holder.cardReceiver.setVisibility(View.GONE);
      if (chat.getBidderId().equals(message.getOwnerId()) && chat.getBidderImgUrl().length() > 0) {
        Picasso.get().load(chat.getBidderImgUrl()).into(holder.imageProfileSender);
      } else if (chat.getPetitionerId().equals(message.getOwnerId()) && chat.getPetitionerImgUrl().length() > 0) {
        Picasso.get().load(chat.getPetitionerImgUrl()).into(holder.imageProfileSender);
      }
      holder.txtSender.setText(message.getMessage());
    } else {
      holder.cardReceiver.setVisibility(View.VISIBLE);
      holder.cardSender.setVisibility(View.GONE);
      if (chat.getBidderId().equals(message.getOwnerId()) && chat.getBidderImgUrl().length() > 0) {
        Picasso.get().load(chat.getBidderImgUrl()).into(holder.imageProfileReceiver);
      } else if (chat.getPetitionerId().equals(message.getOwnerId()) && chat.getPetitionerImgUrl().length() > 0) {
        Picasso.get().load(chat.getPetitionerImgUrl()).into(holder.imageProfileReceiver);
      }
      holder.txtReceiver.setText(message.getMessage());
    }
  }

  @Override
  public int getItemCount() {
    return messages.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    ConstraintLayout cardSender, cardReceiver;
    ImageView imageProfileSender, imageProfileReceiver, imageSeenReceiver;
    TextView txtSender, txtReceiver;


    public ViewHolder(View itemView) {
      super(itemView);
      cardSender = itemView.findViewById(R.id.cardSender);
      cardReceiver = itemView.findViewById(R.id.cardReceiver);
      imageProfileSender = itemView.findViewById(R.id.imageProfileSender);
      imageProfileReceiver = itemView.findViewById(R.id.imageProfileReceiver);
      txtSender = itemView.findViewById(R.id.txtSender);
      txtReceiver = itemView.findViewById(R.id.txtReceiver);
    }
  }
}
