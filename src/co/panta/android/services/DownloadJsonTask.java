package co.panta.android.services;



import java.util.ArrayList;
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
				contacts = jsonObj.getJSONArray(API.TAG_TRIPS);

				// looping through All Contacts
				for (int i = 0; i < contacts.length(); i++) {
					JSONObject c = contacts.getJSONObject(i);

					int id = c.getInt(API.TAG_ID);
					String descripcion = c.getString(API.TAG_DESCRIPTION);
					String date = c.getString(API.TAG_DATE);
					String time = c.getString(API.TAG_TIME);
					int seats = c.getInt(API.TAG_SEATS);

					// Phone node is JSON Object
					JSONObject driver = c.getJSONObject(API.TAG_DRIVER);
					int driverid = driver.getInt(API.TAG_DRIVER_ID);
					String name = driver.getString(API.TAG_DRIVER_NAME);
					String surname = driver.getString(API.TAG_DRIVER_SURNAME);
					String picture = driver.getString(API.TAG_DRIVER_PICTURE);
					String mobile = driver.getString(API.TAG_DRIVER_PHONE);


					Viaje viajePrueba = new Viaje(id, descripcion, date, time, seats);

					Usuario usuarioPrueba = new Usuario(driverid, name,surname, picture, mobile);

					viajePrueba.setDriver(usuarioPrueba);

					array.add(viajePrueba);

					echo("cargó ID" + viajePrueba.id);

				}
				echo("Se acaban de cargar " + array.size() + " viajes");
				
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