package com.plcg.miscuentas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class EliminarFijos extends Activity{
	
	private ListView lstGastosFijos;
	private ListView lstIngresosFijos;
	private String[] conceptosGastosFijos;
	private String[] conceptosIngresosFijos;
	private String conceptoAEliminar;//este seria a gastosfijos
	private String conceptoAEliminarIngresosFijos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eliminarfijos);
		
		lstGastosFijos = (ListView)findViewById(R.id.listGastosFijosEliminar);
		lstIngresosFijos = (ListView)findViewById(R.id.listIngresosFijosEliminar);
		
		//relleno la lista de gastos fijos
		DBAdapterMisCuentas dbGF = new DBAdapterMisCuentas(this);
		dbGF.open();
		conceptosGastosFijos = dbGF.devuelveArrayDeGastosFijos();
		dbGF.close();
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,conceptosGastosFijos);
		lstGastosFijos.setAdapter(adaptador);
		
		//relleno la lista de ingresos fijos
		DBAdapterMisCuentas dbIF = new DBAdapterMisCuentas(this);
		dbIF.open();
		conceptosIngresosFijos = dbIF.devuelveArrayDeIngresosFijos();
		dbIF.close();
		ArrayAdapter<String> adaptador1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,conceptosIngresosFijos);
		lstIngresosFijos.setAdapter(adaptador1);
		
		///Evento lanzado al pulsar en algun item de la lista de gastos Fijos
		lstGastosFijos.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int posicion,
					long arg3) {
				Sonidos.sonar(EliminarFijos.this,"botoncomun");

				conceptoAEliminar=conceptosGastosFijos[posicion];//este es el concepto que se elimina
				
				DBAdapterMisCuentas dbGF = new DBAdapterMisCuentas(EliminarFijos.this);
				dbGF.open();
				double cantidad=dbGF.devuelveCantidadGastosFijos(conceptosGastosFijos[posicion]);
				dbGF.close();
				
				//creacion del cuadro de dialogo
				AlertDialog.Builder dialogoOpc = new AlertDialog.Builder(EliminarFijos.this);
  		    	dialogoOpc.setTitle("Elija una opcion");
  		    	dialogoOpc.setMessage("¿Desea eliminar este gasto Fijo? "+ conceptosGastosFijos[posicion] +" -->"+cantidad+" �");
  		    	
  		    	dialogoOpc.setPositiveButton("Si",new OnClickListener(){
  		    		
  					@Override
  					public void onClick(DialogInterface dialog, int which) {
  						DBAdapterMisCuentas dbGF = new DBAdapterMisCuentas(EliminarFijos.this);
  						dbGF.open();
  						dbGF.eliminarGastoFijo(conceptoAEliminar);
  						dbGF.close();
  	    				dialog.cancel();//si se pulsa aceptar,todo se queda tal cual
  	    				Toast.makeText(EliminarFijos.this,"Eliminado Correctamente "+ conceptoAEliminar, Toast.LENGTH_LONG).show();
  	    				try{
  							CopiaDeSeguridad.CrearCopiaSeguridad(EliminarFijos.this);
  						}catch(Exception e ){Toast.makeText(EliminarFijos.this, "No se pudo copiar", Toast.LENGTH_LONG).show();};
  	    				//Cargo de nuevo la actividad para actualizar los cambios
  	    				Intent intent = new Intent(EliminarFijos.this,MainActivity.class);
  	    				startActivity(intent);
  	  
  					}//fin onClick
  		    	});//fin clase interna
  		    	dialogoOpc.setNegativeButton("No", new OnClickListener(){

  					@Override
  					public void onClick(DialogInterface dialog, int which) {
  						dialog.cancel();
  					}
  		    	});
  		    	dialogoOpc.show();
			}//final onItemClick
		});//final clase interna
		
		///Evento lanzado al pulsar en algun item de la lista de ingresos Fijos
		lstIngresosFijos.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int posicion,
					long arg3) {
				Sonidos.sonar(EliminarFijos.this,"botoncomun");

				conceptoAEliminarIngresosFijos=conceptosIngresosFijos[posicion];//este es el concepto que se elimina
			
				DBAdapterMisCuentas dbIF = new DBAdapterMisCuentas(EliminarFijos.this);
				dbIF.open();
				double cantidad=dbIF.devuelveCantidadIngresosFijos(conceptosIngresosFijos[posicion]);
				dbIF.close();
						
				//creacion del cuadro de dialogo
				AlertDialog.Builder dialogoOpc = new AlertDialog.Builder(EliminarFijos.this);
		  		dialogoOpc.setTitle("Elija una opcion");
		  		dialogoOpc.setMessage("�Desea eliminar este Ingreso Fijo? "+ conceptosIngresosFijos[posicion] +" -->"+cantidad+" �");
		  		    	
		  		dialogoOpc.setPositiveButton("Si",new OnClickListener(){
		  		    		
		  			@Override
		  			public void onClick(DialogInterface dialog, int which) {
		  				DBAdapterMisCuentas dbIF = new DBAdapterMisCuentas(EliminarFijos.this);
		  				dbIF.open();
		  				dbIF.eliminarIngresoFijo(conceptoAEliminarIngresosFijos);
		  				dbIF.close();
		  	    		dialog.cancel();//si se pulsa aceptar,todo se queda tal cual
		  	    		Toast.makeText(EliminarFijos.this,"Eliminado Correctamente "+ conceptoAEliminarIngresosFijos, Toast.LENGTH_LONG).show();
		  	    		try{
  							CopiaDeSeguridad.CrearCopiaSeguridad(EliminarFijos.this);
  						}catch(Exception e ){Toast.makeText(EliminarFijos.this, "No se pudo copiar", Toast.LENGTH_LONG).show();};
		  	    		////Cargo de nuevo la actividad para actualizar los cambios
		  	    		Intent intent = new Intent(EliminarFijos.this,MainActivity.class);
		  	    		startActivity(intent);
		  	  
		  			}//fin onClick
		  		});//fin clase interna
		  		dialogoOpc.setNegativeButton("No", new OnClickListener(){

		  			@Override
		  			public void onClick(DialogInterface dialog, int which) {
		  				dialog.cancel();
		  			}
		  		});
		  		dialogoOpc.show();
				}//final onItemClick
			});//final clase interna
	
	}//final onCreate
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);
		return true;
	}//final onCreateOptionsMenu
	
	public boolean onOptionsItemSelected(MenuItem item){
		return MenuChoice(item);	
	}//final onOptionsItemSelected

	private void CreateMenu(Menu menu){
		MenuItem mnu1 = menu.add(0,0,0,"Home");
		{
			mnu1.setIcon(R.drawable.home);
			mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
	}//final createMenu
	
	private boolean MenuChoice(MenuItem item){
		switch(item.getItemId()){
		case 0 :
			Sonidos.sonar(EliminarFijos.this,"botoncasa");

			Intent intent = new Intent(this,MainActivity.class);
			startActivity(intent);
			return true;
		}//final switch
		return false;
	}//final menuchoice
}//final clase eliminar
