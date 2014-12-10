package moose.tandemr;

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
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
    private Uri fileUri; 
    // image
	ImageView imgView;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.activity_profile, container, false);
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	        
        /**
         * SPINNER
         */
        // Create the spinner
		Spinner spinner = (Spinner) getView().findViewById(R.id.gender_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.gender_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		
		/**
		 * BIRTHDAY DATE
		 */
		birthdate = (EditText) getView().findViewById(R.id.textedit_birthdate);
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
		birthdate.setOnClickListener(
				new View.OnClickListener(){
					@Override
			        public void onClick(View v) {
			            // TODO Auto-generated method stub
						new DatePickerDialog(getActivity(), date, 
								myCalendar.get(Calendar.YEAR), 
								myCalendar.get(Calendar.MONTH), 
								myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			        }
				}
		);
		
		/**
		 * PROFILE PHOTO
		 */
		imgView = (ImageView) getView().findViewById(R.id.profile_imageView);
		
		Button btn_camera = (Button) getView().findViewById(R.id.btn_search);
		btn_camera.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Image_Picker_Dialog();
			}
		});
		
		//Save image when the screen rotates
		if(savedInstanceState != null) {
	        Bitmap bitmap = savedInstanceState.getParcelable("image");
	        imgView.setImageBitmap(bitmap);
	     }
	}
	  
	
	private void updateLabel() {
	    String myFormat = "dd/MM/yyyy"; //In which you need put here
	    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);

	    birthdate.setText(sdf.format(myCalendar.getTime()));
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		/*if (id == R.id.action_settings) {
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * INTERESTS CHECK BOXS
	 */
	public void onCheckboxClicked(View view) {
	    // Is the view now checked?
	    boolean checked = ((CheckBox) view).isChecked();
	    
/*	    AlertDialog.Builder alert_add = new AlertDialog.Builder(getActivity());
	    alert_add.setTitle("Interests");
	    alert_add.setMessage("Are you sure you want to add "+((CheckBox) view).getText()+"?");
	    alert_add.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
            	// do nothing
            }
         });
	    alert_add.setIcon(android.R.drawable.ic_dialog_alert);
	    
	    AlertDialog.Builder alert_delete = new AlertDialog.Builder(getActivity());
	    alert_delete.setTitle("Interests");
	    alert_delete.setMessage("Are you sure you want to delete "+((CheckBox) view).getText()+"?");
	    alert_delete.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
            	// do nothing
            }
         });
	    alert_delete.setIcon(android.R.drawable.ic_dialog_alert);
	    
	    // Check which checkbox was clicked
	    switch(view.getId()) {
	        case R.id.checkbox_sports:
	            if (checked)
	            	alert_add.show();
	            else
	            	alert_delete.show();
	            break;
	        case R.id.checkbox_party:
	            if (checked)
	            	alert_add.show();
	            else
	            	alert_delete.show();
	            break;
	        case R.id.checkbox_music:
	            if (checked)
	            	alert_add.show();
	            else
	            	alert_delete.show();
	            break;
	    }*/
	}
	
	/**
	 * PROFILE PHOTO METHODS
	 */
	
	public void Image_Picker_Dialog()
	{
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this.getActivity());
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
     * returning image / video
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
    
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
    	super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 
    	switch(requestCode) {
		case RESULT_LOAD_IMAGE:
		    if(resultCode == Activity.RESULT_OK){  
		        Uri selectedImage = imageReturnedIntent.getData();
		        
		        try {
					Bitmap bitmap = MediaStore.Images.Media.getBitmap(
							this.getActivity().getContentResolver(), selectedImage);			
					bitmap = circleShape(bitmap);
		            imgView.setImageBitmap(bitmap);
		            
		        } catch (FileNotFoundException e) {
		        	imgView.setImageURI(selectedImage);
				} catch (IOException e) {
					imgView.setImageURI(selectedImage);
				}
		    }

		break; 
		case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
            if (resultCode == Activity.RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(this.getActivity().getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(this.getActivity().getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
		break;
		}
	}
    
    /**
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            imgView.setVisibility(View.VISIBLE);
 
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();
 
            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            //options.inSampleSize = 8;
 
            Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options); 
            bitmap = circleShape(bitmap); 
            imgView.setImageBitmap(bitmap);
            
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
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
     * Save image when the screen rotates
     */
    @Override
	public void onSaveInstanceState(Bundle outState){
    	BitmapDrawable drawable = (BitmapDrawable) imgView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        outState.putParcelable("image", bitmap);
        super.onSaveInstanceState(outState);
    }
}
