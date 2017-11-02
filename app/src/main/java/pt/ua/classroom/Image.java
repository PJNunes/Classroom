package pt.ua.classroom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Pedro Nunes.
 */

public class Image extends AsyncTask<String, Integer, Bitmap>{

    private static final String TAG = "Image";

    protected Bitmap doInBackground(String... urls) {
        Bitmap bitmap = null;
        try {
            Log.d(TAG, urls[0]);
            bitmap = BitmapFactory.decodeStream((InputStream)new URL(urls[0]).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // This is called each time you call publishProgress()
    protected void onProgressUpdate(Integer... progress) {
        //
    }

    // This is called when doInBackground() is finished
    protected void onPostExecute(Long result) {
        //
    }
}
