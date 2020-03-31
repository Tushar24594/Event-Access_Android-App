package in.tushar.eventaccess;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import in.tushar.eventaccess.fragments.CheckInFragment;
import in.tushar.eventaccess.fragments.QRScanFragment;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class Scan extends AppCompatActivity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result result) {
        // Do something with the result here
        Log.v("kkkk", result.getContents()); // Prints scan results
        Log.v("uuuu", result.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)

//        QRScanFragment.qr
        QRScanFragment.qrDataScan.setText(result.getContents());
        QRScanFragment qrScanFragment = new QRScanFragment();
        Bundle bundle = new Bundle();
        bundle.putString("qr",result.getContents());
        qrScanFragment.setArguments(bundle);
        onBackPressed();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
