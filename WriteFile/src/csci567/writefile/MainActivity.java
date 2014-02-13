package csci567.writefile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;



import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button buttonwrite = (Button) findViewById(R.id.button1);
		buttonwrite.setOnClickListener(this);
		Button buttonread = (Button) findViewById(R.id.button2);
		buttonread.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private boolean writeFile(String string){
		try {
			File file = new File(Environment.getExternalStorageDirectory().getPath() +"/test.txt");
			Log.d("FileWrite: ",Environment.getExternalStorageDirectory().getPath() +"/test.txt");
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(string);
			bw.close();
 
			Log.d("FileWrite: ","Done");
 
		} catch (IOException e) {
			Log.d("FileWriteError: ", e.toString());
		}
		return false;
	}
	
	private String readFile(){
		Log.d("FileRead: ","Starting");
		String toReturn = "";
		try {
			File file = new File(Environment.getExternalStorageDirectory().getPath() +"/test.txt");
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				Log.d("FileRead: ","No File");
				return "";
			}
 
			FileReader fr = new FileReader(file.getAbsoluteFile());
			BufferedReader br = new BufferedReader(fr); 
			toReturn = br.readLine();
			br.close();
			Log.d("FileRead: ","Done");
 
		} catch (IOException e) {
			Log.d("FileReadError: ", e.toString());
		}
		return toReturn;
	}

	@Override
	public void onClick(View src) {
		switch (src.getId()) {
	    	case R.id.button1:
	    		EditText txt = (EditText) findViewById(R.id.edittext); 
	        	writeFile(txt.getText().toString());
	        	break;
	    	case R.id.button2:
	    		TextView txt2 = (TextView) findViewById(R.id.textView1);
            	//Replace the text in the textView with the following text.      
            	txt2.setText(readFile());
	        	break;
		}	
		
	}

}
