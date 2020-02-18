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

public class GameTest {

    /**
     * Tests the getters of the Game class
     */
    @Test
    public void testGetters(){
        Team teamOne = new Team("team1",new Member("owner1","lname","email1","77777777777"),"soccer");
        Team teamTwo = new Team("team2",new Member("owner2","lname","email2","77777777777"),"soccer");
        GameTime date = new GameTime(2030,6,21,16,45);
        Game testGame = new Game(teamOne,teamTwo,date,"testlocation","soccer");

        assertEquals(testGame.getTeamOne(),teamOne);
        assertEquals(testGame.getTeamTwo(),teamTwo);

        assertEquals(testGame.getLocation(),"testlocation");
        assertEquals(testGame.getSport(),"soccer");
        assertEquals(testGame.getGameTime(),new GameTime(2030,6,21,16,45));
        assertFalse(testGame.hasBeenPlayed());
        assertFalse(testGame.hasGameStarted());

        // reschedule game
        testGame.rescheduleGame(new GameTime(2025,1,4,0,0));
        assertEquals(testGame.getGameTime(),new GameTime(2025,1,4,0,0));
        // TODO test methods that require a game scheduled in the past, currently cannot create a game in the past
    }







}
