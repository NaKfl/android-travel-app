package anhem1nha.shashank.platform.fancyloginpage;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.apptour.anhem1nha.R;
public class Home extends AppCompatActivity {

    BottomNavigationView mainNav;
    FrameLayout mainFrame;
    FragmentHome homeFragment;
    FragmentHome temp;
    FragmentHistory historyFragment;
    FragmentCreate createFragment;
    FragmentNofi nofiFragment;
    FragmentSetting settingFragment;
    EditText numPage;
    Button btnShow;
    public static String numTour,page="1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mainNav= (BottomNavigationView) findViewById(R.id.nav);
        mainFrame= (FrameLayout) findViewById(R.id.main_frame);

        numPage = (EditText) findViewById(R.id.numPage);
        btnShow = (Button) findViewById(R.id.btnShow);

        numPage.setText("1");
        homeFragment = new FragmentHome();
        setFragment(homeFragment);

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = numPage.getText().toString();
                homeFragment = new FragmentHome();
                setFragment(homeFragment);
            }
        });

        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        homeFragment = new FragmentHome();
                        setFragment(homeFragment);
                        break;
                    case R.id.nav_history:
                        historyFragment = new FragmentHistory();
                        setFragment(historyFragment);
                        break;
                    case R.id.nav_create:
                        createFragment = new FragmentCreate();
                        setFragment(createFragment);
                        break;
                    case R.id.nav_alert:
                        nofiFragment = new FragmentNofi();
                        setFragment(nofiFragment);
                        break;
                    case R.id.nav_setting:
                        settingFragment = new FragmentSetting();
                        setFragment(settingFragment);
                        break;
                }
                return true;
            }
        });
    }

    private void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

}
