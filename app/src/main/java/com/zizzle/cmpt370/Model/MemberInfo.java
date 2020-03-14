package com.zizzle.cmpt370.Model;

import java.io.Serializable;

/**
 * Stores information about a particular team, facilitates easy database access to underlying member object
 */
public class MemberInfo implements InfoInterface, Serializable {

    /** String name of the member being represented */
    private String name;

    /** String key into the database where the Member represented by this object can be accessed, must be used
     * from the correct path in the database */
    private String databaseKey;

    /**
     * Creates a MemberInfo object that stores information about the input member
     * @param member: Member object to store information about
     */
    public MemberInfo(Member member){
        name = member.getDisplayName(); // use first names for now, could extend to first and last names
        // a user's email must be unique, use this as a key into the database
        databaseKey = member.getUserID();
    }

    /**
     * Creates a MemberInfo object using the input fields
     * @param name: String name of some Member
     * @param userID: String user ID of the user
     */
    public MemberInfo(String name, String userID){
        this.name = name;
        databaseKey = userID;
    }

    /**
     * Blank constructor required for database
     */
    public MemberInfo(){

    }

    public String getName(){
        return name;
    }

    public String getDatabaseKey(){
        return databaseKey;
    }

    /**
     * Returns a string representation of the MemberInfo object
     * @return String representation of MemberInfo object
     */
    @Override
    public String toString(){
        return this.name;
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof MemberInfo){
            // other is a MemberInfo object, MemberInfos are equal if their name and database key are equal
            MemberInfo otherInfo = (MemberInfo)other;
            return this.name.equals(otherInfo.getName()) && this.databaseKey.equals(otherInfo.getDatabaseKey());
        }
        // other isn't a MemberInfo object, can't be equal to our memberInfo
        return false;
    }
}
