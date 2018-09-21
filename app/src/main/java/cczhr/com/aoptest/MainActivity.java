package cczhr.com.aoptest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.cczhr.androidserialport.OnDataReceiverListener;

import android_serialport_api.ComControl;

public class MainActivity extends AppCompatActivity {
    private Button btn1;
    private Button btn2;
    private int data=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     /*   btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("cczhr-----", "1");
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("cczhr-----", "2");

                getMessage("");
            }
        });*/
        testAOP("kkkkkkkkkkkk");
        getMessage("444");
        for(String com:ComControl.getDeviceList()){
            Log.e("串口", com );
        }

        ComControl.setsSuPath("/system/xbin/su");
        ComControl comControl=new ComControl("/dev/ttySAC3",9600);

        if(comControl.openCOM()){
            //打开串口成功

        }else {
            //打开串口失败
        }

        comControl.setmSleep(100);
        comControl.setOnDataReceiverListener(new OnDataReceiverListener() {
            @Override
            public void onDataReceiver(byte[] buffer, int size) {
                //收到的数据
            }
        });
        byte[] bytes={0x00};
        comControl.sendData(bytes);



    }


    @DebugTool(resourceId="jj")
    public void testAOP(String s) {


        Log.d("cczhr-----", "哈哈哈哈哈哈哈");
    }

    public void getMessage(String s){
        s="";
        Log.e("cczhr-----", "getMessage: "+s );
    }


}
