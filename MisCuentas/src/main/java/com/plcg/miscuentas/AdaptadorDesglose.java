package com.plcg.miscuentas;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdaptadorDesglose extends ArrayAdapter<Object>{
	Activity context;
	DatoDesglose[] datos;
	
	AdaptadorDesglose(Activity context,DatoDesglose[] dato) {
		super(context,R.layout.listadesglose,dato);
		this.context = context;
		this.datos =dato ;
	}//Fin constructor
	
	public View getView(int position,View convertView,ViewGroup parent){
		//Estas lineas se borran cuando vamos a optimizar la lista
		LayoutInflater inflater = context.getLayoutInflater();
		View item = inflater.inflate(R.layout.listadesglose, null);
		
		//Estas 4 lineas siguientes se borran en la segunda optimizacion para colocar las dos que quedan
		TextView lblDia = (TextView)item.findViewById(R.id.textDiaDesglose);
		lblDia.setText(datos[position].getDia());
		
		TextView lblIngreso = (TextView)item.findViewById(R.id.textIngresoDesglose);
		lblIngreso.setText(datos[position].getIngresos());
		
		TextView lblGasto = (TextView)item.findViewById(R.id.textGastoDesglose);
		double gastosDouble = Double.parseDouble(datos[position].getGastos());
		gastosDouble = Math.rint(gastosDouble*100)/100;//redondeo decimales a solo 2 cifras
		lblGasto.setText(String.valueOf(gastosDouble));

		return (item);
	}//Final getView
}
