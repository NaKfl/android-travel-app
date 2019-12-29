package com.ygaps.travelapp.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ygaps.travelapp.Modal.Noti;
import com.ygaps.travelapp.R;

import java.util.ArrayList;

public class AdapterNoti extends BaseAdapter {
    Context myContext;
    int myLayout;
    private int[]pos=new int[100];
    private ArrayList<Noti> myNoti;
    private ArrayList<Noti> arrayList;

    public AdapterNoti(Context context, int layout, ArrayList<Noti> notis){
        myContext= context;
        myLayout=layout;
        myNoti=notis;
        arrayList=new ArrayList<>();
        arrayList.addAll(myNoti);
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }
    public int[] getPos(){return pos;}
    public void resetPos(){
        for (int k=0;k<myNoti.size();k++){
            pos[k]=k;
        }
    }
    @Override
    public Object getItem(int i) {
        return myNoti.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.noti_single, null);
            holder=new ViewHolder();
            holder.title=view.findViewById(R.id.noti_title);
            holder.content=view.findViewById(R.id.noti_content);
            holder.date=view.findViewById(R.id.noti_date);
            view.setTag(holder);

        } else holder = (ViewHolder)view.getTag();

        holder.title.setText(myNoti.get(i).getTitle());
        holder.content.setText(myNoti.get(i).getContent());
        holder.date.setText(myNoti.get(i).getDate());
        return view;
    }
    static class ViewHolder {
        TextView title;
        TextView content;
        TextView date;
    }
}
