package com.fisheradelakin.blck;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.commonsware.cwac.cam2.CameraActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PORTRAIT_BFC = 1;
    private static final String TAG = "MainActivity";
    private File mFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mFolder = new File(Environment.getExternalStorageDirectory() + "/Blck");

        if (!mFolder.exists()) {
            mFolder.mkdir();
            Log.i(TAG, "Folder doesn't exist");
        } else {
            Log.i(TAG, "Folder exists");
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
            return;
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission();
            return;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, com.fisheradelakin.blck.CameraActivity.class);
                startActivity(intent);
                /*Intent i = new CameraActivity.IntentBuilder(MainActivity.this)
                        .skipConfirm()
                        .to(new File(mFolder, "blck" + new Date().toString() + ".jpg"))
                        .debug()
                        .build();
                startActivityForResult(i, REQUEST_PORTRAIT_BFC);*/
            }
        });
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_PORTRAIT_BFC && resultCode == RESULT_OK) {
            Log.i(TAG, "Everything went well.");

            if(data != null) {
                Uri selectedImage = data.getData();
                String fileString = selectedImage.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(fileString, new BitmapFactory.Options());
                Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                //Bitmap bmpMonochrome = Bitmap.createBitmap(bitmap.getWidth(), bit, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mutableBitmap);
                ColorMatrix ma = new ColorMatrix();
                ma.setSaturation(0);
                Paint paint = new Paint();
                paint.setColorFilter(new ColorMatrixColorFilter(ma));
                canvas.drawBitmap(mutableBitmap, 0, 0, paint);

                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(new File(mFolder, "blck" + new Date().toString() + ".jpg"));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    private void requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new StorageConfirmationDialog().show(getFragmentManager(), Constants.FRAGMENT_DIALOG);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.REQUEST_STORAGE_PERMISSION);
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new ConfirmationDialog().show(getFragmentManager(), Constants.FRAGMENT_DIALOG);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    Constants.REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ErrorDialog.newInstance(getString(R.string.request_permission))
                        .show(getFragmentManager(), Constants.FRAGMENT_DIALOG);
            }
        } else if(requestCode == Constants.REQUEST_STORAGE_PERMISSION) {
            if(grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ErrorDialog.newInstance(getString(R.string.request_storage_permission)).show(getFragmentManager(),
                        Constants.FRAGMENT_DIALOG);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
