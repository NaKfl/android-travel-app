package anhem1nha.shashank.platform.fancyloginpage.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.apptour.anhem1nha.R;

import org.json.JSONObject;

import java.util.ArrayList;

import anhem1nha.shashank.platform.fancyloginpage.Modal.StopPoint;

public class AdapterStopPoint extends BaseAdapter {
    Context myContext;
    int myLayout;
    private int[]pos=new int[100];
    private ArrayList<StopPoint> myStopPoint;
    private ArrayList<StopPoint> arrayList;

    public AdapterStopPoint(Context context, int layout, ArrayList<StopPoint> stopPoints){
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

        ViewHolder holder;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.stop_point_single, null);
            holder=new ViewHolder();
            holder.name=view.findViewById(R.id.stop_point_title);

            view.setTag(holder);

        } else holder = (ViewHolder)view.getTag();

        //ánh xạ
        holder.name.setText(myStopPoint.get(i).getName());

        return view;
    }
    static class ViewHolder {
        ImageView avatar;
        TextView name;
    }
}
