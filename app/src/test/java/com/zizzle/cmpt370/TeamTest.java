package com.zizzle.cmpt370;

import org.junit.Test;

import java.util.Arrays;

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
        User testOwner = new User("owner1","owner@own.com","9726489999");
        Team[] testTeams = {
                new Team("team1",testOwner,"soccer"),
                new Team("team2",testOwner,"baseball"),
                new Team("team3",testOwner,"squash"),
                new Team("team4",testOwner,"tennis"),
        };
        String[] expectedNames = {"team1","team2","team3","team4"};
        User expectedOwner = testOwner;
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
        User owner = new User("owner","email","1111111111");
        Team testTeam = new Team("team1",owner,"dodgeball");
        User newOwner = new User("new owner","email","2222222222");
        testTeam.setOwner(newOwner);
        assertEquals(testTeam.getOwner(),newOwner);

        User[] testUsers = {owner, newOwner, new User("user1","email","3333333333"), new User("user2","email","4444444444")};
        // add extra users to the team
        testTeam.addMember(testUsers[2]);
        testTeam.addMember(testUsers[3]);
        assertArrayEquals(testTeam.getTeamMembers(),testUsers);

        for(int i=0;i<testUsers.length;i++){
            assertTrue(testTeam.teamHasMember(testUsers[i]));
            assertEquals(testTeam.getMemberByName(testUsers[i].getName()),testUsers[i]);
        }

        // remove a member
        testTeam.removeMember(testUsers[0]);
        assertFalse(testTeam.teamHasMember(testUsers[0]));
    }

    /**
     * Tests methods related to games for the Team class
     */
    @Test
    public void testTeamGames(){
        Team testTeam = new Team("team1",new User("owner","email","1111111111"),"dodgeball");
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
