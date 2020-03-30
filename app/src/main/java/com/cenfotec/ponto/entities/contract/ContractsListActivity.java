package com.cenfotec.ponto.entities.contract;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.cenfotec.ponto.R;

import adapter.TabLayoutContractList_Adapter;

public class ContractsListActivity extends AppCompatActivity {

    ViewPager contractListViewPager;
    TabLayoutContractList_Adapter tabLayoutContractListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contracts_list);
        initViewPager();
        setContractListAdapter();
    }

    private void initViewPager(){
        contractListViewPager = findViewById(R.id.contractListViewPager);
    }

    private void setContractListAdapter(){
        tabLayoutContractListAdapter = new TabLayoutContractList_Adapter(getSupportFragmentManager(),
                1);
        contractListViewPager.setAdapter(tabLayoutContractListAdapter);
    }

    public void goToMainProfile(View view) {
        finish();
    }
}
