package in.tushar.eventaccess.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import in.tushar.eventaccess.R;
import in.tushar.eventaccess.Scan;
import in.tushar.eventaccess.SharePref;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScanFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String getQrDataFromIntent;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ZXingScannerView mScannerView;
    public final static String TAG = "--QRScanner";
    public static FrameLayout frameLayout;
    ImageButton imageButton;
    public static String qr = "";
    String QrData = "QRData";
    String QrName = "manufacture";
    String QrCheckIn = "checkin";
    String QrCheckOut = "checkout";
    String checkInTime = "checkintime";
    String checkOutTime = "checkouttime";
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
    String date = "";
    JSONObject userDetail;
    String[] splited;
    QrScannedData qrScannedData;
    public static TextView qrDataScan;
    SharePref sharePref = new SharePref();
    private RequestQueue queue;
    //    SharedPreferences
    String sharedDataPhone;
    SharedPreferences.Editor editor;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String nameKey = "nameKey";
    public static final String phoneKey = "phoneKey";
    public static final String emailKey = "emailKey";
    public static final String manufactureKey = "manufactureKey";
    public static final String checkInTimeKey = "checkInTimeKey";
    public static final String checkOutTimeKey = "checkOutTimeKey";
    String userName = "name";
    String userEmail = "email";
    String userPhone = "phone";
    String manufacture = "manufacture";

    public QRScanFragment() {
        // Required empty public constructor

        Log.e(TAG, "Constructor");
        try {
            if (qrDataScan.getText().toString().contains("1")) {
                Log.e(TAG, "Data : " + qrDataScan.getText().toString().trim());
            } else {
                Log.e(TAG, "Data : " + qrDataScan.getText().toString().trim());
                new QrScannedData().execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QRScanFragment newInstance(String param1, String param2) {
        QRScanFragment fragment = new QRScanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qrscan, container, false);
        qrDataScan = view.findViewById(R.id.qrscanData);
        frameLayout = view.findViewById(R.id.scanBtn);
        imageButton = view.findViewById(R.id.button_x);
        Log.e(TAG, "onCreate Bundle");

        qrScannedData = new QrScannedData();
        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String name = sharedpreferences.getString(nameKey,"");
        String email = sharedpreferences.getString(emailKey,"");
        String phone = sharedpreferences.getString(phoneKey,"");
        String manufacture = sharedpreferences.getString(manufactureKey,"");
        String cin = sharedpreferences.getString(checkInTimeKey,"");
        String cout = sharedpreferences.getString(checkOutTimeKey,"");
//        String data = sharedpreferences.getString()
        Log.e(TAG,"Scan name : "+name+phone+email+manufacture+cin+cout);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButton.setScaleX((float) 0.9);
                imageButton.setScaleY((float) 0.9);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageButton.setScaleX((float) 1.0);
                        imageButton.setScaleY((float) 1.0);
                        Intent intent = new Intent(getActivity(), Scan.class);
                        startActivity(intent);
                    }
                }, 200);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "OnResume" + qrDataScan.getText().toString().trim());
        if(qrDataScan.getText().toString().trim().contains("1")){
            Log.e(TAG,"Not Adding...");
        }else{
            try {
                Log.e(TAG,"Adding...");
                QrData = qrDataScan.getText().toString().trim();
                splited = QrData.split("~");
                QrName = splited[0];
                manufacture = splited[0];
                Log.e(TAG, splited[0] + splited[1]);
                editor = sharedpreferences.edit();
                editor.putString(manufactureKey,manufacture);
                if (splited[1].contains("checkIn")) {
                    date = simpleDateFormat.format(calendar.getTime());
                    QrCheckIn = splited[1];
                    checkInTime = date;
                    editor.putString(checkInTimeKey,checkInTime);
//                    sharePref.dataIn(QrName, QrCheckIn, checkInTime);
                }
                if (splited[1].contains("checkOut")) {
                    date = simpleDateFormat.format(calendar.getTime());
                    QrCheckOut = splited[1];
                    checkOutTime = date;
                    editor.putString(checkOutTimeKey,checkOutTime);
//                    sharePref.dataOut(QrName, QrCheckOut, checkOutTime);
                }
                editor.commit();
//                sendDataToServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(getActivity(), Scan.class);
        startActivity(intent);

    }

    public class QrScannedData extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            Log.e(TAG, "doInBackground : " + voids);
            Log.e(TAG, "DoInBackground : " + qrDataScan.getText().toString().trim());
            try {

                QrData = qrDataScan.getText().toString().trim();
                splited = QrData.split("~");
                QrName = splited[0];
                manufacture = splited[0];
                Log.e(TAG, splited[0] + splited[1]);
                editor = sharedpreferences.edit();
                editor.putString(manufactureKey,manufacture);
                if (splited[1].contains("checkIn")) {
                    date = simpleDateFormat.format(calendar.getTime());
                    QrCheckIn = splited[1];
                    checkInTime = date;
                    editor.putString(checkInTimeKey,checkInTime);
//                    sharePref.dataIn(QrName, QrCheckIn, checkInTime);
                }
                if (splited[1].contains("checkOut")) {
                    date = simpleDateFormat.format(calendar.getTime());
                    QrCheckOut = splited[1];
                    checkOutTime = date;
                    editor.putString(checkOutTimeKey,checkOutTime);
//                    sharePref.dataOut(QrName, QrCheckOut, checkOutTime);
                }
                editor.commit();
                sendDataToServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void sendDataToServer() {
        Log.e(TAG, "Post Api to Server");
        String url;
        StringRequest stringRequest = null;
        try {
//            userName=sharedpreferences.getString(nameKey, null);
//            userEmail=sharedpreferences.getString(emailKey, null);
//            userPhone=sharedpreferences.getString(phoneKey, null);
            url = "https://ea-test.herokuapp.com/api/newUser";
            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "Response : " + response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Volley Error : ", error);
                }
            }) {
                protected Map<String, String> getParms() {
                    Map<String, String> MyData = new HashMap<String, String>();
                    MyData.put("name", userName);
                    MyData.put("email", userEmail);
                    MyData.put("phone", userPhone);
                    MyData.put("manufacture", manufacture);
                    MyData.put("checkIn", checkInTime);
                    MyData.put("checkOut", checkOutTime);
                    return MyData;
                }
            };

            Log.e(TAG, "In Try request" + stringRequest);
//            queue = Volley.newRequestQueue(getContext());
//            queue.add(stringRequest);
        } catch (Exception e) {
            Log.e(TAG, "In Error request" + e);
            e.printStackTrace();
        }
//        queue.add(stringRequest);
    }

}


