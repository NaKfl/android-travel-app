package com.ygaps.travelapp.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ygaps.travelapp.Modal.StopPoint;
import com.ygaps.travelapp.R;

import java.util.ArrayList;

public class AdapterStp extends BaseAdapter {

    Context myContext;
    int myLayout;
    private int[]pos=new int[100];
    private ArrayList<StopPoint> myStopPoint;
    private ArrayList<StopPoint> arrayList;

    public AdapterStp(Context context, int layout, ArrayList<StopPoint> stopPoints){
        myContext= context;
        myLayout=layout;
        myStopPoint=stopPoints;
        arrayList=new ArrayList<>();
        arrayList.addAll(myStopPoint);
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }
    public int[] getPos(){return pos;}
    public void resetPos(){
        for (int k=0;k<myStopPoint.size();k++){
            pos[k]=k;
        }
    }
    @Override
    public Object getItem(int i) {
        return myStopPoint.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        AdapterStp.ViewHolder holder;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.list_stp_single, null);
            holder=new AdapterStp.ViewHolder();
            holder.name= (TextView) view.findViewById(R.id.name_stp);
            holder.cost = (TextView) view.findViewById(R.id.stp_cost);
            holder.address = (TextView) view.findViewById(R.id.stp_address);
            view.setTag(holder);

        } else holder = (AdapterStp.ViewHolder)view.getTag();

        //ánh xạ
        holder.name.setText(myStopPoint.get(i).getName());
        holder.cost.setText(myStopPoint.get(i).getMinCost()+" - "+myStopPoint.get(i).getMaxCost());
        holder.address.setText(myStopPoint.get(i).getAddress());
        return view;
    }
    static class ViewHolder {
        TextView name;
        TextView address;
        TextView cost;
        TextView time;
    }
}
