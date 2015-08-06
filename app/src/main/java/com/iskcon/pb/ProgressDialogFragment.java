package com.iskcon.pb;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;


public class ProgressDialogFragment extends DialogFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        ProgressDialog dialog = new ProgressDialog(getActivity(), getTheme());
        dialog.setTitle("Please Wait...");
        dialog.setMessage("Downloading file");
        dialog.setIndeterminate(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return dialog;
    }
}