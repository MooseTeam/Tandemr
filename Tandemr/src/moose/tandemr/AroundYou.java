package moose.tandemr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Show all people around the user .
 * 
 *
 */
public class AroundYou extends ListFragment {
	
	/**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AroundYou newInstance() {
    	AroundYou fragment = new AroundYou();
        return fragment;
    }
    
    private ForeignUser[] foreignUser;

	/**
	 * A method which defines an instance AroundYou with a table of users
	 * @param foreignUser
	 * @return
	 */
	public static AroundYou newInstance(ForeignUser[] foreignUser){
		AroundYou fragment = new AroundYou();
		fragment.setForeignUser(foreignUser);
		return fragment;
	}

	/**
	 * Set the table of foreign users
	 * @param fu
	 */
	public void setForeignUser(ForeignUser[] fu){
		this.foreignUser = fu;
	}

	/**
	 * Create the view
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * Initialize the view : get each user and display his/her picture and name in a list .
	 */
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		//to modifiy 
		if(this.foreignUser==null || this.foreignUser.length == 0)
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

	/**
	 * the adapter which defines the list 
	 * 
	 *
	 */
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
			return null;
		}

		@Override
		public long getItemId(int position) {
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

	/**
	 * Keep inside what's going to be displayes on each row of the list .
	 * 
	 *
	 */
	private static class ViewHolder {
		public ImageView imageView;
		public TextView textView;
	}

	/**
	 * Defines what happen when we click on an element of the list 
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		TextView view = (TextView) v.findViewById(R.id.name);
		String txt  = view.getText().toString();
		ForeignUser fu = new ForeignUser(getActivity().getApplicationContext());

		for(int i = 0;i< this.foreignUser.length;i++){
			if(foreignUser[i].getName()==txt)
				fu = foreignUser[i];
		}

		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        fragmentManager.beginTransaction()
            .replace(R.id.container, ForeignProfileActivity.newInstance(fu))
            .commit();
	}

	/**
	 * Modify an image to put it round .
	 * @param bitmap
	 * @param width
	 * @param height
	 * @param canvas
	 * @return
	 */
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
		ForeignUser caribou = new ForeignUser(getActivity().getApplicationContext(), "René",R.drawable.caribou,"Stop telling me that I am a moose, damn it!", 0,"",new String[]{"Party","Sports","Music"},10,"Mercury","lagouge@polytech.unice.fr");
		ForeignUser orca = new ForeignUser(getActivity().getApplicationContext(),"Willy",R.drawable.orca,"I like waves :) Do you like waves?",500,"015751103923",new String[]{"Party","Sports"},15,"Venus","");
		ForeignUser seal = new ForeignUser(getActivity().getApplicationContext(),"Martin",R.drawable.seal,"Honk honk!",600,"",new String[]{"Party","Music"},20,"Venus","lagouge@polytech.unice.fr");
		ForeignUser bear = new ForeignUser(getActivity().getApplicationContext(),"Teddy",R.drawable.bear,"Wanna cuddles ?", 700,"",new String[]{"Sports"},18,"Mercury","");
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