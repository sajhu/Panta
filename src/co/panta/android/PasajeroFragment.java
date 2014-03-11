package co.panta.android;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PasajeroFragment extends Fragment {
	
	public static final String ARG_SECTION_NUMBER = "section_number";

	public PasajeroFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_viajes, container, false);
		TextView dummyTextView = (TextView) rootView
				.findViewById(R.id.section_label);
		dummyTextView.setText("Sajhu was here " + Integer.toString(getArguments().getInt(
				ARG_SECTION_NUMBER)));
		return rootView;
	}
	

}


