package com.ayaz.instamart.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ayaz.instamart.marttool.R;
import com.ayaz.instamart.marttool.handler.ConnectHandler;
import com.ayaz.instamart.marttool.model.Demand;
import com.ayaz.instamart.marttool.task.UploadDemandTask;
import com.ayaz.instamart.marttool.task.UploadProductTask;
import com.ayaz.instamart.sdk.Preferences;
import com.ayaz.instamart.sdk.Util;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DemandPublicationActivity extends AppCompatActivity {
    Context context = this;

    @BindView(R.id.demand_publication_button)
    Button demandPublicationButton;

    @BindView(R.id.demand_explanation_text)
    TextView demandExplanationView;

    @BindView(R.id.demand_uploading_progress_bar)
    ProgressBar demandProgressView;

    private Demand demand = new Demand();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demand);
        ButterKnife.bind(this);

        demandPublicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDemand(demandExplanationView.getText().toString());
                uploadProduct();
            }
        });
    }

    void uploadProduct(){
        demandProgressView.setVisibility(View.VISIBLE);
        ConnectHandler connectHandler = new ConnectHandler() {
            @Override
            public void onSucceed(JSONObject result) {
                demandProgressView.setVisibility(View.GONE);
                Intent demandListIntent = new Intent(context, DemandListActivity.class);
                startActivity(demandListIntent);
                finish();
                Toast.makeText(context, "Ürün Gönderildi.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(JSONObject result) {
                demandProgressView.setVisibility(View.GONE);
                finish();
                Toast.makeText(context, "Ürün Gönderilemedi.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProblem() {
                demandProgressView.setVisibility(View.GONE);
                finish();
                Toast.makeText(context, "Gönderimde Problem Var.", Toast.LENGTH_SHORT).show();
            }
        };
        UploadDemandTask task = new UploadDemandTask(context, demand);
        task.setHandler(connectHandler);
        task.start();
    }

    private void setDemand(String explanation){
        Date date = new Date();
        DateFormat df = SimpleDateFormat.getDateInstance();

        demand.setOwnerName(Preferences.getSavedName(context));
        demand.setDate(df.format(date));
        demand.setExplanation(explanation);
        demand.setLongitude(Double.parseDouble(Preferences.getSavedLongitude(context)));
        demand.setLatitude(Double.parseDouble(Preferences.getSavedLatitude(context)));
    }
}
