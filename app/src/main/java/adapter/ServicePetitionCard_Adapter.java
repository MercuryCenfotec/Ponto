package adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.ServicePetition;
import com.cenfotec.ponto.entities.bidder.BidderProfileActivity;
import com.cenfotec.ponto.entities.servicePetition.ServicePetitionDetailActivity;
import com.cenfotec.ponto.entities.user.LoginActivity;

import java.util.List;

public class ServicePetitionCard_Adapter extends RecyclerView.Adapter<ServicePetitionCard_Adapter.ViewHolder> {
    Context context;
    private List<ServicePetition> servicePetitionsArrayList;

    public ServicePetitionCard_Adapter(Context context, List<ServicePetition> servicePetitionsArrayList) {
        this.context = context;
        this.servicePetitionsArrayList = servicePetitionsArrayList;
    }

    @Override
    public ServicePetitionCard_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_service_petition_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ServicePetitionCard_Adapter.ViewHolder holder, final int position) {
//        holder.bitmap1.setImageResource(servicePetitionsArrayList.get(position).getBitmap1());
        holder.servicePetitionName.setText(servicePetitionsArrayList.get(position).getName());
        holder.servicePetitionDescription.setText(servicePetitionsArrayList.get(position).getDescription());
        holder.servicePetitionServiceType.setText(servicePetitionsArrayList.get(position).getServiceTypeId());
        holder.petitionCard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SharedPreferences sharedPreferences = context.getSharedPreferences(LoginActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
                Intent intent = new Intent(context, ServicePetitionDetailActivity.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("servicePetitionId", servicePetitionsArrayList.get(position).getId());
                editor.commit();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return servicePetitionsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView servicePetitionImage;
        TextView servicePetitionName,servicePetitionDescription,servicePetitionServiceType;
        CardView petitionCard;

        public ViewHolder(View itemView) {
            super(itemView);
//            servicePetitionImage=itemView.findViewById(R.id.servicePetitionImage);
            petitionCard = itemView.findViewById(R.id.petitionCard);
            servicePetitionName=itemView.findViewById(R.id.servicePetitionName);
            servicePetitionDescription=itemView.findViewById(R.id.servicePetitionDescription);
            servicePetitionServiceType=itemView.findViewById(R.id.servicePetitionServiceType);


        }
    }
}