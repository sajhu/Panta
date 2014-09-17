package co.panta.android.services;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
	private boolean circular;
	private int ancho;
	private int alto;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
        circular = false;
        this.ancho = 200;
        this.alto = 200;
    }
    
    public DownloadImageTask(ImageView bmImage, boolean circular, int size) {
        this.bmImage = bmImage;
        this.circular = circular;
        this.ancho = size;
        this.alto = size;
    }
    
    public DownloadImageTask(ImageView bmImage, boolean b, int ancho,
			int alto) {
        this.bmImage = bmImage;
        this.circular = b;
        this.ancho = ancho;
        this.alto = alto;
	}

	protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        
//        Log.d("panta img", "Descargando desde " + urldisplay);
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("panta ub img", e.getMessage());
            return null;
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap intermedio) {
    	
    	if(intermedio != null)
    	{
    		Bitmap result = intermedio;
        	
        	if(circular)
        		result = getRoundedShape(intermedio);
        	
            bmImage.
            setImageBitmap(result);
    	}
    	
    }
    
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
            bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
     
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 50;
     
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
     
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
     
        return output;
      }
    
    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = ancho;
        int targetHeight = alto;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, 
                            targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
            ((float) targetHeight - 1) / 2,
            (Math.min(((float) targetWidth), 
            ((float) targetHeight)) / 2),
            Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap, 
            new Rect(0, 0, sourceBitmap.getWidth(),
            sourceBitmap.getHeight()), 
            new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }
}