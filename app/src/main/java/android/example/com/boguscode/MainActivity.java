package android.example.com.boguscode;

import android.example.com.boguscode.adapters.VideoListAdapter;
import android.example.com.boguscode.models.Video;
import android.example.com.boguscode.util.API.VideoResponse;
import android.example.com.boguscode.util.DividerItemDecoration;
import android.example.com.boguscode.util.Helper;
import android.example.com.boguscode.util.PreCachingLayoutManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private int mCurrentPage = 1;
    private final int PER_PAGE = 25;
    private final int RECYCLER_CACHE_SIZE = 50;
    private boolean canFetch = true;
    private Toast mToast = null;
    private RecyclerView mRecyclerView;
    private PreCachingLayoutManager mLayoutManager;
    private RelativeLayout mLoadingWrapper;
    private VideoListAdapter mAdapter;
    private boolean loading = false;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        fetchData();
    }

    private void setViews(){
        mRecyclerView = (RecyclerView) findViewById(R.id.videos_recycler_view);
        mRecyclerView.setItemViewCacheSize(RECYCLER_CACHE_SIZE);
        mLayoutManager = new PreCachingLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);
        mLoadingWrapper = (RelativeLayout) findViewById(R.id.loading_wrapper);
        addOnScrollListener();
    }

    private void addOnScrollListener(){
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if(dy > 0){ //check for scroll down
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                    if (!loading && canFetch){
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount-25){
                            fetchData();
                        }
                    }
                }
            }
        });
    }
    private void fetchData(){
        loading = true;
        Call<VideoResponse> call = Helper.service().listVideos(mCurrentPage, PER_PAGE);
        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Response<VideoResponse> response) {
                VideoResponse videoResponse = response.body();
                if (videoResponse != null && videoResponse.data != null && videoResponse.data.size()>0) {
                    if(mAdapter == null) {
                        mAdapter = new VideoListAdapter(videoResponse.data, MainActivity.this);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    else{
                        mAdapter.updateDataset(videoResponse.data);

                    }
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mLoadingWrapper.setVisibility(View.GONE);
                    preFetchImages(videoResponse.data);
                    mCurrentPage++;
                } else {
                    Helper.showAToast(mToast, MainActivity.this, getString(R.string.error_fetching_videos), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, -20);
                    mLoadingWrapper.setVisibility(View.GONE);
                    //TODO show a "No videos available" textview
                    canFetch = false; //means there aren't more videos on the next page.
                }
                loading = false;
            }

            @Override
            public void onFailure(Throwable t) {
                loading = false;
                Helper.showAToast(mToast, MainActivity.this, getString(R.string.error_fetching_videos), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, -20);
                mLoadingWrapper.setVisibility(View.GONE);
                //TODO show a "No videos available textview"
            }
        });
    }

    private void preFetchImages(ArrayList<Video> videos){
        for(Video v : videos){
            Picasso.with(MainActivity.this)
                    .load(v.pictures.sizes.get(v.pictures.sizes.size() - 2).link)
                    .fetch();
        }
    }
}
