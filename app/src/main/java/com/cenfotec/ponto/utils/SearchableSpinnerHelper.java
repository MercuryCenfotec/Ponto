package com.cenfotec.ponto.utils;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;

import com.cenfotec.ponto.data.model.SpinnerItem;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class SearchableSpinnerHelper {
    Context context;
    SearchableSpinner spinner;

    public SearchableSpinnerHelper() {
    }

    public SearchableSpinnerHelper(Context context, SearchableSpinner spinner) {
        this.context = context;
        this.spinner = spinner;
        spinner.setTitle("Buscar");
        spinner.setPositiveButton("Aceptar");
    }

    public void fillSpinner(List<SpinnerItem> array) {
        List<String> spinnerKeys = new ArrayList<>();
        for (SpinnerItem item : array) {
            spinnerKeys.add(item.getKey());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, spinnerKeys);
        spinner.setAdapter(adapter);
    }
}
