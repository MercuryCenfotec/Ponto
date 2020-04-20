package adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Membership;

import java.text.DecimalFormat;
import java.util.List;

public class MembershipCard_Adapter extends RecyclerView.Adapter<MembershipCard_Adapter.ViewHolder> {
    Context context;
    private List<Membership> membershipList;
    private ItemClickListener mListener;

    public MembershipCard_Adapter(Context context, List<Membership> membershipList, ItemClickListener listener) {
        this.context = context;
        this.membershipList = membershipList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_membership_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        DecimalFormat costFormat = new DecimalFormat("###,###.###");

        final SharedPreferences myPrefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        holder.membershipTitleView.setText(membershipList.get(position).getName());
        if (myPrefs.getString("userMembershipId","none").equals("none")) {
            holder.acquireMembershipBtn.setVisibility(View.VISIBLE);
            holder.acquireMembershipBtn.setText("₡ " + costFormat.format(membershipList.get(position).getPrice()));
        }
        holder.membershipBenefitsView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        holder.membershipBenefitsView.setItemAnimator(new DefaultItemAnimator());
        holder.membershipBenefitsView.setAdapter(new MembershipBenefit_Adapter(context,membershipList.get(position).getBenefits()));

        holder.acquireMembershipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClicked(membershipList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return membershipList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView membershipTitleView, acquireMembershipBtn;
        RecyclerView membershipBenefitsView;

        public ViewHolder(View itemView) {
            super(itemView);
            membershipTitleView = itemView.findViewById(R.id.membershipTitleTextView);
            acquireMembershipBtn = itemView.findViewById(R.id.acquireMembershipBtn);
            membershipBenefitsView = itemView.findViewById(R.id.benefitListView);
        }
    }

    public interface ItemClickListener {
        void onItemClicked(String itemId);
    }
}
