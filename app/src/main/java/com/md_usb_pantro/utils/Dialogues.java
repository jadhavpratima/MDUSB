package com.md_usb_pantro.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by AmrutaP on 12/26/2018.
 */

public class Dialogues {

    Context context;

    public Dialogues(Context context) {
        this.context = context;
    }

    public static ProgressDialog showProgressDialog(Context context, String message) {
        ProgressDialog m_Dialog = new ProgressDialog(context);
        m_Dialog.setMessage(message);
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
        return m_Dialog;
    }

    public static void dismissProgressDialogue(ProgressDialog progressDialog) {
        progressDialog.dismiss();
    }



}
