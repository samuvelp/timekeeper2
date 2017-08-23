package com.example.user.timekeeper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User on 02-08-2017.
 */

public class CustomListAdapter extends BaseAdapter {
    Context context;
    ArrayList<String>  checkInList = new ArrayList<>();
    ArrayList<String> checkOutList = new ArrayList<>();
    ArrayList<String> checkInLocationList = new ArrayList<>();
    ArrayList<String> checkoutLocationList = new ArrayList<>();

    public CustomListAdapter(Context context, ArrayList<String> checkInList, ArrayList<String> checkOutList, ArrayList<String> checkInLocationList, ArrayList<String> checkoutLocationList) {
        this.context = context;
        this.checkInList = checkInList;
        this.checkOutList = checkOutList;
        this.checkInLocationList = checkInLocationList;
        this.checkoutLocationList = checkoutLocationList;
    }

    @Override
    public int getCount() {
        return checkInList.size();
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
        Holder holder;
        LayoutInflater inflater;
        if(convertView==null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.singlelistview,null);

            holder = new Holder();
            holder.TV_checkIn = (TextView) convertView.findViewById(R.id.UI_TV_checkIn);
            holder.TV_checkOut = (TextView) convertView.findViewById(R.id.UI_TV_checkOut);
            holder.TV_checkInLocation=(TextView) convertView.findViewById(R.id.UI_TV_CheckInLocation);
            holder.Tv_checkOutLocation=(TextView) convertView.findViewById(R.id.UI_TV_CheckOutLocation);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.TV_checkIn.setText(checkInList.get(position));
        holder.TV_checkOut.setText(checkOutList.get(position));
        holder.TV_checkInLocation.setText(checkInLocationList.get(position));
        holder.Tv_checkOutLocation.setText(checkoutLocationList.get(position));
        return convertView;
    }
    public class Holder{
        TextView TV_checkIn;
        TextView TV_checkOut;
        TextView TV_checkInLocation;
        TextView Tv_checkOutLocation;
    }
}
