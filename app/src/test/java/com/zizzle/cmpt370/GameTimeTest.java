package com.zizzle.cmpt370;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class GameTimeTest {

    /**
     * Tests the GameTime constructor, in particular whether inputs and fields are checked correctly
     */
    @Test
    public void testConstructor(){
        // try to create a GameTimes which are in the past
        int[][] pastInputs = {{2019,8,21,12,3}, {2020,1,15,0,33},{2020,2,1,3,10}};
        boolean exceptionThrown;
        for(int i=0;i<pastInputs.length;i++){
            exceptionThrown = false;
            try{
                int[] currentInputs = pastInputs[i];
                new GameTime(currentInputs[0],currentInputs[1],currentInputs[3],currentInputs[3],currentInputs[4]);
            }catch(IllegalArgumentException e){
                // expect to get exception
                exceptionThrown = true;
            }
            if(! exceptionThrown){
                System.out.println("Error, no exception thrown when trying to create a GameTime in the past");
            }
        }

        exceptionThrown = false;
        // create GameTimes with fields that are out of bounds
        int[][] invalidInputs = {{2019,12,15,23,59},{2030,13,3,4,14}};
        for(int i=0;i<invalidInputs.length;i++){
            try{
                int[] currentInput = invalidInputs[i];
                new GameTime(currentInput[0],currentInput[1],currentInput[2],currentInput[3],currentInput[4]);
            }catch(IllegalArgumentException e){
                exceptionThrown = true;
            }
            if(! exceptionThrown){
                System.out.println("Error: no exception thrown when trying to create a GameTime in the past");
            }
        }
    }

    /**
     * Tests the getTimeArray() method of the GameTime class
     */
    @Test
    public void testGetTimeArray(){
        // create test GameTimes
        GameTime[] testTimes = {
                new GameTime(2030,2, 1,2,8),
                new GameTime(2030,4,26,14,33),
                new GameTime(2030,1,15,0,0),
                new GameTime(2030,12,15,23,59)
        };

        int[][] expected = {{2030,2, 1,2,8},{2030,4,26,14,33},{2030,1,15,0,0},{2030,12,15,23,59}};

        for(int i=0;i<expected.length;i++){
            assertArrayEquals(testTimes[i].getTimeArray(),expected[i]);
        }
    }

    /**
     * Tests the CompareTo method of the GameTime class
     */
    @Test
    public void testCompareTo(){
        // create test GameTimes
        GameTime testTime = new GameTime(2030,6,3,9,27);

        GameTime expectedEquals = new GameTime(2030,6,3,9,27);

        // testTime should be lesser than these
        GameTime[] expectedLesser = {
                new GameTime(2031,1,1,1,1),
                new GameTime(2030,7,1,1,1),
                new GameTime(2030,6,4,1,1),
                new GameTime(2030,6,3,10,1),
                new GameTime(2030,6,3,9,28)
        };

        // testTime should be greater than these
        GameTime[] expectedGreater = {
                new GameTime(2029,1,1,1,1),
                new GameTime(2030,5,1,1,1),
                new GameTime(2030,6,2,1,1),
                new GameTime(2030,6,3,8,1),
                new GameTime(2030,6,3,9,26)
        };

        for(int i=0;i<expectedLesser.length;i++){
            assertTrue(testTime.compareTo(expectedLesser[i])<0);
            assertTrue(testTime.compareTo(expectedGreater[i])>0);
            assertEquals(testTime.compareTo(expectedEquals),0);
        }

    }

    /**
     * Tests the equals() method of the GameTime class
     */
    @Test
    public void testEquals(){
        GameTime[] testTimes = {
                new GameTime(2030,12,3,16,33),
                new GameTime(2025,5,22,23,0),
                new GameTime(2030,11,29,0,59)
        };
        GameTime[] equalTimes = {
                new GameTime(2030,12,3,16,33),
                new GameTime(2025,5,22,23,0),
                new GameTime(2030,11,29,0,59)
        };
        GameTime[] unequalTimes = {
                new GameTime(2031,12,3,16,33),
                new GameTime(2025,2,22,23,0),
                new GameTime(2030,11,29,3,59)
        };

        for(int i=0;i<testTimes.length;i++){
            assertEquals(testTimes[i],equalTimes[i]);
            assertNotEquals(testTimes[i],unequalTimes[i]);
        }

    }

    /**
     * Tests the isInFuture method of the GameTime class
     */
    @Test
    public void isInFutureTest(){
        GameTime[] expectedFuture = {
                new GameTime(2030,1,1,1,1),
                new GameTime(2021,7,1,1,1),
                new GameTime(2020,3,4,1,1),
        };
        // weird to test, constructor doesn't allow you to create a time in the past

        for(int i=0;i<expectedFuture.length;i++){
            assertTrue(expectedFuture[i].isInFuture());
        }
    }



}
