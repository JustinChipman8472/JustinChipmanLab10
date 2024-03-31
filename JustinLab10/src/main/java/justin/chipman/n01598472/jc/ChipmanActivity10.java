// justin chipman n01598472
package justin.chipman.n01598472.jc;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;

import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.gms.ads.MobileAds;

public class ChipmanActivity10 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.jusbottom_navigation);
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

            getSupportFragmentManager().beginTransaction().replace(R.id.jusfragment_container,selectedFragment).commit();

            return true;
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.jusnav_justin);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
        channel.setDescription(description);
        // Register the channel with the system
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

    }
}

