package moose.tandemr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A class which contains the informations of a foreign user . Those informations are going to be displayed
 * in ForeignProfileActivity .
 * Interests will be here too .
 * @author sualty
 *
 */
public class ForeignUser implements Parcelable {
	
	private int mData;

    public int describeContents() {
        return 0;
    }

    /** save object in parcel */
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    public static final Parcelable.Creator<ForeignUser> CREATOR
            = new Parcelable.Creator<ForeignUser>() {
        public ForeignUser createFromParcel(Parcel in) {
            return new ForeignUser(in);
        }

        public ForeignUser[] newArray(int size) {
            return new ForeignUser[size];
        }
    };

    /** recreate object from parcel */
    private ForeignUser(Parcel in) {
        mData = in.readInt();
    }

	private String name;
	private Bitmap profile_picture;
	private String personal_message;
	private int social_points;
	private String phone_number;
	private String[] interests;
    private int age;
    private String gender;
    private String mail;

	//Default Pingu's profile :)
	public ForeignUser(Context context) {
		this.name = "Pingu";
		this.profile_picture = BitmapFactory.decodeResource(context.getResources(),R.drawable.pinguin);
		this.personal_message = "Ok, for now I'm just a baby, but one day I wanna be a majestic creature !";
		this.social_points = 0;
		this.interests = new String []{
				"Sports",
				"Party",
				"Music"
			};
		
		this.phone_number="01747603793";
        this.age = 15;
        this.gender = "Mercury";
        this.mail = "lagouge@polytech.unice.fr";
	}
	
	//constructor
    public ForeignUser(Context context,String name,int id_profile_picture,String personal_message,int social_points,String phone_number,String[] interests,
            int age, String gender, String mail){
        this.name = name;
        this.profile_picture =BitmapFactory.decodeResource(context.getResources(),id_profile_picture);
        this.personal_message = personal_message;
        this.social_points = social_points;
        this.phone_number = phone_number;
        this.interests = interests;
        this.age = age;
        this.gender = gender;
        this.mail = mail;
    }
 
    public String[] getInterests() {
        return this.interests;
    }
    
    public String getName() {
        return name;
    }
 
    public Bitmap getProfile_picture() {
        return profile_picture;
    }
 
    public String getPersonal_message() {
        return personal_message;
    }
 
    public int getSocial_points() {
        return social_points;
    }
 
    public String getPhone_number() {
        return phone_number;
    }
 
    public int getAge() {
        return age;
    }
 
    public void setAge(int age) {
        this.age = age;
    }
 
    public String getGender() {
        return gender;
    }
 
    public void setGender(String gender) {
        this.gender = gender;
    }
 
    public String getMail() {
        return mail;
    }
 
    public void setMail(String mail) {
        this.mail = mail;
    }   
}