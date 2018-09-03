package com.byavallone.neogitusers.data;

/**
 * Class used to hold a user information
 */
public class UserProfile {

    private String mNickName;
    private String mRealName;
    private String mCompany;
    private String mLocation;
    private String mImageUrl;
    private String mProfileWebUrl;
    private int mRepoCount;
    private int mFollowers;

    /**
     * Constructor
     * @param nickName
     * @param followers
     */
    UserProfile(String nickName, int followers, String imageURL, String profileWebUrl){
        mNickName = nickName;
        mFollowers = followers;
        mImageUrl = imageURL;
        mProfileWebUrl = profileWebUrl;
    }

    /** Method used to get the nickName of the user */
    public String getNickName(){
        return mNickName;
    }

    /** Method used to get the number of followers */
    public int getFollowers(){
        return mFollowers;
    }

    /** Method used to get the image url */
    public String getImageUrl(){return mImageUrl;}

    /** Method used to get the profile web url */
    public String getProfileWebUrl(){return mProfileWebUrl;}
}
