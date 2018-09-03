package com.byavallone.neogitusers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.byavallone.neogitusers.data.UserProfile;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserProfileAdapter extends RecyclerView.Adapter<UserProfileAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private List<UserProfile> mUsersList;
    private Context mContext;
    private ItemClickListener mClickListener;
    private String mQueryTerm;

    UserProfileAdapter(Context context, List<UserProfile> userList, String queryTerm){
        mContext = context;
        mUsersList = userList;
        mInflater = LayoutInflater.from(context);
        mQueryTerm = queryTerm;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        // inflates the item view and return
        View view = mInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        UserProfile user = mUsersList.get(position);

        // Construct the formatted text
        String replacedWith = "<font color='red'>" + mQueryTerm + "</font>";

        // Replace the specified text/word with formatted text/word
        String modifiedString = user.getNickName().replaceAll(mQueryTerm,replacedWith);

        viewHolder.mUserNickname.setText(Html.fromHtml(modifiedString));
        viewHolder.mUserRealName.setText(user.getRealName());
        viewHolder.mUserCompany.setText(user.getCompany());
        viewHolder.mUserLocation.setText(user.getLocation());

        String followers = "Followers: ";
        followers = followers.concat(String.valueOf(user.getFollowers()));
        viewHolder.mUserFollowers.setText(followers);

        String publicRepos = "Public Repos: ";
        publicRepos = publicRepos.concat(String.valueOf(user.getPublicRepos()));
        viewHolder.mUserPublicRepos.setText(publicRepos);
        Picasso.with(mContext).load(user.getImageUrl()).into(viewHolder.mUserImage);
    }

    @Override
    public int getItemCount() {
        return mUsersList.size();
    }

    /**
     * Method used to get the item in position
     * @param position
     * @return
     */
    public UserProfile getItemInPosition(int position){
        return mUsersList.get(position);
    }
    /**
     * Method used to update the list on the adapter
     * @param userList
     */
    public void setUsersList(List<UserProfile> userList){
        mUsersList = userList;
    }

    /**
     * View Holder class used to store and recycles the list item
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mUserImage;
        TextView mUserNickname;
        TextView mUserRealName;
        TextView mUserLocation;
        TextView mUserCompany;
        TextView mUserPublicRepos;
        TextView mUserFollowers;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mUserImage = itemView.findViewById(R.id.user_picture);
            mUserNickname = itemView.findViewById(R.id.user_nickname_text_view);
            mUserRealName = itemView.findViewById(R.id.user_real_name_text_view);
            mUserLocation = itemView.findViewById(R.id.user_location_text_view);
            mUserCompany = itemView.findViewById(R.id.user_company_text_view);
            mUserPublicRepos = itemView.findViewById(R.id.user_public_repos_text_view);
            mUserFollowers = itemView.findViewById(R.id.user_followers_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mClickListener!= null){
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    void setClickListener(ItemClickListener itemClickListener){
        mClickListener = itemClickListener;
    }

    //Need to implement this method on the parent activity
    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }
}
