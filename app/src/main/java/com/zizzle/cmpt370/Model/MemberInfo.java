package com.zizzle.cmpt370.Model;

/**
 * Stores information about a particular team, facilitates easy database access to underlying member object
 */
public class MemberInfo implements InfoInterface{

    /** String name of the member being represented */
    private String memberName;

    /** String key into the database where the Member represented by this object can be accessed, must be used
     * from the correct path in the database */
    private String memberKey;

    /**
     * Creates a MemberInfo object that stores information about the input member
     * @param member: Member object to store information about
     */
    public MemberInfo(Member member){
        memberName = member.getDisplayName(); // use first names for now, could extend to first and last names
        // a user's email must be unique, use this as a key into the database
        memberKey = member.getUserID();
    }

    /**
     * Creates a MemberInfo object using the input fields
     * @param name: String name of some Member
     * @param userID: String user ID of the user
     */
    public MemberInfo(String name, String userID){
        memberName = name;
        memberKey = userID;
    }

    public String getName(){
        return memberName;
    }

    public String getDatabaseKey(){
        return memberKey;
    }
}
