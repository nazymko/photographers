package com.blogspot.games.play.well.photographers.dragimg;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.blogspot.games.play.well.photographers.Image;
import com.blogspot.games.play.well.photographers.ImageRegister;
import com.blogspot.games.play.well.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * User: Andrew.Nazymko
 */
public class Frame extends Fragment {
    private int position;

    public Frame(int position) {
        this.position = position;
    }

    public Frame() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View frm = inflater.inflate(R.layout.scalable_image, null);

        //Use line below for large logo if you have hardware rendering turned on
        //imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        // Line below is optional, depending on what scaling method you want

        ImageView imageView = (ImageView) frm.findViewById(R.id.scalable_image_view);
        imageView.setScaleType(ImageView.ScaleType.MATRIX);

        Image image = ImageRegister.getInstance().getImages().get(position);


        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .showStubImage(R.drawable.loading)
                .showImageOnFail(R.drawable.failed)
                .build();


        ImageLoader.getInstance()
                .displayImage(image.getBigImage(), imageView, options);


        frm.setOnTouchListener(new PanAndZoomListener(frm, imageView, PanAndZoomListener.Anchor.TOPLEFT));


        return frm;
    }
}
