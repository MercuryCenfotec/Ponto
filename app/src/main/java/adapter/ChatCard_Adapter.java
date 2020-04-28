package adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Chat;
import com.cenfotec.ponto.entities.message.ChatMessagesActivity;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.util.List;

import customfonts.MyTextView_SF_Pro_Display_Medium;
import customfonts.TextViewSFProDisplayRegular;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatCard_Adapter extends RecyclerView.Adapter<ChatCard_Adapter.ViewHolder> {

  Context context;
  private List<Chat> chatList;
  private String userId;

  public ChatCard_Adapter(Context context, List<Chat> chatList, String userId) {
    this.context = context;
    this.chatList = chatList;
    this.userId = userId;
  }

  @Override
  public ChatCard_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chat_card, parent, false);
    return new ViewHolder(view);
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    holder.linear.setBackgroundResource(R.drawable.rect_white_border);
    if (chatList.get(position).getMessages() != null) {
      holder.message.setText(chatList.get(position).getMessages().get(chatList.get(position).getMessages().size() - 1).getMessage());
    } else {
      holder.message.setText("No tienes mensajes con esta persona...");
    }

    holder.linear.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        Intent intent;
        intent = new Intent(context, ChatMessagesActivity.class);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("chatId", chatList.get(position).getId());
        editor.commit();
        context.startActivity(intent);
      }
    });

    if (userId.equals(chatList.get(position).getPetitionerId())) {
      holder.name.setText(chatList.get(position).getBidderName());
      if (chatList.get(position).getBidderImgUrl().length() > 0) {
        Picasso.get().load(chatList.get(position).getBidderImgUrl()).into(holder.image);
      }
    } else {
      holder.name.setText(chatList.get(position).getPetitionerName());
      if (chatList.get(position).getPetitionerImgUrl().length() > 0) {
        Picasso.get().load(chatList.get(position).getPetitionerImgUrl()).into(holder.image);
      }
    }
    holder.petitionName.setText(chatList.get(position).getServicePetitionName());
  }

  @Override
  public int getItemCount() {
    return chatList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    TextViewSFProDisplayRegular name, time, petitionName;
    MyTextView_SF_Pro_Display_Medium message;
    CircleImageView image;
    LinearLayout linear;
    TextView n1;

    public ViewHolder(View itemView) {
      super(itemView);
      image = itemView.findViewById(R.id.image);
      message = itemView.findViewById(R.id.message);
      name = itemView.findViewById(R.id.name);
      petitionName = itemView.findViewById(R.id.petitionName);
      linear = itemView.findViewById(R.id.linear);
      n1 = itemView.findViewById(R.id.n1);
    }
  }
}
