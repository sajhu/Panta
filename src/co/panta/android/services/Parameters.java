package co.panta.android.services;

import java.util.ArrayList;

@SuppressWarnings("hiding")
public class Parameters<String> extends ArrayList<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void add(String key, String value) {
		super.add((String) (key + "=" + value));
	}

}
