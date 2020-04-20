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
import androidx.recyclerview.widget.RecyclerView;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.entities.account.AccountActivity;
import com.cenfotec.ponto.entities.appointment.AppointmentAgendaActivity;
import com.cenfotec.ponto.entities.bidder.BidderUpdateActivity;
import com.cenfotec.ponto.entities.contract.ContractsListActivity;
import com.cenfotec.ponto.entities.petitioner.PetitionerUpdateActivity;
import com.cenfotec.ponto.entities.membership.MembershipAcquisitionActivity;
import com.cenfotec.ponto.utils.LogoutHelper;

import java.util.ArrayList;

import model.ProfileModel;

/**
 * Created by wolfsoft4 on 20/9/18.
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    Context context;
    private ArrayList<ProfileModel> profileModelArrayList;

    public ProfileAdapter(Context context, ArrayList<ProfileModel> profileModelArrayList) {
        this.context = context;
        this.profileModelArrayList = profileModelArrayList;
    }

    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder holder, final int position) {
        holder.inbox.setImageResource(profileModelArrayList.get(position).getInbox());
        holder.arrow.setImageResource(profileModelArrayList.get(position).getArrow());
        holder.txttrades.setText(profileModelArrayList.get(position).getTxttrades());
        holder.txthistory.setText(profileModelArrayList.get(position).getTxthistory());
        if (profileModelArrayList.get(position).getUserType().equals("petitioner")){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    switch (position) {
                        case 0:
                            Intent iac = new Intent(context, AppointmentAgendaActivity.class);
                            iac.putExtra("userId", profileModelArrayList.get(position).getUserId());
                            iac.putExtra("petitionerId", "");
                            iac.putExtra("bidderId", "");
                            if(profileModelArrayList.get(position).getUserType().equals("petitioner")){
                                iac.putExtra("userType", "petitioner");
                            }else{
                                iac.putExtra("userType", "bidder");
                            }
                            context.startActivity(iac);
                            break;
                        case 1:
                            //Intent i = new Intent(context, CLASE DE RESEÑAS.class);
                            //context.startActivity(i);
                            break;

                        case 2:
                            Intent inte = new Intent(context, AccountActivity.class);
                            inte.putExtra("userId", profileModelArrayList.get(position).getUserId());
                            context.startActivity(inte);
                            break;

                        case 3:
                            Intent intent = new Intent(context, ContractsListActivity.class);
                            context.startActivity(intent);
                            break;

                        case 4:
                            if(profileModelArrayList.get(position).getUserType().equals("petitioner")){
                                Intent i = new Intent(context, PetitionerUpdateActivity.class);
                                i.putExtra("petitionerId", profileModelArrayList.get(position).getUserId());
                                context.startActivity(i);
                            }else{
                                Intent i = new Intent(context, BidderUpdateActivity.class);
                                i.putExtra("bidderId", profileModelArrayList.get(position).getUserId());
                                context.startActivity(i);
                            }

                            break;

                        case 5:
                            LogoutHelper.logout(context);
                            break;
                    }
                }
            });
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    switch (position) {
                        case 0:
                            Intent iac = new Intent(context, AppointmentAgendaActivity.class);
                            iac.putExtra("userId", profileModelArrayList.get(position).getUserId());
                            iac.putExtra("petitionerId", "");
                            iac.putExtra("bidderId", "");
                            if(profileModelArrayList.get(position).getUserType().equals("petitioner")){
                                iac.putExtra("userType", "petitioner");
                            }else{
                                iac.putExtra("userType", "bidder");
                            }
                            context.startActivity(iac);
                            break;

                        case 1:
                            Intent membershipsIntent = new Intent(context, MembershipAcquisitionActivity.class);
                            context.startActivity(membershipsIntent);
                            break;

                        case 2:
                            //Intent i = new Intent(context, CLASE DE RESEÑAS.class);
                            //context.startActivity(i);
                            break;

                        case 3:
                            Intent inte = new Intent(context, AccountActivity.class);
                            inte.putExtra("userId", profileModelArrayList.get(position).getUserId());
                            context.startActivity(inte);
                            break;

                        case 4:
                            Intent intent = new Intent(context, ContractsListActivity.class);
                            context.startActivity(intent);
                            break;

                        case 5:
                            if(profileModelArrayList.get(position).getUserType().equals("petitioner")){
                                Intent i = new Intent(context, PetitionerUpdateActivity.class);
                                i.putExtra("petitionerId", profileModelArrayList.get(position).getUserId());
                                context.startActivity(i);
                            }else{
                                Intent i = new Intent(context, BidderUpdateActivity.class);
                                i.putExtra("bidderId", profileModelArrayList.get(position).getUserId());
                                context.startActivity(i);
                            }

                            break;

                        case 6:
                            LogoutHelper.logout(context);
                            break;
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return profileModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView inbox,arrow;
        TextView txttrades,txthistory;

        public ViewHolder(View itemView) {
            super(itemView);

            inbox = itemView.findViewById(R.id.inbox);
            arrow=itemView.findViewById(R.id.arrow);
            txttrades=itemView.findViewById(R.id.txttrades);
            txthistory=itemView.findViewById(R.id.txthistory);
        }
    }
}
