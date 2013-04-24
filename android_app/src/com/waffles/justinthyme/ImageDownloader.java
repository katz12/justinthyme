package com.waffles.justinthyme;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


public class ImageDownloader {

	/**
	 * Downloads the image at the given URL and returns it as a Bitmap.
	 * 
	 * @param url
	 *            the address of the image
	 * @return the image as a Bitmap
	 */
	public static Bitmap download(String url, int width, int height){
		Bitmap bm = null;
	    try {
	        URL u = new URL(url);
	        URLConnection conn = u.openConnection();
	        conn.connect();
	        InputStream is = conn.getInputStream();
	        BufferedInputStream bis = new BufferedInputStream(is);
	        bm = BitmapFactory.decodeStream(bis);
	        bis.close();
	        is.close();
	    } catch (IOException e) {
	        Log.e("Image Downloader", "Error getting bitmap", e);
	    }

	    return Bitmap.createScaledBitmap(bm, width, height, false);
	}

}
