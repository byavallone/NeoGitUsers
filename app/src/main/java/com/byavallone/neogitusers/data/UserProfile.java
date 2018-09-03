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
    private String mApiUrl;
    private int mPublicRepoCount;
    private int mFollowers;
    private String mInfoNotAvailable = "Information Not available.";

    /**
     * Constructor
     * @param nickName
     * @param apiUrl
     * @param imageURL
     * @param profileWebUrl
     */
    UserProfile(String nickName, String apiUrl, String imageURL, String profileWebUrl){
        mNickName = nickName;
        mApiUrl = apiUrl;
        mImageUrl = imageURL;
        mProfileWebUrl = profileWebUrl;
    }

    /**
     * Method used to set extra information about the user
     * @param realName
     * @param company
     * @param location
     * @param publicRepoCount
     * @param followers
     */
    public void setExtraInformation(String realName, String company, String location, int publicRepoCount, int followers){
        if(realName == null || realName.contentEquals("null") || realName.isEmpty()){
            realName = mInfoNotAvailable;
        }
        mRealName = realName;
        if(company == null || company.contentEquals("null") ||company.isEmpty()){
            company = mInfoNotAvailable;
        }
        mCompany = company;
        if(location == null || location.contentEquals("null") || location.isEmpty()){
            location = mInfoNotAvailable;
        }
        mLocation = location;
        mPublicRepoCount = publicRepoCount;
        mFollowers = followers;
    }

    /** Method used to get the nickName of the user */
    public String getNickName(){
        return mNickName;
    }

    /** Method used to get the user Real name */
    public String getRealName(){return mRealName;}

    /** Method used to get the users location */
    public String getLocation(){return mLocation;}

    /** Method used to get the users company */
    public String getCompany(){return mCompany;}

    /** Method used to get the count of public repos from user */
    public int getPublicRepos(){return mPublicRepoCount;}

    /** Method used to get the number of followers */
    public int getFollowers(){
        return mFollowers;
    }

    /** Method used to get the image url */
    public String getImageUrl(){return mImageUrl;}

    /** Method used to get the profile web url */
    public String getProfileWebUrl(){return mProfileWebUrl;}

    /** Method used to get the API url for a given profile */
    public String getApiProfileUrl(){return mApiUrl;}
}
