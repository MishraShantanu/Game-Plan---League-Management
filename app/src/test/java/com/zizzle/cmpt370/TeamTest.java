package com.zizzle.cmpt370;

import com.zizzle.cmpt370.Model.Game;
import com.zizzle.cmpt370.Model.GameTime;
import com.zizzle.cmpt370.Model.Member;
import com.zizzle.cmpt370.Model.Team;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TeamTest {

    /**
     * Tests the getters for the Team class
     */
    @Test
    public void testGetters(){
        Member testOwner = new Member("owner1","lname","owner@own.com","19726489999");
        Team[] testTeams = {
                new Team("team1",testOwner,"soccer"),
                new Team("team2",testOwner,"baseball"),
                new Team("team3",testOwner,"squash"),
                new Team("team4",testOwner,"tennis"),
        };
        String[] expectedNames = {"team1","team2","team3","team4"};
        Member expectedOwner = testOwner;
        for(int i=0;i<testTeams.length;i++){
            assertEquals(expectedNames[i],testTeams[i].getName());
            assertEquals(expectedOwner,testTeams[i].getOwner());
        }
    }

    /**
     * Tests methods related to team members in the Team class
     */
    @Test
    public void testTeamMembers(){
        Member owner = new Member("owner","lname","email","11111111111");
        Team testTeam = new Team("team1",owner,"dodgeball");
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
     * Tests methods related to games for the Team class
     */
    @Test
    public void testTeamGames(){
        Team testTeam = new Team("team1",new Member("owner","lname","email","11111111111"),"dodgeball");
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
