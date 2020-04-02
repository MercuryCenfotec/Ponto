package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Contract;
import com.cenfotec.ponto.data.model.ServicePetition;
import com.cenfotec.ponto.entities.contract.GeneratedContractActivity;

import java.util.List;
import java.util.Map;

public class ContractCard_Adapter extends RecyclerView.Adapter<ContractCard_Adapter.ViewHolder> {

    Context context;
    private List<Contract> contractList;
    private Map<String, ServicePetition> servicePetitionList;

    public ContractCard_Adapter(Context context, List<Contract> contractList, Map<String,
            ServicePetition> servicePetitionList) {
        this.context = context;
        this.contractList = contractList;
        this.servicePetitionList = servicePetitionList;
    }

    @Override
    public ContractCard_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_contract_card,
                parent, false);
        return new ContractCard_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContractCard_Adapter.ViewHolder holder, final int position) {
        ServicePetition servicePetition = servicePetitionList.
                get(contractList.get(position).getServicePetitionId());
        holder.contractCardTitle.setText(contractList.get(position).getName());
        holder.contractCardDate.setText(contractList.get(position).getDateCreated());
        holder.contractCardService.setText(servicePetition.getName());
        holder.contractCardView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, GeneratedContractActivity.class);
                        intent.putExtra("petitionerId", contractList.get(position).getPetitionerId());
                        intent.putExtra("bidderUserId", contractList.get(position).getBidderId());
                        intent.putExtra("contractId", contractList.get(position).getId());
                        intent.putExtra("finalCost", contractList.get(position).getFinalCost());
                        context.startActivity(intent);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return contractList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView contractCardTitle, contractCardDate, contractCardService;
        CardView contractCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            contractCardView = itemView.findViewById(R.id.contractCardView);
            contractCardTitle = itemView.findViewById(R.id.contractNameCardTextView);
            contractCardDate = itemView.findViewById(R.id.contractDateCardTextView);
            contractCardService = itemView.findViewById(R.id.contractPetitionCardTextView);
        }
    }

}
