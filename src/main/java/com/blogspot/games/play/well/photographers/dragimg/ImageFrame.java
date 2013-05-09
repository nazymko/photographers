package com.blogspot.games.play.well.photographers.dragimg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.blogspot.games.play.well.photographers.IFRegister;
import com.blogspot.games.play.well.photographers.Image;
import com.blogspot.games.play.well.R;

import com.blogspot.games.play.well.photographers.ac.AcAuthor;
import com.blogspot.games.play.well.photographers.services.FeedNormalLoader;
import com.blogspot.games.play.well.photographers.util.Log;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * User: Andrew.Nazymko
 */
public class ImageFrame extends Fragment {
    private int position;
    private IFRegister source;

    public ImageFrame(int position) {
        this.position = position;
    }

    public ImageFrame() {

    }

    public ImageFrame(int position, IFRegister source) {
        this.position = position;
        this.source = source;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View frm = inflater.inflate(R.layout.scalable_image, null);

        //Use line below for large logo if you have hardware rendering turned on
        //imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        // Line below is optional, depending on what scaling method you want

        ImageView imageView = (ImageView) frm.findViewById(R.id.scalable_image_view);
        imageView.setScaleType(ImageView.ScaleType.MATRIX);

        Image image = source.getImages().get(position);


        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .showStubImage(R.drawable.loading)
                .showImageOnFail(R.drawable.adult_censored)
                .showImageForEmptyUri(R.drawable.adult_censored)

                .cacheInMemory()
                .build();

        Intent changeName = new Intent();
        changeName.setAction(FeedNormalLoader.ACTION_NAME_CHANGE);
        changeName.putExtra(AcAuthor.NAME, image.getAuthor());

        getActivity().sendBroadcast(changeName);


        String bigImage = image.getBigImageUrl();

        Log.d("Image loader get url :" + bigImage);

        ImageLoader.getInstance()
                .displayImage(bigImage, imageView, options);


        frm.setOnTouchListener(new PanAndZoomListener(frm, imageView, PanAndZoomListener.Anchor.TOPLEFT));
        return frm;
    }
}
