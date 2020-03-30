package adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cenfotec.ponto.entities.contract.ContractListFragment;

public class TabLayoutContractList_Adapter extends FragmentStatePagerAdapter {

    int numberOfTabs;

    public TabLayoutContractList_Adapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    public TabLayoutContractList_Adapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ContractListFragment contractListTab = new ContractListFragment();
                return contractListTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 1;
    }
}
