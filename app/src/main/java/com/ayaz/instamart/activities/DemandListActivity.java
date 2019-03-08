package com.ayaz.instamart.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ayaz.instamart.marttool.EndlessScrollListener;
import com.ayaz.instamart.marttool.R;
import com.ayaz.instamart.marttool.adapter.DemandListAdapter;
import com.ayaz.instamart.marttool.handler.DemandPresentationHandler;
import com.ayaz.instamart.marttool.handler.ProductPresentationHandler;
import com.ayaz.instamart.marttool.model.Demand;
import com.ayaz.instamart.marttool.model.Product;
import com.ayaz.instamart.marttool.task.LoadDemandTask;
import com.ayaz.instamart.marttool.task.LoadProductTask;

import java.util.ArrayList;

public class DemandListActivity extends AppCompatActivity {
    Context context = this;
    DemandListAdapter demandAdapter;
    private ProgressBar progressBar;
    boolean x = false;
    int pageCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demand_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.getIndeterminateDrawable().setColorFilter(0xAA6BC1C1, PorterDuff.Mode.SRC_IN);

        ListView listView = (ListView) findViewById(R.id.demand_list);
        demandAdapter = new DemandListAdapter(context);
        listView.setAdapter(demandAdapter);
        EndlessScrollListener scrollListener = new EndlessScrollListener(new EndlessScrollListener.LoadListener() {
            @Override
            public boolean onRefresh(int pageCount) {
                DemandPresentationHandler handler = new DemandPresentationHandler() {
                    @Override
                    public void onCompleted(String result, ArrayList<Demand> demands) {
                        demandAdapter.notifyDataSetChanged();
                        Log.e("after", String.valueOf(demands.size()));
                        progressBar.setVisibility(View.GONE);
                        x = true;
                    }
                };

                //Log.e("beforeWorkProductTask", String.valueOf(products.size()));
                LoadDemandTask loadDemandTask = new LoadDemandTask(context, pageCounter++);
                loadDemandTask.setHandler(handler);
                loadDemandTask.start();
                return x;
            }
        });

        listView.setOnScrollListener(scrollListener);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_demand);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent demandPublicationIntent = new Intent(context, DemandPublicationActivity.class);
                startActivity(demandPublicationIntent);
                finish();
            }
        });
    }

}
