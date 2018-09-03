package com.byavallone.neogitusers.data;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/** Class used to network requests */
public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    //Holds the base URL used to request a list of users to GitHub API
    private final static String GITHUB_USERS_BASE_URL = "https://api.github.com/search/users";

    // Params and Values used on the API requests
    private final static String PARAM_QUERY = "q";
    private final static String PARAM_SORT = "sort";
    private final static String PARAM_ORDER_BY = "order";
    private final static String VALUE_SORT_BY_FOLLOWERS = "followers";
    private final static String VALUE_ORDER_DESC = "desc";

    /**
     * Method used to build a URL using the the term requested, the search will be sort by number of followers in desc order.
     *
     * @param gitHubTerm
     * @return
     */
    public static URL buildUserSearchByFollowersURL(String gitHubTerm) {
        Uri buildUri = Uri.parse(GITHUB_USERS_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, gitHubTerm)
                .appendQueryParameter(PARAM_SORT, VALUE_SORT_BY_FOLLOWERS)
                .appendQueryParameter(PARAM_ORDER_BY, VALUE_ORDER_DESC)
                .build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Method used to make a HTTP Request for the given URL
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Error code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    /**
     * Convert the inputStream into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Method used to parse the result from the API request
     *
     * @param jsonResult
     * @return
     */
    public static List<UserProfile> parseFromUsersResultString(String jsonResult) {

        // Create an empty ArrayList that we can start adding users to
        ArrayList<UserProfile> usersList = new ArrayList<>();

        try {
            JSONObject response = new JSONObject(jsonResult);
            JSONArray results = response.getJSONArray("items");
            for (int i = 0; i < results.length(); i++) {
                JSONObject jSonUser = results.getJSONObject(i);

                String nickname = "";
                if (jSonUser.has("login")) {
                    nickname = jSonUser.getString("login");
                }

                String image_url = "";
                if (jSonUser.has("avatar_url")) {
                    image_url = jSonUser.getString("avatar_url");
                }

                String profile_url = "";
                if (jSonUser.has("html_url")) {
                    profile_url = jSonUser.getString("html_url");
                }

                String api_profile_url = "";
                if (jSonUser.has("url")) {
                    api_profile_url = jSonUser.getString("url");
                }

                UserProfile user = new UserProfile(nickname, api_profile_url, image_url, profile_url);
                usersList.add(user);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return usersList;
    }

    /**
     * Method used to build a URL from a string
     *
     * @param stringURL
     * @return
     */
    public static URL buildURL(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static void requestExtraInformation(List<UserProfile> userProfileList) {

        for (int i = 0; i < userProfileList.size(); i++) {
            UserProfile user = userProfileList.get(i);
            String stringUrl = user.getApiProfileUrl();
            URL url = buildURL(stringUrl);
            String result = "";
            try {
                result = makeHttpRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (result != null && !result.isEmpty()) {
                try {
                    JSONObject response = new JSONObject(result);

                    String realName = "";
                    if (response.has("name")) {
                        realName = response.getString("name");
                    }

                    String company = "";
                    if (response.has("company")) {
                        company = response.getString("company");
                    }

                    String location = "";
                    if (response.has("location")) {
                        location = response.getString("location");
                    }

                    int followers = 0;
                    if(response.has("followers")){
                        followers = response.getInt("followers");
                    }

                    int publicRepos = 0;
                    if(response.has("public_repos")){
                        publicRepos = response.getInt("public_repos");
                    }

                    user.setExtraInformation(realName, company, location, publicRepos, followers);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
