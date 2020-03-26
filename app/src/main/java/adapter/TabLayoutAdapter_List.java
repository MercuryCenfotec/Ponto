package adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cenfotec.ponto.data.model.ServicePetition;
import com.cenfotec.ponto.entities.bidder.BidderHome;
import com.cenfotec.ponto.entities.servicePetition.ServicePetitionListActivity;
import com.cenfotec.ponto.entities.servicePetition.ServicePetitionsList;

import java.util.List;

public class TabLayoutAdapter_List extends FragmentStatePagerAdapter {

    int mnooftabs;

    public TabLayoutAdapter_List(FragmentManager fm, int mnooftabs) {
        super(fm);
        this.mnooftabs = mnooftabs;
    }

    public TabLayoutAdapter_List(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ServicePetitionsList tab1 = new ServicePetitionsList();
                return tab1;
            case 1:
                ServicePetitionsList tab2 = new ServicePetitionsList();
                return tab2;
            case 2:
                ServicePetitionsList tab3 = new ServicePetitionsList();
                return tab3;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 3;
    }
}