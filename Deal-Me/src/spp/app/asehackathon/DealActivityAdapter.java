package spp.app.asehackathon;

import java.util.ArrayList;

import spp.app.asehackathon.Database.DealDbDetails;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DealActivityAdapter extends BaseAdapter {

	private ArrayList<DealDbDetails> listElements = new ArrayList<DealDbDetails>();
	public ArrayList<DealDbDetails> getListElements() {
		return listElements;
	}

	public void setListElements(ArrayList<DealDbDetails> listElements) {
		this.listElements = listElements;
	}

	private Context context;
	
	public DealActivityAdapter(Context context,ArrayList<DealDbDetails> list) {
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
		Holder2 holder = null;
	    // Holder represents the elements of the view to use
	    // Here are initialized   
	    if(null == row) {               
	        row = LayoutInflater.from(context).inflate(R.layout.wallet_listview, parent, false);                

	        holder = new Holder2();
	        
	       
	        
	        holder.name = (TextView)row.findViewById(R.id.storeNamed);
	        holder.dealInfo = (TextView)row.findViewById(R.id.dealInformation);
	        holder.address = (TextView)row.findViewById(R.id.storeAddressed);
	        
	        
	        row.setTag(holder);
	    } else {
	        holder = (Holder2) row.getTag();
	    }       

	    // here do operations in holder variable example
	    holder.name.setText(listElements.get(position).getStoreName());
	    holder.dealInfo.setText(listElements.get(position).getDealTitle());
	    holder.dealInfo.setTextColor(Color.RED);
	    holder.address.setText(listElements.get(position).getStoreAddress());
	    return row;
	}
	
	public static class Holder2{
		
		
		TextView name;
		TextView address;
		TextView dealInfo;
	}

}

