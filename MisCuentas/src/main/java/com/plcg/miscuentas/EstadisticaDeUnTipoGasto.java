package com.plcg.miscuentas;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class EstadisticaDeUnTipoGasto extends Activity{
	
	private ElementoGastoCompuesto[] datos;//almacena los datos solicitados
	//en la base de datos
	private TextView textGastoTotal;
	private ListView listaTipoGasto;
	
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
	    
	    //aqui obtengo  el tipo de gasto y el anio seleccionado
	    String tipoGasto = getIntent().getStringExtra("TipoGasto");
	    String anio = getIntent().getStringExtra("Anio");
	    
	    ///obtengo los datos de la base de datos
	    DBAdapterMisCuentas dbMC = new DBAdapterMisCuentas(this);
	    dbMC.open();
	    datos = dbMC.devuelveArrayElementoGastoCompuestoDandoAnioYTipoGasto(anio, tipoGasto);
	    dbMC.close();
	    
	    //obtener el gasto total de los datos  escogidos
	    Double total = 0.0;
	    for(int i = 0; i<datos.length; i++){
	    	total = total + Double.parseDouble(datos[i].getCantidad());
	    }//final bucle
	    total = Math.rint(total*100)/100;
	    textGastoTotal.setText( tipoGasto +" en "+ anio + ": "+ total+" �");
	    
	    String idioma = Locale.getDefault().getDisplayLanguage();
		   
	    if (idioma.equalsIgnoreCase("English")){
	    	String tipoGastoNuevo="";
	    	
	    	for(int i=0; i<tipoGastoCopia.length;i++){
	    		if (tipoGasto.equalsIgnoreCase(tipoGastoCopia[i]))
	    		{
	    			tipoGastoNuevo = tipoGastoIngles[i];
	    		}
	    	}
	    	textGastoTotal.setText(tipoGastoNuevo +" in "+ anio + ": "+ total+" �");
		}
	    
	    //paso los datos al listview
	    //relleno el listViewGastosPuntuales
	  	AdaptadorTipoGasto adaptador = new AdaptadorTipoGasto(this,datos);
	  	listaTipoGasto.setAdapter(adaptador);
	    
	}//final onCreate
}//final clase EstadisticaDeUnTipoGasto

//////////////////clase adaptadorLista
class AdaptadorTipoGasto extends ArrayAdapter<Object>{
	Activity context;
	ElementoGastoCompuesto[] datos;

	AdaptadorTipoGasto(Activity context,ElementoGastoCompuesto[] dato) {
		super(context,R.layout.lista_un_elemento_corriente,dato);

		this.context = context;
		this.datos =dato ;
	}//Fin constructor

	public View getView(int position,View convertView,ViewGroup parent){
		//Estas lineas se borran cuando vamos a optimizar la lista
		LayoutInflater inflater = context.getLayoutInflater();

		//segun el tipo de lista se carga un layout u otro diferente
		View item = inflater.inflate(R.layout.lista_un_elemento_corriente_tipogasto, null);
		
		//TextView lblTipoGasto = (TextView)item.findViewById(R.id.textElementoTipoGastoIngresoTipoGasto);
		//lblTipoGasto.setText(datos[position].getTipoGastoIngreso()+" ");	

		//Estas 4 lineas siguientes se borran en la segunda optimizacion para colocar las dos que quedan
		TextView lblConcepto = (TextView)item.findViewById(R.id.textElementoConceptoTipoGasto);
		lblConcepto.setText(datos[position].getConcepto());

		TextView lblCantidad = (TextView)item.findViewById(R.id.textElementoCantidadTipoGasto);
		lblCantidad.setText(datos[position].getCantidad()+" �");
		
		TextView lblDia = (TextView)item.findViewById(R.id.textElementoDiaTipoGasto);
		lblDia.setText(datos[position].getDia());
		
		String[] meses = {"enero","febrero","marzo","abril","mayo","junio","julio","agosto",
				"septiembre","octubre","noviembre","diciembre"};
		TextView lblMes = (TextView)item.findViewById(R.id.textElementoMesTipoGasto);
		int mes = Integer.parseInt(datos[position].getMes().toString());
		lblMes.setText(meses[mes-1]);
		
		TextView lblAnio = (TextView)item.findViewById(R.id.textElementoAnioTipoGasto);
		lblAnio.setText(datos[position].getAnio());
		return (item);
	}//Final getView
}//fin clase Adaptador Titular