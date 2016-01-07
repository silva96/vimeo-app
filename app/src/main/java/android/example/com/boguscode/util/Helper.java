package android.example.com.boguscode.util;



import android.content.Context;
import android.example.com.boguscode.util.API.APIService;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by benjamin on 1/6/16.
 */
public class Helper {

    private static String BASE_URL = "https://api.vimeo.com/";

    public static APIService service(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.NONE); //Change NONE for BODY if you want to see full log
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();
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
