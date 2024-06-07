package com.plcg.miscuentas;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class EstadisticaDeUnTipoGastoMes extends Activity{
	
	private ElementoGastoCompuesto[] datos;//almacena los datos solicitados
	//en la base de datos
	private TextView textGastoTotal;
	private ListView listaTipoGasto;
	
	String[] meses={"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre",
			"Octubre","Noviembre","Diciembre"};
	String[] mesesIngles={"January","February","March","April","May","June","July","August","September",
			"October","November","December"};
	
	String[] tipoGastoCopia={"comida","drogueria","colegio","casa","loteria","coche","diversion","telefonia",
			"gimnasio","estetica","farmacia","prestamo","hipoteca","ropa","regalos","otros"};;
	String[] tipoGastoIngles={"food","drugstore","school","home","lottery","car","entertainment","telephony",
			"gym","aesthetics","pharmacy","loan","mortgage","clothing","gifts","others"};;
	
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.estadisticadeuntipogasto);
	    
	    //control de los componentes
	    textGastoTotal = (TextView)findViewById(R.id.textGastoTotalEstadisticasTipoGasto);
	    listaTipoGasto = (ListView)findViewById(R.id.listEstadistigaTipoGasto);
	    
	    //aqui obtengo  el tipo de gasto y el anio seleccionado y el mes
	    String tipoGasto = getIntent().getStringExtra("TipoGasto");
	    String anio = getIntent().getStringExtra("Anio");
	    String mes = getIntent().getStringExtra("Mes");
	    
	    ///obtengo los datos de la base de datos
	    DBAdapterMisCuentas dbMC = new DBAdapterMisCuentas(this);
	    dbMC.open();
	    datos = dbMC.devuelveArrayElementoGastoCompuestoDandoAnioYTipoGastoYMes(anio, tipoGasto,mes);
	    dbMC.close();
	    
	    //obtener el gasto total de los datos  escogidos
	    Double total = 0.0;
	    for(int i = 0; i<datos.length; i++){
	    	total = total + Double.parseDouble(datos[i].getCantidad());
	    }//final bucle
	    total = Math.rint(total*100)/100;
	    textGastoTotal.setText( tipoGasto + " "+ meses[Integer.parseInt(mes)-1] +" "+ anio + ": "+ total+" �");
	    
	    String idioma = Locale.getDefault().getDisplayLanguage();
	   
	    if (idioma.equalsIgnoreCase("English")){
	    	String tipoGastoNuevo="";
	    	
	    	for(int i=0; i<tipoGastoCopia.length;i++){
	    		if (tipoGasto.equalsIgnoreCase(tipoGastoCopia[i]))
	    		{
	    			tipoGastoNuevo = tipoGastoIngles[i];
	    		}
	    	}
	    	textGastoTotal.setText( tipoGastoNuevo + " "+ mesesIngles[Integer.parseInt(mes)-1] +" "+ anio + ": "+ total+" �");
		}
	     
	    //paso los datos al listview
	    //relleno el listViewGastosPuntuales
	  	AdaptadorTipoGasto adaptador = new AdaptadorTipoGasto(this,datos);
	  	listaTipoGasto.setAdapter(adaptador);
	    
	}//final onCreate
}//final clase EstadisticaDeUnTipoGasto