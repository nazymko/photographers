package com.blogspot.games.play.well.photographers.services.support;

import android.app.IntentService;
import android.content.Intent;
import com.blogspot.games.play.well.photographers.IFRegister;
import com.blogspot.games.play.well.photographers.ImageAuthorRegister;
import com.blogspot.games.play.well.photographers.ImageNormalRegister;
import com.blogspot.games.play.well.photographers.ac.AcBig;
import com.blogspot.games.play.well.photographers.ac.AcPre;
import com.blogspot.games.play.well.photographers.Image;
import com.blogspot.games.play.well.photographers.util.ImageUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

/**
 * User: patronus
 */
public class BINGetter extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BINGetter(String name) {
        super(name);
    }

    public BINGetter() {
        super("BINGetter");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        int startFrom = 0;
        List<Image> images = null;
        IFRegister register = null;

        register = ImageNormalRegister.getInstance();
        images = register.getImages();
        startFrom = register.getImageProcessed();
        if (images == null) {
            return;//Fuck this bugs!
        }
        int to = images.size();
        images = register.getImages();
        for (; startFrom < to; startFrom++) {
            Image image = images.get(startFrom);
            String bigImagePage = image.getBigImagePage();
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
