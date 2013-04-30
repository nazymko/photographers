package com.blogspot.games.play.well.photographers.services;

import android.app.IntentService;
import android.content.Intent;
import com.blogspot.games.play.well.photographers.IFRegister;
import com.blogspot.games.play.well.photographers.ImageAuthorRegister;
import com.blogspot.games.play.well.photographers.ImageNormalRegister;
import com.blogspot.games.play.well.photographers.ac.AcBig;
import com.blogspot.games.play.well.photographers.ac.AcPre;
import com.blogspot.games.play.well.photographers.Image;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

/**
 * User: patronus
 */
public class BigImageGetter extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BigImageGetter(String name) {
        super(name);
    }

    public BigImageGetter() {
        super("BigImageGetter");
    }

    public static String getBigImage(String page) throws IOException {
        Document document = Jsoup.connect(page).get();

        String src = document.select("img#theImage").attr("src");

        return AcPre.BASE_PATH + src;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        int mode = intent.getIntExtra(AcBig.MODE, AcBig.MODE_NORMAL);
        int startFrom=0;
        List<Image> images = null;
        IFRegister register = null;
        switch (mode) {
            case AcBig.MODE_AUTHOR:
                register = ImageAuthorRegister.getInstance();
                images = register.getImages();
                startFrom = register.getImageProcessed();
                break;
            case AcBig.MODE_NORMAL:
                register = ImageNormalRegister.getInstance();
                images = register.getImages();
                startFrom = register.getImageProcessed();
                break;
        }

        if (images == null) {
            return;//Fuck this bugs!
        }
        int to = images.size();

        for (; startFrom < to; startFrom++) {
            Image image = images.get(startFrom);
            String bigImagePage = image.getBigImagePage();
            try {
                String bigImageUrl = getBigImage(bigImagePage);
                image.setBigImage(bigImageUrl);
            } catch (IOException e) {
                image.setBigImage("");
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        register.setImageProcessed(to);


    }
}
