package com.plcg.miscuentas;

import java.util.Locale;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdaptadorMeses extends ArrayAdapter<Object>{
	Activity context;
	DatoAnioMesGasto[] datos;
	String[] meses={"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre",
			"Octubre","Noviembre","Diciembre"};
	String[] mesesIngles={"January","February","March","April","May","June","July","August","September",
			"October","November","December"};
	
	AdaptadorMeses(Activity context,DatoAnioMesGasto[] dato) {
		super(context,R.layout.listameses,dato);
		this.context = context;
		this.datos =dato ;
	}//Fin constructor
	
	public View getView(int position,View convertView,ViewGroup parent){
		//Estas lineas se borran cuando vamos a optimizar la lista
		LayoutInflater inflater = context.getLayoutInflater();
		View item = inflater.inflate(R.layout.listameses, null);
		
		TextView lblMes = (TextView)item.findViewById(R.id.textMesListaMeses);
		TextView lblAnio = (TextView)item.findViewById(R.id.textAnioListaMeses);
		TextView lblGasto = (TextView)item.findViewById(R.id.textGastosListaMeses);
		TextView lblIngreso = (TextView)item.findViewById(R.id.textIngresosListaMeses);

		lblMes.setText(meses[Integer.parseInt(datos[position].getMes())-1]);
		String idioma = Locale.getDefault().getDisplayLanguage();
		if (idioma.equalsIgnoreCase("English")){
			lblMes.setText(mesesIngles[Integer.parseInt(datos[position].getMes())-1]);
		}
		lblAnio.setText(datos[position].getAnio());
		
		double ingreso = Double.parseDouble(datos[position].getIngreso());
		ingreso = Math.rint(ingreso*100)/100;
		
		double gasto = Double.parseDouble(datos[position].getGasto());
		gasto = Math.rint(gasto*100/100);
		
		if((ingreso - gasto)>0){
			lblMes.setTextColor(Color.rgb(0x00, 0x72, 0x00));
		}else if((ingreso - gasto)<0){
			lblMes.setTextColor(Color.rgb(0xab, 0x00, 0x00));
		}else if ((ingreso - gasto) == 0){
			lblMes.setTextColor(Color.rgb(0x00, 0x00, 0x00));
		}
		
		
		lblGasto.setText("Gasto: "+gasto);
		lblIngreso.setText("Ingreso: "+ingreso);
		
		if (idioma.equalsIgnoreCase("English")){
			lblGasto.setText("Spending: "+gasto);
			lblIngreso.setText("Entry: "+ingreso);
		}

		return (item);
	}//Final getView
}