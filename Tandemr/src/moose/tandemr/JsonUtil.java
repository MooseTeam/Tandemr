package moose.tandemr;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonUtil {

	public static String toJSon(User user) {
		try {
			// Here we convert Java Object to JSON 
			JSONObject jsonObj = new JSONObject();
			
			jsonObj.put("name", user.getName()); // Set the first name/pair 
			jsonObj.put("gender", user.getGender());
			jsonObj.put("birthdate", user.getBirthdate());
			jsonObj.put("profile_picture_uri", user.getProfilePictureUri());
			jsonObj.put("personal_message", user.getPersonalMessage());
			jsonObj.put("mail", user.getMail());
			jsonObj.put("phone", user.getPhone());
			
			JSONArray jArr = new JSONArray();
			for(String i : user.getInterestsArray())
				jArr.put(i);
			jsonObj.put("interests_array", jArr);

			return jsonObj.toString();

		}
		catch(JSONException ex) {
			ex.printStackTrace();
		}

		return null;
	}
	
	public static User fromJSon(String data) {
		try {
			// Here we convert JSON to Java Object
			JSONObject jObj = new JSONObject(data);
			
			ArrayList<String> interests = new ArrayList<String>();
			JSONArray jArr = jObj.optJSONArray("interests_array");
			if (jArr != null) { 
			   for (int i=0; i<jArr.length(); i++){ 
				   interests.add(jArr.get(i).toString());
			   } 
			} 
			
			User mUser = new User(	jObj.getString("name"),
									jObj.getString("gender"),
									jObj.getString("birthdate"),
									jObj.getString("profile_picture_uri"),
									jObj.getString("personal_message"),
									jObj.getString("mail"),
									jObj.getString("phone"),
									interests
					);

			return mUser;

		}
		catch(JSONException ex) {
			ex.printStackTrace();
		}

		return null;
	}
}
