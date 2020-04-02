package adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Offer;
import com.cenfotec.ponto.entities.offer.OfferDetailActivity;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class OfferCard_Adapter extends RecyclerView.Adapter<OfferCard_Adapter.ViewHolder> {
    Context context;
    private List<Offer> offerList;

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

        final SharedPreferences myPrefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        holder.cardCost.setText("₡ " + costFormat.format(offerList.get(position).getCost()));
        holder.cardDuration.setText(durationFormat.format(offerList.get(position).getDuration()) + (offerList.get(position).getDurationType().equals("hour") ? " horas" : " dias"));
        holder.cardDescription.setText(offerList.get(position).getDescription());
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

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cardTitle, cardDuration, cardCost, cardDescription;
        CardView offerCard;
        ImageView cardImage;

        public ViewHolder(View itemView) {
            super(itemView);
            offerCard = itemView.findViewById(R.id.offerCard);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardDuration = itemView.findViewById(R.id.cardDuration);
            cardCost = itemView.findViewById(R.id.cardCost);
            cardDescription = itemView.findViewById(R.id.cardDescription);
            cardImage = itemView.findViewById(R.id.bidderImage);
        }
    }
}
