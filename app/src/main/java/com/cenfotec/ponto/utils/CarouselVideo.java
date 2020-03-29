package com.cenfotec.ponto.utils;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

import com.cenfotec.ponto.R;
import com.squareup.picasso.Picasso;

public class CarouselVideo extends Fragment {

    View view;
    VideoView detailVideo;
    ImageView btnPlay;
    String videoUrl;
    Drawable videoForeground;

    public CarouselVideo(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_carousel_video, container, false);
        detailVideo = view.findViewById(R.id.detailVideo);
        btnPlay = view.findViewById(R.id.btnPlay);
        detailVideo.setVideoURI(Uri.parse(videoUrl));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            videoForeground = detailVideo.getForeground();
        }
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    detailVideo.setForeground(Drawable.createFromPath(""));
                }
                detailVideo.requestFocus();
                detailVideo.start();
                btnPlay.setVisibility(View.INVISIBLE);
            }
        });
        detailVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

            @Override
            public void onCompletion(MediaPlayer mp){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    detailVideo.setForeground(videoForeground);
                }
                btnPlay.setVisibility(View.VISIBLE);
            }
        });
//        Picasso.get().load(videoUrl).into(detailVideo);
        return view;
    }
}
