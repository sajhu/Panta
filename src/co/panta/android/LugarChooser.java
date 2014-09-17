package co.panta.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LugarChooser extends Activity {

	private GoogleMap mapa;
	private MapView mapView;
	private double latitud;
	private double longitud;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lugar_chooser);
		
		mapView = (MapView) findViewById(R.id.mapa);
		
		mapa = mapView.getMap();

		

	    mapView.onCreate(savedInstanceState);
	    mapView.onResume(); //without this, map showed but was empty 

	    // Gets to GoogleMap from the MapView and does initialization stuff
	   

	    MapsInitializer.initialize(this);
	    setUpMapIfNeeded();
		
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_enviar_ubicacion:
			aceptarUbicacion();
			return true;
//		case R.id.action_logout:
//			cerrarSesion();
//			return true;
//		case R.id.action_filtrar:
//			F.echo(this, "Hay " + panta.viajes.size() + " viajes");
//			return true;
			

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	public void aceptarUbicacion()
	{
		Intent devolverResultado = new Intent();
		devolverResultado.putExtra(PublicarViajeFragment.LATITUD, "" + latitud);
		devolverResultado.putExtra(PublicarViajeFragment.LONGITUD, "" + longitud);
		
		setResult(PublicarViajeFragment.ESCOGER_UBICACION_OK, devolverResultado);				
		
		mapa.setMyLocationEnabled(false);

		finish();
	}

	private boolean setUpMapIfNeeded() {
	    // Do a null check to confirm that we have not already instantiated the map.
	    if (mapa == null) {
	    	mapa = mapView.getMap();
	        // Check if we were successful in obtaining the map.
	        if (mapa != null) 
	        {
	        	mapa.getUiSettings().setMyLocationButtonEnabled(true);
	    	    mapa.setMyLocationEnabled(true);
	    	    mapa.setBuildingsEnabled(true);
	    	    mapa.setOnMapClickListener(new OnMapClickListener() {
					
					
					public void onMapClick(LatLng click) {
						
						mapa.clear();
						latitud = click.latitude;
						longitud = click.longitude;
						MarkerOptions marker = new MarkerOptions();
						marker.draggable(false);
						marker.position(click);
						
						mapa.addMarker(marker);
						
					}
				});
	    	    
	    	    // Updates the location and zoom of the MapView
	    	    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(4.602904,-74.065236), 16);
	    	    mapa.animateCamera(cameraUpdate);
	        	return true;
	        }
	        else
	        	return false;
	    }
	    return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lugar_chooser, menu);
		return true;
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		mapa.setMyLocationEnabled(false);
		
		
	}

}
