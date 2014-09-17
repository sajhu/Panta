package co.panta.android.services;



import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import co.panta.android.MainActivity;
import co.panta.android.pojos.Usuario;
import co.panta.android.pojos.Viaje;

public class DownloadJsonTask extends AsyncTask<String, Void, Integer>{



	private ArrayList<Viaje> array;
	private MainActivity caller;


	public DownloadJsonTask(MainActivity caller, ArrayList<Viaje> array) {
		this.caller = caller;
		this.array = array;

	}

	//String JsonURL = urls[0];
	///


	// contacts JSONArray
	JSONArray contacts = null;




	/**
	 * Async task class to get json by making HTTP call
	 * */



	protected Integer doInBackground(String... args) {

		String JsonURL = args[0];
		// Creating service handler class instance
		ServiceHandler sh = new ServiceHandler();
		//Toast.makeText(getApplicationContext(), "Empieza background",Toast.LENGTH_SHORT).show();


		// Making a request to url and getting response
		String jsonStr = sh.makeServiceCall(JsonURL, ServiceHandler.GET);

		Log.d("Viajes: ", "> " + jsonStr);

		if (jsonStr != null) {
			try {
				JSONObject jsonObj = new JSONObject(jsonStr);

				// Getting JSON Array node

				int response = jsonObj.getInt(API.TAG_RESPONSE);


				Log.i("panta", "Código Respuesta: " + response);

				if(response != 0)
				{
					Log.e("panta", "No se retornaron viajes (" + response + ": "+ jsonObj.getString(API.TAG_DESCRIPTION) +")");
					return response;
				}

				else {
					contacts = jsonObj.getJSONArray(API.TAG_TRIPS);

					// looping through All Contacts
					for (int i = 0; i < contacts.length(); i++) {
						JSONObject c = contacts.getJSONObject(i);

						int id = c.getInt(API.TAG_ID);
						String descripcion = c.getString(API.TAG_DESCRIPTION);
						String date = c.getString(API.TAG_DATE);
						String time = c.getString(API.TAG_TIME);
						String lat = c.getString(API.TAG_LATITUD);
						String lon = c.getString(API.TAG_LONGITUD);
						int seats = c.getInt(API.TAG_SEATS);
						int destiny = c.getInt(API.TAG_DESTINO);

						// Phone node is JSON Object
						JSONObject driver = c.getJSONObject(API.TAG_DRIVER);
						int driverid = driver.getInt(API.TAG_DRIVER_ID);
						String name = driver.getString(API.TAG_DRIVER_NAME);
						String surname = driver.getString(API.TAG_DRIVER_SURNAME);
						String picture = driver.getString(API.TAG_DRIVER_PICTURE);
						String mobile = driver.getString(API.TAG_DRIVER_PHONE);


						Viaje viajePrueba = new Viaje(id, descripcion, date, time, seats, destiny);
						viajePrueba.setLatLng(lat, lon);
						
						
						Usuario usuarioPrueba = new Usuario(driverid, name,surname, picture, mobile);

						viajePrueba.setDriver(usuarioPrueba);

						array.add(viajePrueba);

						echo("cargó ID" + viajePrueba.id);

					}
					echo("Se acaban de cargar " + array.size() + " viajes");
					return API.Error.RESPONSE_OK;
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
		}

		return 1;
	}

	public void onPostExecute(Integer resultado){
		
		int code = resultado.intValue();
		
		if(code == API.Error.RESPONSE_OK)
			caller.actualizarListaViajes();
		else
			caller.ventana("Error " + code);
	}

	private void echo(String string) {
		Log.i("JSON LOADER", string);

	}




}