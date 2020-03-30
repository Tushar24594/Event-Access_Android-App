package in.tushar.eventaccess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import in.tushar.eventaccess.fragments.CheckInFragment;
import in.tushar.eventaccess.fragments.NotificationFragment;
import in.tushar.eventaccess.fragments.QRScanFragment;

public class DetailedActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    private BottomNavigationView bottomNavigationItemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(CheckInFragment.newInstance("", ""));
    }
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.scanQR:
                            openFragment(QRScanFragment.newInstance("", ""));
                            return true;
                        case R.id.chechIn:
                            openFragment(CheckInFragment.newInstance("", ""));
                            return true;
                        case R.id.notifications:
                            openFragment(NotificationFragment.newInstance("", ""));
                            return true;
                    }
                    return false;
                }
            };
}
