package anhem1nha.shashank.platform.fancyloginpage.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.apptour.anhem1nha.R;
import java.util.ArrayList;
import anhem1nha.shashank.platform.fancyloginpage.Modal.Comment;

public class AdapterComment extends BaseAdapter {
    Context myContext;
    int myLayout;
    private int[]pos=new int[100];
    private ArrayList<Comment> myComment;
    private ArrayList<Comment> arrayList;

    public AdapterComment(Context context, int layout, ArrayList<Comment> commnets){
        myContext= context;
        myLayout=layout;
        myComment=commnets;
        arrayList=new ArrayList<>();
        arrayList.addAll(myComment);
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }
    public int[] getPos(){return pos;}
    public void resetPos(){
        for (int k=0;k<myComment.size();k++){
            pos[k]=k;
        }
    }
    @Override
    public Object getItem(int i) {
        return myComment.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.comment_single, null);
            holder=new ViewHolder();
            holder.name=view.findViewById(R.id.comment_name);
            holder.commentContent=view.findViewById(R.id.comment_content);


            view.setTag(holder);

        } else holder = (ViewHolder)view.getTag();

        //ánh xạ
        holder.name.setText(myComment.get(i).getName());
        holder.commentContent.setText(myComment.get(i).getCommentContent());

        return view;
    }
    static class ViewHolder {
        ImageView avatar;
        TextView name;
        TextView commentContent;
    }
}
