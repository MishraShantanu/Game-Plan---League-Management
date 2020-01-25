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


    /**
     * Testing for setDescription() function from the League class.
     */
    @Test
    public void testSetDescription() {
        String[] expected = {
                "description",
                "new description",
                "123",
                "",
                "       31  3fd"
        };
        // Owner for league.
        User testUser = new User("Brayden", "b@mail.com", "1234567890");
        // Leagues for testing.
        League[] testLeagues = {
                new League("UofS", testUser, "sport", "description"),
                new League("UofS", testUser, "sport", "description"),
                new League("UofS", testUser, "sport", "description"),
                new League("UofS", testUser, "sport", "description"),
                new League("UofS", testUser, "sport", "description")
        };
        // Changing the name of the leagues.
        testLeagues[1].setDescription("new description");
        testLeagues[2].setDescription("123");
        testLeagues[3].setDescription("");
        testLeagues[4].setDescription("       31  3fd");
        // Checking if the league names have been changed.
        for (int i = 0; i < testLeagues.length; i++) {
            assertEquals(expected[i], testLeagues[i].getDescription());
        }
    }


    /**
     * Testing for toString() function from the League class.
     */
    @Test
    public void testToString() {
        String[] expected = {
                "League Name: UofS\n" +
                        "Owner: Brayden\n" +
                        "Sport: Volley Ball\n" +
                        "Description: Bump, Set, Spike!\n" +
                        "Teams:",
                "League Name: Kids Soccer\n" +
                        "Owner: Tod\n" +
                        "Sport: Soccer\n" +
                        "Description: Tykes\n" +
                        "Teams:",
                "League Name: Senior Squash\n" +
                        "Owner: Jill\n" +
                        "Sport: Squash\n" +
                        "Description: Laid back squash\n" +
                        "Teams:",
                "League Name: Joe's Dodgeball\n" +
                        "Owner: Joe\n" +
                        "Sport: Dodgeball\n" +
                        "Description: If you can dodge a wrench, you can dodge a ball\n" +
                        "Teams:",
                "League Name: test\n" +
                        "Owner: Ted\n" +
                        "Sport: test\n" +
                        "Description: test\n" +
                        "Teams:"
        };
        // Owners for league.
        User[] testUsers = {
                new User("Brayden", "b@mail.com", "1234567890"),
                new User("Tod", "t@m.ca", "123"),
                new User("Jill", "jillybean@email.com", "321"),
                new User("Joe", "joeybean@email.com", "1q2w3e4r5t6y7u8i9o0p"),
                new User("Ted", "t@email.com", "0987654321")
        };
        // Leagues for testing.
        League[] testLeagues = {
                new League("UofS", testUsers[0], "Volley Ball", "Bump, Set, Spike!"),
                new League("Kids Soccer", testUsers[1], "Soccer", "Tykes"),
                new League("Senior Squash", testUsers[2], "Squash", "Laid back squash"),
                new League("Joe's Dodgeball", testUsers[3], "Dodgeball", "If you can dodge a wrench, you can dodge a ball"),
                new League("test", testUsers[4], "test", "test")
        };
        // Checking if the league names have been changed.
        for (int i = 0; i < testLeagues.length; i++) {
            assertEquals(expected[i], testLeagues[i].toString());
        }
    }


    // TODO 23/01/2020 - Add tests for addTeam(), removeTeam(), and sortTeam().
}
