package in.tushar.eventaccess.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import in.tushar.eventaccess.HttpRequest;
import in.tushar.eventaccess.R;
import in.tushar.eventaccess.notifications.notificationAdapter;
import in.tushar.eventaccess.notifications.notificationModel;
import in.tushar.eventaccess.qrCode;


public class NotificationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = ">>Notification Fragment";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    ImageButton qrImageButton;
    ArrayList<notificationModel> notificationModelArrayList;
    private notificationAdapter notificationAdapter;
    private String jsonURL = "https://demonuts.com/Demonuts/JsonTest/Tennis/json_parsing.php";
    private final int jsoncode = 1;
    private static ProgressDialog mProgressDialog;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM, hh:mm aa");
    String date;
    public NotificationFragment() {
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
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        recyclerView = view.findViewById(R.id.notificationrecycler);
        qrImageButton = view.findViewById(R.id.notificationqrImage);
        qrImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), qrCode.class);
                startActivity(intent);
            }
        });
        fetchNotificationData();
        return view;
    }

    private void fetchNotificationData() {
        showSimpleProgressDialog(getActivity(),"Loading........","Fetching Data",false);
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... voids) {
                String response = "";
                HashMap<String,String> map =new HashMap<>();
                try{
                    HttpRequest request =new HttpRequest(jsonURL);
                    response = request.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                }catch (Exception e){
                    response = e.getMessage();
                }
                return  response;
            }
            protected void onPostExecute(String result){
                Log.e(TAG,result);
                onTaskCompleted(result,jsoncode);
            }
        }.execute();
    }

    private ArrayList<notificationModel> getInfo(String response) {
        ArrayList<notificationModel> notificationAdapterArrayList = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getString("status").equals("true")){
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                date = simpleDateFormat.format(calendar.getTime());
                Log.e(TAG,"Date and Time : "+date);
                for(int i=0;i<jsonArray.length();i++){
                    notificationModel list = new notificationModel();
                    JSONObject dataObj =jsonArray.getJSONObject(i);
                    list.setImageUrl(dataObj.getString("imgURL"));
                    list.setNotificationHeader(dataObj.getString("name"));
                    list.setNotificationDes(dataObj.getString("country")+" - "+dataObj.getString("city"));
                    list.setDateTime(date);
                    notificationAdapterArrayList.add(list);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return notificationAdapterArrayList;
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

    private void onTaskCompleted(String response, int serviceCode) {

        Log.e(TAG,response.toString());
        switch (serviceCode){
            case jsoncode:
                if(isSuccess(response)){
                    removeSimpleProgressDialog();
                    notificationModelArrayList = getInfo(response);
                    notificationAdapter =new notificationAdapter(getActivity(),notificationModelArrayList);
                    recyclerView.setAdapter(notificationAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                }else{
                    Log.e(TAG,"Error Message : "+getErrorCode(response));

                }
        }
    }

    private void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getErrorCode(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "No data";
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

}
