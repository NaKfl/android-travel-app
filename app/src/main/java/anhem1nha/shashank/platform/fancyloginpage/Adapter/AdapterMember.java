package anhem1nha.shashank.platform.fancyloginpage.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.apptour.anhem1nha.R;
import java.util.ArrayList;
import anhem1nha.shashank.platform.fancyloginpage.Modal.Member;

public class AdapterMember extends BaseAdapter {
    Context myContext;
    int myLayout;
    private int[]pos=new int[100];
    private ArrayList<Member> myMember;
    private ArrayList<Member> arrayList;

    public AdapterMember(Context context, int layout, ArrayList<Member> members){
        myContext= context;
        myLayout=layout;
        myMember=members;
        arrayList=new ArrayList<>();
        arrayList.addAll(myMember);
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }
    public int[] getPos(){return pos;}
    public void resetPos(){
        for (int k=0;k<myMember.size();k++){
            pos[k]=k;
        }
    }
    @Override
    public Object getItem(int i) {
        return myMember.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.member_single, null);
            holder=new ViewHolder();
            holder.name=view.findViewById(R.id.member_name);
            view.setTag(holder);

        } else holder = (ViewHolder)view.getTag();
        //ánh xạ
        holder.name.setText(myMember.get(i).getName());
        return view;
    }
    static class ViewHolder {
        TextView name;
    }
}
