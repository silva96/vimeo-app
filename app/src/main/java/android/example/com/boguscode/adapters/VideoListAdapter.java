package android.example.com.boguscode.adapters;

import android.content.Context;
import android.example.com.boguscode.R;
import android.example.com.boguscode.models.Video;
import android.example.com.boguscode.util.Helper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder>{

    private ArrayList<Video> mDataset;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView mTitle;
        protected TextView mUser;
        protected TextView mDuration;
        protected ImageView mPicture;
        protected RelativeLayout mLoaderWrapper;

        public ViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.video_title);
            mUser = (TextView) v.findViewById(R.id.video_user);
            mDuration = (TextView) v.findViewById(R.id.video_duration);
            mPicture = (ImageView) v.findViewById(R.id.video_image);
            mLoaderWrapper = (RelativeLayout) v.findViewById(R.id.progress_bar_infinite_scroll_wrapper);
        }
    }

    public VideoListAdapter(ArrayList<Video> mDataset, Context mContext) {
        this.mDataset = mDataset;
        this.mContext = mContext;
    }
    @Override
    public VideoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_video, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Video v = mDataset.get(position);
        holder.mTitle.setText(v.name);
        holder.mUser.setText(v.user.name);
        holder.mDuration.setText(Helper.formatTime(v.duration));
        Picasso.with(mContext)
                .load(v.pictures.sizes.get(v.pictures.sizes.size() - 2).link)
                .placeholder(R.drawable.placeholder)
                .into(holder.mPicture);
        if(position == mDataset.size() - 1){
            holder.mLoaderWrapper.setVisibility(View.VISIBLE);
        }

    }

    public void updateDataset(ArrayList<Video> diff){
        final int start = mDataset.size()-1;
        mDataset.addAll(diff);
        //notifyItemRangeInserted(start, mDataset.size()-1);
        //should use rangeInserted but android has a bug on this: https://code.google.com/p/android/issues/detail?id=77846
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
