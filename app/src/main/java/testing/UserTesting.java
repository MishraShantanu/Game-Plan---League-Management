package testing;

import com.zizzle.cmpt370.User;

public class UserTesting {

    /**
     * Tests the setName() function from the User class.
     */
    private static void testSetName() {
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
            if (!testUsers[i].getName().equals(expected[i])) {
                System.out.println("Error testSetName(): Test #" + (i+1) + "\n\tExpected: " +
                        expected[i] + ", received: " + testUsers[i].getName() + "\n");
            }
        }
    }


    private static void testSetEmail() {
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
            if (!testUsers[i].getEmail().equals(expected[i])) {
                System.out.println("Error testSetEmail(): Test #" + (i+1) + "\n\tExpected: " +
                        expected[i] + ", received: " + testUsers[i].getEmail() + "\n");
            }
        }
    }


    /**
     * Tests the setSetPhoneNumber() function from the User class.
     * (setPhoneNumber() is used inside the User constructor)
     */
    private static void testSetPhoneNumber() {
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
        for (int i = 0; i < testUsers.length; i++) {
            if (testUsers[i].getPhoneNumber() != expected) {
                System.out.println("Error testSetPhoneNumber(): Test #" + (i+1) + "\n\tExpected: " +
                        expected + ", received: " + testUsers[i].getPhoneNumber() + "\n");
            }
        }

        // Changing the phone number of the users.
        testUsers[0].setPhoneNumber("1");
        testUsers[1].setPhoneNumber("q1w2e3r4t5y6u7i8o9p0");
        testUsers[2].setPhoneNumber("3");
        testUsers[3].setPhoneNumber("(((4)))");
        testUsers[4].setPhoneNumber("nijvnlfbavlfsbl5");

        // Checking if setPhoneNumber() stripped non-numeric character from Phone Number.
        for (int i = 0; i < testUsers.length; i++) {
            if (testUsers[i].getPhoneNumber() != secondExpected[i]) {
                System.out.println("Error testSetPhoneNumber(): Test #" + (i+1+testUsers.length) + "\n\tExpected: " +
                        secondExpected[i] + ", received: " + testUsers[i].getPhoneNumber());
            }
        }
    }


    public static void main(String[] args) {
        System.out.println("*** Beginning Testing ***\n");

        System.out.println("-- SetName() --");
        testSetName();

        System.out.println("-- SetEmail() --");
        testSetEmail();

        System.out.println("-- SetPhoneNumber() --");
        testSetPhoneNumber();

        System.out.println("\n*** Testing Complete ***");
    }
}
