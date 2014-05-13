package co.panta.android;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import co.panta.android.services.API;
import co.panta.android.services.F;
import co.panta.android.services.Parameters;
import co.panta.android.services.ServiceHandler;

public class PublicarViajeFragment extends Fragmento {


	private String mDescripcion;

	private String mTiempo;

	private int mDestino;

	private View view;

	private EditText mDescripcionView;

	private TimePicker mPickerHoraView;

	private Switch mToggleDestinoView;

	private String mCupos;

	private TextView mCuposView;

	private Button botonPublicar;


	public PublicarViajeFragment() {}




	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_publicar_viaje, container, false);
		
		
		Log.d("panta pub frag", panta.toString());
		
		mDescripcionView = (EditText) view.findViewById(R.id.form_publicar_descipcion);
		mCuposView = (EditText) view.findViewById(R.id.form_publicar_cupos);
		mPickerHoraView = (TimePicker) view.findViewById(R.id.pickerHoraViaje);
		mToggleDestinoView = (Switch) view.findViewById(R.id.publicar_toggle_destino);

		botonPublicar = (Button) view.findViewById(R.id.boton_enviar_publicar);
		botonPublicar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				publicar();
				
			}
		});
		return view;
	}
	
	public void publicar()
	{
		mDescripcion = mDescripcionView.getText().toString();
		mCupos = mCuposView.getText().toString();
		mTiempo = mPickerHoraView.getCurrentHour() + "" + mPickerHoraView.getCurrentMinute();
		mDestino = (mToggleDestinoView.isChecked())? 1:0;
				
		
		if (TextUtils.isEmpty(mDescripcion)) {
			mDescripcionView.setError(getString(R.string.error_field_required));
			mDescripcionView.requestFocus();
		} 
		else if(TextUtils.isEmpty(mCupos)) {
			mCuposView.setError(getString(R.string.error_field_required));
			mCuposView.requestFocus();
		}
		else if(Integer.parseInt(mCupos) > 6) {
			mCuposView.setError(getString(R.string.error_max_6_seats));
			mCuposView.requestFocus();
		}		
		else
		{
			// Desactivar el botón para evitar doble publicacion
			botonPublicar.setEnabled(false);
			
			Parameters<String> param = new Parameters<String>();

			param.add(API.DESCRIPTION_PARAM, mDescripcion);
			param.add(API.TIME_PARAM, mTiempo);
			param.add(API.DATE_PARAM, API.DATE_TODAY);
			param.add(API.DESTINATION_PARAM, Integer.toString(mDestino));
			param.add(API.TAG_SEATS, mCupos);

			String JsonURL = API.buildURL(API.PUBLISH, panta.usuario, panta.userSecret, param);
			
			new PublishTripTask().execute(JsonURL);		
		}
		

	}


	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class PublishTripTask extends AsyncTask<String, Void, Integer> {
		@Override
		protected Integer doInBackground(String... params) {


			try{
				String JsonURL = params[0];

				Log.i("panta api", JsonURL);
				ServiceHandler sh = new ServiceHandler();


				// Making a request to url and getting response
				String jsonStr = sh.makeServiceCall(JsonURL, ServiceHandler.GET);


				Log.d("Response: ", "> " + jsonStr);

				if (jsonStr != null) {

					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					int responseCode = jsonObj.getInt(API.TAG_RESPONSE);
					Log.i("panta", "PUBLICAR: " + responseCode);

					return Integer.valueOf(responseCode);

				} else {
					Log.e("ServiceHandler", "Couldn't get any data from the url");
					return Integer.valueOf(API.Error.SERVER_ERROR);
				}

			} catch (JSONException e) {
				Log.e("panta", e.getMessage());
				return Integer.valueOf(API.Error.SERVER_ERROR);
			}

		}

		@Override
		protected void onPostExecute(Integer result) {

			int codigo = result.intValue();

			switch (codigo) {
			case API.Error.RESPONSE_OK:
				F.echo(getActivity(), getString(R.string.api_publish_ok));

				break;

			default:
				F.echo(getActivity(), getString(R.string.api_error_publishing) + " ("+ codigo +")");
				break;
			}

			//setError(mPasswordView, R.string.error_incorrect_password);


		}

		@Override
		protected void onCancelled() {

		}
	}


	public void setError(EditText view, int error)
	{
		view.setError(getString(error));
		view.requestFocus();
	}
}
