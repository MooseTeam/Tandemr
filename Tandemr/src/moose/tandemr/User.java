package moose.tandemr;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A class which contains the informations of a foreign user . Those informations are going to be displayed
 * in ForeignProfileActivity .
 * Interests will be here too .
 * @author sualty
 *
 */
public class User implements Parcelable {
	
	private int mData;

    public int describeContents() {
        return 0;
    }

    /** save object in parcel */
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /** recreate object from parcel */
    private User(Parcel in) {
        mData = in.readInt();
    }

	private String name;
    private String gender;
    private String birthdate;
    private int age;
	private Bitmap profile_picture;
	private String profile_picture_uri;
	private String personal_message;
    private String mail;
	private String phone;
	private ArrayList<String> interests_array;
	private String[] interests;
	private int social_points;
	
	public User() {
	}

	//Default Pingu's profile :)
	public User(Context context) {
		this.name = "Pingu";
		this.profile_picture = BitmapFactory.decodeResource(context.getResources(),R.drawable.pinguin);
		this.personal_message = "Ok, for now I'm just a baby, but one day I wanna be a majestic creature !";
		this.social_points = 0;
		this.interests = new String []{
				"Sports",
				"Party",
				"Music"
			};
		
		this.phone="01747603793";
        this.age = 15;
        this.gender = "Mercury";
        this.mail = "lagouge@polytech.unice.fr";
	}
	
	//constructor
    public User(Context context,String name,int id_profile_picture,String personal_message,int social_points,String phone_number,String[] interests,
            int age, String gender, String mail){
        this.name = name;
        this.profile_picture =BitmapFactory.decodeResource(context.getResources(),id_profile_picture);
        this.personal_message = personal_message;
        this.social_points = social_points;
        this.phone = phone_number;
        this.interests = interests;
        this.age = age;
        this.gender = gender;
        this.mail = mail;
    }
    
    //constructor for profile activity
    public User(String name, String gender, String birthdate, String profile_picture_uri, 
    		String personal_message, String mail, String phone, ArrayList<String> interests_array){
        this.name = name;
        this.gender = gender;
        this.birthdate = birthdate;
        //this.age = age;
        this.profile_picture_uri = profile_picture_uri;
        this.personal_message = personal_message;
        this.mail = mail;
        this.phone = phone;
        this.interests_array = interests_array;
    }
    
    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }
 
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getBirthdate() {
    	return birthdate;
    }
 
    public int getAge() {
        return age;
    }
 
    public void setAge(int age) {
        this.age = age;
    }
 
    public Bitmap getProfilePicture() {
        return profile_picture;
    }
    
    public String getProfilePictureUri() {
        return profile_picture_uri;
    }
 
    public String getPersonalMessage() {
        return personal_message;
    }
 
    public String getMail() {
        return mail;
    }
 
    public void setMail(String mail) {
        this.mail = mail;
    }
 
    public String getPhone() {
        return phone;
    }

    public String[] getInterests() {
        return this.interests;
    }


    public ArrayList<String> getInterestsArray() {
        return this.interests_array;
    }
 
    public int getSocial_points() {
        return social_points;
    }
}