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
        memberName = member.getFirstName(); // use first names for now, could extend to first and last names
        // a user's email must be unique, use this as a key into the database
        memberKey = member.getEmail();
    }

    /**
     * Creates a MemberInfo object using the input fields
     * @param name: String name of some Member
     * @param email: String email address of this Member
     */
    public MemberInfo(String name, String email){
        memberName = name;
        memberKey = email;

    }

    public String getName(){
        return memberName;
    }

    public String getDatabaseKey(){
        return memberKey;
    }
}
