package moose.tandemr;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
import android.support.v4.app.FragmentManager;
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
import android.widget.Toast;

public class ProfileActivity extends Fragment{
	
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

	/**
	 * Profile photo
	 */
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int RESULT_LOAD_IMAGE = 1;
	// directory name to store captured images
	private static final String IMAGE_DIRECTORY_NAME = "Tandemr";
	// file url to store image
	private Uri fileUri = null; 
	// image
	private ImageView imgView;
	
	/**
	 * To save the view
	 */
	private View view;
	
	/**
	 * To save this.getActivity()
	 */
	private MainActivity activity;


	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		view = inflater.inflate(R.layout.activity_profile, container, false);
		Log.i("Done", "I'm in onCreateView");
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		activity = (MainActivity)ProfileActivity.this.getActivity();

		/**
		 * SPINNER
		 */
		// Create the spinner
		Spinner spinner = (Spinner) getView().findViewById(R.id.gender_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
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
						new DatePickerDialog(activity, date, 
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
			}
		};
		// Add the listener to all checkboxes
		getView().findViewById(R.id.checkbox_sports).setOnClickListener(checkbox_listener);
		getView().findViewById(R.id.checkbox_party).setOnClickListener(checkbox_listener);
		getView().findViewById(R.id.checkbox_music).setOnClickListener(checkbox_listener);

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
		if(settings.contains("profile_image")) {
			fileUri = Uri.parse(settings.getString("profile_image", null));
			if (fileUri != null) {
				previewLoadImage();
				changeNavdrawerImage();
			}
		}
		
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
            	Log.i("Done", "I'm in done button!");
            	
            	/**
            	 * Save data
            	 */
            	if (fileUri != null) {
        			// We need an Editor object to make preference changes.
        			// All objects are from android.context.Context
        			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        			SharedPreferences.Editor editor = settings.edit();
        			
        			// Save the profile image's uri in a string
        			editor.putString("profile_image", fileUri.toString());
        	
        			// Commit the edits!
        			editor.commit();
        		}
            	
            	/**
            	 * Update the Navdrawer
            	 */
            	updateNavdrawer();
            	
            	/**
            	 * Change fragment
            	 */
            	activity.selectFrag(btn_done);
            }
        });
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
		// Datepicker format
		String myFormat = "dd/MM/yyyy";
		// German format
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);
		// Set format in the birthday's textedit
		birthdate.setText(sdf.format(myCalendar.getTime()));
	}

	/**
	 * PROFILE PHOTO METHODS
	 */

	public void Image_Picker_Dialog()
	{
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(activity);
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
	private void previewLoadImage() {
		try {
			// Get the bitmap from the uri saved
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(
					activity.getContentResolver(), fileUri);	
			
			bitmap = circleShape(bitmap);
	        imgView.setImageBitmap(bitmap);
	        
	    } catch (FileNotFoundException e) {
			Drawable myDrawable = getResources().getDrawable(R.drawable.moose);
			imgView.setImageDrawable(myDrawable);
			
		} catch (IOException e) {
			imgView.setImageURI(fileUri);

		}
	}
	
	/**
	 * Update data of Navdrawer
	 */
	private void updateNavdrawer(){
		changeNavdrawerImage();
	}
	
	/**
	 * Create a from the image
	 */
	private void changeNavdrawerImage(){
		// Get the bitmap from the actual uri
		Log.i("Done", fileUri.toString());
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
		BitmapDrawable icon = new BitmapDrawable(this.getResources(), bitmapsmall);
		//navDrawerItems.get(0).setIcon(icon);
		activity.getNavigationDrawerFragment().getNavDrawerAdapter().setIcon(icon);
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
}