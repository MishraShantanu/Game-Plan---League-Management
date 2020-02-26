package com.zizzle.cmpt370;

import com.zizzle.cmpt370.Model.Game;
import com.zizzle.cmpt370.Model.GameTime;
import com.zizzle.cmpt370.Model.League;
import com.zizzle.cmpt370.Model.Member;
import com.zizzle.cmpt370.Model.Team;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class TeamTest {

    /**
     * Tests the getters for the Team class
     */
    @Test
    public void testGetters(){
        Member testOwner = new Member("owner1","lname","owner@own.com","19726489999");
        League testLeague = new League("test-league",testOwner,"soccer","fun soccer league");
        Team[] testTeams = {
                new Team("team1",testOwner,"soccer",testLeague),
                new Team("team2",testOwner,"soccer",testLeague),
                new Team("team3",testOwner,"soccer",testLeague),
                new Team("team4",testOwner,"soccer",testLeague),
        };
        String[] expectedNames = {"team1","team2","team3","team4"};
        Member expectedOwner = testOwner;
        League expectedLeague = testLeague;
        for(int i=0;i<testTeams.length;i++){
            assertEquals(expectedNames[i],testTeams[i].getName());
            assertEquals(expectedOwner,testTeams[i].getOwner());
            assertEquals(expectedLeague,testTeams[i].getLeague());
            // make sure league now contains new team
            assertEquals(testLeague.getTeams().get(i),testTeams[i]);
        }

        // try to create invalid teams

        boolean exceptionThrown = false;
        // create a team with a non-unique name
        try{
            new Team("team1",testOwner,"soccer",testLeague);
        }catch(IllegalArgumentException e){
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        // create a team whose sport doesn't match the league
        try{
            new Team("team22",testOwner,"tennis",testLeague);
        }catch(IllegalArgumentException e){
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    /**
     * Tests methods related to team members in the Team class
     */
    @Test
    public void testTeamMembers(){
        Member owner = new Member("owner","lname","email","11111111111");
        League testLeague = new League("test-league",owner,"dodgeball","fun dodge-ball league");
        Team testTeam = new Team("team1",owner,"dodgeball",testLeague);
        Member newOwner = new Member("new owner","lname","email","22222222222");
        testTeam.setOwner(newOwner);
        assertEquals(testTeam.getOwner(),newOwner);

        Member[] testMembers = {owner, newOwner, new Member("Member1","lname","email","33333333333"), new Member("Member2","lname","email","44444444444")};
        // add extra Members to the team
        testTeam.addMember(testMembers[2]);
        testTeam.addMember(testMembers[3]);
        assertArrayEquals(testTeam.getTeamMembers(),testMembers);

        for(int i=0;i<testMembers.length;i++){
            assertTrue(testTeam.teamHasMember(testMembers[i]));
            assertEquals(testTeam.getMemberByFirstName(testMembers[i].getFirstName()),testMembers[i]);
        }

        // remove a member
        testTeam.removeMember(testMembers[0]);
        assertFalse(testTeam.teamHasMember(testMembers[0]));
    }


    /**
     * Tests the equals method of the Team class
     */
    @Test
    public void testEquals(){
        Member owner = new Member("owner","lname","email","11111111111");
        League league = new League("league",owner,"dodgeball","generic description");
        Team testTeam = new Team("team1",owner,"dodgeball",league);
        League otherLeague = new League("other-league",owner,"dodgeball","other generic description");
        Member otherOwner = new Member("other owner","other lname","email","11111111111");

        // teams differ by name
        assertNotEquals(testTeam,new Team("team11",owner,"dodgeball",league));

        // teams differ by owner also
        assertNotEquals(testTeam,new Team("team12",otherOwner,"dodgeball",league));

        // teams have same name but differ by league
        assertNotEquals(testTeam,new Team("team1",owner,"dodgeball",otherLeague));

        // teams are the same
        assertEquals(testTeam,testTeam);
    }

    /**
     * Tests methods related to games for the Team class
     */
    @Test
    public void testTeamGames(){
        Member owner = new Member("owner","lname","email","11111111111");
        League league = new League("league",owner,"dodgeball","generic description");
        Team testTeam = new Team("team1",owner,"dodgeball",league);
        // check team's record
        assertEquals(testTeam.getWins(),0);
        assertEquals(testTeam.getLosses(),0);
        assertEquals(testTeam.getTies(),0);

        assertFalse(testTeam.hasGamesScheduled());
        assertFalse(testTeam.hasPlayedGame());

        Game game1 = new Game(testTeam,testTeam,new GameTime(2030,1,1,1,1),"location","dodgeball");
        Game game2 = new Game(testTeam,testTeam,new GameTime(2025,1,1,1,1),"location","dodgeball");
        Game game3 = new Game(testTeam,testTeam,new GameTime(2025,3,3,3,3),"location","dodgeball");

        testTeam.scheduleNewGame(game1);
        testTeam.scheduleNewGame(game2);
        testTeam.scheduleNewGame(game3);

        assertTrue(testTeam.hasGamesScheduled());
        // games should be in order of increasing starting times
        assertArrayEquals(testTeam.getScheduledGames(),new Game[] {game2,game3,game1});

        assertEquals(testTeam.getClosestScheduledGame(),game2);

        // cancel game2
        testTeam.cancelGame(game2);
        assertArrayEquals(testTeam.getScheduledGames(),new Game[] {game3,game1});

        // TODO test methods that require a game in the past, currently games cannot be created in the past
    }


}
