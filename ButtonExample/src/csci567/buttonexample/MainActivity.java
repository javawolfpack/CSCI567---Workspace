package csci567.buttonexample;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	int count;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		count = 0;
		Button buttontest = (Button) findViewById(R.id.button1);
		buttontest.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View src) {
		switch (src.getId()) {
        	case R.id.button1:
        		count++;
        		TextView txt = (TextView) findViewById(R.id.textView1);
            	//Replace the text in the textView with the following text.      
            	txt.setText("Button Pressed " + count + " times.");
		}		
	}

}
