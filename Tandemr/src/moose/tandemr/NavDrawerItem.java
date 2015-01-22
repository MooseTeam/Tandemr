package moose.tandemr;

import android.graphics.drawable.Drawable;

public class NavDrawerItem {
	
	private String title;
	private Drawable icon;
	private String count = "0";
	// boolean to set visiblity of the counter
	private boolean isCounterVisible = false;
	public boolean isHeader;

	
	public NavDrawerItem(){}

	public NavDrawerItem(String title, Drawable icon, boolean header){
		this.title = title;
		this.icon = icon;
		this.isHeader=header;

	}
	
	public NavDrawerItem(String title, Drawable icon, boolean isCounterVisible, String count, boolean header){
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
		this.isHeader=header;

	}
	
	public String getTitle(){
		return this.title;
	}
	
	public Drawable getIcon(){
		return this.icon;
	}
	
	public String getCount(){
		return this.count;
	}
	
	public boolean getCounterVisibility(){
		return this.isCounterVisible;
	}

	public boolean getIsHeader(){
		return this.isHeader;
	}
		
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setIcon(Drawable icon){
		this.icon = icon;
	}
	
	public void setCount(String count){
		this.count = count;
	}
	
	public void setCounterVisibility(boolean isCounterVisible){
		this.isCounterVisible = isCounterVisible;
	}
	
	public void setIsHeader(boolean isHeader){
		this.isHeader = isHeader;
	}
	
}
