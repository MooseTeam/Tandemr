package moose.tandemr;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 
public class FindPeople extends Fragment {
	
	private static int SECTION_NUMBER = 1;
 
	/**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FindPeople newInstance() {
    	FindPeople fragment = new FindPeople();
        return fragment;
    }
	
    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
 
        return inflater.inflate(R.layout.find_people, container, false);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(SECTION_NUMBER);
    }
}