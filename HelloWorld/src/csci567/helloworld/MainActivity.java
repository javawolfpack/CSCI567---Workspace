package csci567.helloworld;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //Initialize TextView Object to the textview we provided in our layout xml referencing the id we gave it
    	TextView txt = (TextView) findViewById(R.id.textView1);
    	//Replace the text in the textView with the following text.      
    	txt.setText("CSCI567 Hello World");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
