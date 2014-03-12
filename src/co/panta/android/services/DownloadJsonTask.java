package co.panta.android.services;



import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.AsyncTask;
import android.util.Log;
import co.panta.android.pojos.Usuario;
import co.panta.android.pojos.Viaje;

public class DownloadJsonTask extends AsyncTask<String, Void, ArrayList<Viaje>>{



	private ArrayList<Viaje> array;


	public DownloadJsonTask(ArrayList<Viaje> array) {

		this.array = array;

	}

	//String JsonURL = urls[0];
	///
	public final static String URL_BASICA = "http://wheels.comoj.com/api/trips.php?userId=1";

	/**
	 * Asignación del JSON a variables, el JSON se obtiene de la url de arriba .
	 */
	private static final String TAG_TRIPS = "trips";
	private static final String TAG_ID = "id";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_DATE = "date";
	private static final String TAG_TIME = "time";
	private static final String TAG_SEATS = "seats";
	private static final String TAG_DRIVER = "driver";
	private static final String TAG_DRIVER_ID = "id";
	private static final String TAG_DRIVER_NAME = "name";
	private static final String TAG_DRIVER_SURNAME = "surname";
	private static final String TAG_DRIVER_PICTURE = "picture";
	private static final String TAG_DRIVER_PHONE = "phone";
	// contacts JSONArray
	JSONArray contacts = null;




	/**
	 * Async task class to get json by making HTTP call
	 * */



	protected ArrayList<Viaje> doInBackground(String... args) {
		
		String JsonURL = args[0];
		// Creating service handler class instance
		ServiceHandler sh = new ServiceHandler();
		//Toast.makeText(getApplicationContext(), "Empieza background",Toast.LENGTH_SHORT).show();


		// Making a request to url and getting response
		String jsonStr = sh.makeServiceCall(JsonURL, ServiceHandler.GET);

		Log.d("Response: ", "> " + jsonStr);

		if (jsonStr != null) {
			try {
				JSONObject jsonObj = new JSONObject(jsonStr);

				// Getting JSON Array node
				contacts = jsonObj.getJSONArray(TAG_TRIPS);

				// looping through All Contacts
				for (int i = 0; i < contacts.length(); i++) {
					JSONObject c = contacts.getJSONObject(i);

					int id = c.getInt(TAG_ID);
					String descripcion = c.getString(TAG_DESCRIPTION);
					String date = c.getString(TAG_DATE);
					String time = c.getString(TAG_TIME);
					int seats = c.getInt(TAG_SEATS);

					// Phone node is JSON Object
					JSONObject driver = c.getJSONObject(TAG_DRIVER);
					int driverid = driver.getInt(TAG_DRIVER_ID);
					String name = driver.getString(TAG_DRIVER_NAME);
					String surname = driver.getString(TAG_DRIVER_SURNAME);
					String picture = driver.getString(TAG_DRIVER_PICTURE);
					String mobile = driver.getString(TAG_DRIVER_PHONE);


					Viaje viajePrueba = new Viaje(id, descripcion, date, time, seats);

					Usuario usuarioPrueba = new Usuario(driverid, name,surname, picture, mobile);

					viajePrueba.setDriver(usuarioPrueba);

					array.add(viajePrueba);

					echo("cargó ID" + viajePrueba.id);

				}
				echo("cargados " + array.size() + " viajes");
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
		}

		return null;
	}

	private void echo(String string) {
		Log.i("JSON LOADER", string);

	}

	


}