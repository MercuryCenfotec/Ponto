package com.cenfotec.ponto.entities.chat;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cenfotec.ponto.R;
import com.google.android.material.tabs.TabLayout;

import adapter.TabLayoutAdapter_Messages;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserChats extends Fragment {

  protected TabLayout tabLayout;
  protected ViewPager viewPager;
  protected View view;


  public UserChats() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_user_chats, container, false);
    initComponents();
    chargeContent();

    return view;
  }

  private void chargeContent() {
    tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    TabLayoutAdapter_Messages adapter = new TabLayoutAdapter_Messages(getChildFragmentManager(), tabLayout.getTabCount());
    viewPager.setAdapter(adapter);

    wrapTabIndicatorToTitle(tabLayout, 50, 50);

    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());

      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {
      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {

      }
    });
  }

  public void wrapTabIndicatorToTitle(TabLayout tabLayout, int externalMargin, int internalMargin) {
    View tabStrip = tabLayout.getChildAt(0);
    if (tabStrip instanceof ViewGroup) {
      ViewGroup tabStripGroup = (ViewGroup) tabStrip;
      int childCount = ((ViewGroup) tabStrip).getChildCount();
      for (int i = 0; i < childCount; i++) {
        View tabView = tabStripGroup.getChildAt(i);
        //set minimum width to 0 for instead for small texts, indicator is not wrapped as expected
        tabView.setMinimumWidth(0);
        // set padding to 0 for wrapping indicator as title
        tabView.setPadding(0, tabView.getPaddingTop(), 0, tabView.getPaddingBottom());
        // setting custom margin between tabs
        if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
          ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
          if (i == 0) {
            // left
            settingMargin(layoutParams, externalMargin, internalMargin);
          } else if (i == childCount - 1) {
            // right
            settingMargin(layoutParams, internalMargin, externalMargin);
          } else {
            // internal
            settingMargin(layoutParams, internalMargin, internalMargin);
          }
        }
      }

      tabLayout.requestLayout();
    }
  }

  private void settingMargin(ViewGroup.MarginLayoutParams layoutParams, int start, int end) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      layoutParams.setMarginStart(start);
      layoutParams.setMarginEnd(end);
      layoutParams.leftMargin = start;
      layoutParams.rightMargin = end;
    } else {
      layoutParams.leftMargin = start;
      layoutParams.rightMargin = end;
    }
  }

  private void initComponents() {
    viewPager = view.findViewById(R.id.viewPager);
    tabLayout = view.findViewById(R.id.tabLayout);
  }
}
