package com.jo.android.smartrestaurant;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrReaderActivity extends AppCompatActivity {

    private ZXingScannerView scanner;
     TextView textResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scanner = new ZXingScannerView(this);
        setContentView(scanner);
        textResult = findViewById(R.id.qr_focus);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scanner.setResultHandler(new ZXingScannerView.ResultHandler() {
                            @Override
                            public void handleResult(Result rawResult) {

                                Log.e("Result",rawResult.getText());
                                Intent intent = new Intent(QrReaderActivity.this,UserMenuActivity.class);
                                intent.putExtra("QR Result",rawResult.getText());
                                startActivity(intent);
                            }
                        });
                        scanner.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(QrReaderActivity.this,
                                "You must accept this permission",
                                Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
    }

    @Override
    protected void onDestroy() {
        scanner.stopCamera();
        super.onDestroy();
    }
}
