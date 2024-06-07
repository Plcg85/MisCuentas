package com.plcg.miscuentas;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class EliminarPuntuales extends Activity{
	
	private ListView lstGastosPuntuales;
	private ListView lstIngresosPuntuales;
	private ElementoGastoCompuesto[] datosGastosCorrientes;
	private ElementoGastoCompuesto[] datosIngresosPuntuales;
	private String conceptoElegido,cantidadElegida,diaElegido,mesElegido,anioElegido;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eliminarpuntuales);
		
		//referenciar los controles
		lstGastosPuntuales = (ListView)findViewById(R.id.listGastosPuntualesEliminar);
		lstIngresosPuntuales = (ListView)findViewById(R.id.listIngresosPuntualesEliminar);
		
		/////gastos////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		//solicito el array de elementoGastoCompuesto;
		DBAdapterMisCuentas dbGC = new DBAdapterMisCuentas(this);
		dbGC.open();
		datosGastosCorrientes = dbGC.devuelveArrayElementoGastoCompuesto();
		dbGC.close();
		
		//relleno el listViewGastosPuntuales
		Adaptador adaptador = new Adaptador(this,datosGastosCorrientes,"gastos");
		lstGastosPuntuales.setAdapter(adaptador);
		////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////
		
		//////ingresos//////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////
		//solicito el array de elementoGastoCompuesto para ingresos puntuales
		DBAdapterMisCuentas dbIP = new DBAdapterMisCuentas(this);
		dbIP.open();
		datosIngresosPuntuales = dbIP.devuelveArrayElementoGastoCompuestoIngresosPuntuales();
		dbIP.close();
		
		//relleno el listViewIngresosPuntuales
		Adaptador adaptador1 = new Adaptador(this,datosIngresosPuntuales,"ingresos");
		lstIngresosPuntuales.setAdapter(adaptador1);
		///////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////
		
		
		///Evento lanzado al pulsar en algun item de la lista de gastosPuntuales
		lstGastosPuntuales.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int posicion,
					long arg3) {
				Sonidos.sonar(EliminarPuntuales.this,"botoncomun");
				
				conceptoElegido = datosGastosCorrientes[posicion].getConcepto();
				cantidadElegida = datosGastosCorrientes[posicion].getCantidad();
				diaElegido = datosGastosCorrientes[posicion].getDia();
				mesElegido = datosGastosCorrientes[posicion].getMes();
				anioElegido = datosGastosCorrientes[posicion].getAnio();
						
				String idioma = Locale.getDefault().getDisplayLanguage();
				   
			    if (idioma.equalsIgnoreCase("English")){
			    	//creacion del cuadro de dialogo
					AlertDialog.Builder dialogoOpc = new AlertDialog.Builder(EliminarPuntuales.this);
			  		dialogoOpc.setTitle("Choose an option");
			  		dialogoOpc.setMessage("¿Delete this expense? "+ datosGastosCorrientes[posicion].getConcepto()
			  		    	+" -->"+datosGastosCorrientes[posicion].getCantidad()+" �");
			  		    	
			  		dialogoOpc.setPositiveButton("Yes",new OnClickListener(){
			  		    		
			  			@Override
			  			public void onClick(DialogInterface dialog, int which) {
			  						
			  				DBAdapterMisCuentas dbGC = new DBAdapterMisCuentas(EliminarPuntuales.this);
							dbGC.open();
							dbGC.eliminarGasto(conceptoElegido, diaElegido, mesElegido, anioElegido);
							dbGC.close();
			  	    		Toast.makeText(EliminarPuntuales.this,"Succesfully removed "+ conceptoElegido, Toast.LENGTH_LONG).show();
			  	    		try{
								CopiaDeSeguridad.CrearCopiaSeguridad(EliminarPuntuales.this);
							}catch(Exception e ){Toast.makeText(EliminarPuntuales.this, "Copy Error", Toast.LENGTH_LONG).show();};
			  	    		//Cargo de nuevo la actividad para actualizar los cambios
			  	    		Intent intent = new Intent(EliminarPuntuales.this,MainActivity.class);
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
			    }//final si es ingles
			    if (idioma.equalsIgnoreCase("Espa�ol")){
			    	//creacion del cuadro de dialogo
					AlertDialog.Builder dialogoOpc = new AlertDialog.Builder(EliminarPuntuales.this);
			  		dialogoOpc.setTitle("Elija una opcion");
			  		dialogoOpc.setMessage("�Desea eliminar este gasto puntual? "+ datosGastosCorrientes[posicion].getConcepto() 
			  		    	+" -->"+datosGastosCorrientes[posicion].getCantidad()+" �");
			  		    	
			  		dialogoOpc.setPositiveButton("Si",new OnClickListener(){
			  		    		
			  			@Override
			  			public void onClick(DialogInterface dialog, int which) {
			  						
			  				DBAdapterMisCuentas dbGC = new DBAdapterMisCuentas(EliminarPuntuales.this);
							dbGC.open();
							dbGC.eliminarGasto(conceptoElegido, diaElegido, mesElegido, anioElegido);
							dbGC.close();
			  	    		Toast.makeText(EliminarPuntuales.this,"Eliminado Correctamente "+ conceptoElegido, Toast.LENGTH_LONG).show();
			  	    		try{
								CopiaDeSeguridad.CrearCopiaSeguridad(EliminarPuntuales.this);
							}catch(Exception e ){Toast.makeText(EliminarPuntuales.this, "No se pudo copiar", Toast.LENGTH_LONG).show();};
			  	    		//Cargo de nuevo la actividad para actualizar los cambios
			  	    		Intent intent = new Intent(EliminarPuntuales.this,MainActivity.class);
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
			    }//final si es espa�ol

			}//final onItemClick

		
		});//final clase interna
		
		
		///Evento lanzado al pulsar en algun item de la lista de IngresosPuntuales
		lstIngresosPuntuales.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int posicion,
					long arg3) {
				Sonidos.sonar(EliminarPuntuales.this,"botoncomun");
				
				conceptoElegido = datosIngresosPuntuales[posicion].getConcepto();
				cantidadElegida = datosIngresosPuntuales[posicion].getCantidad();
				diaElegido = datosIngresosPuntuales[posicion].getDia();
				mesElegido = datosIngresosPuntuales[posicion].getMes();
				anioElegido = datosIngresosPuntuales[posicion].getAnio();
						
				String idioma = Locale.getDefault().getDisplayLanguage();
				   
			    if (idioma.equalsIgnoreCase("English")){
			    	//creacion del cuadro de dialogo
					AlertDialog.Builder dialogoOpc = new AlertDialog.Builder(EliminarPuntuales.this);
					dialogoOpc.setTitle("Choose an option");
					dialogoOpc.setMessage("�Would you like to delete this entry point? "+ conceptoElegido 
					  		+" -->"+cantidadElegida+" �");
					  		    	
					dialogoOpc.setPositiveButton("Yes",new OnClickListener(){
					  		    		
					  	@Override
					  	public void onClick(DialogInterface dialog, int which) {
					  						
					  		DBAdapterMisCuentas dbIP = new DBAdapterMisCuentas(EliminarPuntuales.this);
							dbIP.open();
							dbIP.eliminarIngresoPuntual(conceptoElegido, diaElegido, mesElegido, anioElegido);
							dbIP.close();
							String idioma = Locale.getDefault().getDisplayLanguage();
							   
						    if (idioma.equalsIgnoreCase("English")){
						  	    Toast.makeText(EliminarPuntuales.this,"Successfully removed "+ conceptoElegido, Toast.LENGTH_LONG).show();
						    }
						    if (idioma.equalsIgnoreCase("Espa�ol")){
						  	    Toast.makeText(EliminarPuntuales.this,"Eliminado Correctamente "+ conceptoElegido, Toast.LENGTH_LONG).show();
						    }
					  	  try{
								CopiaDeSeguridad.CrearCopiaSeguridad(EliminarPuntuales.this);
							}catch(Exception e ){Toast.makeText(EliminarPuntuales.this, "No se pudo copiar", Toast.LENGTH_LONG).show();};
					  	    //Cargo de nuevo la actividad para actualizar los cambios
					  	    Intent intent = new Intent(EliminarPuntuales.this,MainActivity.class);
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
			    }//fin si ingles
			    if (idioma.equalsIgnoreCase("Espa�ol")){
			    	//creacion del cuadro de dialogo
					AlertDialog.Builder dialogoOpc = new AlertDialog.Builder(EliminarPuntuales.this);
					dialogoOpc.setTitle("Elija una opcion");
					dialogoOpc.setMessage("�Desea eliminar este ingreso puntual? "+ conceptoElegido 
					  		+" -->"+cantidadElegida+" �");
					  		    	
					dialogoOpc.setPositiveButton("Si",new OnClickListener(){
					  		    		
					  	@Override
					  	public void onClick(DialogInterface dialog, int which) {
					  						
					  		DBAdapterMisCuentas dbIP = new DBAdapterMisCuentas(EliminarPuntuales.this);
							dbIP.open();
							dbIP.eliminarIngresoPuntual(conceptoElegido, diaElegido, mesElegido, anioElegido);
							dbIP.close();
					  	    Toast.makeText(EliminarPuntuales.this,"Eliminado Correctamente "+ conceptoElegido, Toast.LENGTH_LONG).show();
					  	  try{
								CopiaDeSeguridad.CrearCopiaSeguridad(EliminarPuntuales.this);
							}catch(Exception e ){Toast.makeText(EliminarPuntuales.this, "No se pudo copiar", Toast.LENGTH_LONG).show();};
					  	    //Cargo de nuevo la actividad para actualizar los cambios
					  	    Intent intent = new Intent(EliminarPuntuales.this,MainActivity.class);
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
			    }//fin si espa�ol
				
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
			Sonidos.sonar(EliminarPuntuales.this,"botoncasa");
	
			Intent intent = new Intent(this,MainActivity.class);
			startActivity(intent);
			return true;
		}//final switch
		return false;
	}//final menuchoice
}//final clase eliminarpuntuales


//////////////////clase adaptador
class Adaptador extends ArrayAdapter<Object>{
	Activity context;
	ElementoGastoCompuesto[] datos;
	String tipo;
	String[] tipoGastoCopia={"comida","drogueria","colegio","casa","loteria","coche","diversion","telefonia",
			"gimnasio","estetica","farmacia","prestamo","hipoteca","ropa","regalos","otros"};;
	String[] tipoGastoIngles={"food","drugstore","school","home","lottery","car","entertainment","telephony",
			"gym","aesthetics","pharmacy","loan","mortgage","clothing","gifts","others"};;
	
	Adaptador(Activity context,ElementoGastoCompuesto[] dato,String tipoAdaptador) {
		super(context,R.layout.lista_un_elemento_corriente,dato);
		this.tipo = tipoAdaptador;
		this.context = context;
		this.datos =dato ;
	}//Fin constructor
	
	public View getView(int position,View convertView,ViewGroup parent){
		//Estas lineas se borran cuando vamos a optimizar la lista
		LayoutInflater inflater = context.getLayoutInflater();
		
		//segun el tipo de lista se carga un layout u otro diferente
		View item = inflater.inflate(R.layout.lista_un_elemento_corriente_ingresos, null);
		if (tipo == "gastos"){
			item = inflater.inflate(R.layout.lista_un_elemento_corriente, null);
			TextView lblTipoGasto = (TextView)item.findViewById(R.id.textElementoTipoGastoIngreso);
			lblTipoGasto.setText(datos[position].getTipoGastoIngreso()+" ");
			
			String idioma = Locale.getDefault().getDisplayLanguage();
			   
		    if (idioma.equalsIgnoreCase("English")){
		    	String tipoGastoNuevo="";
		    	
		    	for(int i=0; i<tipoGastoCopia.length;i++){
		    		if (datos[position].getTipoGastoIngreso().equalsIgnoreCase(tipoGastoCopia[i]))
		    		{
		    			tipoGastoNuevo = tipoGastoIngles[i];
		    		}
		    	}
				lblTipoGasto.setText(tipoGastoNuevo+" ");
			}
		}		
		
		//Estas 4 lineas siguientes se borran en la segunda optimizacion para colocar las dos que quedan
		TextView lblConcepto = (TextView)item.findViewById(R.id.textElementoConcepto);
		lblConcepto.setText(datos[position].getConcepto());
		
		TextView lblCantidad = (TextView)item.findViewById(R.id.textElementoCantidad);
		lblCantidad.setText(datos[position].getCantidad()+" �");
		
		TextView lblDia = (TextView)item.findViewById(R.id.textElementoDia);
		lblDia.setText(datos[position].getDia());
		
		String[] meses = {"enero","febrero","marzo","abril","mayo","junio","julio","agosto",
				"septiembre","octubre","noviembre","diciembre"};
		String[] mesesIngles={"January","February","March","April","May","June","July","August","September",
				"October","November","December"};
		TextView lblMes = (TextView)item.findViewById(R.id.textElementoMes);
		int mes = Integer.parseInt(datos[position].getMes().toString());
		lblMes.setText(meses[mes-1]);
		
		String idioma = Locale.getDefault().getDisplayLanguage();
		if (idioma.equalsIgnoreCase("English")){
			lblMes.setText(mesesIngles[mes-1]);
		}

		TextView lblAnio = (TextView)item.findViewById(R.id.textElementoAnio);
		lblAnio.setText(datos[position].getAnio());
		return (item);
	}//Final getView
}//fin clase Adaptador Titulares


