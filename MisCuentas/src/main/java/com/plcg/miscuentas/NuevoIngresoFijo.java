package com.plcg.miscuentas;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NuevoIngresoFijo extends Activity{

	EditText editConceptoIngresoFijo,editCuantiaIngresoFijo;
	Button btnGuardarIngresoFijo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevoingresofijo);
		
		editConceptoIngresoFijo = (EditText)findViewById(R.id.editConceptoIngresoFijo);
		editCuantiaIngresoFijo = (EditText)findViewById(R.id.editCuantiaIngresoFijo);
		btnGuardarIngresoFijo = (Button)findViewById(R.id.btnGuardarIngresoFijo);
		
		//evento del boton guardar Ingreso Fijo
		btnGuardarIngresoFijo.setOnClickListener(new View.OnClickListener() {
													
			@Override
			public void onClick(View v) {
				try{
					Sonidos.sonar(NuevoIngresoFijo.this,"botonguardar");
					
					String concepto = editConceptoIngresoFijo.getText().toString();
					double cantidad = Double.parseDouble(editCuantiaIngresoFijo.getText().toString());
					DBAdapterMisCuentas dbIF = new DBAdapterMisCuentas(NuevoIngresoFijo.this);
					dbIF.open();
					dbIF.nuevoIngresoFijo(cantidad, concepto);
					dbIF.close();
					try{
						CopiaDeSeguridad.CrearCopiaSeguridad(NuevoIngresoFijo.this);
					}catch(Exception e ){Toast.makeText(NuevoIngresoFijo.this, "No se pudo copiar", Toast.LENGTH_LONG).show();};
				}catch(NumberFormatException e){
					Toast toast1 = Toast.makeText(NuevoIngresoFijo.this, "Valores no correctos",Toast.LENGTH_LONG);
					toast1.setGravity(Gravity.BOTTOM, 0, 0);
					toast1.show();
				}
				Intent intent = new Intent(NuevoIngresoFijo.this,MainActivity.class);
				startActivity(intent);
			}//finalonClick
		});//final clase interna
	}//final onCreate
}
