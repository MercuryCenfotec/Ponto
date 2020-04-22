package adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Offer;
import com.cenfotec.ponto.data.model.User;
import com.cenfotec.ponto.entities.offer.OfferDetailActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class OfferCard_Adapter extends RecyclerView.Adapter<OfferCard_Adapter.ViewHolder> {
    Context context;
    private List<Offer> offerList;
    private User activeUser;

    public OfferCard_Adapter(Context context, List<Offer> offerList) {
        this.context = context;
        this.offerList = offerList;
    }

    @Override
    public OfferCard_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_offer_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OfferCard_Adapter.ViewHolder holder, final int position) {
        DecimalFormat costFormat = new DecimalFormat("###,###.###");
        DecimalFormat durationFormat = new DecimalFormat("###.###");

        getUserById(offerList.get(position).getUserId(), holder);

        final SharedPreferences myPrefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        holder.cardCost.setText("₡ " + costFormat.format(offerList.get(position).getCost()));
        holder.cardDuration.setText(durationFormat.format(offerList.get(position).getDuration()) + (offerList.get(position).getDurationType().equals("hour") ? " horas" : " dias"));
        holder.cardDescription.setText(offerList.get(position).getDescription());

        if (offerList.get(position).getCounterOffer() && myPrefs.getString("userId", "none").equals(offerList.get(position).getUserId()) && !offerList.get(position).getCounterOfferCost().equals(offerList.get(position).getCost())) {
            holder.counterOfferIcon.setVisibility(View.VISIBLE);
            holder.counterOfferText.setVisibility(View.VISIBLE);
        } else {
            holder.counterOfferIcon.setVisibility(View.GONE);
            holder.counterOfferText.setVisibility(View.GONE);
        }

        holder.offerCard.setOnClickListener(
        new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, OfferDetailActivity.class);
                        myPrefs.edit().putString("offerId", offerList.get(position).getId()).commit();
                        context.startActivity(intent);
                    }
                });
        if (myPrefs.getString("userId", "none").equals(offerList.get(position).getUserId())) {
            holder.cardTitle.setText(offerList.get(position).getServicePetitionTitle());
        } else {
            holder.cardTitle.setText(offerList.get(position).getBidderName());
            holder.cardImage.setVisibility(View.VISIBLE);
            if (!offerList.get(position).getBidderImageUrl().equals(""))
                Picasso.get().load(offerList.get(position).getBidderImageUrl()).into(holder.cardImage);
        }
    }

    private void getUserById(String userId, final OfferCard_Adapter.ViewHolder holder) {
        final DatabaseReference userDBReference = FirebaseDatabase.getInstance().getReference();
        Query getUserByIdQuery = userDBReference.child("Users").orderByChild("id").equalTo(userId);
        getUserByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    activeUser = snapshot.getValue(User.class);
                    holder.ratingValue.setText(String.valueOf(activeUser.getRating()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cardTitle, cardDuration, cardCost, cardDescription, counterOfferText, ratingValue;
        CardView offerCard;
        ImageView cardImage, counterOfferIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            offerCard = itemView.findViewById(R.id.offerCard);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardDuration = itemView.findViewById(R.id.cardDuration);
            cardCost = itemView.findViewById(R.id.cardCost);
            cardDescription = itemView.findViewById(R.id.cardDescription);
            cardImage = itemView.findViewById(R.id.bidderImage);
            ratingValue = itemView.findViewById(R.id.ratingValue);

            counterOfferIcon = itemView.findViewById(R.id.counterOfferIcon);
            counterOfferText = itemView.findViewById(R.id.counterOfferText);
        }
    }
}
