package com.msi.getwebpage;

import android.app.Activity;
import android.os.Bundle;
// used for interacting with user interface
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;
// used for passing data 
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
// used for connectivity
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.telephony.gsm.*;

public class GetWebPage extends Activity {
    /** Called when the activity is first created. */

    Handler h;
    String resultado;

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final EditText eText = (EditText) findViewById(R.id.address);
        final TextView tView = (TextView) findViewById(R.id.pagetext);
        
        this.h = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // process incoming messages here
                switch (msg.what) {
                    case 0:
                    	//tView.append((String) msg.obj);
                    	resultado=(String) msg.obj;
                    	break;
                }
                super.handleMessage(msg);
            }
        };
        
        final Button button = (Button) findViewById(R.id.ButtonGo);
        button.setOnClickListener(new Button.OnClickListener() {
        
        public void onClick(View v) {
        	tView.setText("");
        	new DownloadWebpageTask().execute(eText.getText().toString());
        	
            }
        });
        
        
        
        
        
        
    }
	
	
	
	private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
              
            
            try {     		
	        	URL url = new URL(urls[0]);
	            URLConnection conn = url.openConnection();
	            // Get the response
	            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            String line = "";
	            while ((line = rd.readLine()) != null) {
	        		Message lmsg;
	                lmsg = new Message();
	                if ( line.substring(0, 2) == "<td"  ){
	                	lmsg.obj = line;
	                    lmsg.what = 0;
	                    GetWebPage.this.h.sendMessage(lmsg);
	                }
	                
	            }
	            SmsManager sm = SmsManager.getDefault();
	            //sm.sendDataMessage("test",null,(short)5556,"hello there".getBytes(),null,null);
	            sm.sendTextMessage("test",null,"hello there",null,null);
	    	
            } catch (IOException e) {
            	System.out.print( "Unable to retrieve web page. URL may be invalid.");
            }
            return "Success";
            
        }
        
    }
	
	
}
