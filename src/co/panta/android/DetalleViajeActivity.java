package co.panta.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import co.panta.android.pojos.Viaje;

public class DetalleViajeActivity extends Activity {

	private Viaje viaje;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_viaje);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Intent intent = getIntent();
		
		viaje = (Viaje) intent.getSerializableExtra(MainPanta.INTENT_DAR_VIAJE);
		
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

	public void echo(String text)
	{
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;

		
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

}
