package com.plcg.miscuentas;

import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NuevoGastoCorriente extends Activity{
	
	String[] tipoGasto={"comida","drogueria","colegio","casa","loteria","coche","diversion","telefonia",
			"gimnasio","estetica","farmacia","prestamo","hipoteca","ropa","regalos","otros"};;
	String[] tipoGastoIngles={"food","drugstore","school","home","lottery","car","entertainment","telephony",
			"gym","aesthetics","pharmacy","loan","mortgage","clothing","gifts","others"};;
			
	String tipoDeGasto;//tipo de gasto elegido en el spiner que luego se guardara en la bd
	Spinner spinerNuevoGastoCorriente;
	Button btnGuardarGastoCorriente;
	AutoCompleteTextView autoCompleteConceptos;
	EditText editCuantia,editDia,editMes,editAnio;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevogastocorriente);
		
		//recuperar controles
		spinerNuevoGastoCorriente = (Spinner)findViewById(R.id.idSpinnerNuevoGastoCorriente);
		btnGuardarGastoCorriente = (Button)findViewById(R.id.btnGuardarGastosCorrientes);
		autoCompleteConceptos = (AutoCompleteTextView)findViewById(R.id.autoCompleteConceptos);
		editCuantia = (EditText)findViewById(R.id.editCuantia);
		editDia = (EditText)findViewById(R.id.editDiaN);
		editMes = (EditText)findViewById(R.id.editMesN);
		editAnio = (EditText)findViewById(R.id.editAnioN);
		
		////RELLENO EL SPINNER DE TIPO GASTO
		String idioma = Locale.getDefault().getDisplayLanguage();
		if (idioma.equalsIgnoreCase("English")){
			ArrayAdapter<String> adaptadorSpinner = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tipoGastoIngles);
			adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinerNuevoGastoCorriente.setAdapter(adaptadorSpinner);
		}
		if (idioma.equalsIgnoreCase("Español")){
			ArrayAdapter<String> adaptadorSpinner = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tipoGasto);
			adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinerNuevoGastoCorriente.setAdapter(adaptadorSpinner);
		}
		//ArrayAdapter<String> adaptadorSpinner = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tipoGasto);
		//adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//spinerNuevoGastoCorriente.setAdapter(adaptadorSpinner);
		/////////////////////////////////////////////////////////////////////////////////////
		
		Calendar hoy = Calendar.getInstance();//primero obtengo el mes en el que estamos y el año
		int dia = hoy.get(Calendar.DAY_OF_MONTH);
		int mes = hoy.get(Calendar.MONTH);//siempre devuelve desde 0
		int anio = hoy.get(Calendar.YEAR);
		
		editDia.setText(String.valueOf(dia).toString());
		editMes.setText(String.valueOf(mes+1).toString());
		editAnio.setText(String.valueOf(anio).toString());
		
		String[] conceptos;//esta variable tendra todos los conceptos añadidos anteriormente para rellenar auto
		DBAdapterMisCuentas dbGC = new DBAdapterMisCuentas(this);
		dbGC.open();
		conceptos = dbGC.devuelveListaConceptos();
		dbGC.close();
		
		//rellenar el autocompletetextView
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,conceptos);
		autoCompleteConceptos.setThreshold(2);
		autoCompleteConceptos.setAdapter(adaptador);
		
		///////////////EVENTO DEL SPINNER ////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////
		spinerNuevoGastoCorriente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?>parent,android.view.View v,int position,long id){
				tipoDeGasto=tipoGasto[position];
			}//fin onItemSelected

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			
			}
		});
		
		//evento del boton gasto Corriente
		btnGuardarGastoCorriente.setOnClickListener(new View.OnClickListener() {
											
			@Override
			public void onClick(View v) {
				
				try{
					Sonidos.sonar(NuevoGastoCorriente.this,"botonguardar");
					
					Double cantidad = Double.parseDouble(editCuantia.getText().toString());
					String concepto = autoCompleteConceptos.getText().toString();
					int diaN = Integer.parseInt((editDia.getText().toString()));
					int mesN = Integer.parseInt(editMes.getText().toString());
					int anioN = Integer.parseInt(editAnio.getText().toString());
					
					DBAdapterMisCuentas dbGC1 = new DBAdapterMisCuentas(NuevoGastoCorriente.this);
					dbGC1.open();
					dbGC1.nuevoGasto(cantidad, concepto, diaN, mesN, anioN,tipoDeGasto);
					dbGC1.close();
					try{
						CopiaDeSeguridad.CrearCopiaSeguridad(NuevoGastoCorriente.this);
					}catch(Exception e ){Toast.makeText(NuevoGastoCorriente.this, "No se pudo copiar", Toast.LENGTH_LONG).show();};
				}catch(NumberFormatException e){
					Toast toast1 = Toast.makeText(NuevoGastoCorriente.this, "Valores no correctos",Toast.LENGTH_LONG);
					toast1.setGravity(Gravity.BOTTOM, 0, 0);
					toast1.show();

				}//fin catch
				Intent intent = new Intent(NuevoGastoCorriente.this,MainActivity.class);
				startActivity(intent);
				finish();
			}//finalonClick
		});//final clase interna
	}//final onCreate
}//final clase nuevogastocorriente
