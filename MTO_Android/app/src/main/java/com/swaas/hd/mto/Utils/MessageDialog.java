package com.swaas.hd.mto.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.swaas.hd.mto.R;

public class MessageDialog {


    private Context context;
    private TextView content,dialogTitle;
    private Dialog dialog;
    Button updateButton;
    private Button okButton,laterButton;

    Dialog dialogAR;

    public MessageDialog(Context context) {
        this.context = context;
        initDialog();
    }
    private void initDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.message_dialog);
        content = (TextView) dialog.findViewById(R.id.text_dialog);
        okButton = (Button) dialog.findViewById(R.id.ok_button);
        laterButton = (Button) dialog.findViewById(R.id.later_button);
        dialogTitle = (TextView) dialog.findViewById(R.id.title);
    }



    public void showDialog(final Context context, String message, View.OnClickListener okClickListener, boolean isCancelable){
        dialog.setCancelable(isCancelable);
        // set message content
        content.setText(message);
        laterButton.setVisibility(View.GONE);
        okButton.setText("OK");
        // set if any listener
        if(okClickListener != null) {
            okButton.setOnClickListener(okClickListener);
        } else {
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        if(!isCancelable) {
            dialog.setOnKeyListener(new Dialog.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                    }
                    return false;
                }
            });
        }
        if (! ((Activity) context).isFinishing()) {
            dialog.show();
        }
    }

}
