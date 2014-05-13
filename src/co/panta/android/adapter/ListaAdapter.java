package co.panta.android.adapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.ocpsoft.prettytime.PrettyTime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.panta.android.R;
import co.panta.android.pojos.Viaje;
import co.panta.android.services.DownloadImageTask;

@SuppressLint("SimpleDateFormat")
public class ListaAdapter extends ArrayAdapter<Viaje> {

	private Context context;
	private List<Viaje> viajes;


	public ListaAdapter(Context context, List<Viaje> objects) {

		super(context, R.layout.fila_viaje, objects);
		this.context = context;
		this.viajes = objects;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.fila_viaje, parent, false);
		
		Viaje viaje = viajes.get(position);
		
		TextView descripcionView = (TextView) rowView.findViewById(R.id.lista_viajes_descripcion);
		TextView horaView = (TextView) rowView.findViewById(R.id.lista_viajes_hora);
		TextView horaContextualView = (TextView) rowView.findViewById(R.id.lista_viajes_hora_contextual);
		ImageView fotoView = (ImageView) rowView.findViewById(R.id.lista_viajes_foto);
		
		
		descripcionView.setText(viaje.descripcion);
		horaView.setText(horaEstandar(viaje.hora));
		horaContextualView.setText(tiempoRelativo(viaje.fecha, viaje.hora));

		
		new DownloadImageTask((ImageView) fotoView, true, 120).execute(viaje.conductor.foto);

		return rowView;
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
