package anhem1nha.shashank.platform.fancyloginpage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.apptour.anhem1nha.R;

public class FragmentSetting extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting,container,false);
        Spinner spinner=(Spinner) rootView.findViewById(R.id.language);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(rootView.getContext(),
        //     R.array.languages, android.R.layout.simple_spinner_dropdown_item);
        String[] a=new String[]{
                "Black birch",
                "Bolean birch",
                "Canoe birch",
                "Cherry birch",
                "European weeping birch"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                rootView.getContext(),R.layout.spinner_items,a
        );
        spinner.setAdapter(adapter);
        return rootView;
    }
}
