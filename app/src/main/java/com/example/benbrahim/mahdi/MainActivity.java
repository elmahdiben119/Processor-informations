package com.example.benbrahim.mahdi;

import android.Manifest;
import android.app.VoiceInteractor;
import android.nfc.Tag;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    Button Exit,Afficher;
    TextView textView ;
    TextView tv;
    ProcessBuilder processBuilder;
    String Holder = "";
    String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
    InputStream inputStream;
    Process process ;
    byte[] byteArry ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);
        Exit=(Button)findViewById(R.id.btnExit);
        Afficher=(Button)findViewById(R.id.btnAfficher);
        byteArry = new byte[1024];
        Afficher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    processBuilder = new ProcessBuilder(DATA);

                    process = processBuilder.start();

                    inputStream = process.getInputStream();

                    while(inputStream.read(byteArry) != -1){

                        Holder = Holder + new String(byteArry);
                    }

                    inputStream.close();

                } catch(IOException ex){

                    ex.printStackTrace();
                }

                textView.setText(Holder);
            }
        });
       Exit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(getApplicationContext(),"Application Distroyed",Toast.LENGTH_LONG);
               finish();
               System.exit(0);
           }
       });
    }
    public void getCpuInfo() {
        try {
            Process proc = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            InputStream is = proc.getInputStream();
            tv= (TextView)findViewById(R.id.tvcmd);
            tv.setText(getStringFromInputStream(is));
        }
        catch (IOException e) {
           // Log.e( "------ getCpuInfo " + e.getMessage());
        }
    }

    public void getMemoryInfo() {
        try {
            Process proc = Runtime.getRuntime().exec("cat /proc/meminfo");
            InputStream is = proc.getInputStream();
            tv = (TextView)findViewById(R.id.tvcmd);
            tv.setText(getStringFromInputStream(is));
        }
        catch (IOException e) {
           // Log.e(TAG, "------ getMemoryInfo " + e.getMessage());
        }
    }

    private static String getStringFromInputStream(InputStream is) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;

        try {
            while((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        }
        catch (IOException e) {
           // Log.e(TAG, "------ getStringFromInputStream " + e.getMessage());
        }
        finally {
            if(br != null) {
                try {
                    br.close();
                }
                catch (IOException e) {
                    //Log.e(TAG, "------ getStringFromInputStream " + e.getMessage());
                }
            }
        }

        return sb.toString();
    }
}
