package adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cenfotec.ponto.entities.contract.ContractListFragment;

public class TabLayoutContractList_Adapter extends FragmentStatePagerAdapter {

    int mnooftabs;

    public TabLayoutContractList_Adapter(FragmentManager fm, int mnooftabs) {
        super(fm);
        this.mnooftabs = mnooftabs;
    }

    public TabLayoutContractList_Adapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ContractListFragment tab1 = new ContractListFragment();
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
