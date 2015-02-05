package moose.tandemr;

import java.util.Calendar;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

//the profile of another user 's profile
public class ForeignProfileActivity extends Fragment{

	/**
	 * Returns a new instance of this fragment for the given section
	 * number.
	 */
	public static ForeignProfileActivity newInstance(User foreign_user) {
		ForeignProfileActivity fragment = new ForeignProfileActivity();
		fragment.setUser(foreign_user);
		return fragment;
	}

	SharedPreferences sharedpreferences;

	public static final String MyPREFERENCES = "MyPrefs" ;

	public static final String foreign_social_points ="foreign social points";

	private static final String STATE_SELECTED_PROFILE = "selected_profile";

	private User foreign_user;//contains the foreign user informations that we are going to display

	private Calendar last_add_point;

	public static ForeignProfileActivity myInstance(User foreign_user){
		ForeignProfileActivity myFragment = new ForeignProfileActivity();
		myFragment.setUser(foreign_user);
		return myFragment;
	}

	public void setUser(User user) {
		this.foreign_user = user;
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.activity_foreign_profile, container, false);
	}

	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		//Recover image when the screen rotates
		if(savedInstanceState != null) {
			foreign_user = savedInstanceState.getParcelable(STATE_SELECTED_PROFILE);
		}

		//Displaying of the round profile image
		setBitmapClippedCircle(foreign_user.getProfilePicture(),300,300);

		//Personnal message
		setMessage(foreign_user.getPersonalMessage());

		//intializing sharedpreferences
		sharedpreferences = super.getActivity().getSharedPreferences(MyPREFERENCES, 0);
		SharedPreferences.Editor editor = sharedpreferences.edit();

		//creating the entry of the social points of another user . THIS IS TEMPORARY.
		//when we will use bluetooth, we'll get this info directly from the other user .
		//the sharedpreferences will be used to store OUR social points, which will be displayed to others .
		if(!sharedpreferences.contains(foreign_social_points)){
			editor.putInt(foreign_social_points, foreign_user.getSocial_points());
			editor.commit();
		}

		//setting the name 
		setName(this.foreign_user.getName());

		//setting the gender
		setGender(this.foreign_user.getGender());

		//Getting the social points via sharedpreference and displaying
		int points = sharedpreferences.getInt(foreign_social_points, -1);
		setPoints(points);



		//init the button which permit to add a social point to the foreign user
		this.last_add_point = Calendar.getInstance();
		this.last_add_point.set(2000, 02, 2, 2, 2, 2);

		Button add_1_point = (Button) getView().findViewById(R.id.add_1_social_point_button);
		add_1_point.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
					SharedPreferences.Editor editor = sharedpreferences.edit();
					int points = sharedpreferences.getInt(foreign_social_points, -1);
					editor.putInt(foreign_social_points, points+1);
					editor.commit();
					setPoints(points);
			}
		});



		//setting the age
		setAge(this.foreign_user.getAge());

		//init the button which is used to send a mail . It calls the sendMail functuin
		Button send_mail = (Button) getView().findViewById(R.id.button_send_mail);
		send_mail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendMail(foreign_user.getMail());

			}
		});

		String mail = foreign_user.getMail();
		if(mail.length()>4)
			send_mail.setVisibility(View.VISIBLE);

		//init the button which is used to send a sms . It calls the sendSMS function
		Button send_sms = (Button) getView().findViewById(R.id.button_send_sms);
		send_sms.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendSMS(foreign_user.getPhone());
			}
		});

		String phone = foreign_user.getPhone();
		if(phone.length()>=6)
			send_sms.setVisibility(View.VISIBLE);

		//setting the interests
		String[] interests = this.foreign_user.getInterests();
		TextView sports = (TextView) getView().findViewById(R.id.interest_sport);
		TextView music = (TextView) getView().findViewById(R.id.interest_music);
		TextView party = (TextView) getView().findViewById(R.id.interest_party);
		TextView languages = (TextView) getView().findViewById(R.id.interest_languages);
		TextView food = (TextView) getView().findViewById(R.id.interest_food);
		TextView flirt = (TextView) getView().findViewById(R.id.interest_flirt);

		for(int i = 0; i < interests.length;i++){
			if(interests[i].equals("Sports"))
				sports.setVisibility(View.VISIBLE);

			if(interests[i].equals("Music"))
				music.setVisibility(View.VISIBLE);

			if(interests[i].equals("Party"))
				party.setVisibility(View.VISIBLE);

			if(interests[i].equals("Languages"))
				languages.setVisibility(View.VISIBLE);

			if(interests[i].equals("Food"))
				food.setVisibility(View.VISIBLE);

			if(interests[i].equals("Flirt"))
				flirt.setVisibility(View.VISIBLE);

		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(STATE_SELECTED_PROFILE,  foreign_user);
	}

	/**
	 * Set the name
	 */
	public void setName(String name) {
		TextView view = (TextView) getView().findViewById(R.id.foreign_name);
		view.setText(name);
	}

	/**
	 * Set the age
	 */
	public void setAge(int age) {
		TextView view = (TextView) getView().findViewById(R.id.foreign_age);
		String age_string = Integer.toString(age);
		view.setText(age_string);
	}

	/**
	 * Set the gender
	 */
	public void setGender(String gender) {
		TextView view = (TextView) getView().findViewById(R.id.foreign_gender);
		view.setText(gender);
	}
	/**
	 * Take a bitmap, modify it and then put it in the imageview foreign_image
	 * @param bitmap the bitmap to display
	 * @param width the final width
	 * @param height the final height
	 */
	public void setBitmapClippedCircle(Bitmap bitmap,int width,int height) {
		ImageView imageview = (ImageView) getView().findViewById(R.id.foreign_image);

		bitmap = Bitmap.createScaledBitmap(bitmap, width,height, false);

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
		imageview.setImageBitmap(outputBitmap);
	}

	/**
	 * Displaying a personnal message
	 * @return
	 */
	public void setMessage(String message) {
		TextView message_view = (TextView) getView().findViewById(R.id.foreign_message);
		String tmp ="";

		for(int i=0;i<message.length();i++){
			if((i%25 == 0) && (i!=0))
				tmp=tmp+"\n";

			String c = message.substring(i,i+1);
			tmp=tmp+c;

		}
		message_view.setText(tmp);
	}

	/**Displaying the social points
	 * 
	 */
	public void setPoints(int points) {		
		TextView points_view = (TextView) getView().findViewById(R.id.foreign_points);

		String tmp= points + " "+getString(R.string.points_text);
		points_view.setText(tmp);
	}

	/**
	 * Sending a mail to the mail 
	 */
	public void sendMail(String mail) {
		Intent emailIntent = new Intent( android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { mail });
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Email Subject");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Mail sent by a Tandemr user .\n");
		try {
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		}
		catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(ForeignProfileActivity.super.getActivity(), 
					"Mail faild, please try again later.", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Sending a SMS to the phone number phone_number
	 */

	public void sendSMS(String phone_number) {
		Intent smsIntent = new Intent(Intent.ACTION_VIEW);
		smsIntent.setData(Uri.parse("smsto:"));
		smsIntent.setType("vnd.android-dir/mms-sms");
		smsIntent.putExtra("address",new String(phone_number));
		smsIntent.putExtra("sms_body","Sms sent by a Tandemr user .\n");
		try {
			startActivity(smsIntent);
			super.getActivity().finish();
			Log.i("Finished sending SMS...", "");
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(ForeignProfileActivity.super.getActivity(), 
					"SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();/*
		if (id == R.id.action_settings) {
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}
}