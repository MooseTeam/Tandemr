package com.example.gallery;
 
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
 
public class ImageGalleryDemoActivity extends Activity {
     
     
    private static int RESULT_LOAD_IMAGE = 1;
     
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery_demo);
         
        Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View arg0) {
                 
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                 
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }
     
     
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            
            ImageView imageView = (ImageView) findViewById(R.id.imgView);
            Bitmap circular_image = BitmapFactory.decodeFile(picturePath);
            circular_image = Bitmap.createScaledBitmap(circular_image, 500, 500, false);
            circular_image = getBitmapClippedCircle(circular_image);
            
            imageView.setImageBitmap(circular_image);
        }
     
     
    }
    public static Bitmap getBitmapClippedCircle(Bitmap bitmap) {

        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final Bitmap outputBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);

        final Path path = new Path();
        path.addCircle(
                  (float)(width / 2)
                , (float)(height / 2)
                , (float) Math.min(width, (height / 2))
                , Path.Direction.CCW);

        final Canvas canvas = new Canvas(outputBitmap);
        canvas.clipPath(path);
        canvas.drawBitmap(bitmap, 0, 0, null);
        return outputBitmap;
    }
    
    public void resizeAndDisplay(Bitmap bitmap,float x, float y) {
    	Canvas canvas = new Canvas();
    	Matrix matrix = new Matrix();
    	Paint paint = new Paint();
    	canvas.drawBitmap(bitmap,matrix,paint);
    	matrix.setScale(x,y);
    }
}