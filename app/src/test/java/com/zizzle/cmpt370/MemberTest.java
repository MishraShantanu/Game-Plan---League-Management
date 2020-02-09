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
                new Member("a", "a","a", "12345678901"),
                new Member("a", "a","a", "12345678901"),
                new Member("a", "a","a", "12345678901"),
                new Member("a", "a","a", "12345678901"),
                new Member("a", "a","a", "12345678901")
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
                new Member("a", "a","a", "12345678901"),
                new Member("a", "a","a", "12345678901"),
                new Member("a", "a","a", "12345678901"),
                new Member("a", "a","a", "12345678901"),
                new Member("a", "a","a", "12345678901")
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
                new Member("a", "a","a@mail.com", "12345678901"),
                new Member("a", "a","b@mail.com", "12345678901"),
                new Member("a", "a","c@mail.com", "12345678901"),
                new Member("a", "a","d@mail.com", "12345678901"),
                new Member("a", "a","e@mail.com", "12345678901")
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
        String expected = "1-234-567-8901";
        String secondExpected = "0-987-654-3211";
        // Setting up test cases.
        Member[] testMembers = {
                new Member("a", "a","a", "(123) 456 - 78901"),
                new Member("b", "b","a", "12345678901"),
                new Member("c", "c","a", "123-456-78901"),
                new Member("d", "d","a", "(123) 45 6  - 78901"),
                new Member("e", "e","a", "123.456.78901")
        };
        // Checking if setPhoneNumber() stripped non-numeric character from Phone Number.
        for (Member member : testMembers) {
            System.out.println(member.getFirstName() + " " + member.getPhoneNumber());
            assertEquals(expected, member.getPhoneNumber());
        }
        // Changing the phone number of the users.
        testMembers[0].setPhoneNumber("09876543211");
        testMembers[1].setPhoneNumber("0f987d6543g211");
        testMembers[2].setPhoneNumber("0f9876a543g211");
        testMembers[3].setPhoneNumber("(((098a76d543d211)))");
        testMembers[4].setPhoneNumber("098f765g4321g1");
        // Checking if setPhoneNumber() stripped non-numeric character from Phone Number.
        for (Member member : testMembers) {
            assertEquals(secondExpected, member.getPhoneNumber());
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
                        "Phone Number: 1-234-567-8901\n" +
                        "Teams:",
                "Name: Shantanu Mishra\n" +
                        "Email: shantanu@mail.com\n" +
                        "Phone Number: 1-987-654-3211\n" +
                        "Teams:",
                "Name: Lee Cadotte\n" +
                        "Email: lee@mail.gov\n" +
                        "Phone Number: 1-234-567-8901\n" +
                        "Teams:",
                "Name: Jay Shah\n" +
                        "Email: jay@mail.com\n" +
                        "Phone Number: 0-987-654-3210\n" +
                        "Teams:",
                "Name: Brayden Martin\n" +
                        "Email: bkm257@usask.ca\n" +
                        "Phone Number: 1-306-681-4380\n" +
                        "Teams:"
        };
        // Setting up test cases.
        Member[] testMembers = {
                new Member("a", "a","a@mail.com", "12345678901"),
                new Member("Shantanu", "Mishra","shantanu@mail.com", "19876543211"),
                new Member("Lee", "Cadotte","lee@mail.gov", "1q2w3p4p5p6p7p8p9p0p1"),
                new Member("Jay", "Shah","jay@mail.com", "number:09876543210"),
                new Member("Brayden", "Martin","bkm257@usask.ca", "1-306-681--gfh4380")
        };
        // Checking if toString() outputs information on Member object in expected format.
        for (int i = 0; i < testMembers.length; i++) {
            assertEquals(expected[i], testMembers[i].toString());
        }
    }
}