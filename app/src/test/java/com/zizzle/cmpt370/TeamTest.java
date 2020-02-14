package com.zizzle.cmpt370;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
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


}
