package com.blogspot.games.play.well.photographers.services;

import android.app.IntentService;
import android.content.Intent;
import com.blogspot.games.play.well.photographers.IFRegister;
import com.blogspot.games.play.well.photographers.Image;
import com.blogspot.games.play.well.photographers.ImageAuthorRegister;
import com.blogspot.games.play.well.photographers.ac.AcBig;
import com.blogspot.games.play.well.photographers.util.Log;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * User: patronus
 */
public class AuthorLoader extends IntentService {

    public static final String AUTHOR_PAGE_LOADED = "AUTHOR_PAGE_LOADED";
    public static final String PAGE = "PAGE";
    public static final String PAGE_URL = "PAGE_URL";
    public static final String AUTHOR_PAGE_SOME_PROBLEMS = "AUTHOR_PAGE_SOME_PROBLEMS";
    //    private static final String pageTemplate = "http://photographers.com.ua/pictures/user/";
    private static final String pageNumber = "?page=";
    public static String AUTHOR_NAME = "AUTHOR_NAME";
    private static String prevPage = null;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AuthorLoader(String name) {
        super(name);
    }

    public AuthorLoader() {
        super(AuthorLoader.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int page = intent.getIntExtra(PAGE, 0);
        String userPage = intent.getExtras().getString(PAGE_URL);
        //TODO: FUCKING SHIT!

        if (userPage != null) {
            prevPage = userPage;
        } else {
            userPage = prevPage;
        }

        //Generate page url

        if (page != 0) {
            userPage = userPage + pageNumber + page;
        }

        Log.w("Page:" + userPage);
        Connection connect = Jsoup.connect(userPage);
        connect.timeout(5000);

        try {
            Document document = connect.get();

            Elements select = document.select("div.photo");
            IFRegister imageCache = ImageAuthorRegister.getInstance();
            for (Element element : select) {
                Image image = new Image();

                String rate = element.select("span.score.plus").text();
                String smallImagePage = element.select("a img").attr("src");
                String bigImagePage = element.select("a").attr("href");
                String imageTitle = element.select("div.name a").text();

                image.setBigImagePage(bigImagePage);
                image.setRate(rate);
                image.setSmallImageUrl(smallImagePage);
                image.setImageName(imageTitle);


                imageCache.getImages().add(image);


                Log.w(image.toString());

            }
            imageCache.setPage(page + 1);
            sendFinish();

        } catch (IOException e) {
            //Send error message to user
            sendError();
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        Intent service = new Intent(this,BigImageGetter.class);
        service.putExtra(AcBig.MODE, AcBig.MODE_AUTHOR);

        startService(service);


    }

    private void sendError() {
        //Handle this message
        Intent intent = new Intent(AUTHOR_PAGE_SOME_PROBLEMS);
        sendBroadcast(intent);

    }

    private void sendFinish() {

        Intent intent = new Intent(AUTHOR_PAGE_LOADED);
        sendBroadcast(intent);


    }
}
