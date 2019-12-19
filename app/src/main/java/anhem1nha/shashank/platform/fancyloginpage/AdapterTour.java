package anhem1nha.shashank.platform.fancyloginpage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apptour.anhem1nha.R;

import java.util.ArrayList;

import anhem1nha.shashank.platform.fancyloginpage.Adapter.Tour;

public class AdapterTour extends BaseAdapter {
    Context myContext;
    int myLayout;
    private int[]pos=new int[100];
    private ArrayList<Tour> myTour;
    private ArrayList<Tour> arrayList;

    public AdapterTour(Context context,int layout,ArrayList<Tour> tours){
        myContext=context;
        myLayout=layout;
        myTour=tours;
        arrayList=new ArrayList<>();
        arrayList.addAll(myTour);
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }
    public int[] getPos(){return pos;}
    public void resetPos(){
        for (int k=0;k<myTour.size();k++){
            pos[k]=k;
        }
    }
    @Override
    public Object getItem(int i) {
        return myTour.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.tour_single, null);
            holder=new ViewHolder();
            holder.avatar=view.findViewById(R.id.avatar);
            holder.destination=view.findViewById(R.id.destination);
            holder.datetodate=view.findViewById(R.id.datetodate);
            holder.people=view.findViewById(R.id.people);
            holder.cash=view.findViewById(R.id.cash);
            view.setTag(holder);

        } else holder = (ViewHolder)view.getTag();

        //ánh xạ
        holder.avatar.setImageResource(R.drawable.imgtour);
        holder.destination.setText(myTour.get(i).destination);
        holder.datetodate.setText(myTour.get(i).date);
        holder.people.setText(myTour.get(i).people);
        holder.cash.setText(myTour.get(i).cash);

        return view;
    }
    static class ViewHolder {
        ImageView avatar;
        TextView destination;
        TextView datetodate;
        TextView people;
        TextView cash;
    }
}
