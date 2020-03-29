package in.tushar.eventaccess;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class qrCode extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    public static String TAG = ">>QR Code Activity";
    TextView t1,t2;
    ImageView qrImage;
    String genQRCodeImage;
    Typeface light, medium, regular, bold;
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
        genQRCodeImage = getIntent().getStringExtra("QRimage");
        Log.e(TAG,"QR IMAGE : "+genQRCodeImage);
        File imgFile = new  File(genQRCodeImage);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            qrImage.setImageBitmap(myBitmap);

        }
    }
}
