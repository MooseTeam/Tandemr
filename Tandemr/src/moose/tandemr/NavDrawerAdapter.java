package moose.tandemr;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.provider.CalendarContract.Colors;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerAdapter extends BaseAdapter {
	
	private Context context;
	private String[] titles;
	private Drawable icon;
	private LayoutInflater inflater;
	private int selectedposition;
	
	static class ViewHolder{
		TextView text;
		ImageView image;
	}
	
	public NavDrawerAdapter(Context context, String[] titles, Drawable icon, 
			int selectedposition){
		//TODO Auto-generated method stub
		this.context = context;
		this.titles = titles;
		this.icon = icon;
		this.inflater = LayoutInflater.from(this.context);
		this.selectedposition = selectedposition;
	}
	
	@Override
	public int getCount() {
		//TODO Auto-generated method stub
		return titles.length;
	}

	@Override
	public Object getItem(int position) {	
		//TODO Auto-generated method stub	
		return null;
	}

	@Override
	public long getItemId(int position) {	
		//TODO Auto-generated method stub	
		return 0;
	}
	
	public void setIcon(Drawable icon){
		this.icon = icon;
	}
	
	public void setTitle(String[] titles){
		this.titles = titles;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//TODO Auto-generated method stub
		ViewHolder mViewHolder;
		int layout;
		
		if (convertView == null) {
			if (position == 0) {
				layout = R.layout.drawer_list_header;
			}
			else {
				layout = R.layout.drawer_list_item;
			}
			convertView = inflater.inflate(layout, null);
            mViewHolder = new ViewHolder();
            convertView.setTag(mViewHolder);
        }
		else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		
		mViewHolder.text = (TextView) convertView.findViewById(R.id.title);
		if (position == 0 )
			mViewHolder.image = (ImageView) convertView.findViewById(R.id.icon);
		
		mViewHolder.text.setText(titles[position]);
		if (position == 0 )
			mViewHolder.image.setImageDrawable(icon);
		
		mViewHolder.text.setTextColor(Color.WHITE);
		
		//Highlight the selected list item
//		if (position == selectedposition) {
//			convertView.setBackgroundColor(R.color.list_background_pressed);
//		}
//		else {
//			convertView.setBackgroundColor(R.color.list_background);
//		}
        
        return convertView;
	}

}
