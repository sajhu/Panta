package co.panta.android;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.ocpsoft.prettytime.PrettyTime;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import co.panta.android.pojos.Viaje;
import co.panta.android.services.DownloadImageTask;
import co.panta.android.services.F;

@SuppressLint("SimpleDateFormat")
public class DetalleViajeActivity extends Activity  {

	private Viaje viaje;

	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_viaje);
		// Show the Up button in the action bar.
		setupActionBar();

		Intent intent = getIntent();

		viaje = (Viaje) intent.getSerializableExtra(MainActivity.INTENT_DAR_VIAJE);

		if(viaje == null)
		{
			poblarTexto(R.id.detalle_descripcion_viaje, "No hay viajes disponibles en este momento.");

		}
		else
		{
			cargarImagen(R.id.detalle_usuario_foto, viaje.conductor.foto);
			cargarImagen(R.id.detalle_ubicacion_foto, urlUbicacion(viaje), 1200, 600);


			poblarTexto(R.id.detalle_descripcion_viaje, viaje.descripcion);

			poblarTexto(R.id.detalle_conductor_nombre, viaje.conductor.toString());

			String destino = (viaje.destino == 1)?"Uniandes":"la ciudad";
			
			poblarTexto(R.id.detalle_viaje_cupos, viaje.sillas + " cupos hacia " +destino);

			Button boton_llamar = (Button) findViewById(R.id.boton_detalle_llamar);
			Button boton_sms = (Button) findViewById(R.id.boton_detalle_sms);



			poblarTexto(R.id.detalle_viaje_hora, horaEstandar(viaje.hora));
			poblarTexto(R.id.detalle_hora_contextual, tiempoRelativo(viaje.fecha, viaje.hora));
			


		}

	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		hideKeyboard(this);
	}
	
	public static void hideKeyboard(Activity activity) {
	    View v = activity.getWindow().getCurrentFocus();
	    if (v != null) {
	        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
	        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	    }
	}
	
	private String urlUbicacion(Viaje viaje) {
		if(viaje.latitud == null || viaje.longitud == null || viaje.latitud.equals("0"))
			return "http://maps.googleapis.com/maps/api/staticmap?center=4.602904,-74.065236&zoom=18&size=1000x500&markers=color:blue|4.602904,-74.065236&sensor=false";
		else
			return "http://maps.googleapis.com/maps/api/staticmap?center="+viaje.latitud+","+viaje.longitud+"&zoom=18&size=2000x500&markers=color:blue|"+viaje.latitud+","+viaje.longitud+"&sensor=false";
	}

	private void poblarTexto(int id, String texto) {

		TextView descripcion = (TextView) findViewById(id);

		descripcion.setText(texto);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detalle_viaje, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.action_share:
			echo("Compartiendo ...");
			break;	

		case R.id.action_favorite:
			guardarFavorito();
			break;	

		case R.id.action_block_user:
			bloquearUsuario();
			break;	        
		case android.R.id.home:

			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void bloquearUsuario() {
		Intent bloquear = new Intent(this, MainActivity.class);
		
		bloquear.putExtra(MainActivity.INTENT_BLOQUEAR_USUARIO, viaje.conductor);
		
		startActivity(bloquear);
	}

	private void guardarFavorito() {


		F.echo(this, "Guardado ...");
	}


	public void sms(View view)
	{
		Uri smsUri = Uri.parse("sms:"+viaje.conductor.telefono);
		Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
		intent.putExtra("sms_body", getString(R.string.sms_question));
		startActivity(intent);

	}

	public void llamar(View view)
	{
		Intent callIntent = new Intent(Intent.ACTION_CALL);          
		callIntent.setData(Uri.parse("tel:"+viaje.conductor.telefono));          
		startActivity(callIntent);  
	}

	public void echo(String text)
	{
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;


		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}


	public void cargarImagen(int id, String url)
	{
		new DownloadImageTask((ImageView) findViewById(id), true, 200).execute(url);
	}
	
	public void cargarImagen(int id, String url, int ancho, int alto)
	{
		new DownloadImageTask((ImageView) findViewById(id), false, ancho, alto).execute(url);
	}

	public String horaEstandar(String hora)
	{
		DateFormat conHoras = new SimpleDateFormat("HHmm");
		DateFormat laHora = new SimpleDateFormat("h:mm");
		
		try {
			Date fechaCreada = conHoras.parse(hora);
			return laHora.format(fechaCreada);

		} catch (ParseException e) {
			return hora;
		}
		
		
	}
	
	@SuppressLint("SimpleDateFormat")
	public String tiempoRelativo(String fecha, String hora)
	{

		String fechaViaje = fecha + " " + hora;

		DateFormat conHoras = new SimpleDateFormat("yyyy-MM-dd HHmm");

		try {

			Date fechaCreada = conHoras.parse(fechaViaje);

			PrettyTime p = new PrettyTime(new Locale("es"));

			return p.format(fechaCreada);
			
		} catch (ParseException e) {
			return "";
		}
	}

}
