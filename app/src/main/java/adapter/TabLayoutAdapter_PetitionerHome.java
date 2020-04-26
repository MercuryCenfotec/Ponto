package adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cenfotec.ponto.entities.chat.UserChats;
import com.cenfotec.ponto.entities.notification.NotificationListFragment;
import com.cenfotec.ponto.entities.petitioner.PetitionerProfileFragment;
import com.cenfotec.ponto.entities.servicePetition.ServicePetitionPetitionerList;

public class TabLayoutAdapter_PetitionerHome extends FragmentStatePagerAdapter {
  int mnooftabs;
  int actViewPos = 0;

  public TabLayoutAdapter_PetitionerHome(FragmentManager fm, int mnooftabs) {
    super(fm);
    this.mnooftabs = mnooftabs;
  }

  public TabLayoutAdapter_PetitionerHome(FragmentManager fm) {
    super(fm);
  }


  @Override
  public Fragment getItem(int position) {

    switch (actViewPos) {
      //a case for fragment
      case 0:
        ServicePetitionPetitionerList tab1 = new ServicePetitionPetitionerList();
        return tab1;
      case 1:
        UserChats tab2 = new UserChats();
        return tab2;
      case 2:
        NotificationListFragment tab3 = new NotificationListFragment();
        return tab3;
      case 3:
        PetitionerProfileFragment tab4 = new PetitionerProfileFragment();
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

  public void setActViewPos(int actViewPos) {
    this.actViewPos = actViewPos;
  }
}
