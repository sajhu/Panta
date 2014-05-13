package co.panta.android;

import co.panta.android.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CommunityFragment extends Fragmento {
	
	public CommunityFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_sobre_panta, container, false);
         
        return rootView;
    }
}
