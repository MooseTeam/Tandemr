package moose.tandemr;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
//the profile of another user 's profile
//TODO
public class ForeignProfileActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_foreign_profile);

		//Creation of the round profile image
		ImageView imageview = (ImageView) findViewById(R.id.foreign_image);
		Bitmap circular_image = BitmapFactory.decodeResource(getResources(),R.drawable.pinguin);
		circular_image = Bitmap.createScaledBitmap(circular_image, 300, 300, false);
		circular_image = getBitmapClippedCircle(circular_image);

		imageview.setImageBitmap(circular_image);
		
		//Personnal message
		TextView message_view = (TextView) findViewById(R.id.foreign_message);
		String message = editMessage();
		message_view.setText(message);
	}

	/**
	 * creates a round image from a Bitmap image .
	 * @param bitmap
	 * @return
	 */
	public Bitmap getBitmapClippedCircle(Bitmap bitmap) {

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

	/**
	 * Return a String that is the personal message which has bit modified for fitting in the page .
	 * @return
	 */
	public String editMessage() {
		String message =  getString(R.string.foreign_message);
		String tmp ="";

		for(int i=0;i<message.length();i++){
			if((i%25 == 0) && (i!=0))
				tmp=tmp+"\n";
			else {
				String c = message.substring(i,i+1);
				tmp=tmp+c;
			}
		}
		return tmp;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.foreign_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
