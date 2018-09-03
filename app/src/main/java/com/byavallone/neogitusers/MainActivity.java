package com.byavallone.neogitusers;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.byavallone.neogitusers.data.UserProfile;
import com.byavallone.neogitusers.data.UserProfileAsyncLoader;

import java.util.List;

public class MainActivity extends AppCompatActivity implements UserProfileAdapter.ItemClickListener, android.support.v4.app.LoaderManager.LoaderCallbacks<List<UserProfile>> {

    private EditText mQueryEditText;
    private RecyclerView mResultsList;
    private ImageButton mSearchButton;
    private UserProfileAdapter mAdapter;
    private ScrollView mContentView;
    private FrameLayout mLoadingBarView;
    private LinearLayout mWarningView;
    private TextView mWarningMessageView;
    private ImageView mWarningImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Mapping UI
        mQueryEditText = findViewById(R.id.main_edit_text);
        mSearchButton = findViewById(R.id.main_search_button);
        mResultsList = findViewById(R.id.main_result_view);
        mLoadingBarView = findViewById(R.id.loading_bar);
        mContentView = findViewById(R.id.list_scroll_view);
        mWarningView = findViewById(R.id.warning_layout);
        mWarningMessageView = findViewById(R.id.warning_message);
        mWarningImageView = findViewById(R.id.warning_icon);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mResultsList.setLayoutManager(layoutManager);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasInternet(MainActivity.this)) {
                    mContentView.setVisibility(View.GONE);
                    mLoadingBarView.setVisibility(View.VISIBLE);
                    mWarningView.setVisibility(View.GONE);
                    getSupportLoaderManager().initLoader(1, null, MainActivity.this);

                }else{
                    mWarningView.setVisibility(View.VISIBLE);
                    mLoadingBarView.setVisibility(View.GONE);
                    mWarningImageView.setImageResource(android.R.drawable.stat_notify_error);
                    mWarningMessageView.setText(R.string.warning_no_internet);
                }
            }
        });
    }

    @NonNull
    @Override
    public Loader<List<UserProfile>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new UserProfileAsyncLoader(this, mQueryEditText.getText().toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<UserProfile>> loader, List<UserProfile> userProfiles) {
        //Hide the loading
        mLoadingBarView.setVisibility(View.GONE);
        //checking if we have a list of movies and if the list is not empty
        if(userProfiles != null && !userProfiles.isEmpty()){
            //Hide the warning UI and show the content UI
            mWarningView.setVisibility(View.GONE);
            mContentView.setVisibility(View.VISIBLE);

            if(mAdapter == null){
                mAdapter = new UserProfileAdapter(MainActivity.this, userProfiles);
                //Setting this activity to the click listener on the adapter
                mAdapter.setClickListener(this);
                mResultsList.setAdapter(mAdapter);
            }else{

                mAdapter.setUsersList(userProfiles);
                mAdapter.notifyDataSetChanged();
            }
        }else{
            //In case the list is null or empty we show a no suggestion message
            mWarningView.setVisibility(View.VISIBLE);
            mWarningImageView.setImageResource(android.R.drawable.stat_notify_error);
            mWarningMessageView.setText(R.string.warning_user_found);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<UserProfile>> loader) {

    }

    /**
     * Method used to check internet connection
     * @return
     */
    private boolean hasInternet(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    @Override
    public void onItemClick(View view, int position) {
        UserProfile user = mAdapter.getItemInPosition(position);
        String profileUrl = user.getProfileWebUrl();
        //Just checking if we have a URL
        if(profileUrl != null && !profileUrl.isEmpty()){
            Uri uri = Uri.parse(profileUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if(intent.resolveActivity(getPackageManager()) != null){
                startActivity(intent);
            }
        }else{
            Toast.makeText(MainActivity.this, "Link unavailable", Toast.LENGTH_LONG).show();
        }
    }
}
