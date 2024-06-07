package com.plcg.miscuentas;


import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
public class NuevoGastoFijo extends Activity{

	String[] tipoGasto={"comida","drogueria","colegio","casa","loteria","coche","diversion","telefonia",
			"gimnasio","estetica","farmacia","prestamo","hipoteca","ropa","regalos","otros"};;
	String[] tipoGastoIngles={"food","drugstore","school","home","lottery","car","entertainment","telephony",
			"gym","aesthetics","pharmacy","loan","mortgage","clothing","gifts","others"};;
	String tipoDeGasto;
	EditText editConceptoGastoFijo,editCuantiaGastoFijo;
	Button btnGuardarGastoFijo;
	Spinner spinerNuevoGastoFijo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevogastofijo);
		
		spinerNuevoGastoFijo = (Spinner)findViewById(R.id.idSpinnerNuevoGastoFijo);
		editConceptoGastoFijo = (EditText)findViewById(R.id.editConceptoGastoFijo);
		editCuantiaGastoFijo = (EditText)findViewById(R.id.editCuantiaGastoFijo);
		btnGuardarGastoFijo = (Button)findViewById(R.id.btnGuardarGastoFijo);
		
		////RELLENO EL SPINNER DE TIPO GASTO
		String idioma = Locale.getDefault().getDisplayLanguage();
		if (idioma.equalsIgnoreCase("English")){
			ArrayAdapter<String> adaptadorSpinner = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tipoGastoIngles);
			adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinerNuevoGastoFijo.setAdapter(adaptadorSpinner);
		}
		if (idioma.equalsIgnoreCase("Español")){
			ArrayAdapter<String> adaptadorSpinner = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tipoGasto);
			adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinerNuevoGastoFijo.setAdapter(adaptadorSpinner);
		}
//		ArrayAdapter<String> adaptadorSpinner = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tipoGasto);
//		adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spinerNuevoGastoFijo.setAdapter(adaptadorSpinner);
		/////////////////////////////////////////////////////////////////////////////////////
		
		///////////////EVENTO DEL SPINNER ////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////
		spinerNuevoGastoFijo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?>parent,android.view.View v,int position,long id){
				tipoDeGasto=tipoGasto[position];
			}//fin onItemSelected

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
		
			}
		});
		
		//evento del boton guardar Ingreso Fijo
		btnGuardarGastoFijo.setOnClickListener(new View.OnClickListener() {
													
			@Override
			public void onClick(View v) {
				try{
					Sonidos.sonar(NuevoGastoFijo.this,"botonguardar");

					String concepto = editConceptoGastoFijo.getText().toString();
					double cantidad = Double.parseDouble(editCuantiaGastoFijo.getText().toString());
					DBAdapterMisCuentas dbGF = new DBAdapterMisCuentas(NuevoGastoFijo.this);
					dbGF.open();
					dbGF.nuevoGastoFijo(cantidad, concepto,tipoDeGasto);
					dbGF.close();
					try{
						CopiaDeSeguridad.CrearCopiaSeguridad(NuevoGastoFijo.this);
					}catch(Exception e ){Toast.makeText(NuevoGastoFijo.this, "No se pudo copiar", Toast.LENGTH_LONG).show();};
				}catch(NumberFormatException e){
					Toast toast1 = Toast.makeText(NuevoGastoFijo.this, "Valores no correctos",Toast.LENGTH_LONG);
					toast1.setGravity(Gravity.BOTTOM, 0, 0);
					toast1.show();
				}
				Intent intent = new Intent(NuevoGastoFijo.this,MainActivity.class);
				startActivity(intent);
			}//finalonClick
		});//final clase interna
	}//final onCreate
}

