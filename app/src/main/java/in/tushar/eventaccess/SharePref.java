package in.tushar.eventaccess;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

public class SharePref {
    private static SharePref sharePref = new SharePref();
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static final String PLACE_OBJ = "users";
    public static final String nameKey = "nameKey";
    public static final String checkInKey = "checkInKey";
    public static final String checkInTimeKey = "checkInTimeKey";
    public static final String checkOutKey = "checkOutKey";
    public static final String checkOutTimeKey = "checkOutTimeKey";

    public SharePref() {} //prevent creating multiple instances by making the constructor private

    //The context passed into the getInstance should be application level context.
    public static SharePref getInstance(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PLACE_OBJ, Activity.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        return sharePref;
    }

    public void dataIn(String name,String checkIn,String checkInTime) {
        Log.e("<<Shared Data",name+checkIn+checkInTime);
        editor.putString(nameKey, name);
        editor.putString(checkInKey, checkIn);
        editor.putString(checkInTimeKey, checkInTime);
        editor.commit();
    }
    public void dataOut(String name,String checkOut,String checkOutTime) {
        Log.e("<<Shared Data",name+checkOut+checkOutTime);
        editor.putString(nameKey, name);
        editor.putString(checkOutKey, checkOut);
        editor.putString(checkOutTimeKey,checkOutTime);
        editor.commit();
    }

    public String getPlaceObj() {
        String obj = "";
        try {
            obj =  sharedPreferences.getString(PLACE_OBJ, "");

        }catch (Exception e){
            e.printStackTrace();
        }
        return obj;
    }

    public void removePlaceObj() {
        editor.remove(PLACE_OBJ);
        editor.commit();
    }

    public void clearAll() {
        editor.clear();
        editor.commit();
    }

}
