package moose.tandemr;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 
public class FindPeople extends Fragment {
 
    private static final String ARG_SECTION_NUMBER = "section_number";

	/**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FindPeople newInstance(int sectionNumber) {
    	FindPeople fragment = new FindPeople();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
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
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

}