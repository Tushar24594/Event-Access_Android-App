package in.tushar.eventaccess.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.qrcode.encoder.QRCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import in.tushar.eventaccess.HttpRequest;
import in.tushar.eventaccess.R;
import in.tushar.eventaccess.SharePref;
import in.tushar.eventaccess.checkInCardAdapter;
import in.tushar.eventaccess.checkInModel;
import in.tushar.eventaccess.qrCode;

import static android.app.Activity.RESULT_OK;

public class CheckInFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = ">>CheckIn Fragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    ArrayList<checkInModel> checkInModelArrayList;
    private checkInCardAdapter checkInCardAdapter;
    private String jsonURL = "https://demonuts.com/Demonuts/JsonTest/Tennis/json_parsing.php";
    private final int jsoncode = 1;
    private static ProgressDialog mProgressDialog;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
    String date;
    ImageButton qrImageButton;
    SharePref sharePref = new SharePref();
    //    SharedPreferences
    String sharedDataPhone;
    SharedPreferences.Editor editor;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private static final String PLACE_OBJ = "users";
    public static final String nameKey = "nameKey";
    public static final String phoneKey = "phoneKey";
    public static final String emailKey = "emailKey";
    public static final String manufactureKey = "manufactureKey";
    public static final String checkInTimeKey = "checkInTimeKey";
    public static final String checkOutTimeKey = "checkOutTimeKey";
    String name ;
    String email;
    String phone ;
    String manufacture ;
    String cin ;
    String cout ;
    public CheckInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckInFragment newInstance(String param1, String param2) {
        CheckInFragment fragment = new CheckInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checkin, container, false);
        try{
            sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
             name = sharedpreferences.getString(nameKey,"");
             email = sharedpreferences.getString(emailKey,"");
             phone = sharedpreferences.getString(phoneKey,"");
             manufacture = sharedpreferences.getString(manufactureKey,"");
             cin = sharedpreferences.getString(checkInTimeKey,"");
             cout = sharedpreferences.getString(checkOutTimeKey,"");
            Log.e(TAG,"Shared name : "+name+phone+email+manufacture+cin+cout);
            recyclerView = view.findViewById(R.id.recycler);
            qrImageButton = view.findViewById(R.id.qrImage);
//            checkInModel checkInModels = new checkInModel();
//            checkInModels.setHeader(manufacture);
//            checkInModels.setCheckInTime(cin);
//            checkInModels.setCheckOutTime(cout);
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG,"Error to add card : "+e);
        }

        qrImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "Showing QR Code.....");
                Intent intent = new Intent(getActivity(), qrCode.class);
                startActivity(intent);
            }
        });
        fetchJson();
        return view;
    }

    private void fetchJson() {
        showSimpleProgressDialog(getActivity(), "Loading...", "Fetching Data", false);
        new AsyncTask<Void, Void, String>() {
            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                try {
                    HttpRequest httpRequest = new HttpRequest(jsonURL);
                    response = httpRequest.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (Exception e) {
                    response = e.getMessage();
                }
                return response;
            }

            public void onPostExecute(String result) {
                //do something with response
//                Log.d("newwwss", result);
                onTaskCompleted(result, jsoncode);
            }
        }.execute();
    }

    private void onTaskCompleted(String response, int serviceCode) {
//        Log.d("responsejson", response.toString());
        switch (serviceCode) {
            case jsoncode:
                if (isSuccess(response)) {
                    removeSimpleProgressDailog();
                    checkInModelArrayList = getInfo(response);
                    checkInCardAdapter = new checkInCardAdapter(getActivity(), checkInModelArrayList);
                    recyclerView.setAdapter(checkInCardAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                } else {
//                    Toast.makeText(getActivity(), "Error fetch json", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private ArrayList<checkInModel> getInfo(String response) {
        ArrayList<checkInModel> modelArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            date = simpleDateFormat.format(calendar.getTime());
//            String getQrData = sharePref.getPlaceObj();
//            Log.e(">>>CheckIn Activity",getQrData);

            if (jsonObject.get("status").equals("true")) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < 1; i++) {
                    if(!manufacture.isEmpty()){
                        checkInModel checkInModels = new checkInModel();
                        JSONObject dataObj = jsonArray.getJSONObject(i);
//                    checkInModels.setHeader(dataObj.getString("name"));
//                    checkInModels.setCheckInTime(date);
                        checkInModels.setHeader(manufacture);
                        checkInModels.setCheckInTime(cin);
                        checkInModels.setCheckOutTime(cout);
                        modelArrayList.add(checkInModels);
                    }else{
                        Toast.makeText(getActivity(), "No one can CheckIn", Toast.LENGTH_SHORT).show();
                    }

                }
//                Log.e(">>>CheckIn Json", String.valueOf(jsonArray));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
//        Log.e(">>>CheckIn Array", String.valueOf(modelArrayList));
        return modelArrayList;
    }

    private void removeSimpleProgressDailog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean isSuccess(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("status").equals("true")) {
                return true;
            } else {
                return false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
