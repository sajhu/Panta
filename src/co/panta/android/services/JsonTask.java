package co.panta.android.services;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class JsonTask extends AsyncTask<Void, Void, Boolean> {

	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO: attempt authentication against a network service.

		try {
			// Simulate network access.

//			String JsonURL = F.buildLoginURL(mEmail, mPassword);

			ServiceHandler sh = new ServiceHandler();


			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(API.BASE_API_URL, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {

				JSONObject jsonObj = new JSONObject(jsonStr);

				// Getting JSON Array node
				int responseCode = jsonObj.getInt(API.TAG_RESPONSE);

				if(responseCode == API.Error.AUTH_SUCCESS)
				{
					return true;
				}

			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}
			
		} catch (JSONException e) {
			return false;
		}


		// TODO: register the new account here.
		return true;
	}

	@Override
	protected void onPostExecute(final Boolean success) {
	

		if (success) {
			
//			finish();
		} else {
		
		}
	}

	@Override
	protected void onCancelled() {

	}
}