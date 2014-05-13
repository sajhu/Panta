package co.panta.android;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import co.panta.android.adapter.ListaAdapter;
import co.panta.android.pojos.Viaje;
import co.panta.android.services.F;

public class ListaViajesFragment extends Fragmento {


	private ArrayAdapter<Viaje> adapter;
	private EditText campoBusqueda;

	public ListaViajesFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_lista_viajes, container, false);


		ListView listaViajes = (ListView) rootView.findViewById(R.id.listaViajes);

		campoBusqueda = (EditText) rootView.findViewById(R.id.form_lista_viajes_filtrar);

		campoBusqueda.setImeActionLabel("Filtrar", KeyEvent.KEYCODE_ENTER);

		
		campoBusqueda.setOnKeyListener(new OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        // If the event is a key-down event on the "enter" button
		        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
		            (keyCode == KeyEvent.KEYCODE_ENTER)) {
		          // Perform action on key press
		          F.echo(getActivity(), "Estamos buscando " + campoBusqueda.getText().toString());
		          
		          buscar();
		          
		          return true;
		        }
		        return false;
		    }
		});
		
		
		adapter = new ListaAdapter(getActivity(), panta.viajes);

		listaViajes.setAdapter(adapter);
		listaViajes.setSmoothScrollbarEnabled(true);
		listaViajes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {


				Intent detalleViaje = new Intent(getActivity(), DetalleViajeActivity.class);
				detalleViaje.putExtra(MainActivity.INTENT_DAR_VIAJE, panta.viajes.get(position));

				Log.d("panta lista", "enviando" + panta.viajes.get(position));

				startActivity(detalleViaje);

			}

		});

		ProgressBar progressBar = new ProgressBar(getActivity());
		progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, Gravity.CENTER));
		progressBar.setIndeterminate(true);
		listaViajes.setEmptyView(progressBar);


		return rootView;
	}

	public void buscar() {
		Intent buscar = new Intent(getActivity(), BusquedaViajes.class);
		
		buscar.putExtra(BusquedaViajes.REALIZAR_BUSQUEDA_VIAJES, panta.viajes);
		buscar.putExtra(BusquedaViajes.REALIZAR_BUSQUEDA_QUERY, campoBusqueda.getText().toString());
		
		startActivity(buscar);
	}

	public void actualizarLista()
	{
		adapter.notifyDataSetChanged();
	}



}
