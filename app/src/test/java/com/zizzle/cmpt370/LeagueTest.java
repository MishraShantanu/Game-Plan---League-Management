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
        Member testMember =  new Member("Brayden", "b@mail.com", "1234567890");
        // Leagues for testing.
        League[] testLeagues = {
                new League("UofS", testMember, "sport", "description"),
                new League("UofS", testMember, "sport", "description"),
                new League("UofS", testMember, "sport", "description"),
                new League("UofS", testMember, "sport", "description"),
                new League("UofS", testMember, "sport", "description")
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
        Member[] testMembers = {
                new Member("Brayden", "b@mail.com", "1234567890"),
                new Member("Tod", "t@m.ca", "123"),
                new Member("Jill", "jillybean@email.com", "321")
        };
        // Expected owners.
        Member[] expected = {
                testMembers[0],
                testMembers[0],
                testMembers[1],
                testMembers[2],
                testMembers[1],
        };
        // Leagues for testing.
        League[] testLeagues = {
                new League("UofS", testMembers[0], "sport", "description"),
                new League("UofS", testMembers[1], "sport", "description"),
                new League("UofS", testMembers[2], "sport", "description"),
                new League("UofS", testMembers[1], "sport", "description"),
                new League("UofS", testMembers[0], "sport", "description")
        };
        // Changing the name of the leagues.
        testLeagues[1].setOwner(testMembers[0]);
        testLeagues[2].setOwner(testMembers[1]);
        testLeagues[3].setOwner(testMembers[2]);
        testLeagues[4].setOwner(testMembers[1]);
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
        Member testMember = new Member("Brayden", "b@mail.com", "1234567890");
        // Leagues for testing.
        League[] testLeagues = {
                new League("UofS", testMember, "Football", "description"),
                new League("UofS", testMember, "Football", "description"),
                new League("UofS", testMember, "Soccer", "description"),
                new League("UofS", testMember, "Soccer", "description"),
                new League("UofS", testMember, "Hockey", "description")
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
        Member testMember = new Member("Brayden", "b@mail.com", "1234567890");
        // Leagues for testing.
        League[] testLeagues = {
                new League("UofS", testMember, "sport", "description"),
                new League("UofS", testMember, "sport", "description"),
                new League("UofS", testMember, "sport", "description"),
                new League("UofS", testMember, "sport", "description"),
                new League("UofS", testMember, "sport", "description")
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
        Member[] testMembers = {
                new Member("Brayden", "b@mail.com", "1234567890"),
                new Member("Tod", "t@m.ca", "123"),
                new Member("Jill", "jillybean@email.com", "321"),
                new Member("Joe", "joeybean@email.com", "1q2w3e4r5t6y7u8i9o0p"),
                new Member("Ted", "t@email.com", "0987654321")
        };
        // Leagues for testing.
        League[] testLeagues = {
                new League("UofS", testMembers[0], "Volley Ball", "Bump, Set, Spike!"),
                new League("Kids Soccer", testMembers[1], "Soccer", "Tykes"),
                new League("Senior Squash", testMembers[2], "Squash", "Laid back squash"),
                new League("Joe's Dodgeball", testMembers[3], "Dodgeball", "If you can dodge a wrench, you can dodge a ball"),
                new League("test", testMembers[4], "test", "test")
        };
        // Checking if the league names have been changed.
        for (int i = 0; i < testLeagues.length; i++) {
            assertEquals(expected[i], testLeagues[i].toString());
        }
    }


    // TODO 23/01/2020 - Add tests for addTeam(), removeTeam(), and sortTeam().
}
