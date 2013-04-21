package com.example.photographers.services;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;
import com.example.photographers.util.Log;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * User: patronus
 */
public class FileLoader extends IntentService {

    public static final String FILE_URL = "FILE_URL";
    public static final String FILE_LOADED = "FILE_LOADED";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public FileLoader(String name) {
        super(name);
    }

    public FileLoader() {
        super("Load service");
    }

    @Override

    protected void onHandleIntent(Intent intent) {
        String fileUrl = intent.getExtras().getString(FILE_URL);
        if (fileUrl == null) {
            return;
        }

        Log.d("URL:" + fileUrl);

        File fileDir = new File("/sdcard/" + "PhotoGraphers/");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
            Log.d("Directory created!");
        }

        try {
            URL url = new URL(fileUrl);
            URLConnection connection = url.openConnection();
            connection.connect();

            String fileName = getName(fileUrl);

            // download the file
            InputStream input = new BufferedInputStream(url.openStream());
            File img = new File(fileDir + fileName);

            img.createNewFile();


            OutputStream output = new FileOutputStream(img);

            byte data[] = new byte[1024];

            int count;
            Log.d("Start loading file:" + fileUrl);
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

            output.flush();
            output.close();

            Log.d("Sending broadcast");

            Intent goodNewsSummoner = new Intent(FILE_LOADED);
            sendBroadcast(goodNewsSummoner);

        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    private String getName(String fileUrl) {
        String substring = fileUrl.substring(fileUrl.lastIndexOf("/"));
        //Unsafe code!!!
        return substring;
    }
}
