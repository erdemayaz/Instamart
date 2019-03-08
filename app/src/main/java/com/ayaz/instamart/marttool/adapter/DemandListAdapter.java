package com.ayaz.instamart.marttool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ayaz.instamart.marttool.R;
import com.ayaz.instamart.marttool.model.DemandList;

/**
 * Created by Ayaz on 23.5.2017
 */

public class DemandListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;

    public DemandListAdapter(Context context){
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return DemandList.demands.size();
    }

    @Override
    public Object getItem(int position) {
        return DemandList.demands.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;

        if(convertView == null){
            rowView = layoutInflater.inflate(R.layout.demand_row_view, null);
            TextView ownerView = (TextView) rowView.findViewById(R.id.demandOwnerView);
            TextView explanationView = (TextView) rowView.findViewById(R.id.demandView);
            TextView dateView = (TextView) rowView.findViewById(R.id.demandDateView);

            ownerView.setText(DemandList.demands.get(position).getOwnerName());
            explanationView.setText(DemandList.demands.get(position).getExplanation());
            dateView.setText(DemandList.demands.get(position).getDate());
        } else{
            rowView = convertView;
        }

        return rowView;
    }
}
