package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Offer;

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
    public void onBindViewHolder(OfferCard_Adapter.ViewHolder holder, int position) {
        holder.cardTitle.setText(offerList.get(position).getServicePetitionTitle());
        holder.cardCost.setText(offerList.get(position).getCost().toString());
        holder.cardDuration.setText(offerList.get(position).getDuration() + (offerList.get(position).getDurationType().equals("hour") ? " horas" : " dias"));
        holder.cardDescription.setText(offerList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cardTitle, cardDuration, cardCost, cardDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardDuration = itemView.findViewById(R.id.cardDuration);
            cardCost = itemView.findViewById(R.id.cardCost);
            cardDescription = itemView.findViewById(R.id.cardDescription);


        }
    }
}
