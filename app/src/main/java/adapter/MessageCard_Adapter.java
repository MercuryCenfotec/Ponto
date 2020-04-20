package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Chat;
import com.cenfotec.ponto.data.model.Message;
import com.cenfotec.ponto.entities.message.ChatMessagesActivity;

import java.util.ArrayList;
import java.util.List;

public class MessageCard_Adapter extends RecyclerView.Adapter<MessageCard_Adapter.ViewHolder> {


  private Context context;
  private Chat chat;
  private String userId;
  private List<Message> messages;

  public MessageCard_Adapter() {
  }

  public MessageCard_Adapter(ChatMessagesActivity context, Chat chat, String userId) {
    this.context = context;
    this.chat = chat;
    this.userId = userId;
    if (chat.getMessages() != null) {
      this.messages = chat.getMessages();
    } else {
      this.messages = new ArrayList<>();
    }
  }

  @NonNull
  @Override
  public MessageCard_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_message_card, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    if(messages.get(position).getOwnerId().equals(userId)){

    }else{

    }
  }

  @Override
  public int getItemCount() {
    return messages.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    public ViewHolder(View itemView) {
      super(itemView);
//      timePoint = itemView.findViewById(R.id.timePoint);
    }
  }
}
