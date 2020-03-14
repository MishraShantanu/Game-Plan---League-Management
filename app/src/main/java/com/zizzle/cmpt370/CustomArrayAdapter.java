package com.zizzle.cmpt370;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zizzle.cmpt370.Activities.HomeActivity;
import com.zizzle.cmpt370.Activities.TeamActivity;
import com.zizzle.cmpt370.Model.LeagueInfo;
import com.zizzle.cmpt370.Model.TeamInfo;
import com.zizzle.cmpt370.R;

import java.util.ArrayList;


public class CustomArrayAdapter extends BaseAdapter{

    ArrayList<String> leagues;
    ArrayList<TeamInfo> teams;
    Context context;

    private static LayoutInflater inflater = null;

    public CustomArrayAdapter(Activity activity, ArrayList<String> leagues, ArrayList<TeamInfo> teams) {
        this.leagues = leagues;
        this.teams = teams;
        context = activity;

        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return teams.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        TextView team;
        TextView league;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.home_listview, null);
        holder.team = rowView.findViewById(R.id.home_team_text);
        holder.league = rowView.findViewById(R.id.home_league_text);
        holder.team.setText(teams.get(position).toString());
        holder.league.setText(leagues.get(position));
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // TeamInfo object that was clicked.
                TeamInfo clickedTeamInfo = teams.get(position);

                // Intent for the team clicked.
                Intent teamIntent = new Intent(context, TeamActivity.class);
                // pass the clicked TeamInfo to the Team page through this intent
                teamIntent.putExtra("TEAM_INFO_CLICKED",clickedTeamInfo);
                context.startActivity(teamIntent);
                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        return rowView;
    }

}