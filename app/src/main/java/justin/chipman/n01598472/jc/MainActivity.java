package justin.chipman.n01598472.jc;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.jusnav_justin) {
                selectedFragment = new Jus1tinFragment();
            } else if (itemId == R.id.jusnav_chipman) {
                selectedFragment = new Chip2manFragment();
            } else if (itemId == R.id.jusnav_n01598472) {
                selectedFragment = new N015938472Fragment();
            } else if (itemId == R.id.jusnav_jc) {
                selectedFragment = new J4CFragment();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

            return true;
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.jusnav_justin);
    }
}

