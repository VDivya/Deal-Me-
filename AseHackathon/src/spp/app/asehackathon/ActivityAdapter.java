package spp.app.asehackathon;

import java.util.ArrayList;

import spp.app.asehackathon.Database.PlaceDbDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ActivityAdapter extends BaseAdapter {

	private ArrayList<PlaceDbDetails> listElements = new ArrayList<PlaceDbDetails>();
	public ArrayList<PlaceDbDetails> getListElements() {
		return listElements;
	}

	public void setListElements(ArrayList<PlaceDbDetails> listElements) {
		this.listElements = listElements;
	}

	private Context context;
	
	public ActivityAdapter(Context context,ArrayList<PlaceDbDetails> list) {
		this.listElements = list;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listElements.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listElements.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		Holder holder = null;
	    // Holder represents the elements of the view to use
	    // Here are initialized   
	    if(null == row) {               
	        row = LayoutInflater.from(context).inflate(R.layout.wish_listview, parent, false);                

	        holder = new Holder();
	        
	       
	        
	        holder.name = (TextView)row.findViewById(R.id.placeName);
	        
	        holder.address = (TextView)row.findViewById(R.id.placeAddress);
	        
	        
	        row.setTag(holder);
	    } else {
	        holder = (Holder) row.getTag();
	    }       

	    // here do operations in holder variable example
	    holder.name.setText(listElements.get(position).getPname());
	    holder.address.setText(listElements.get(position).getPaddress());
	    return row;
	}
	
	public static class Holder{
		
		
		TextView name;
		TextView address;
	}

}

