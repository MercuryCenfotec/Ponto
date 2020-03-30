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
                BidderHome tab1 = new BidderHome();
                return tab1;
            case 1:
                ServicePetitionBidderList tab2 = new ServicePetitionBidderList();
                return tab2;
            case 2:
                BidderProfileFragment tab3 = new BidderProfileFragment();
                return tab3;
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