package com.example.ejemplo;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * A class which contains the informations of a foreign user . Those informations are going to be displayed
 * in ForeignProfileActivity .
 * Interests will be here too .
 * @author sualty
 *
 */
public class ForeignUser {

	private String name;
	private Bitmap profile_picture;
	private String personal_message;
	private int social_points;
	private String phone_number;
	

	//Default Pingu's profile :)
	public ForeignUser(Context context) {
		this.name = "Pingu";
		this.profile_picture = BitmapFactory.decodeResource(context.getResources(),R.drawable.pinguin);
		this.personal_message = "Ok, for now I'm just a baby, but one day I wanna be a majestic creature !";
		this.social_points = 0;
		this.phone_number="01747603793";
	}
	
	//constructor
	public ForeignUser(Context context,String name,int id_profile_picture,String personal_message,int social_points,String phone_number){
		this.name = name;
		this.profile_picture =BitmapFactory.decodeResource(context.getResources(),id_profile_picture);
		this.personal_message = personal_message;
		this.social_points = social_points;
		this.phone_number = phone_number;
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
}
