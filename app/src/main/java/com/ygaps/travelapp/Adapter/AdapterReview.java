package com.ygaps.travelapp.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ygaps.travelapp.Modal.Review;
import com.ygaps.travelapp.R;

import java.util.ArrayList;

public class AdapterReview extends BaseAdapter {
    Context myContext;
    int myLayout;
    private int[]pos=new int[100];
    private ArrayList<Review> myReview;
    private ArrayList<Review> arrayList;

    public AdapterReview(Context context, int layout, ArrayList<Review> ratings){
        myContext= context;
        myLayout=layout;
        myReview=ratings;
        arrayList=new ArrayList<>();
        arrayList.addAll(myReview);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }
    public int[] getPos(){return pos;}
    public void resetPos(){
        for (int k=0;k<myReview.size();k++){
            pos[k]=k;
        }
    }
    @Override
    public Object getItem(int i) {
        return myReview.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.rate_single, null);
            holder=new ViewHolder();
            holder.name=view.findViewById(R.id.name_review);
            holder.review=view.findViewById(R.id.review);
            holder.createOn = view.findViewById(R.id.dateCreateReview);
            view.setTag(holder);

        } else holder = (ViewHolder)view.getTag();
        //ánh xạ
        holder.review.setText(myReview.get(i).getReview());
        holder.name.setText(myReview.get(i).getName());
        holder.createOn.setText(myReview.get(i).getCreatedOn());
        return view;
    }
    static class ViewHolder {
        TextView name;
        TextView review;
        TextView createOn;
        ImageView avatar;
    }
}

