package com.zizzle.cmpt370;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zizzle.cmpt370.Activities.TeamActivity;
import com.zizzle.cmpt370.Model.TeamInfo;

import java.util.ArrayList;


/**
 * Array adapter for the homepage to display league name above the teams
 */
public class TeamsArrayAdapter extends BaseAdapter{

    /** Teams names */
    private ArrayList<String> teamRank;

    /** Team Info */
    private ArrayList<TeamInfo> teams;

    /** Activity this is being used in */
    private Context context;

    /** Inflater */
    private static LayoutInflater inflater = null;


    /**
     * Constructor for CustomArrayAdapter.
     * @param activity the activity this adapter was made in
     * @param teamRank array of teamRank names that will be displayed
     * @param teams array of team info that will be clicked
     */
    public TeamsArrayAdapter(Activity activity, ArrayList<String> teamRank, ArrayList<TeamInfo> teams) {
        this.teamRank = teamRank;
        this.teams = teams;
        context = activity;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    /**
     * Return the size of the array.
     * @return array size
     */
    @Override
    public int getCount() {
        return teams.size();
    }


    /** Auto-Generated and unused. */
    @Override
    public Object getItem(int position) {
        return 0;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }


    /**
     * Holder class for containing multiple TextViews.
     */
    public class Holder {
        TextView team;
        TextView teamsRank;
    }


    /**
     * Set up the view for the listview.
     * @param position position of item clicked
     * @param convertView View
     * @param parent parent of the view
     * @return view
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.teams_listview, null);
        holder.team = rowView.findViewById(R.id.teams_list);
        holder.teamsRank = rowView.findViewById(R.id.teams_rank_list);
        holder.team.setText(teams.get(position).toString());
        holder.teamsRank.setText(teamRank.get(position));
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