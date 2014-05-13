package co.panta.android;

import android.app.Fragment;
import android.util.Log;
import co.panta.android.model.Panta;

public abstract class Fragmento extends Fragment {
	
	protected Panta panta;
	
	public void setMundo(Panta mundo)
	{
		panta = mundo;
		Log.d("panta frag", "SW " + this.getClass().getSimpleName() + " mundo " + panta);
		
	}

}
