package com.zizzle.cmpt370.Model;

/**
 * Interface specifying methods for objects storing meta information about other objects
 */
public interface InfoInterface {

    /**
     * Gets the name associated with the object
     * @return String name of the associated object
     */
    public String getName();

    /**
     * Gets a string key into the database which can be used to read in the object corresponding to this
     * information
     * @return String key that can be used to read an object from the database at a specific path
     */
    public String getDatabaseKey();


}
