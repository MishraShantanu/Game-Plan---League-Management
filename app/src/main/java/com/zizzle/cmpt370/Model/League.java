package com.zizzle.cmpt370.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


/**
 * League class for holding the information about a league.
 */
public class League implements Serializable {

    /** Name of the league. */
    private String name;

    /** Information of owner of the league. */
    private MemberInfo ownerInfo;

    /** Sport played in the league. */
    private String sport;

    /** Description of the league. */
    private String description;

    /**
     * Map associating string team names with TeamInfo objects
     */
    private HashMap<String,TeamInfo> teamsInfoMap;


    /**
     * Constructor for league object.
     * @param name: Name of the league.
     * @param ownerInfo: MemberInfo object, containing information of the member that owns the league.
     * @param sport: Type of the sport that the league plays.
     * @param description: Description of the league.
     */
    public League(String name, MemberInfo ownerInfo, String sport, String description) {
        this.name = name;
        this.ownerInfo = ownerInfo;
        this.sport = sport;
        this.description = description;
        this.teamsInfoMap = new HashMap<>();
    }

    /**
     * Blank constructor required for reassembling leagues when read in from database
     */
    public League(){

    }

    /**
     * Returns a HashMap<String,TeamInfo> with string names of teams and TeamInfo values of teams in this league
     * @return HashMap<String,TeamInfo> described above
     */
    public HashMap<String,TeamInfo>getTeamsInfoMap(){
        // this method is only used to allow firebase to recognize and store our instance variable teamsInfoMap
        return this.teamsInfoMap;
    }


    /**
     * Retrieves the name of the league.
     * @return name of the league.
     */
    public String getName() { return name; }

    /**
     * Retrieves a MemberInfo object representing the owner of the league
     * @return MemberInfo object containing info about the owner of the league
     */
    public MemberInfo getOwnerInfo(){
        return ownerInfo;
    }


    /**
     * Retrieves the sport the league plays.
     * @return sport the league plays.
     */
    public String getSport() { return sport; }


    /**
     * Retrieves the description of the league.
     * @return description of the league.
     */
    public String getDescription() { return description; }


    /**
     * Retrieves a sorted ArrayList of TeamInfo objects for the teams of the league, this list is sorted
     * by wins of each team so TeamInfos with more wins will appear first in the list
     * @return sorted ArrayList of TeamInfo for the teams in the league.
     */
    public ArrayList<TeamInfo> getSortedTeamInfos() {
        // this.teamsInfoMap may be null if we've don't yet have any teams and have read this league from the database
        if(this.teamsInfoMap == null){
            // if this is the case, simply return an empty arraylist
            return new ArrayList<TeamInfo>();
        }
        // otherwise create an arraylist from the values of our hashmap
        ArrayList<TeamInfo> teamInfos = new ArrayList<>(this.teamsInfoMap.values());
        Collections.sort(teamInfos);
        for(TeamInfo ti : teamInfos){
            Log.d("ti",ti.toString());
        }
        return teamInfos;
    }


    /**
     * Set the name the league to one provided.
     * @param name: new name for the league.
     */
    public void setName(String name) { this.name = name; }


    /**
     * Transfer ownership to the new owner provided.
     * @param newOwner: Member object describing the new owner of the league.
     */
    public void setOwner(Member newOwner) {
        this.ownerInfo = new MemberInfo(newOwner);
        // TODO update database to reflect this new owner
    }


    /**
     * Set the sport the league plays to one provided.
     * @param sport: Name of the sport.
     */
    public void setSport(String sport) {
        this.sport = sport;
        // TODO update database to reflect this new sport
    }


    /**
     * Set the description to the one provided.
     * @param description: String of the new team description.
     */
    public void setDescription(String description) {
        this.description = description;
        // TODO update the database to reflect the new description
    }


    /**
     * Add a team to the league.
     * @param team: Team to be added, team must have a unique name for this league
     * @throws IllegalStateException if the input team doesn't have a unique name in this league
     */
    public void addTeam(Team team) throws IllegalStateException, IllegalArgumentException{
        // represent this team with a TeamInfo object
        TeamInfo newTeamInfo = new TeamInfo(team);
        // ensure that the new team is unique in this league, if this team has a unique name, it is considered to be unique
        if(this.teamsInfoMap.containsKey(newTeamInfo.getName())){
            // team name isn't unique
            throw new IllegalArgumentException("Team: " + team.getName() + " cannot be added to league: " + this.getName() + " another team with this name already exists");
        }
        // TODO write team object to database, update league in database to contain this TeamInfo
    }


    /**
     * Remove a team from the list of teams in the league, if the specified teamName isn't present in this
     * league, this becomes a no-op. Team can only be removed if it has no members except for the owner
     * @param teamName: Name of the team to remove.
     * @throws IllegalStateException if Team to be deleted has more members than the owner
     */
    public void removeTeam(String teamName) {
        // TODO make sure that team to be removed has no members except the owner, need to read in Team off the database
        // TODO could also remove all members from team once team is to be removed
        // delete TeamInfo from league locally
        this.teamsInfoMap.remove(teamName);
    }


    /**
     * Sort the teams from best to worst.
     */
    public void sortTeams() {
        // TODO 18/01/2020 Sort teams based on the standings of teams.
        // TODO could store wins with each TeamInfo object so TeamInfos can be sorted
    }


    /**
     * Retrieve a string of the information stored inside the League object.
     * @return string of information about the league.
     */
    @NonNull
    public String toString() {
        return this.getName();
    }

    /**
     * Determines if this League is equal to another object
     * @param other: Object being compared to this league
     * @return boolean true if other is equal to this league, false if other isn't equal to this league
     */
    @Override
    public boolean equals(Object other){
        if(other instanceof League){
            // compare league fields, equal leagues have same: name, sport, description, teams, and owner
            League otherLeague = (League) other;
            boolean teamsEqual = this.getSortedTeamInfos().equals(otherLeague.getSortedTeamInfos());
            boolean ownerEqual = this.ownerInfo.equals(otherLeague.getOwnerInfo());
            return teamsEqual && ownerEqual && this.description.equals(otherLeague.description) && this.name.equals(otherLeague.name) && this.sport.equals(otherLeague.sport);
        }
        // other isn't a league, cannot be equal to this league
        return false;
    }
}
