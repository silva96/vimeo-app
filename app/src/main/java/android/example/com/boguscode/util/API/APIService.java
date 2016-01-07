package android.example.com.boguscode.util.API;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface APIService {
    String TOKEN = "bearer b8e31bd89ba1ee093dc6ab0f863db1bd";

    @Headers("Authorization: "+TOKEN)
    @GET("/channels/staffpicks/videos")
    Call<VideoResponse> listVideos(@Query("page") int page,
                                              @Query("per_page") int per_page);
}
