package com.plcg.miscuentas;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NuevoIngresoPuntual extends Activity{
	
	EditText editConcepto,editCantidad,editDia,editMes,editAnio;
	Button btnGuardarIngresoPuntual;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevoingresopuntual);
		
		editConcepto = (EditText)findViewById(R.id.editConceptoIngresoPuntual);
		editCantidad = (EditText)findViewById(R.id.editCuantiaIngresoPuntual);
		editDia = (EditText)findViewById(R.id.editDiaIngresoPuntual);
		editMes = (EditText)findViewById(R.id.editMesIngresoPuntual);
		editAnio = (EditText)findViewById(R.id.editAnioIngresoPuntual);
		btnGuardarIngresoPuntual = (Button)findViewById(R.id.btnGuardarIngresoPuntual);
		
		//aqui obtengo el total de ingresos puntuales (De Este mes)
		Calendar hoy = Calendar.getInstance();//primero obtengo el mes en el que estamos y el año
		int dia = hoy.get(Calendar.DAY_OF_MONTH);
		int mes = hoy.get(Calendar.MONTH);//siempre devuelve desde 0
		int anio = hoy.get(Calendar.YEAR);
		
		//relleno los editViews con la fecha
		editDia.setText(String.valueOf(dia));
		editMes.setText(String.valueOf(mes+1));
		editAnio.setText(String.valueOf(anio));
		
		//evento del boton gasto Corriente
		btnGuardarIngresoPuntual.setOnClickListener(new View.OnClickListener() {
													
			@Override
			public void onClick(View v) {
				try{
					Sonidos.sonar(NuevoIngresoPuntual.this,"botonguardar");
					
					//recoger valores de edit text
					String concepto = editConcepto.getText().toString();
					double cantidad = Double.parseDouble(editCantidad.getText().toString());
					int diag = Integer.parseInt(editDia.getText().toString());
					int mesg = Integer.parseInt(editMes.getText().toString());
					int aniog = Integer.parseInt(editAnio.getText().toString());
					//lo guardo en la base de datos
					DBAdapterMisCuentas dbIP = new DBAdapterMisCuentas(NuevoIngresoPuntual.this);
					dbIP.open();
					dbIP.nuevoIngresoPuntual(cantidad, concepto, diag, mesg, aniog);
					dbIP.close();
					try{
						CopiaDeSeguridad.CrearCopiaSeguridad(NuevoIngresoPuntual.this);
					}catch(Exception e ){Toast.makeText(NuevoIngresoPuntual.this, "No se pudo copiar", Toast.LENGTH_LONG).show();};
				}catch(NumberFormatException e){
					Toast toast1 = Toast.makeText(NuevoIngresoPuntual.this, "Valores no correctos",Toast.LENGTH_LONG);
					toast1.setGravity(Gravity.BOTTOM, 0, 0);
					toast1.show();
				}
				//vuelvo a la pantalla principal
				Intent intent = new Intent(NuevoIngresoPuntual.this,MainActivity.class);
				startActivity(intent);
			}//finalonClick
		});//final clase interna
	}//fin oncreate
}//fin clase nuevoingresopuntual
