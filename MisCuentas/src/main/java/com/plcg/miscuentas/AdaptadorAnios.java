package com.plcg.miscuentas;

import java.util.Locale;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

//se llama a esta clase desde la clase circuitos, la clase coches y la clase persona
public class AdaptadorAnios extends ArrayAdapter<Object>{
	String[] meses={"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre",
			"Octubre","Noviembre","Diciembre"};
	String[] mesesIngles={"January","February","March","April","May","June","July","August","September",
			"October","November","December"};
	Activity context;
	String[] datos;
	
	AdaptadorAnios(Activity context,String[] dato) {
		super(context,R.layout.listaanios,dato);
		this.context = context;
		this.datos =dato ;
	}//Fin constructor
	
	public View getView(int position,View convertView,ViewGroup parent){
		//Estas lineas se borran cuando vamos a optimizar la lista
		LayoutInflater inflater = context.getLayoutInflater();
		View item = inflater.inflate(R.layout.listaanios, null);
		
		//Estas 4 lineas siguientes se borran en la segunda optimizacion para colocar las dos que quedan
		TextView lblMes = (TextView)item.findViewById(R.id.textAnioListaAnios);
		lblMes.setText(datos[position]);
		
		//texto info anio
		TextView infoAnio = (TextView)item.findViewById(R.id.textInfoAnio);
		
		DBAdapterMisCuentas dbMC = new DBAdapterMisCuentas(context);
		dbMC.open();
		int desde;
		desde = dbMC.devuelveMesDeInicioDadoUnAnio(datos[position]); 
		int hasta;
		hasta = dbMC.devuelveMesFinalDadoUnAnio(datos[position]);
		dbMC.close();
		infoAnio.setText(""+meses[desde-1]+"--"+meses[hasta-1]);
		
		String idioma = Locale.getDefault().getDisplayLanguage();
		if (idioma.equalsIgnoreCase("English")){
			infoAnio.setText(""+mesesIngles[desde-1]+"--"+mesesIngles[hasta-1]);
		}
		
		return (item);
	}//Final getView
}//final clase 
