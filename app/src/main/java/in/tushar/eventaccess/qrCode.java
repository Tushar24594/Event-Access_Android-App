package in.tushar.eventaccess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class qrCode extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    public static String TAG = ">>QR Code Activity";
    TextView t1,t2;
    ImageView qrImage,back;
    String genQRCodeImage;
    Typeface light, medium, regular, bold;
    //    SharedPreferences
    String sharedData;
    SharedPreferences.Editor editor;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String nameKey = "nameKey";
    public static final String phoneKey = "phoneKey";
    public static final String emailKey = "emailKey";
    public static final String qrCodeKey = "qrCodeKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        light = Typeface.createFromAsset(getAssets(),"fonts/light.ttf");
        medium = Typeface.createFromAsset(getAssets(),"fonts/medium.ttf");
        regular = Typeface.createFromAsset(getAssets(),"fonts/regular.ttf");
        bold = Typeface.createFromAsset(getAssets(),"fonts/bold.ttf");
        t1 = findViewById(R.id.qrText);
        t1.setTypeface(bold);
        t2 = findViewById(R.id.showQRText);
        t2.setTypeface(regular);
        qrImage = findViewById(R.id.qrImage);
        back = findViewById(R.id.back);
        //shared preferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        genQRCodeImage = sharedpreferences.getString(qrCodeKey,null);
        if(genQRCodeImage != null){
            Log.e(TAG," Qr image : "+genQRCodeImage);
            File imgFile = new  File(genQRCodeImage);

            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                qrImage.setImageBitmap(myBitmap);

            }
        }else{
            Log.e(TAG,"Empty Qr Image");
            qrImage.setImageResource(R.drawable.sample);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DetailedActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
