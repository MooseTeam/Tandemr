package moose.tandemr;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends Fragment{
	
	private static int SECTION_NUMBER = 2;
	
	/**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ProfileActivity newInstance() {
    	ProfileActivity fragment = new ProfileActivity();
        return fragment;
    }
	
	/**
	 * To save data
	 */
	//public static final String PREFS_NAME = "MyPrefsFile";

	/**
	 * Personal data
	 */
	private static final Calendar myCalendar = Calendar.getInstance();
	private static EditText birthdate;
	private static DatePickerDialog.OnDateSetListener date;
	// Datepicker format
	private String myFormat = "dd/MM/yyyy";
	// German format
	private SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);
	private ArrayList<Boolean> interests = new ArrayList<Boolean>();

	/**
	 * Profile photo
	 */
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int RESULT_LOAD_IMAGE = 1;
	// directory name to store captured images
	private static final String IMAGE_DIRECTORY_NAME = "Tandemr";
	// file url to store image
	private static Uri fileUri = null; 
	// image
	private static ImageView imgView;
	
	/**
	 * To save the view
	 */
	private static View mView;
	
	/**
	 * To save this.getActivity()
	 */
	private static MainActivity mActivity;
	
	/**
	 * To save the profile data
	 */
	private static User mUser;


	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		mView = inflater.inflate(R.layout.activity_profile, container, false);
		Log.i("Done", "I'm in onCreateView");
		return mView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mActivity = (MainActivity)ProfileActivity.this.getActivity();
		
		( (TextView) getView().findViewById(R.id.interests) ).setTextColor(
				( (TextView) getView().findViewById(R.id.name) ).getCurrentTextColor() );

		/**
		 * SPINNER
		 */
		// Create the spinner
		Spinner spinner = (Spinner) getView().findViewById(R.id.gender_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mActivity,
				R.array.gender_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);

		/**
		 * BIRTHDAY DATE
		 */
		// Find the birthdate's textedit
		birthdate = (EditText) getView().findViewById(R.id.textedit_birthdate);
		// Create datepicker
		date = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateLabel();
			}
		};	
		// Set listener to show the datepicker on click the birthdate's textedit
		birthdate.setOnClickListener(
				new View.OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						new DatePickerDialog(mActivity, date, 
								myCalendar.get(Calendar.YEAR), 
								myCalendar.get(Calendar.MONTH), 
								myCalendar.get(Calendar.DAY_OF_MONTH)).show();
					}
				}
				);

		/**
		 * INTERESTS CHECK BOXS
		 */
		// Create listener to check the checkboxes - with fragments
		OnClickListener checkbox_listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Is the view now checked?
				boolean checked = ((CheckBox) v).isChecked();
				if(checked){

		        }
			}
		};
		// Add the listener to all checkboxes
		getView().findViewById(R.id.checkbox_sports).setOnClickListener(checkbox_listener);
		getView().findViewById(R.id.checkbox_party).setOnClickListener(checkbox_listener);
		getView().findViewById(R.id.checkbox_music).setOnClickListener(checkbox_listener);
		getView().findViewById(R.id.checkbox_languages).setOnClickListener(checkbox_listener);
		getView().findViewById(R.id.checkbox_food).setOnClickListener(checkbox_listener);
		getView().findViewById(R.id.checkbox_flirt).setOnClickListener(checkbox_listener);
		/**
		 * PROFILE PHOTO
		 */
		// Find the profile's imageview
		imgView = (ImageView) getView().findViewById(R.id.profile_imageView);

		// Find the button to launch the image picker dialog
		Button btn_choosePhoto = (Button) getView().findViewById(R.id.btn_search);
		// Add the listener to launch the image picker dialog
		btn_choosePhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Image_Picker_Dialog();
			}
		});

		// Restore preferences - if there are previous preferences
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
		if(settings.contains("profile_user"))
			stringToProfile(settings.getString("profile_user", null));
		
		//Recover image when the screen rotates
		if(savedInstanceState != null) {
			Bitmap bitmap = savedInstanceState.getParcelable("image");
			imgView.setImageBitmap(bitmap);
		}
		
		/**
		 * DONE BUTTON
		 */
		final Button btn_done = (Button) getView().findViewById(R.id.btn_done);
		btn_done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// Perform action on click
            	if ( checkValidation () ){
	            	/**
	            	 * Save data
	            	 */
	            	// We need an Editor object to make preference changes.
	    			// All objects are from android.context.Context
	    			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
	    			SharedPreferences.Editor editor = settings.edit();  			
	    			// Save the profile data in a string
	    			editor.putString("profile_user", profileToString());
	            	// Commit the edits!
	    			editor.commit();
	    			
	    			// Unlocked navdrawer if it is locked
	    			if(MainActivity.navIsLocked()){
	    				// Set up the drawer.
	    	            mActivity.getNavigationDrawerFragment().setUp(
	    	                    R.id.navigation_drawer,
	    	                    (DrawerLayout) mActivity.findViewById(R.id.drawer_layout));
	    	            // Unlock the drawer
	    	            ((DrawerLayout) mActivity.findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
	    	            MainActivity.setNavUnlocked();
	    			}
	    			
	    		    /**
	    		     * Update the profile image
	    		     */
	    		    previewLoadImage();
	    		    
	    		    /**
	    		     * Update the navigation drawer
	    		     */
	    		    updateNavdrawer();
	            	
	            	/**
	            	 * Change fragment
	            	 */
	            	mActivity.selectFrag(btn_done);
            	}
            	else
                    Toast.makeText(mActivity, "Form contains error", Toast.LENGTH_LONG).show();
            }
        });
	}
	
    private boolean checkValidation() {
        boolean ret = true;
        
        EditText name = (EditText) getView().findViewById(R.id.textedit_name);
        EditText email = (EditText) getView().findViewById(R.id.textedit_email);
        EditText phone = (EditText) getView().findViewById(R.id.textedit_phone);
 
        if (!Validation.requiredText(name)) ret = false;
        if (fileUri == null){ ret = false; ((Button) getView().findViewById(R.id.btn_search)).setError("required");}
        if (Validation.hasText(email) && !Validation.isEmailAddress(email, true)) ret = false;
        if (Validation.hasText(phone) && !Validation.isPhoneNumber(phone, true)) ret = false;
        if (!Validation.interestChecked(getView())) ret = false;
 
        return ret;
    }

	/**
	 * Save image when the screen rotates
	 */
	@Override
	public void onSaveInstanceState(Bundle outState){
		if ( imgView.getDrawable() != null ) {
			// Get the profile image's bitmap
			BitmapDrawable drawable = (BitmapDrawable) imgView.getDrawable();
			Bitmap bitmap = drawable.getBitmap();
			// Save the bitmap
			outState.putParcelable("image", bitmap);
		}
		super.onSaveInstanceState(outState);
	}

	/**
	 * Apply format to the choose date in the birthday's textedit
	 */
	private void updateLabel() {
		// Set format in the birthday's textedit
		birthdate.setText(sdf.format(myCalendar.getTime()));
	}

	/**
	 * PROFILE PHOTO METHODS
	 */

	public void Image_Picker_Dialog()
	{
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(mActivity);
		myAlertDialog.setTitle("Pictures Option");
		myAlertDialog.setMessage("Select Picture Mode");

		myAlertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1)
			{
				Intent pickPhoto = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
			}
		});

		myAlertDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1)
			{
				captureImage();
			}
		});
		myAlertDialog.show();
	}

	/**
	 * Capturing Camera Image will lauch camera app requrest image capture
	 */
	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri();

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	/**
	 * Creating file uri to store image/video
	 */
	public Uri getOutputMediaFileUri() {
		return Uri.fromFile(getOutputMediaFile());
	}

	/**
	 * Returning image
	 */
	private static File getOutputMediaFile() {

		// External sdcard location
		File mediaStorageDir = new File(
				Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
						+ IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + timeStamp + ".jpg");

		return mediaFile;
	}

	/**
	 * onActivityResult
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
		
		switch(requestCode) {
			case RESULT_LOAD_IMAGE:
				if(resultCode == Activity.RESULT_OK){  
					// Successfully loaded the image
					// Display it in image view
					fileUri = imageReturnedIntent.getData();
					previewLoadImage();
				}
				break; 
				
			case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
				if (resultCode == Activity.RESULT_OK) {
					// Successfully captured the image
					// Display it in image view
					previewCapturedImage();
					
				}/* else if (resultCode == Activity.RESULT_CANCELED) {
					// User cancelled Image capture
					Toast.makeText(activity.getApplicationContext(),
							"User cancelled image capture", Toast.LENGTH_SHORT)
							.show();
				} else {
					// Failed to capture image
					Toast.makeText(activity.getApplicationContext(),
							"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
							.show();
				}*/
				break;
		}
	}

	/**
	 * Display capture image from a path to ImageView
	 */
	private void previewCapturedImage() {
		try {
			imgView.setVisibility(View.VISIBLE);

			// Bimatp factory
			BitmapFactory.Options options = new BitmapFactory.Options();

			// Downsizing image as it throws OutOfMemory Exception for larger images
			//options.inSampleSize = 8;

			Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
					options); 
			// 
			bitmap = circleShape(bitmap); 
			imgView.setImageBitmap(bitmap);

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Display load image from a path to ImageView
	 */
	private static void previewLoadImage() {
//		TODO
//		if (fileUri != null) {
//		}
		try {
			// Get the bitmap from the uri saved
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(
					mActivity.getContentResolver(), fileUri);	
			
			bitmap = circleShape(bitmap);
	        imgView.setImageBitmap(bitmap);
	        
	    } catch (FileNotFoundException e) {
			Drawable myDrawable = mActivity.getResources().getDrawable(R.drawable.moose);
			imgView.setImageDrawable(myDrawable);
			
		} catch (IOException e) {
			imgView.setImageURI(fileUri);

		}
	}
	
	/**
	 * Update data of Navdrawer
	 */
	private static void updateNavdrawer(){
		changeNavdrawerImage();
		
		EditText name = (EditText) mView.findViewById(R.id.textedit_name);
		//Put the user name in the navigation drawer
        mActivity.getNavigationDrawerFragment().getNavDrawerAdapter().setTitle(
        		name.getText().toString() );
	}
	
	/**
	 * Update data of Navdrawer
	 */
	private static void updateNavdrawer(Activity activity, String name){
		changeNavdrawerImage(activity);

		//Put the user name in the navigation drawer
		((MainActivity) activity).getNavigationDrawerFragment().getNavDrawerAdapter().setTitle(name);
	}
	
	/**
	 * Create a from the image
	 */
	private static void changeNavdrawerImage(){
//		TODO
//		if (fileUri != null) {
//		}
		// Get the bitmap from the actual uri
		Bitmap bitmapsmall = null;
		try {
			bitmapsmall = MediaStore.Images.Media.getBitmap(
					mActivity.getContentResolver(), fileUri);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // Set navdrawer profile image
		bitmapsmall = Bitmap.createScaledBitmap(bitmapsmall, 100,100, false);
		bitmapsmall = circleShape(bitmapsmall);
		BitmapDrawable icon = new BitmapDrawable(mActivity.getResources(), bitmapsmall);
		//navDrawerItems.get(0).setIcon(icon);
		mActivity.getNavigationDrawerFragment().getNavDrawerAdapter().setIcon(icon);
	}
	
	/**
	 * Create a from the image
	 */
	private static void changeNavdrawerImage(Activity activity){
//		TODO
//		if (fileUri != null) {
//		}
		// Get the bitmap from the actual uri
		Bitmap bitmapsmall = null;
		try {
			bitmapsmall = MediaStore.Images.Media.getBitmap(
					activity.getContentResolver(), fileUri);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // Set navdrawer profile image
		bitmapsmall = Bitmap.createScaledBitmap(bitmapsmall, 100,100, false);
		bitmapsmall = circleShape(bitmapsmall);
		BitmapDrawable icon = new BitmapDrawable(activity.getResources(), bitmapsmall);
		//navDrawerItems.get(0).setIcon(icon);
		((MainActivity) activity).getNavigationDrawerFragment().getNavDrawerAdapter().setIcon(icon);
	}

	/**
	 * Make a round image
	 */
	public static Bitmap circleShape(Bitmap preview_bitmap) {
		Bitmap circleBitmap = Bitmap.createBitmap(preview_bitmap.getWidth(),
				preview_bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		BitmapShader shader = new BitmapShader(preview_bitmap, TileMode.CLAMP,
				TileMode.CLAMP);
		Paint paint = new Paint();
		paint.setShader(shader);
		Canvas c = new Canvas(circleBitmap);
		c.drawCircle(
				preview_bitmap.getWidth() / 2,
				preview_bitmap.getHeight() / 2,
				Math.min(preview_bitmap.getWidth() / 2,
						preview_bitmap.getHeight() / 2), paint);
		return circleBitmap;

	}
	
	/**
	 * Convert profile to string
	 */
	private String profileToString(){
		// Save the profile data
		EditText name = (EditText) mView.findViewById(R.id.textedit_name);
		Spinner gender = (Spinner) mView.findViewById(R.id.gender_spinner);
		EditText birthdate = (EditText) mView.findViewById(R.id.textedit_birthdate);
		EditText personal_message = (EditText) mView.findViewById(R.id.textedit_message);
		EditText mail = (EditText) mView.findViewById(R.id.textedit_email);
		EditText phone = (EditText) mView.findViewById(R.id.textedit_phone);
		ArrayList<String> interests = new ArrayList<String>();
		
		if(	( (CheckBox) getView().findViewById(R.id.checkbox_sports) ).isChecked()	){
			CheckBox mInterest = (CheckBox) getView().findViewById(R.id.checkbox_sports);
			interests.add(mInterest.getText().toString());
		}
		if(	( (CheckBox) getView().findViewById(R.id.checkbox_party) ).isChecked()	){
			CheckBox mInterest = (CheckBox) getView().findViewById(R.id.checkbox_party);
			interests.add(mInterest.getText().toString());
		}
		if(	( (CheckBox) getView().findViewById(R.id.checkbox_music) ).isChecked()	){
			CheckBox mInterest = (CheckBox) getView().findViewById(R.id.checkbox_music);
			interests.add(mInterest.getText().toString());
		}
		
		if(	( (CheckBox) getView().findViewById(R.id.checkbox_languages) ).isChecked()	){
			CheckBox mInterest = (CheckBox) getView().findViewById(R.id.checkbox_languages);
			interests.add(mInterest.getText().toString());
		}
		
		if(	( (CheckBox) getView().findViewById(R.id.checkbox_food) ).isChecked()	){
			CheckBox mInterest = (CheckBox) getView().findViewById(R.id.checkbox_food);
			interests.add(mInterest.getText().toString());
		}
		
		if(	( (CheckBox) getView().findViewById(R.id.checkbox_flirt) ).isChecked()	){
			CheckBox mInterest = (CheckBox) getView().findViewById(R.id.checkbox_flirt);
			interests.add(mInterest.getText().toString());
		}
		
		mUser = new User(	(String) name.getText().toString(), 
							(String) gender.getSelectedItem().toString(), 
							(String) birthdate.getText().toString(), 
							fileUri.toString(), 
							(String) personal_message.getText().toString(), 
							(String) mail.getText().toString(), 
							(String) phone.getText().toString(), 
							interests);

		return JsonUtil.toJSon(mUser);
	}
	
	public static void updateProfile(Activity activity){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(activity);
		if(settings.contains("profile_user")){
			
			mUser = JsonUtil.fromJSon(settings.getString("profile_user", null));
			
			fileUri = Uri.parse(mUser.getProfilePictureUri());
		    
		    /**
		     * Update the navigation drawer
		     */
		    updateNavdrawer(activity, mUser.getName());
		}
	}
	
	/**
	 * Retrieve profile from string
	 */
	private static void stringToProfile(String data){
		// Retrieve the profile data
		mUser = JsonUtil.fromJSon(data);
		
		EditText name = (EditText) mView.findViewById(R.id.textedit_name);
		Spinner gender = (Spinner) mView.findViewById(R.id.gender_spinner);
		EditText birthdate = (EditText) mView.findViewById(R.id.textedit_birthdate);
		EditText personal_message = (EditText) mView.findViewById(R.id.textedit_message);
		EditText mail = (EditText) mView.findViewById(R.id.textedit_email);
		EditText phone = (EditText) mView.findViewById(R.id.textedit_phone);
		
		name.setText(mUser.getName());
		
		String compareValue = mUser.getGender();
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mActivity, R.array.gender_array, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    gender.setAdapter(adapter);
	    if (!compareValue.equals(null)) {
	        int spinnerPostion = adapter.getPosition(compareValue);
	        gender.setSelection(spinnerPostion);
	    }
	    
	    birthdate.setText(mUser.getBirthdate());
	    
	    fileUri = Uri.parse(mUser.getProfilePictureUri());
	    
	    personal_message.setText(mUser.getPersonalMessage());
	    mail.setText(mUser.getMail());
	    phone.setText(mUser.getPhone());
	    
	    ArrayList<String> interests = mUser.getInterestsArray();
	    
	    for(String i : interests) {
	    	if( i.equals( mActivity.getResources().getString(R.string.sports) ) )
	    		( (CheckBox) mActivity.findViewById(R.id.checkbox_sports) ).setChecked(true);
	    	else if( i.equals( mActivity.getResources().getString(R.string.party) ) )
	    		( (CheckBox) mActivity.findViewById(R.id.checkbox_party) ).setChecked(true);
	    	else if( i.equals(  mActivity.getResources().getString(R.string.music) ) )
	    		( (CheckBox) mActivity.findViewById(R.id.checkbox_music) ).setChecked(true);
	    }
	    
	    /**
	     * Update the profile image
	     */
	    previewLoadImage();
	    
	    /**
	     * Update the navigation drawer
	     */
	    updateNavdrawer();
	}
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(SECTION_NUMBER);
    }
}