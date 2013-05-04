package com.blogspot.games.play.well.photographers.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import com.blogspot.games.play.well.R;
import com.blogspot.games.play.well.photographers.Image;
import com.blogspot.games.play.well.photographers.services.FileSaver;

import java.io.File;

/**
 * User: patronus
 */
public class Dif {

    public static void share(Activity activity, Image img) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        String file = img.getBigImage();
        shareIntent.setType("text/plain");

        if (file == null) {
            Toast.makeText(activity, "Unable share this image right now :(", Toast.LENGTH_SHORT).show();
            return;
        }

        //Send just image url

        Log.d("Share intend, img = " + file);


        shareIntent.putExtra(Intent.EXTRA_TEXT, file);
        String subject = activity.getString(R.string.app_name) + getInfo(img);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);


        if (imageLoaded(file)) {
            Uri ur = Uri.fromFile(new File(FileSaver.SAVE_PATH + file.substring(file.lastIndexOf("/"))));
            shareIntent.putExtra(Intent.EXTRA_STREAM, ur);
        }

        activity.startActivity(Intent.createChooser(shareIntent, "Share image to.."));
    }

    private static boolean imageLoaded(String file) {
        String substring = file.substring(file.lastIndexOf("/"));
        return new File(FileSaver.SAVE_PATH + substring).exists();
    }

    private static String getInfo(Image img) {
        if (img.getImageName() != null && img.getImageName().length() > 3) {
            return " - " + img.getImageName();
        } else if (img.getAuthor() != null && img.getAuthor().length() > 3) {
            return " - " + img.getAuthor();
        }

        return "";
    }
}
