package com.example.photographers.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.photographers.Image;
import com.example.photographers.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Andrew.Nazymko
 */
public class MainPageListAdapter extends BaseAdapter {

    private List<Image> images = new ArrayList<Image>();
    private Activity context;

    public MainPageListAdapter(Activity context, List<Image> images) {
        this.context = context;
        this.images.addAll(images);

    }

    public void setImages(List<Image> images) {
        this.images.clear();
        this.images.addAll(images);
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
            convertView = context.getLayoutInflater().inflate(R.layout.endles_list_element, null);
        }
        Image item = getItem(position);
        ((TextView) convertView.findViewById(R.id.author_name)).setText(item.getAuthor());

        ImageView img = ((ImageView) convertView.findViewById(R.id.imageView));

        TextView rate = (TextView) convertView.findViewById(R.id.img_rate);

        rate.setText(context.getString(R.string.rate_word) + item.getRate());

        TextView title = (TextView) convertView.findViewById(R.id.picture_name);

        title.setText(item.getImageName());

        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.noimage).showImageForEmptyUri(R.drawable.simple).cacheOnDisc().build();

        ImageLoader.getInstance().displayImage(item.getSmallImageUrl(), img, options);

        return convertView;
    }
}
