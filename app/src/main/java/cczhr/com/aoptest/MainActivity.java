package cczhr.com.aoptest;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;

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
