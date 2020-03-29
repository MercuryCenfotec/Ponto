package adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.ServicePetition;
import com.cenfotec.ponto.data.model.ServiceType;
import com.cenfotec.ponto.entities.servicePetition.ServicePetitionBidderDetailActivity;
import com.cenfotec.ponto.entities.user.LoginActivity;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class ServicePetitionCard_Adapter extends RecyclerView.Adapter<ServicePetitionCard_Adapter.ViewHolder> {
    Context context;
    private List<ServicePetition> servicePetitionsArrayList;
    private Map<String,ServiceType> serviceTypesList;

    public ServicePetitionCard_Adapter(Context context, List<ServicePetition> servicePetitionsArrayList, Map<String,ServiceType> serviceTypesList) {
        this.context = context;
        this.servicePetitionsArrayList = servicePetitionsArrayList;
        this.serviceTypesList = serviceTypesList;
    }

    @Override
    public ServicePetitionCard_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_service_petition_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ServicePetitionCard_Adapter.ViewHolder holder, final int position) {
        ServiceType serviceType = serviceTypesList.get(servicePetitionsArrayList.get(position).getServiceTypeId());
        LightingColorFilter filter = new LightingColorFilter(Color.BLACK, Color.WHITE);

        holder.imageContainer.setBackgroundColor(Color.parseColor(serviceType.getColor()));
        Picasso.get().load(serviceType.getImgUrl()).into(holder.servicePetitionImage);
        holder.servicePetitionImage.setColorFilter(filter);
        holder.servicePetitionName.setText(servicePetitionsArrayList.get(position).getName());
        holder.servicePetitionDescription.setText(servicePetitionsArrayList.get(position).getDescription());
        holder.servicePetitionServiceType.setText(serviceType.getServiceType());
        holder.petitionCard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SharedPreferences sharedPreferences = context.getSharedPreferences(LoginActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
                Intent intent = new Intent(context, ServicePetitionBidderDetailActivity.class);
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
        ImageView servicePetitionImage;
        TextView servicePetitionName,servicePetitionDescription,servicePetitionServiceType;
        CardView petitionCard;
        LinearLayout imageContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            imageContainer = itemView.findViewById(R.id.imageContainer);
            servicePetitionImage=itemView.findViewById(R.id.servicePetitionImage);
            petitionCard = itemView.findViewById(R.id.petitionCard);
            servicePetitionName=itemView.findViewById(R.id.servicePetitionName);
            servicePetitionDescription=itemView.findViewById(R.id.servicePetitionDescription);
            servicePetitionServiceType=itemView.findViewById(R.id.servicePetitionServiceType);


        }
    }
}