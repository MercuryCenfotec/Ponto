package adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cenfotec.ponto.entities.chat.ChatsList;

public class TabLayoutAdapter_Messages extends FragmentStatePagerAdapter {
  int mnooftabs;


  public TabLayoutAdapter_Messages(FragmentManager fm, int mnooftabs) {
    super(fm);
    this.mnooftabs = mnooftabs;
  }

  public TabLayoutAdapter_Messages(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int position) {

    switch (position) {
      case 0:
        ChatsList tab1 = new ChatsList("active");
        return tab1;
      case 1:
        ChatsList tab3 = new ChatsList("closed");
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
