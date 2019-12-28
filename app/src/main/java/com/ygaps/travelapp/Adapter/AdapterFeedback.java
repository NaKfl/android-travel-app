package com.ygaps.travelapp.Adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ygaps.travelapp.Modal.Feedback;
import com.ygaps.travelapp.Modal.Review;
import com.ygaps.travelapp.R;

import java.util.ArrayList;

public class AdapterFeedback extends BaseAdapter {
    Context myContext;
    int myLayout;
    private int[]pos=new int[100];
    private ArrayList<Feedback> myFeeback;
    private ArrayList<Feedback> arrayList;

    public AdapterFeedback(Context context, int layout, ArrayList<Feedback> feedbacks){
        myContext= context;
        myLayout=layout;
        myFeeback=feedbacks;
        arrayList=new ArrayList<>();
        arrayList.addAll(myFeeback);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }
    public int[] getPos(){return pos;}
    public void resetPos(){
        for (int k=0;k<myFeeback.size();k++){
            pos[k]=k;
        }
    }
    @Override
    public Object getItem(int i) {
        return myFeeback.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        AdapterFeedback.ViewHolder holder;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.feedback_single, null);
            holder=new AdapterFeedback.ViewHolder();
            holder.name=view.findViewById(R.id.fback_name);
            holder.feedback=view.findViewById(R.id.fback_content);
            holder.createOn = view.findViewById(R.id.fback_createOn);
            holder.point= view.findViewById(R.id.fback_point);
            view.setTag(holder);

        } else holder = (AdapterFeedback.ViewHolder)view.getTag();
        //ánh xạ
        holder.name.setText(myFeeback.get(i).getName());
        holder.createOn.setText(myFeeback.get(i).getCreatedOn());
        float temp = Float.parseFloat(myFeeback.get(i).getPoint());
        LayerDrawable stars = (LayerDrawable) holder.point.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(myContext.getResources().getColor(R.color.yellowStart), PorterDuff.Mode.SRC_ATOP);
        holder.point.setRating(temp);
        holder.feedback.setText(myFeeback.get(i).getFeedBack());
        return view;
    }
    static class ViewHolder {
        TextView name;
        TextView createOn;
        TextView feedback;
        RatingBar point;
        ImageView avatar;
    }
}
