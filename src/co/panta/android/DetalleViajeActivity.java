package co.panta.android;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.ocpsoft.prettytime.PrettyTime;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import co.panta.android.pojos.Viaje;
import co.panta.android.services.DownloadImageTask;

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
		
		viaje = (Viaje) intent.getSerializableExtra(MainPanta.INTENT_DAR_VIAJE);
		
		cargarImagen(R.id.detalle_usuario_foto, viaje.conductor.foto);

		
		poblarTexto(R.id.detalle_descripcion_viaje, viaje.descripcion);
		
		poblarTexto(R.id.detalle_conductor_nombre, viaje.conductor.toString());
//		
//		Button boton_llamar = (Button) findViewById(R.id.boton_detalle_llamar);
//		Button boton_sms = (Button) findViewById(R.id.boton_detalle_sms);
		

		
		poblarTexto(R.id.detalle_viaje_hora, tiempoRelativo(viaje.fecha, viaje.hora));

		
		
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
//	        Intent intentSettings = new Intent(this, SettingsActivity.class);
//	        this.startActivity(intentSettings);
	    	echo("Compartiendo ...");
	        break;	
	        
	    case R.id.action_favorite:
//	        Intent intentSettings = new Intent(this, SettingsActivity.class);
//	        this.startActivity(intentSettings);
	    	guardarFavorito();
	        break;	
	        
	    case R.id.action_settings:
	        Intent intentSettings = new Intent(this, SettingsActivity.class);
	        this.startActivity(intentSettings);
	        break;	
	        
		case android.R.id.home:

			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void guardarFavorito() {
		
		
		echo("Guardado ...");
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
		new DownloadImageTask((ImageView) findViewById(id)).execute(url);
	}

	@SuppressLint("SimpleDateFormat")
	public String tiempoRelativo(String fecha, String hora)
	{		
		

		String fechaViaje = fecha + " " + hora;
		
		DateFormat conHoras = new SimpleDateFormat("yyyy-MM-dd HHmm");

		try {
			
			Date fechaCreada = conHoras.parse(fechaViaje);
			
			
			PrettyTime p = new PrettyTime();
			
			poblarTexto(R.id.detalle_viaje_hora, p.format(fechaCreada));
			
			return p.format(fechaCreada);

			
		} catch (ParseException e) {
			return hora + " (" + fecha + ")";
		}
	}
	
}
