package com.mashkovska.authentication;

import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends AppCompatActivity {

    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout linearLayout;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mFirebaseAuth = FirebaseAuth.getInstance();

        initViews();
        loadMovies();
        registerNetworkMonitoring();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.data_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        linearLayout = findViewById(R.id.linearLayout);
        swipeRefreshLayout = findViewById(R.id.data_list_swipe_refresh);
        setupSwipeToRefresh();
    }


    private void loadMovies(){
        swipeRefreshLayout.setRefreshing(true);
        final MovieApi apiService = getApplicationEx().getMovieService();
        final Call<List<Movie>> call = apiService.getAllMovies();


        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(final Call<List<Movie>> call,
                                   final Response<List<Movie>> response) {
                adapter = new CustomAdapter(response.body());
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Snackbar.make(linearLayout, "Failure", Snackbar.LENGTH_LONG).show();
            }

        });
    }

    private void registerNetworkMonitoring() {
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        NetworkChangeReceiver receiver = new NetworkChangeReceiver(linearLayout);
        this.registerReceiver(receiver, filter);
    }

    private void setupSwipeToRefresh(){
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    loadMovies();
                    swipeRefreshLayout.setRefreshing(false);
                }
        );
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    }
    private ApplicationEx getApplicationEx(){
        return ((ApplicationEx) getApplication());
    }
}

