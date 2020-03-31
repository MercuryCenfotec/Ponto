package adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cenfotec.ponto.entities.bidder.BidderHome;
import com.cenfotec.ponto.entities.bidder.BidderProfileFragment;
import com.cenfotec.ponto.entities.servicePetition.ServicePetitionBidderList;

public class TabLayoutAdapter_BidderHome extends FragmentStatePagerAdapter {
    int mnooftabs;
    int actViewPos = 0;

    public TabLayoutAdapter_BidderHome(FragmentManager fm, int mnooftabs) {
        super(fm);
        this.mnooftabs = mnooftabs;
    }

    public TabLayoutAdapter_BidderHome(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {

        switch (actViewPos) {
            //a case for fragment
            case 0:
                ServicePetitionBidderList tab1 = new ServicePetitionBidderList();
                return tab1;
            case 4:
                BidderProfileFragment tab4 = new BidderProfileFragment();
                return tab4;
            default:
                return null;
        }
    }
//this has to be the same length or less than the switchCase valid cases otherwise it will throw an error
    @Override
    public int getCount() {
        return 1;
    }

    public void setActViewPos(int actViewPos){this.actViewPos = actViewPos;}
}
