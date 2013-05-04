package com.blogspot.games.play.well.photographers.services.support;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.blogspot.games.play.well.photographers.IFRegister;
import com.blogspot.games.play.well.photographers.Image;
import com.blogspot.games.play.well.photographers.ImageAuthorRegister;
import com.blogspot.games.play.well.photographers.ImageNormalRegister;
import com.blogspot.games.play.well.photographers.util.ImageUtil;
import com.blogspot.games.play.well.photographers.util.Log;

import java.io.IOException;
import java.util.List;

/**
 * User: patronus
 */
public class BIAGetter extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BIAGetter(String name) {
        super(name);
    }

    public BIAGetter() {
        super("BIAGetter");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int startFrom = 0;
        List<Image> images = null;
        IFRegister register = null;
        Log.d("BIAGetter started");

        register = ImageAuthorRegister.getInstance();
        images = register.getImages();
        startFrom = register.getImageProcessed();
        if (images == null) {
            return;//Fuck this bugs!
        }
        int to = images.size();
        images = register.getImages();
        //process all images from begin to end (get big images url)
        for (; startFrom < images.size(); startFrom++) {
            Image image = images.get(startFrom);
            String bigImagePage = image.getBigImagePage();
            if (image.getBigImage() != null) continue;
            try {
                String bigImageUrl = ImageUtil.getBigImage(bigImagePage);
                image.setBigImage(bigImageUrl);
            } catch (IOException e) {
                image.setBigImage("");
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        register.setImageProcessed(to);
    }
}
