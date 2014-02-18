package csci567.FragmentExample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;


public class MyStaticFragment extends Fragment implements OnClickListener{
	public static final String TAG = "mystaticfrag";
	
	private Button changefrag;

	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
	setRetainInstance(true);
	View result = inflater.inflate(R.layout.my_fragment, container, false);
	changefrag = (Button) result.findViewById(R.id.button);
	changefrag.setOnClickListener(this);
	
    // Inflate the layout for this fragment
    return result; 
    }

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.button){
			Intent fragmentmanagerintent = new Intent(getActivity(), Fragmentmanageractivity.class);
			getActivity().startActivity(fragmentmanagerintent);
		}
	}
}
