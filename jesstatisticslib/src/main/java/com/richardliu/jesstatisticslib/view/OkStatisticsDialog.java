package com.richardliu.jesstatisticslib.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.richardliu.jesstatisticslib.R;


/**
 * Created by allen on 2017/9/12.
 */

public class OkStatisticsDialog extends Dialog {

    private Context context;
    private String activity;
    private String id;

    private static OkStatisticsDialog okStatisticsDialog;
    private TextView tvId;
    private EditText etExplain;
    private EditText etAction;

    public synchronized static OkStatisticsDialog getInstance(Context context, String activity, String id) {
        if (okStatisticsDialog == null) {
            okStatisticsDialog = new OkStatisticsDialog(context);
        }
        okStatisticsDialog.init(activity, id);
        return okStatisticsDialog;
    }

    private void init(String activity, String id) {
        this.activity = activity;
        this.id = id;
    }

    private OkStatisticsDialog(Context context) {
        super(context);
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 去掉头
        Window window = getWindow();
        window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        window.setContentView(R.layout.dialog_statistics_des);
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = WindowManager.LayoutParams.ALPHA_CHANGED;
        window.setAttributes(params);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        window.setBackgroundDrawable(dw);
        setCancelable(false);

        tvId = (TextView) findViewById(R.id.tv_id);
        etExplain = (EditText) findViewById(R.id.tv_explain);
        etAction = (EditText) findViewById(R.id.tv_action);
        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        Button btnOk = (Button) findViewById(R.id.btn_ok);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okStatisticsDialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String explain = etExplain.getText().toString().trim();
                String action = etAction.getText().toString().trim();
                Toast.makeText(context, "activity:" + activity + " Id:" + id
                        + " 说明:" + explain + " 动作:" + action, Toast.LENGTH_LONG).show();
                okStatisticsDialog.dismiss();
            }
        });

    }

    @Override
    protected void onStart() {
        tvId.setText(id);
        etExplain.setText("");
        etAction.setText("");
    }
}
