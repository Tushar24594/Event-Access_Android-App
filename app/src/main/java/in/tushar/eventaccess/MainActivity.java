package in.tushar.eventaccess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.regex.Pattern;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class MainActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    public static String Tag = ">>MainActivity";
    TextView registerText, detailText, nameText, emailText, phoneText;
    EditText nameEdit, emailEdit, phoneEdit;
    String userName, userEmail, userPhone;
    Button submitBtn;
    Typeface light, medium, regular, bold;
    DBHelper dbHelper;
    ArrayList usersDataList;
    String dbName = "usersData";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference(dbName);
    Boolean isFind = false;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyy hh:mm:ss aa");
    String date;
    private String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);
        light = Typeface.createFromAsset(getAssets(), "fonts/light.ttf");
        medium = Typeface.createFromAsset(getAssets(), "fonts/medium.ttf");
        regular = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        bold = Typeface.createFromAsset(getAssets(), "fonts/bold.ttf");
        checkMultiplePermission();
        registerText = findViewById(R.id.registerText);
        registerText.setTypeface(regular);
        detailText = findViewById(R.id.detailsText);
        detailText.setTypeface(regular);
        nameText = findViewById(R.id.fullNameText);
        nameText.setTypeface(regular);
        emailText = findViewById(R.id.emailText);
        emailText.setTypeface(regular);
        phoneText = findViewById(R.id.phoneText);
        phoneText.setTypeface(regular);
        nameEdit = findViewById(R.id.userName);
        nameEdit.setTypeface(medium);
        emailEdit = findViewById(R.id.userEmail);
        emailEdit.setTypeface(medium);
        phoneEdit = findViewById(R.id.userPhone);
        phoneEdit.setTypeface(medium);
        submitBtn = findViewById(R.id.submit);
        submitBtn.setTypeface(medium);
        phoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (phoneEdit.requestFocus()) {
                    Log.e(Tag, "Focus...." + s);
                    if (phoneEdit.getText().toString().trim().length() == 10) {
                        if (!isFind) {
                            getDataFromFirebase(phoneEdit.getText().toString().trim());
                            phoneEdit.clearFocus();
                            phoneEdit.setFocusable(false);
                            phoneEdit.setFocusableInTouchMode(false);
                            submitBtn.requestFocus();
                            return;
                        }

                    }
                }
            }
        });
//        Data Fetch From SQLite
//        usersDataList = dbHelper.getAllData();
//        for (Object users :usersDataList){
//            Log.e(Tag,"Users Data : "+users.toString());
//
//        }

    }

    public void submitData(View v) {
        Log.e(Tag, "Clicked! : " + v);

        userName = nameEdit.getText().toString().trim();
        userEmail = emailEdit.getText().toString().trim();
        userPhone = phoneEdit.getText().toString().trim();
        if (userName.isEmpty()) {
            nameEdit.setError("Please Enter Your Name");
            nameEdit.requestFocus();
            return;
        }
        if (userEmail.isEmpty() || !validEmail(userEmail)) {
            emailEdit.setError("Please enter your Email");
            emailEdit.requestFocus();
            return;
        }
        if (userPhone.isEmpty() || userPhone.length() < 10) {
            phoneEdit.setError("Please Enter Your Mobile Number");
            phoneEdit.requestFocus();
            return;
        }

        submitBtn.setScaleX((float) 0.9);
        submitBtn.setScaleY((float) 0.9);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                submitBtn.setScaleX((float) 1.0);
                submitBtn.setScaleY((float) 1.0);
                if (!userName.isEmpty() && !userEmail.isEmpty() && !userPhone.isEmpty()) {
//            FireBase Realtime Database
                    date = simpleDateFormat.format(calendar.getTime());
                    myRef.child(userPhone).child("name").setValue(userName);
                    myRef.child(userPhone).child("email").setValue(userEmail);
                    myRef.child(userPhone).child("phone").setValue(userPhone);
                    myRef.child(userPhone).child("date").setValue(date);
                    generateQRCode(userName, userEmail, userPhone);
//            Data insert into SQLite
//            dbHelper.insertData(userName,userPhone,userEmail);
//            Cursor result = dbHelper.getData(userPhone);
//            if(result.getCount()==0){
//                Log.e(Tag," DB Count is Zero.."+result);
//            }else{
//                StringBuffer buffer = new StringBuffer();
//                while (result.moveToNext()){
//                    buffer.append("Id :"+ result.getString(0)+"\n");
//                    buffer.append("Name :"+ result.getString(1)+"\n");
//                    buffer.append("Mobile :"+ result.getString(2)+"\n");
//                    buffer.append("Email :"+ result.getString(3)+"\n\n");
//                }
//                Log.e(Tag," DB >>>>>>>> "+buffer.toString());
//            }
                }
            }
        }, 300);
    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public void getDataFromFirebase(String phone) {
        Log.e(Tag, "Get Data From Firebase with that number...." + phone);
        try {
            myRef.child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    Log.e(Tag, " Data is :" + map);
                    try {
                        JSONObject object = new JSONObject(map);
                        Log.d(Tag, " Name is :" + object.getString("name"));
                        Log.d(Tag, " email is :" + object.getString("email"));
                        Log.d(Tag, " phone is :" + object.getString("phone"));
                        nameEdit.setText(object.getString("name"));
                        emailEdit.setText(object.getString("email"));
                        phoneEdit.setText(object.getString("phone"));
                        isFind = true;
                        phoneEdit.clearFocus();
                        phoneEdit.setFocusable(false);
                        phoneEdit.setFocusableInTouchMode(true);
                        nameEdit.setFocusable(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateQRCode(String userName, String userEmail, String userPhone) {
        Log.e(Tag, "Generate QR Code...");
        String qrData = "name=" + userName + ";" + "email=" + userEmail + ";" + "phone=" + userPhone + ";";
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;
        try {
            QRGEncoder qrgEncoder = new QRGEncoder(qrData, null, QRGContents.Type.TEXT, smallerDimension);
            Bitmap bitmap = qrgEncoder.getBitmap();
            QRGSaver qrgSaver = new QRGSaver();
            qrgSaver.save(savePath, userPhone, bitmap, QRGContents.ImageType.IMAGE_JPEG);
            Intent intent = new Intent(getApplicationContext(), qrCode.class);
            intent.putExtra("QRimage", savePath + userPhone + ".jpg");
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Boolean checkMultiplePermission(){

        return true;
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}

