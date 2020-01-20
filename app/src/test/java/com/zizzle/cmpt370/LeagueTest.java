package com.zizzle.cmpt370;

import org.junit.Test;
import static org.junit.Assert.*;

public class LeagueTest {

    /**
     * Testing for setName() function from the League class.
     */
    @Test
    public void testSetName() {
        String[] expected = {
                "UofS",
                "Kid's Summer Soccer",
                "U of S",
                "123",
                ""
        };
        // Owner for league.
        User testUser =  new User("Brayden", "b@mail.com", "1234567890");
        // Leagues for testing.
        League[] testLeagues = {
                new League("UofS", testUser, "sport", "description"),
                new League("UofS", testUser, "sport", "description"),
                new League("UofS", testUser, "sport", "description"),
                new League("UofS", testUser, "sport", "description"),
                new League("UofS", testUser, "sport", "description")
        };
        // Changing the name of the leagues.
        testLeagues[1].setName("Kid's Summer Soccer");
        testLeagues[2].setName("U of S");
        testLeagues[3].setName("123");
        testLeagues[4].setName("");
        // Checking if the league names have been changed.
        for (int i = 0; i < testLeagues.length; i++) {
            assertEquals(expected[i], testLeagues[i].getName());
        }
    }


    /**
     * Testing for setOwner() function from the League class.
     */
    @Test
    public void testSetOwner() {
        // Owners for league.
        User[] testUsers = {
                new User("Brayden", "b@mail.com", "1234567890"),
                new User("Tod", "t@m.ca", "123"),
                new User("Jill", "jillybean@email.com", "321")
        };
        // Expected owners.
        User[] expected = {
                testUsers[0],
                testUsers[0],
                testUsers[1],
                testUsers[2],
                testUsers[1],
        };
        // Leagues for testing.
        League[] testLeagues = {
                new League("UofS", testUsers[0], "sport", "description"),
                new League("UofS", testUsers[1], "sport", "description"),
                new League("UofS", testUsers[2], "sport", "description"),
                new League("UofS", testUsers[1], "sport", "description"),
                new League("UofS", testUsers[0], "sport", "description")
        };
        // Changing the name of the leagues.
        testLeagues[1].setOwner(testUsers[0]);
        testLeagues[2].setOwner(testUsers[1]);
        testLeagues[3].setOwner(testUsers[2]);
        testLeagues[4].setOwner(testUsers[1]);
        // Checking if the league names have been changed.
        for (int i = 0; i < testLeagues.length; i++) {
            assertEquals(expected[i], testLeagues[i].getOwner());
        }
    }


    /**
     * Testing for setSport() function from the League class.
     */
    @Test
    public void testSetSport() {
        String[] expected = {
                "Football",
                "Soccer",
                "Hockey",
                "Football",
                "Tennis"
        };
        // Owner for league.
        User testUser = new User("Brayden", "b@mail.com", "1234567890");
        // Leagues for testing.
        League[] testLeagues = {
                new League("UofS", testUser, "Football", "description"),
                new League("UofS", testUser, "Football", "description"),
                new League("UofS", testUser, "Soccer", "description"),
                new League("UofS", testUser, "Soccer", "description"),
                new League("UofS", testUser, "Hockey", "description")
        };
        // Changing the name of the leagues.
        testLeagues[1].setSport("Soccer");
        testLeagues[2].setSport("Hockey");
        testLeagues[3].setSport("Football");
        testLeagues[4].setSport("Tennis");
        // Checking if the league names have been changed.
        for (int i = 0; i < testLeagues.length; i++) {
            assertEquals(expected[i], testLeagues[i].getSport());
        }
    }
}
