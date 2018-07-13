package android.example.com.boguscode.util;



import android.content.Context;
import android.example.com.boguscode.util.API.APIService;
import android.widget.Toast;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

public class Helper {

    private static String BASE_URL = "https://api.vimeo.com/";

    public static APIService service(){
        OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        APIService service = retrofit.create(APIService.class);
        return service;
    }

    public static void showAToast (Toast mToast, Context context, String message, int gravity, int xoffset, int yoffset){
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        int x_offset = Math.round(xoffset * context.getResources().getDisplayMetrics().density);
        int y_offset = Math.round(yoffset * context.getResources().getDisplayMetrics().density);
        mToast.setGravity(gravity, x_offset, y_offset);
        mToast.show();
    }

    public static String formatTime(int seconds){
        int millis = seconds*1000;
        String formatted = "";
        if(seconds >= 3600){
            formatted = String.format("%d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
                    );
        }
        else{
            formatted = String.format("%d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
            );
        }
        return formatted;
    }
}
