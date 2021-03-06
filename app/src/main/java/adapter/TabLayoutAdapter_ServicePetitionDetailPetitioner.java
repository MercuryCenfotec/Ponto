package adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cenfotec.ponto.entities.offer.OffersListFragment;
import com.cenfotec.ponto.entities.petitioner.PetitionerHome;
import com.cenfotec.ponto.entities.servicePetition.ServicePetitionDetail;

public class TabLayoutAdapter_ServicePetitionDetailPetitioner extends FragmentStatePagerAdapter {

    int mnooftabs;

    public TabLayoutAdapter_ServicePetitionDetailPetitioner(FragmentManager fm, int mnooftabs) {
        super(fm);
        this.mnooftabs = mnooftabs;
    }

    public TabLayoutAdapter_ServicePetitionDetailPetitioner(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ServicePetitionDetail tab1 = new ServicePetitionDetail();
                return tab1;
            case 1:
                OffersListFragment tab2 = new OffersListFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}