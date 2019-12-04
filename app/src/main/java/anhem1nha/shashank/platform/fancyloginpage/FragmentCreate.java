package anhem1nha.shashank.platform.fancyloginpage;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.apptour.anhem1nha.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FragmentCreate extends Fragment {
    EditText tour_name,startDate,endDate,adults,children,mincost,maxcost;
    RadioGroup Private;
    RadioButton truePrivate;
    RadioButton falsePrivate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create,container,false);
        tour_name = rootView.findViewById(R.id.tour_name);
        startDate= rootView.findViewById(R.id.startDate);
        startDate.setFocusable(false);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickTime1();
            }
        });
        endDate= rootView.findViewById(R.id.endDate);
        endDate.setFocusable(false);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickTime2();
            }
        });
        adults = rootView.findViewById(R.id.adults);
        children = rootView.findViewById(R.id.children);
        mincost = rootView.findViewById(R.id.mincost);
        maxcost = rootView.findViewById(R.id.maxcost);







        return rootView;
    }
    private void PickTime1()
    {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                calendar.set(i,i1,i2);
                startDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },1999,01,01);
        datePickerDialog.show();
    }
    private void PickTime2()
    {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                calendar.set(i,i1,i2);
                endDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },1999,01,01);
        datePickerDialog.show();
    }
}