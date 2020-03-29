package adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cenfotec.ponto.entities.offer.OffersListFragment;
import com.cenfotec.ponto.entities.servicePetition.ServicePetitionPetitionerList;
import com.cenfotec.ponto.entities.servicePetition.ServicePetitionsList;

public class TabLayoutAdapter_PetitionerList extends FragmentStatePagerAdapter {

    int mnooftabs;

    public TabLayoutAdapter_PetitionerList(FragmentManager fm, int mnooftabs) {
        super(fm);
        this.mnooftabs = mnooftabs;
    }

    public TabLayoutAdapter_PetitionerList(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ServicePetitionsList tab1 = new ServicePetitionsList(true);
                return tab1;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 1;
    }
}