package co.panta.android;

import java.util.ArrayList;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import co.panta.android.adapter.ListaAdapter;
import co.panta.android.pojos.Viaje;
import co.panta.android.services.F;

public class BusquedaViajes extends Activity {

	public static final String REALIZAR_BUSQUEDA_VIAJES = "co.panta.android.BusquedaViajes";
	public static final String REALIZAR_BUSQUEDA_QUERY = "BUSQUEDA_QUERY";
	private ArrayList<Viaje> viajes;
	private String query;
	private ListView listaViajes;
	private ListaAdapter adapter;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_busqueda_viajes);
		// Show the Up button in the action bar.
		setupActionBar();

		Intent intent = getIntent();

		viajes = (ArrayList<Viaje>) intent.getSerializableExtra(REALIZAR_BUSQUEDA_VIAJES);
	
		query = (String) intent.getStringExtra(REALIZAR_BUSQUEDA_QUERY);

		Log.d("panta busc", "buscando " + query + " con " + viajes.size());

		if (viajes == null) {
			F.echo(this, "Error al recobrar los viajes");
			NavUtils.navigateUpFromSameTask(this);
		}
		else if (query == null) {
			F.echo(this, "No se obtuvo nada para buscar");
			NavUtils.navigateUpFromSameTask(this);
		}
		
		setTitle("Buscar: " +query);
		
		filtrarViajes();

		listaViajes = (ListView) findViewById(R.id.busqueda_lista_viajes);
		
		adapter = new ListaAdapter(this, viajes);

		listaViajes.setAdapter(adapter);
		listaViajes.setSmoothScrollbarEnabled(true);
		listaViajes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {


				Intent detalleViaje = new Intent(getApplicationContext(), DetalleViajeActivity.class);
				detalleViaje.putExtra(MainActivity.INTENT_DAR_VIAJE, viajes.get(position));

				Log.d("panta lista", "enviando" + viajes.get(position));

				startActivity(detalleViaje);

			}

		});



	}

	private void filtrarViajes() {
		ArrayList<Viaje> nuevo = new ArrayList<Viaje>();
		query = query.toLowerCase();
		
		if(query.startsWith("despues:"))
		{
			int hora = Integer.parseInt(query.split(":")[1]);
			for (int i = 0; i < viajes.size(); i++) {
				
				Viaje viaje = viajes.get(i);
				if(Integer.parseInt(viaje.hora) >= hora)
					nuevo.add(viaje);
				
			}			
		}
		else if(query.startsWith("antes:"))
		{
			int hora = Integer.parseInt(query.split(":")[1]);
			for (int i = 0; i < viajes.size(); i++) {
				
				Viaje viaje = viajes.get(i);
				if(Integer.parseInt(viaje.hora) <= hora)
					nuevo.add(viaje);
				
			}			
		}
		else
		{
			for (int i = 0; i < viajes.size(); i++) {
				
				Viaje viaje = viajes.get(i);
				if(viaje.descripcion.toLowerCase().contains(query))
					nuevo.add(viaje);
				
			}
		}
	
		viajes = nuevo;
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);	

	}

	@Override	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.busqueda_viajes, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
