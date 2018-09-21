package com.cczhr.serialportdemo;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cczhr.androidserialport.ByteUtil;
import com.cczhr.androidserialport.OnDataReceiverListener;

import java.util.ArrayList;
import java.util.List;

import android_serialport_api.ComControl;

public class MainActivity extends AppCompatActivity {
    private Spinner spDrivers;
    private Spinner spBaudrate;
    private EditText edData;
    private Button btnOpenCom;
    private Button btnSendCom;
    private Button btnClearMessage;
    private TextView tvMessage;
    private String[] mDevices;
    private ScrollView sv;


    private List<Integer> mBaudrates = new ArrayList<>();

    private ArrayAdapter<String> mdeviceAdapter;
    private ArrayAdapter<Integer> mbaudratesAdapter;
    private ComControl comControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spDrivers = findViewById(R.id.sp_drivers);
        spBaudrate = findViewById(R.id.sp_baudrate);
        edData = findViewById(R.id.ed_data);
        btnOpenCom = findViewById(R.id.btn_open_com);
        btnSendCom = findViewById(R.id.btn_send_com);
        btnClearMessage = findViewById(R.id.btn_clear_message);
        tvMessage = findViewById(R.id.tv_message);
        sv = findViewById(R.id.sv);
        initView();
    }

    private void initView() {
        mBaudrates.clear();
        mDevices = ComControl.getDeviceList();
        if (mDevices.length == 0) {
            mDevices = new String[]{
                    "无设备"
            };
        }
        // 波特率
        for (int b : ComControl.getBaudrateList()) {
            mBaudrates.add(b);
        }


        mdeviceAdapter = new ArrayAdapter<String>(this, R.layout.spinner_default_item, mDevices);
        mdeviceAdapter.setDropDownViewResource(R.layout.spinner_item);
        mbaudratesAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner_default_item, mBaudrates);
        mdeviceAdapter.setDropDownViewResource(R.layout.spinner_item);
        spDrivers.setAdapter(mdeviceAdapter);
        spBaudrate.setAdapter(mbaudratesAdapter);
        mbaudratesAdapter.notifyDataSetChanged();
        mdeviceAdapter.notifyDataSetChanged();

        btnOpenCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnOpenCom.getText().toString().equals("打开串口")) {
                    if (spDrivers.getSelectedItem().toString().equals("无设备") || comControl != null)
                        return;
                    openCom();
                } else {
                    if (comControl == null)
                        return;
                    comControl.closeCOM();
                    comControl = null;
                    spBaudrate.setEnabled(true);
                    spDrivers.setEnabled(true);
                    btnOpenCom.setText("打开串口");
                }

            }
        });
        btnSendCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (comControl == null)
                    return;
                comControl.sendData(ByteUtil.hexStringToBytes(edData.getText().toString()));
            }
        });
        btnClearMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvMessage.setText("");
            }
        });


    }

    private void openCom() {
        comControl = new ComControl(spDrivers.getSelectedItem().toString(), (int) spBaudrate.getSelectedItem());
        comControl.setOnDataReceiverListener(new OnDataReceiverListener() {
            @Override
            public void onDataReceiver(final byte[] buffer, final int size) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvMessage.append("收到数据"+ByteUtil.bytes2HexStr(buffer,0,size)+"\n");
                        sv.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });

            }
        });
        if (comControl.openCOM()) {
            Toast.makeText(getApplicationContext(), "打开串口设备成功", Toast.LENGTH_SHORT).show();
            btnOpenCom.setText("关闭串口");
            spBaudrate.setEnabled(false);
            spDrivers.setEnabled(false);
        } else {
            Toast.makeText(getApplicationContext(), "打开串口设备失败", Toast.LENGTH_SHORT).show();
        }
    }


}
