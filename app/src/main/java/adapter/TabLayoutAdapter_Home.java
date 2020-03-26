package adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cenfotec.ponto.entities.bidder.BidderHome;
import com.cenfotec.ponto.entities.servicePetition.ServicePetitionBidder;
import com.cenfotec.ponto.entities.servicePetition.ServicePetitionsList;

public class TabLayoutAdapter_Home extends FragmentStatePagerAdapter {
    int mnooftabs;

    public TabLayoutAdapter_Home(FragmentManager fm, int mnooftabs) {
        super(fm);
        this.mnooftabs = mnooftabs;
    }

    public TabLayoutAdapter_Home(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            //a case for fragment
            case 0:
                BidderHome tab1 = new BidderHome();
                return tab1;
            case 1:
                ServicePetitionBidder tab2 = new ServicePetitionBidder();
                return tab2;
            default:
                return null;
        }
    }
//this has to be the same length or less than the switchCase valid cases otherwise it will throw an error
    @Override
    public int getCount() {
        return 2;
    }
}
