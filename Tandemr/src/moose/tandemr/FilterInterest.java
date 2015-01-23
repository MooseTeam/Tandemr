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
	
    private static final String ARG_SECTION_NUMBER = "section_number";

	/**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FilterInterest newInstance(int sectionNumber) {
    	FilterInterest fragment = new FilterInterest();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

	private ArrayList<ForeignUser> foreignUser;

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
						ArrayList<ForeignUser> result = filter("Sports");
						ForeignUser[] fu = new ForeignUser[result.size()];

						for(int i =0; i< result.size();i++) {
							fu[i] = result.get(i);
						}

						AroundYou fragment = AroundYou.newInstance(fu,4);
						FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
						fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
					} 
				});
		
		Button music = (Button) FilterInterest.this.getView().findViewById(R.id.btn_music);
		music.setOnClickListener(
				new View.OnClickListener() { 
					@Override 
					public void onClick(View v) {
						ArrayList<ForeignUser> result = filter("Music");
						ForeignUser[] fu = new ForeignUser[result.size()];

						for(int i =0; i< result.size();i++) {
							fu[i] = result.get(i);
						}

						AroundYou fragment = AroundYou.newInstance(fu,4);
						FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
						fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
					} 
				});
		
		Button party = (Button) FilterInterest.this.getView().findViewById(R.id.btn_party);
		party.setOnClickListener(
				new View.OnClickListener() { 
					@Override 
					public void onClick(View v) {
						ArrayList<ForeignUser> result = filter("Party");
						ForeignUser[] fu = new ForeignUser[result.size()];

						for(int i =0; i< result.size();i++) {
							fu[i] = result.get(i);
						}

						AroundYou fragment = AroundYou.newInstance(fu,4);
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
		ForeignUser caribou = new ForeignUser(getActivity().getApplicationContext(), "René",R.drawable.caribou,"Stop telling me that I am a moose, damn it!", 0,"",new String[]{"Party","Sports","Music"},10,"Mercury","lagouge@polytech.unice.fr");
        ForeignUser orca = new ForeignUser(getActivity().getApplicationContext(),"Willy",R.drawable.orca,"I like waves :) Do you like waves?",500,"015751103923",new String[]{"Party","Sports"},15,"Venus","");
        ForeignUser seal = new ForeignUser(getActivity().getApplicationContext(),"Martin",R.drawable.seal,"Honk honk!",600,"",new String[]{"Party","Music"},20,"Venus","lagouge@polytech.unice.fr");
        ForeignUser bear = new ForeignUser(getActivity().getApplicationContext(),"Teddy",R.drawable.bear,"Wanna cuddles ?", 700,"",new String[]{"Sports"},18,"Mercury","");
        ForeignUser pingu = new ForeignUser(getActivity().getApplicationContext());
	this.foreignUser = new ArrayList<ForeignUser>();
		this.foreignUser.add(caribou);
		this.foreignUser.add(bear);
		this.foreignUser.add(orca);
		this.foreignUser.add(seal);
		this.foreignUser.add(pingu);

	}

	public ArrayList<ForeignUser> filter(String fliter){

		ArrayList<ForeignUser> result = new ArrayList<ForeignUser>();
		
		for(int i = 0; i < this.foreignUser.size();i++){
			ForeignUser fu = this.foreignUser.get(i);
		
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
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

}