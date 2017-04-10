package com.mantas.visualrecognition.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.mantas.visualrecognition.Enums.ScanningMethod;
import com.mantas.visualrecognition.Settings.MainSettings;

/**
 * Created by manta on 4/10/2017.
 */

public class SettingsDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final CharSequence[] items = {"Start", "Stop", "Haar", "Template"};

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    MainSettings.scanning = true;
                } else if (which == 1){
                    MainSettings.scanning = false;
                } else if (which == 2) {
                    MainSettings.method = ScanningMethod.HAAR;
                } else if (which == 3) {
                    MainSettings.method = ScanningMethod.TEMPLATE;
                }

                dialog.dismiss();
            }
        });

        return builder.create();
    }
}
