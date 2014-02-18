package csci567.FragmentExample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.widget.Toast;

import csci567.FragmentExample.MyFragment.GetFragNumber;

public class Fragmentmanageractivity extends FragmentActivity implements GetFragNumber{
	public static final String FIRST_FRAGMENT_TAG = "first";
	public static final String SECOND_FRAGMENT_TAG = "second";
	private Fragment visible;
	private Fragment mVisibleCached;
	private MyFragment myFrag;
	private MyFragment myFragTwo;
	private int currentfragnumber = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_activity);
		setupFragments();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void setupFragments(){
		final FragmentManager fm = getSupportFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();
		myFrag = ((MyFragment) fm.findFragmentByTag(FIRST_FRAGMENT_TAG));
		if (myFrag == null) {
			myFrag = MyFragment.newInstance();
			ft.add(R.id.fragment_container, myFrag, FIRST_FRAGMENT_TAG);
		}
		visible = myFrag;
		ft.commit();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		visible = mVisibleCached;

	}
	
	public void addFragment() {
		currentfragnumber++;
		final FragmentManager fm = getSupportFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();
		ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
		
		
		if(visible.getTag() == FIRST_FRAGMENT_TAG)
		{
			Toast.makeText(this, "FirstFragmentShowing", Toast.LENGTH_LONG).show();
			myFragTwo = ((MyFragment) fm.findFragmentByTag(SECOND_FRAGMENT_TAG));
			if(myFragTwo == null){
				myFragTwo = MyFragment.newInstance();
			}
			ft.replace(R.id.fragment_container,  myFragTwo, SECOND_FRAGMENT_TAG);
			ft.addToBackStack(null);
			visible = myFragTwo;
		}
		else
		{
			Toast.makeText(this, "SecondFragmentShowing", Toast.LENGTH_LONG).show();
			myFrag = ((MyFragment) fm.findFragmentByTag(FIRST_FRAGMENT_TAG));
			if(myFrag == null){
				myFrag = MyFragment.newInstance();
			}
			
			ft.replace(R.id.fragment_container,  myFrag, FIRST_FRAGMENT_TAG);
			ft.addToBackStack(null);
			visible = myFrag;
		}
		ft.commit();
		
	}

	@Override
	public int getData() {
		return currentfragnumber;
	}
}
