package com.zizzle.cmpt370.Model;

import android.support.annotation.NonNull;

import java.util.HashSet;


/**
 * League class for holding the information about a league.
 */
public class League {

    /** Name of the league. */
    private String name;

    /** Information of owner of the league. */
    private MemberInfo ownerInfo;

    /** Sport played in the league. */
    private String sport;

    /** Description of the league. */
    private String description;

    /**
     * Teams involved in the league.
     */
    private HashSet<TeamInfo> teamsInfo; // set is used so database fields can easily be updated


    /**
     * Constructor for league object.
     * @param name: Name of the league.
     * @param owner: Member that owns the league.
     * @param sport: Type of the sport that the league plays.
     * @param description: Description of the league.
     */
    public League(String name, Member owner, String sport, String description) {
        this.name = name;
        this.ownerInfo = new MemberInfo(owner);
        this.sport = sport;
        this.description = description;
        this.teamsInfo = new HashSet<>();
    }

    /**
     * Blank constructor required for reassembling leagues when read in from database
     */
    public League(){

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
     * Retrieves the owner of the league.
     * @return owner of the league.
     */
    public Member getOwner() {
        // read the owner in from the database using ownerInfo
        return Storage.readMember(ownerInfo);
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
     * Retrieves a list of TeamInfo objects for the teams of the league
     * @return list of TeamInfo for the teams in the league.
     */
    public HashSet<TeamInfo> getTeamInfos() { return teamsInfo; }


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
        // update database to reflect this new owner
        Storage.updateLeagueField(this,Storage.LEAGUE_OWNER,newOwner);
    }


    /**
     * Set the sport the league plays to one provided.
     * @param sport: Name of the sport.
     */
    public void setSport(String sport) {
        this.sport = sport;
        // update database to reflect this new sport
        Storage.updateLeagueField(this,Storage.LEAGUE_SPORT,sport);
    }


    /**
     * Set the description to the one provided.
     * @param description: String of the new team description.
     */
    public void setDescription(String description) {
        this.description = description;
        // update the database to reflect the new description
        Storage.updateLeagueField(this,Storage.LEAGUE_DESCRIPTION,description);
    }


    /**
     * Add a team to the league.
     * @param team: Team to be added, team must have a unique name for this league
     * @throws IllegalStateException if the input team doesn't have a unique name in this league
     */
    public void addTeam(Team team) throws IllegalStateException, IllegalArgumentException{
        // represent this team with a TeamInfo object
        TeamInfo newTeamInfo = new TeamInfo(team);
        // ensure that the new team is unique in this league, if the TeamInfo for this new team is
        // unique, the team must be unique
        if(this.teamsInfo.contains(newTeamInfo)){
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
        for (TeamInfo currentInfo : teamsInfo) {
            if (currentInfo.getName().equals(teamName)) {
                teamsInfo.remove(currentInfo);
                // TODO remove TeamInfo from league on the database
                // TODO remove Team from the database
            }
        }
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
        StringBuilder sb = new StringBuilder();
        sb.append("League Name: " + this.name + "\n");
        sb.append("Owner: " + this.ownerInfo.getName() + "\n");
        sb.append("Sport: " + this.sport + "\n");
        sb.append("Description: " + this.description + "\n");
        sb.append("Teams:");
        for (TeamInfo teamInfo : this.teamsInfo) {
            sb.append("\n\t" + teamInfo.getName());
        }
        return sb.toString();
    }
}
