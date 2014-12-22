package moose.tandemr;


import java.util.ArrayList;
import android.widget.ArrayAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;
	
	public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}
	
	//included in main activity changing isheader=false to true
	/*public void addHeader(String title, int icon) {
		navDrawerItems.add(new NavDrawerItem(title, icon, true));
	}*/

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {		
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NavDrawerItem item = (NavDrawerItem) getItem(position);
		int layout;
		if (convertView == null) {
			

			if (item.isHeader){
				layout = R.layout.drawer_list_header;
			}
			else{
				layout = R.layout.drawer_list_item;

			}

            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(layout, null);
        }
         
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
         
        imgIcon.setImageDrawable(navDrawerItems.get(position).getIcon());        
        txtTitle.setText(navDrawerItems.get(position).getTitle());
        
        
        return convertView;
	}

}
