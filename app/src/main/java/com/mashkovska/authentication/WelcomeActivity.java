package com.mashkovska.authentication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;

@SuppressWarnings("ALL")
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initTabFragments();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener((task) -> {
                        if (!task.isSuccessful()) {
                            Log.w("getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = "Retrieved token: " + token;
                        Log.d("WelcomeActivity", "Retrieved token: " + token);
                        Toast.makeText(WelcomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                });
    }

    public void initTabFragments(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabItem tabMovies = findViewById(R.id.movie_tab);
        TabItem tabTab2 = findViewById(R.id.tab_tab2);
        TabItem tabProfile = findViewById(R.id.profile_tab);
        ViewPager viewPager = findViewById(R.id.viewPager);

        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}