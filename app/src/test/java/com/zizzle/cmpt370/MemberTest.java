package com.zizzle.cmpt370;

import com.zizzle.cmpt370.Model.Member;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MemberTest {
    /**
     * Tests the setName() function from the Member class.
     */
    @Test
    public void testSetFirstName() {
        String[] expected = {"a", "b", "JOHN", "123", ""};
        // Setting up test cases.
        Member[] testMembers = {
                new Member("a", "a","a", "1"),
                new Member("a", "a","a", "1"),
                new Member("a", "a","a", "1"),
                new Member("a", "a","a", "1"),
                new Member("a", "a","a", "1")
        };
        // Setting user names to another name.
        testMembers[1].setFirstName("b");
        testMembers[2].setFirstName("JOHN");
        testMembers[3].setFirstName("123");
        testMembers[4].setFirstName("");
        // Checking to see if setName() changed the name of the user.
        for (int i = 0; i < testMembers.length; i++) {
            assertEquals(expected[i], testMembers[i].getFirstName());
        }
    }


    /**
     * Tests the setName() function from the Member class.
     */
    @Test
    public void testSetLastName() {
        String[] expected = {"a", "b", "JOHN", "123", ""};
        // Setting up test cases.
        Member[] testMembers = {
                new Member("a", "a","a", "1"),
                new Member("a", "a","a", "1"),
                new Member("a", "a","a", "1"),
                new Member("a", "a","a", "1"),
                new Member("a", "a","a", "1")
        };
        // Setting user names to another name.
        testMembers[1].setLastName("b");
        testMembers[2].setLastName("JOHN");
        testMembers[3].setLastName("123");
        testMembers[4].setLastName("");
        // Checking to see if setName() changed the name of the user.
        for (int i = 0; i < testMembers.length; i++) {
            assertEquals(expected[i], testMembers[i].getLastName());
        }
    }


    /**
     * Tests the setEmail() function from the Member class.
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
        Member[] testMembers = {
                new Member("a", "a","a@mail.com", "1"),
                new Member("a", "a","b@mail.com", "1"),
                new Member("a", "a","c@mail.com", "1"),
                new Member("a", "a","d@mail.com", "1"),
                new Member("a", "a","e@mail.com", "1")
        };
        // Changing the email of the users.
        testMembers[0].setEmail("tom@mail.com");
        testMembers[1].setEmail("123@mail.com");
        testMembers[2].setEmail("test@gmail.com");
        testMembers[3].setEmail("foo@mail.ca");
        testMembers[4].setEmail("bar@mail.ca");
        // Checking to see if setEmail() changed the email of the user.
        for (int i = 0; i < testMembers.length; i++) {
            assertEquals(expected[i], testMembers[i].getEmail());
        }
    }


    /**
     * Tests the setSetPhoneNumber() function from the Member class.
     * (setPhoneNumber() is used inside the Member constructor)
     */
    @Test
    public void testSetPhoneNumber() {
        int expected = 1234567890;
        int[] secondExpected = {1, 1234567890, 3, 4, 5};
        // Setting up test cases.
        Member[] testMembers = {
                new Member("a", "a","a", "(123) 456 - 7890"),
                new Member("a", "a","a", "1234567890"),
                new Member("a", "a","a", "123-456-7890"),
                new Member("a", "a","a", "(123)    45 6  - 7890"),
                new Member("a", "a","a", "123.456.7890")
        };
        // Checking if setPhoneNumber() stripped non-numeric character from Phone Number.
        for (Member member : testMembers) {
            assertEquals(expected, member.getPhoneNumber());
        }
        // Changing the phone number of the users.
        testMembers[0].setPhoneNumber("1");
        testMembers[1].setPhoneNumber("q1w2e3r4t5y6u7i8o9p0");
        testMembers[2].setPhoneNumber("3");
        testMembers[3].setPhoneNumber("(((4)))");
        testMembers[4].setPhoneNumber("nijvnlfbavlfsbl5");
        // Checking if setPhoneNumber() stripped non-numeric character from Phone Number.
        for (int i = 0; i < testMembers.length; i++) {
            assertEquals(secondExpected[i], testMembers[i].getPhoneNumber());
        }
    }


    /**
     * Tests the toString() function from the Member class.
     */
    @Test
    public void testToString() {
        String[] expected = {
                "Name: a a\n" +
                        "Email: a@mail.com\n" +
                        "Phone Number: 1234567890\n" +
                        "Teams:",
                "Name: Shantanu Mishra\n" +
                        "Email: shantanu@mail.com\n" +
                        "Phone Number: 1987654321\n" +
                        "Teams:",
                "Name: Lee Cadotte\n" +
                        "Email: lee@mail.gov\n" +
                        "Phone Number: 123\n" +
                        "Teams:",
                "Name: Jay Shah\n" +
                        "Email: jay@mail.com\n" +
                        "Phone Number: 321\n" +
                        "Teams:",
                "Name: Brayden Martin\n" +
                        "Email: bkm257@usask.ca\n" +
                        "Phone Number: 3066814380\n" +
                        "Teams:"
        };
        // Setting up test cases.
        Member[] testMembers = {
                new Member("a", "a","a@mail.com", "1234567890"),
                new Member("Shantanu", "Mishra","shantanu@mail.com", "1987654321"),
                new Member("Lee", "Cadotte","lee@mail.gov", "1q2w3"),
                new Member("Jay", "Shah","jay@mail.com", "number:321"),
                new Member("Brayden", "Martin","bkm257@usask.ca", "3066814380")
        };
        // Checking if toString() outputs information on Member object in expected format.
        for (int i = 0; i < testMembers.length; i++) {
            assertEquals(expected[i], testMembers[i].toString());
        }
    }
}