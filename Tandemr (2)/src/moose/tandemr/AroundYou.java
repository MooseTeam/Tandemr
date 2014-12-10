package moose.tandemr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AroundYou extends ListFragment {
	private ForeignUser[] foreignUser;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		initForeignUsers();
		Bitmap[] pictures = new Bitmap[this.foreignUser.length];
		String[] names = new String[this.foreignUser.length];
		
		for(int i = 0; i < pictures.length;i++){
			ForeignUser fu = this.foreignUser[i];
			Bitmap img = fu.getProfile_picture();
			String txt = fu.getName();
			pictures[i] = img;
			names[i] = txt;
		}
		
		MyAdapter adapter = new MyAdapter(pictures,names);
		setListAdapter(adapter);
		((MyAdapter) getListAdapter()).setImages();

	}

	private class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private Bitmap[] images;
		private String[] names;
		
		MyAdapter(Bitmap[] img,String[] txt) {
			mInflater = (LayoutInflater)AroundYou.this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			images = img;
			names = txt;
		}
		public void setImages() {
			for(int i = 0;i < foreignUser.length;i++){
				View view = getView(i, null, getListView());
				//ImageView imageview = (ImageView) view.findViewById(R.id.image);

				Bitmap foreign_image = images[i];
				Canvas canvas = null;
				Bitmap bitmap = getBitmapClippedCircle(foreign_image,200,200,canvas);
				//TODO :store the new image (bitmap) in a tmp rep . And when we are closing the app, the tmp rep is deleted
				images[i] = bitmap;
			}
			notifyDataSetChanged();
		}
		@Override
		public int getCount() {
			return names.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.around_you_line_of_list, null);
				holder = new ViewHolder();
				holder.textView = (TextView)convertView.findViewById(R.id.name);
				holder.imageView = (ImageView)convertView.findViewById(R.id.image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}
			holder.textView.setText(names[position]);
			holder.imageView.setImageBitmap(images[position]);
			return convertView;
		}
	}
	
	
	private static class ViewHolder {
		public ImageView imageView;
		public TextView textView;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		TextView view = (TextView) v.findViewById(R.id.name);
		String txt  = view.getText().toString();
		ForeignUser fu = new ForeignUser(getActivity().getApplicationContext(),"Martin",R.drawable.seal,"Honk honk!",600,"");

		
		for(int i = 0;i< this.foreignUser.length;i++){
			if(foreignUser[i].getName()==txt)
				fu = foreignUser[i];
		}
		
		Fragment fr= ForeignProfileActivity.myInstance(fu);
		FragmentManager fm = getActivity().getSupportFragmentManager();

		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.content_frame, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}
	
	public Bitmap getBitmapClippedCircle(Bitmap bitmap,int width,int height,Canvas canvas) {

		bitmap = Bitmap.createScaledBitmap(bitmap, width,height, false);

		final Bitmap outputBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);

		final Path path = new Path();

		path.addCircle(
				(float)(width / 2)
				, (float)(height / 2)
				, (float) Math.min(width, (height / 2))
				, Path.Direction.CCW);

		canvas = new Canvas(outputBitmap);
		canvas.clipPath(path);
		canvas.drawBitmap(bitmap, 0, 0, null);
		return outputBitmap;
	}
	
	/**
	 * Temporary method which fill the table of ForeignUser . Later, we'll get those foreign user 
	 * instance via bluetooth
	 */
	public void initForeignUsers() {
		ForeignUser caribou = new ForeignUser(getActivity().getApplicationContext(), "René",R.drawable.caribou,"Stop telling me that I am a mose, damn it!", 0,"");
		ForeignUser orca = new ForeignUser(getActivity().getApplicationContext(),"Willy",R.drawable.orca,"I like waves :) Do you likes waves?",500,"01747603793");
		ForeignUser seal = new ForeignUser(getActivity().getApplicationContext(),"Martin",R.drawable.seal,"Honk honk!",600,"");
		ForeignUser bear = new ForeignUser(getActivity().getApplicationContext(),"Teddy",R.drawable.bear,"Wanna cuddles ?", 700,"");
		ForeignUser pingu = new ForeignUser(getActivity().getApplicationContext());
		
		this.foreignUser = new ForeignUser[]{
			caribou,
			orca,
			seal,
			pingu,
			bear
		};
	}

}




