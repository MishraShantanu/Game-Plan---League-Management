package com.zizzle.cmpt370;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserTest {
    /**
     * Tests the setName() function from the User class.
     */
    @Test
    public void testSetName() {
        String[] expected = {"a", "b", "JOHN BYER", "123", ""};
        // Setting up test cases.
        User[] testUsers = {
                new User("a", "a", "1"),
                new User("a", "a", "1"),
                new User("a", "a", "1"),
                new User("a", "a", "1"),
                new User("a", "a", "1")
        };
        // Setting user names to another name.
        testUsers[1].setName("b");
        testUsers[2].setName("JOHN BYER");
        testUsers[3].setName("123");
        testUsers[4].setName("");
        // Checking to see if setName() changed the name of the user.
        for (int i = 0; i < testUsers.length; i++) {
            assertEquals(expected[i], testUsers[i].getName());
        }
    }


    /**
     * Tests the setEmail() function from the User class.
     */
    @Test
    public void testSetEmail() {
        String[] expected = {
                "tom@mail.com",
                "123@mail.com",
                "test@gmail.com",
                "foo@mail.ca",
                "bar@mail.ca"};
        // Setting up test cases.
        User[] testUsers = {
                new User("a", "a@mail.com", "1"),
                new User("a", "b@mail.com", "1"),
                new User("a", "c@mail.com", "1"),
                new User("a", "d@mail.com", "1"),
                new User("a", "e@mail.com", "1")
        };
        // Changing the email of the users.
        testUsers[0].setEmail("tom@mail.com");
        testUsers[1].setEmail("123@mail.com");
        testUsers[2].setEmail("test@gmail.com");
        testUsers[3].setEmail("foo@mail.ca");
        testUsers[4].setEmail("bar@mail.ca");
        // Checking to see if setEmail() changed the email of the user.
        for (int i = 0; i < testUsers.length; i++) {
            assertEquals(expected[i], testUsers[i].getEmail());
        }
    }


    /**
     * Tests the setSetPhoneNumber() function from the User class.
     * (setPhoneNumber() is used inside the User constructor)
     */
    @Test
    public void testSetPhoneNumber() {
        int expected = 1234567890;
        int[] secondExpected = {1, 1234567890, 3, 4, 5};
        // Setting up test cases.
        User[] testUsers = {
                new User("a", "a", "(123) 456 - 7890"),
                new User("a", "a", "1234567890"),
                new User("a", "a", "123-456-7890"),
                new User("a", "a", "(123)    45 6  - 7890"),
                new User("a", "a", "123.456.7890")
        };
        // Checking if setPhoneNumber() stripped non-numeric character from Phone Number.
        for (User user : testUsers) {
            assertEquals(expected, user.getPhoneNumber());
        }
        // Changing the phone number of the users.
        testUsers[0].setPhoneNumber("1");
        testUsers[1].setPhoneNumber("q1w2e3r4t5y6u7i8o9p0");
        testUsers[2].setPhoneNumber("3");
        testUsers[3].setPhoneNumber("(((4)))");
        testUsers[4].setPhoneNumber("nijvnlfbavlfsbl5");
        // Checking if setPhoneNumber() stripped non-numeric character from Phone Number.
        for (int i = 0; i < testUsers.length; i++) {
            assertEquals(secondExpected[i], testUsers[i].getPhoneNumber());
        }
    }


    /**
     * Tests the toString() function from the User class.
     */
    @Test
    public void testToString() {
        String[] expected = {
                "Name: a\n" +
                        "Email: a@mail.com\n" +
                        "Phone Number: 1234567890\n" +
                        "Teams:",
                "Name: Shantanu\n" +
                        "Email: shantanu@mail.com\n" +
                        "Phone Number: 1987654321\n" +
                        "Teams:",
                "Name: Lee\n" +
                        "Email: lee@mail.gov\n" +
                        "Phone Number: 123\n" +
                        "Teams:",
                "Name: Jay Shah\n" +
                        "Email: jay@mail.com\n" +
                        "Phone Number: 321\n" +
                        "Teams:",
                "Name: Brayden\n" +
                        "Email: bkm257@usask.ca\n" +
                        "Phone Number: 3066814380\n" +
                        "Teams:"
        };
        // Setting up test cases.
        User[] testUsers = {
                new User("a", "a@mail.com", "1234567890"),
                new User("Shantanu", "shantanu@mail.com", "1987654321"),
                new User("Lee", "lee@mail.gov", "1q2w3"),
                new User("Jay Shah", "jay@mail.com", "number:321"),
                new User("Brayden", "bkm257@usask.ca", "3066814380")
        };
        // Checking if toString() outputs information on User object in expected format.
        for (int i = 0; i < testUsers.length; i++) {
            assertEquals(expected[i], testUsers[i].toString());
        }
    }
}