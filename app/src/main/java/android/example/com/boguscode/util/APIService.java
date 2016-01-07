package android.example.com.boguscode.util;

import com.silva.benjamin.ninetyninecoins.models.Item;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    @GET("api/items")
    Call<ArrayList<Item>> listItems(@Query("n") String n,
                                    @Query("page") String page,
                                    @Query("skip") String skip,
                                    @Query("category") String category);
}
