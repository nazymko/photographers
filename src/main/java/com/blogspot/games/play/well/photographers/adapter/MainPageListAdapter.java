package com.blogspot.games.play.well.photographers.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.blogspot.games.play.well.photographers.Image;
import com.blogspot.games.play.well.R;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


import java.util.ArrayList;
import java.util.List;

/**
 * User: Andrew.Nazymko
 */
public class MainPageListAdapter extends BaseAdapter {

    public static final int ID_KEY = 3;
    private static int previousPosition = 0;
    private ArrayList<Image> images = new ArrayList<Image>();
    private Activity context;

    public MainPageListAdapter(Activity context, List<Image> images) {
        this.context = context;
        this.images.addAll(images);
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> imagesCache) {
        this.images.addAll(imagesCache);
        imagesCache.clear();
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Image getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = context.getLayoutInflater().inflate(R.layout.list_element, null);
        }
        Image item = getItem(position);

        ImageView img = ((ImageView) convertView.findViewById(R.id.imageView));

        TextView rate = (TextView) convertView.findViewById(R.id.img_rate);

        rate.setText(item.getRate());

        TextView title = (TextView) convertView.findViewById(R.id.picture_name);

        String imageName = item.getImageName();
        if (!(imageName.length() == 0)) {
            title.setText(imageName);
        } else {
            title.setText(item.getAuthor());
        }


        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.noimage).showImageForEmptyUri(R.drawable.simple).cacheInMemory().cacheOnDisc().build();

        ImageLoader.getInstance().displayImage(item.getSmallImageUrl(), img, options);

        convertView.setTag(ID_KEY, position);
        return convertView;
    }

    public void addNew(Image img) {
        images.add(img);
    }
}
