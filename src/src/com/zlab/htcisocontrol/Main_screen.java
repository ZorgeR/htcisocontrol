package com.zlab.htcisocontrol;

import java.io.DataOutputStream;
import java.io.IOException;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class Main_screen extends Activity {

    TextView t;
    static final int PICK_ISO_REQUEST = 0;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_screen, menu);
        return true;
    }
    
    public void ClickISOchooser(View view) {
        switch (view.getId()) {
        case R.id.button1:
    		//filepickerthread();
        	Intent fileChooserActivity = new Intent(this, FileChooser.class);
        	startActivityForResult(fileChooserActivity, PICK_ISO_REQUEST);
        	//filepickerthread();
        	break;
        }
    }
    
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if (requestCode == PICK_ISO_REQUEST) {
            if (resultCode == RESULT_OK) {
                // A contact was picked.  Here we will just display it
                // to the user.
                String path = data.getStringExtra("path");
                t = (TextView) findViewById(R.id.textView2);
            	t.setText(path);
            	
            	try {
                	Process process = Runtime.getRuntime().exec("su");
        			DataOutputStream os = new DataOutputStream(process.getOutputStream());
        			os.writeBytes("mount -o rw,remount /system\n");
        			os.flush();
        			os.writeBytes("rm /system/etc/PCTOOL.ISO\n");
        			os.flush();
        			os.writeBytes("ln -s " +path +" /system/etc/PCTOOL.ISO\n");
        			os.flush();
        			os.writeBytes("mount -o ro,remount /system\n");
        			os.flush();
        			os.writeBytes("exit\n");
        		    os.flush();
        		    os.close();

            		/*
					Runtime.getRuntime().exec("mount -o rw,remount /system");
					Runtime.getRuntime().exec("rm " +"/system/etc/PCTOOL.ISO");
					Runtime.getRuntime().exec("ln -s " +path +" /system/etc/PCTOOL.ISO");
					Runtime.getRuntime().exec("mount -o ro,remount /system");
					*/
            		
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    }
    }
    }
}
