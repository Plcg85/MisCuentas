package com.plcg.miscuentas;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdaptadorGastosHoy extends ArrayAdapter<Object>{
	Activity context;
	DatoGastoHoy[] datos;
	
	AdaptadorGastosHoy(Activity context,DatoGastoHoy[] dato) {
		super(context,R.layout.lista_un_gasto,dato);
		this.context = context;
		this.datos =dato ;
	}//Fin constructor
	
	public View getView(int position,View convertView,ViewGroup parent){
		//Estas lineas se borran cuando vamos a optimizar la lista
		LayoutInflater inflater = context.getLayoutInflater();
		View item = inflater.inflate(R.layout.lista_un_gasto, null);
		
		//Estas 4 lineas siguientes se borran en la segunda optimizacion para colocar las dos que quedan
		TextView lblNumero = (TextView)item.findViewById(R.id.textNumeroLista);
		lblNumero.setText(String.valueOf(position+1)+": ");
		
		TextView lblTipoGasto = (TextView)item.findViewById(R.id.textTipoGasto);
		lblTipoGasto.setText(datos[position].getTipoGasto()+" ");
		
		TextView lblConcepto = (TextView)item.findViewById(R.id.textConcepto);
		lblConcepto.setText(datos[position].getConcepto());
		
		TextView lblCantidad = (TextView)item.findViewById(R.id.textCantidad);
		lblCantidad.setText(datos[position].getCantidad());
		
		TextView lblEuros = (TextView)item.findViewById(R.id.textEuros);
		lblEuros.setText(" €");
		
		TextView lblFlecha = (TextView)item.findViewById(R.id.textFlecha);
		lblFlecha.setText("------>");

		return (item);
	}//Final getView
}
