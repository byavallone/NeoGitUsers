package com.byavallone.neogitusers.data;

/**
 * Class used to hold a user information
 */
public class UserProfile {

    private String mNickName;
    private String mRealName;
    private String mCompany;
    private String mLocation;
    private int mRepoCount;
    private int mFollowers;

    /**
     * Constructor
     * @param nickName
     * @param followers
     */
    UserProfile(String nickName, int followers){
        mNickName = nickName;
        mFollowers = followers;
    }

    /** Method used to get the nickName of the user */
    public String getNickName(){
        return mNickName;
    }

    /** Method used to get the number of followers */
    public int getFollowers(){
        return mFollowers;
    }
}
