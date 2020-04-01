package com.cenfotec.ponto.utils;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cenfotec.ponto.R;
import com.squareup.picasso.Picasso;

public class CarouselImage extends Fragment {
    View view;
    ImageView detailImage;
    String imageUrl;
    Boolean isDefault = false;

    public CarouselImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public CarouselImage(String imageUrl, boolean isDefault) {
        this.imageUrl = imageUrl;
        this.isDefault = isDefault;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_carousel_image, container, false);
        detailImage = view.findViewById(R.id.detailImage);
        detailImage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        Picasso.get().load(imageUrl).into(detailImage);
        if(isDefault) {
            LightingColorFilter filter = new LightingColorFilter(Color.BLACK, Color.WHITE);
            detailImage.setColorFilter(filter);
        }
        return view;
    }
}
