package com.cenfotec.ponto.entities.contract;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.cenfotec.ponto.R;

import adapter.ContractCard_Adapter;
import adapter.TabLayoutContractList_Adapter;

public class ContractsListActivity extends AppCompatActivity {

    ViewPager viewPager;
    ContractCard_Adapter contractAdapter;
    TabLayoutContractList_Adapter tabLayoutContractListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contracts_list);
        getAll();
        setAdapter();
    }

    private void getAll(){
        viewPager = findViewById(R.id.contractListViewPager);
    }

    private void setAdapter(){
        tabLayoutContractListAdapter = new TabLayoutContractList_Adapter(getSupportFragmentManager(), 1);
        viewPager.setAdapter(tabLayoutContractListAdapter);
    }

    public void goToMainProfile(View view) {
        finish();
    }
}
