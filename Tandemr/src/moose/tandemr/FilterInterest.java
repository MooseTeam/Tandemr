package moose.tandemr;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FilterInterest extends Fragment {
	
	private static int SECTION_NUMBER = 4;
	
	/**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FilterInterest newInstance() {
    	FilterInterest fragment = new FilterInterest();
        return fragment;
    }

	private ArrayList<User> foreignUser;

	@Override
	public View onCreateView(
			LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.interest_filter, container, false);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initForeignUsers();

		Button sport = (Button) FilterInterest.this.getView().findViewById(R.id.btn_sport);
		sport.setOnClickListener(
				new View.OnClickListener() { 
					@Override 
					public void onClick(View v) {
						ArrayList<User> result = filter("Sports");
						User[] fu = new User[result.size()];

						for(int i =0; i< result.size();i++) {
							fu[i] = result.get(i);
						}

						AroundYou fragment = AroundYou.newInstance(fu);
						FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
						fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
					} 
				});
		
		Button music = (Button) FilterInterest.this.getView().findViewById(R.id.btn_music);
		music.setOnClickListener(
				new View.OnClickListener() { 
					@Override 
					public void onClick(View v) {
						ArrayList<User> result = filter("Music");
						User[] fu = new User[result.size()];

						for(int i =0; i< result.size();i++) {
							fu[i] = result.get(i);
						}

						AroundYou fragment = AroundYou.newInstance(fu);
						FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
						fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
					} 
				});
		
		Button party = (Button) FilterInterest.this.getView().findViewById(R.id.btn_party);
		party.setOnClickListener(
				new View.OnClickListener() { 
					@Override 
					public void onClick(View v) {
						ArrayList<User> result = filter("Party");
						User[] fu = new User[result.size()];

						for(int i =0; i< result.size();i++) {
							fu[i] = result.get(i);
						}

						AroundYou fragment = AroundYou.newInstance(fu);
						FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
						fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
					} 
				});
		
		Button languages = (Button) FilterInterest.this.getView().findViewById(R.id.btn_languages);
		languages.setOnClickListener(
				new View.OnClickListener() { 
					@Override 
					public void onClick(View v) {
						ArrayList<User> result = filter("Languages");
						User[] fu = new User[result.size()];

						for(int i =0; i< result.size();i++) {
							fu[i] = result.get(i);
						}

						AroundYou fragment = AroundYou.newInstance(fu);
						FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
						fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
					} 
				});
		
		Button food = (Button) FilterInterest.this.getView().findViewById(R.id.btn_food);
		food.setOnClickListener(
				new View.OnClickListener() { 
					@Override 
					public void onClick(View v) {
						ArrayList<User> result = filter("Food");
						User[] fu = new User[result.size()];

						for(int i =0; i< result.size();i++) {
							fu[i] = result.get(i);
						}

						AroundYou fragment = AroundYou.newInstance(fu);
						FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
						fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
					} 
				});
		
		Button flirt = (Button) FilterInterest.this.getView().findViewById(R.id.btn_flirt);
		flirt.setOnClickListener(
				new View.OnClickListener() { 
					@Override 
					public void onClick(View v) {
						ArrayList<User> result = filter("Flirt");
						User[] fu = new User[result.size()];

						for(int i =0; i< result.size();i++) {
							fu[i] = result.get(i);
						}

						AroundYou fragment = AroundYou.newInstance(fu);
						FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
						fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
					} 
				});
		
		

	}
	/**
	 * Temporary method which fill the table of ForeignUser . Later, we'll get those foreign user 
	 * instance via bluetooth
	 */
	public void initForeignUsers() {
		User caribou = new User(getActivity().getApplicationContext(), "René",R.drawable.caribou,"Stop telling me that I am a moose, damn it!", 0,"",new String[]{"Party","Sports","Music","Languages","Flirt"},10,"Mercury","lagouge@polytech.unice.fr");
		User orca = new User(getActivity().getApplicationContext(),"Willy",R.drawable.orca,"I like waves :) Do you like waves?",500,"015751103923",new String[]{"Party","Sports","Languages"},15,"Venus","");
		User seal = new User(getActivity().getApplicationContext(),"Martin",R.drawable.seal,"Honk honk!",600,"",new String[]{"Party","Music","Food"},20,"Venus","lagouge@polytech.unice.fr");
		User bear = new User(getActivity().getApplicationContext(),"Teddy",R.drawable.bear,"Wanna cuddles ?", 700,"",new String[]{"Sports","Food","Flirt"},18,"Mercury","");
		User pingu = new User(getActivity().getApplicationContext());
	this.foreignUser = new ArrayList<User>();
		this.foreignUser.add(caribou);
		this.foreignUser.add(bear);
		this.foreignUser.add(orca);
		this.foreignUser.add(seal);
		this.foreignUser.add(pingu);

	}

	public ArrayList<User> filter(String fliter){

		ArrayList<User> result = new ArrayList<User>();
		
		for(int i = 0; i < this.foreignUser.size();i++){
			User fu = this.foreignUser.get(i);
		
			String[] interests = fu.getInterests();
			
			for(int k = 0; k < interests.length;k++){
			
				if(interests[k].equals(fliter)){
					result.add(fu);
				}
			}
		}
		return result;
	}
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(SECTION_NUMBER);
    }
}