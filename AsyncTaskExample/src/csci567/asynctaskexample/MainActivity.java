package csci567.asynctaskexample;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * @author Prabu
 * AsyncTask example
 *
 */
public class MainActivity extends Activity {
	private Button button;
	private EditText time;
	private TextView finalResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		time = (EditText) findViewById(R.id.et_time);
		button = (Button) findViewById(R.id.btn_do_it);
		finalResult = (TextView) findViewById(R.id.tv_result);
		//In-line OnClick Listener
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AsyncTaskRunner runner = new AsyncTaskRunner();
				String sleepTime = time.getText().toString();
				runner.execute(sleepTime);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * @author Prabu
	 * Private class which runs the long operation. ( Sleeping for some time )
	 * AsyncTask<Params, Progress, Result>
	 */
	private class AsyncTaskRunner extends AsyncTask<String, String, String> {

		private String resp;

		@Override
		protected String doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			try {
				// Do your long operations here and return the result
				int time = Integer.parseInt(params[0]);    
				// Sleeping for given time period
				Thread.sleep(time);
				resp = "Slept for " + time + " milliseconds";
			} catch (InterruptedException e) {
				e.printStackTrace();
				resp = e.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
				resp = e.getMessage();
			}
			return resp;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result) {
			// execution of result of Long time consuming operation
			finalResult.setText(result);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// Things to be done before execution of long running operation. For
			// example showing ProgessDialog
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		@Override
		protected void onProgressUpdate(String... text) {
			finalResult.setText(text[0]);
			// Things to be done while execution of long running operation is in
			// progress. For example updating ProgessDialog
		}
	}
}