package adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cenfotec.ponto.R;

import java.text.DecimalFormat;
import java.util.List;

public class MembershipBenefit_Adapter extends RecyclerView.Adapter<MembershipBenefit_Adapter.ViewHolder> {
    Context context;
    private List<String> benefitList;

    public MembershipBenefit_Adapter(Context context, List<String> benefitList) {
        this.context = context;
        this.benefitList = benefitList;
    }

    @Override
    public MembershipBenefit_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_membership_beneift, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MembershipBenefit_Adapter.ViewHolder holder, int position) {
        holder.membershipBenefit.setText(benefitList.get(position));
    }

    @Override
    public int getItemCount() {
        return benefitList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView membershipBenefit;

        ViewHolder(View itemView) {
            super(itemView);
            membershipBenefit = itemView.findViewById(R.id.benefitTextView);
        }
    }
}
