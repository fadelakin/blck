package com.fisheradelakin.blck;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

/**
 * Created by temidayo on 2/7/16.
 */
public class StorageConfirmationDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Fragment parent = getParentFragment();
        final Activity activity = getActivity();
        return new AlertDialog.Builder(getActivity())
                .setMessage(R.string.request_permission)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(activity, new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_STORAGE_PERMISSION);
                            }
                        }

                )
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Activity activity = parent.getActivity();
                                if (activity != null) {
                                    activity.finish();
                                }
                            }
                        }

                )
                .create();
    }
}
