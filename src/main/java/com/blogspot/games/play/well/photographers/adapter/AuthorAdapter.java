package com.blogspot.games.play.well.photographers.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.blogspot.games.play.well.R;
import com.blogspot.games.play.well.photographers.Image;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * User: patronus
 */
public class AuthorAdapter extends BaseAdapter {
    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    private List<Image> images = new ArrayList<Image>();
    private int padding = 5;
    private Activity context;
    private boolean loading;

    public AuthorAdapter(Activity context, List<Image> images) {
        this.context = context;
        this.images.addAll(images);
    }

    public void addAll(List<Image> newImages) {
        //Add only new
        for (Image newImage : newImages) {
            if (!images.contains(newImage)) {
                images.add(newImage);
            }
        }

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

        ImageView image = (ImageView) convertView.findViewById(R.id.imageView);

        DisplayImageOptions options = new DisplayImageOptions.
                Builder()
                .showStubImage(R.drawable.noimage)
                .cacheInMemory()
                .showImageOnFail(R.drawable.failed)
                .build();

        TextView rate = (TextView) convertView.findViewById(R.id.img_rate);
        rate.setText(item.getRate());
        TextView name = (TextView) convertView.findViewById(R.id.picture_name);

        name.setText(item.getImageName());

        ImageLoader.getInstance().displayImage(getItem(position).getSmallImageUrl(), image, options);

        return convertView;
    }
}
