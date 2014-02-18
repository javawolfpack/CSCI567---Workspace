package csci567.FragmentExample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class MyFragment extends Fragment implements OnClickListener{
	public static final String TAG = "myfrag";
	private GetFragNumber gfn;
	private Button changefrag;
	private TextView text;
	private int fragnumber = 0;
	private Fragmentmanageractivity fragmanager = null;
	
	static MyFragment newInstance() {
		MyFragment frag = new MyFragment();
		return frag;
	}
	
	public interface GetFragNumber {
		public int getData();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			gfn = (GetFragNumber) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement GetFragNumber");
		}
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
	setRetainInstance(true);
	View result = inflater.inflate(R.layout.my_fragment, container, false);
	fragnumber = gfn.getData();
	changefrag = (Button) result.findViewById(R.id.button);
	changefrag.setOnClickListener(this);

	text = (TextView) result.findViewById(R.id.text);
	text.setText(Integer.toString(fragnumber));
    // Inflate the layout for this fragment
    return result; 
    }

	@Override
	public void onClick(View v) {
		fragmanager = (Fragmentmanageractivity) getActivity();
		int id = v.getId();
		if (id == R.id.button){
			if(fragmanager != null)
			fragmanager.addFragment();
		}
	}
}
