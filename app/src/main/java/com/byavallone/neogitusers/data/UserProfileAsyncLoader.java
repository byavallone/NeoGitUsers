package com.byavallone.neogitusers.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/** AsyncLoader class used to request data to the API, parse and return a list of objects(UserProfile) */
public class UserProfileAsyncLoader extends android.support.v4.content.AsyncTaskLoader<List<UserProfile>> {

    private String mSearchTerm;

    public UserProfileAsyncLoader(@NonNull Context context, String searchTerm) {
        super(context);
        mSearchTerm = searchTerm;
        forceLoad();
    }

    @Nullable
    @Override
    public List<UserProfile> loadInBackground() {
        String result = "";

        //creating the URL
        URL url = NetworkUtils.buildUserSearchByFollowersURL(mSearchTerm);

        if(url != null){
            try {
                result = NetworkUtils.makeHttpRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(result != null && !result.isEmpty()) {
            List<UserProfile> userProfileList = NetworkUtils.parseFromUsersResultString(result);
            return userProfileList;
        }
        return null;
    }
}
