package com.plcg.miscuentas;

import java.util.Locale;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;


public class EstadisticaMensual extends Activity{
	String[] meses={"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre",
			"Octubre","Noviembre","Diciembre"};
	String[] mesesIngles={"January","February","March","April","May","June","July","August","September",
			"October","November","December"};
	
	String[] tipoGasto={"comida","drogueria","colegio","casa","loteria","coche","diversion","telefonia",
			"gimnasio","estetica","farmacia","prestamo","hipoteca","ropa","regalos","otros"};;
	String[] tipoGastoIngles={"food","drugstore","school","home","lottery","car","entertainment","telephony",
			"gym","aesthetics","pharmacy","loan","mortgage","clothing","gifts","others"};;
	
	private Double [] valores;
	
	//colores que se usan en el grafico
	private static int[] COLORS = new int[] {Color.BLUE,Color.CYAN,Color.DKGRAY,Color.GRAY,Color.GREEN,
		  Color.LTGRAY,Color.MAGENTA,Color.RED,Color.WHITE,Color.YELLOW,Color.rgb(0xff, 0x8e, 0x00),
		  Color.rgb(0x8e, 0x00, 0x8d),Color.rgb(0x00, 0x91, 0x8d),Color.rgb(0xff, 0x91, 0x8d),
		  Color.rgb(0xd3,0xb2,0x99),Color.rgb(0x93,0x3b,0x2c)};
	/** The main series that will include all the data. */
	private CategorySeries mSeries = new CategorySeries("");
	/** The main renderer for the main dataset. */
	private DefaultRenderer mRenderer = new DefaultRenderer();
	/** The chart view that displays the data. */
	private GraphicalView mChartView;
	
	private TextView textAnio, textMes;
	int request_Code = 1;
	
	public TextView textGastoTotal;
	public TextView textIngresoTotal;
	
	private int mes;
	
	@Override
	protected void onRestoreInstanceState(Bundle savedState) {
	    super.onRestoreInstanceState(savedState);
	    mSeries = (CategorySeries) savedState.getSerializable("current_series");
	    mRenderer = (DefaultRenderer) savedState.getSerializable("current_renderer");
	}//final onRestoreInstanceState

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    outState.putSerializable("current_series", mSeries);
	    outState.putSerializable("current_renderer", mRenderer);
	}//final onSaveInstanceState

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.estadisticamensual);
	    
	    //aqui obtengo  el anio y el mes elegido
	    int anio = getIntent().getIntExtra("anioelegido",0);	    
	    mes = getIntent().getIntExtra("meselegido",0);
	   
	    ///Rellenar el array de valores con la misma longitud que el array de tipo gasto
	    rellenaArray(anio,mes);
	   
	    ////////////////////////////////////////////////////////////////////////////////
	    setearAnioYAnimaciones(anio);
	    //////////////////////////////////////////////////
	   
	    //rellenando la tarta///////
	    ////////////////////////////
	    rellenarLaTarta();
	   
	    //Control gasto total e ingreso total
	    textGastoTotal = (TextView)findViewById(R.id.textoGastoTotal);
	    textIngresoTotal = (TextView)findViewById(R.id.textoIngresoTotal);
	    
	    double total = setearGastoTotal();
	    total = Math.rint(total*100)/100;
	    textGastoTotal.setText("Gasto Total = "+total+" €");
	    
	    String idioma = Locale.getDefault().getDisplayLanguage();
		if (idioma.equalsIgnoreCase("English")){
			textGastoTotal.setText("Total Expenditure = "+total+" €");
		}
	    
	    //ingreso total
	    DBAdapterMisCuentas dbMC = new DBAdapterMisCuentas(this);
	    dbMC.open();
	    double ingresoTotal = dbMC.devuelveTotalIngresosPuntualesDelMes((Integer)mes-1,(Integer)anio);
	    dbMC.close();
	    textIngresoTotal.setText("Ingreso Total = "+
       		String.valueOf(ingresoTotal)+" €");
	    if (idioma.equalsIgnoreCase("English")){
	    	textIngresoTotal.setText("Total Income = "+
	           		String.valueOf(ingresoTotal)+" €");
		}
	    
	    ///////////evento del textview de a�o
		textAnio.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Sonidos.sonar(EstadisticaMensual.this,"botoncomun");
				
				//se le manda el a�o a la otra actividad
				int anio = Integer.parseInt(textAnio.getText().toString());
				
				EstadisticaMensual2 bar = new EstadisticaMensual2(anio,mes);
				Intent graficaIntent = bar.getIntent(EstadisticaMensual.this);
				startActivity(graficaIntent);
			}//final onClick
		});
	}//final onCreate
	  
	/////////////procedimientos
	///////////////////////////////////////
	/////////////////////////////////////
	/////////////////////////////////////
	private double setearGastoTotal() {
		double total = 0;
		for(int i = 0; i<valores.length; i++){
			total = total + valores[i];
		}//final bucle for
		return total;
	}//final setear gasto total
	  
	  
	private void rellenaArray(int anio,int mes) {		  
		valores = new Double[tipoGasto.length];
		DBAdapterMisCuentas dbMC = new DBAdapterMisCuentas(this);
		dbMC.open();
		for(int i=0;i<valores.length;i++){
		    valores[i] = dbMC.devuelveTotalDandoTipoGastoYAnioYMes(tipoGasto[i],anio,mes);
		}//final del bucle for
		dbMC.close();		
	}//final rellenaArray
	  
	private void setearAnioYAnimaciones(int anio) {
		textAnio = (TextView)findViewById(R.id.textoAnio);
		textMes = (TextView)findViewById(R.id.textoMes);
		textMes.setText(meses[mes-1]);
		String idioma = Locale.getDefault().getDisplayLanguage();
		if (idioma.equalsIgnoreCase("English")){
			textMes.setText(mesesIngles[mes-1]);
		}
		textAnio.setText(String.valueOf(anio));
		    
		///animaciones/////
		Animation escalar = AnimationUtils.loadAnimation(this, R.anim.escalar);
		escalar.reset();
		textAnio.startAnimation(escalar);
	}//fin setearanioyanimaciones
	private void rellenarLaTarta() {
		//averiguo la resolucion de la pantalla para ajustar mejor el tama�o de las letras
		Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		
		@SuppressWarnings("deprecation")
		int width = display.getWidth();
		if(width>=720){
			mRenderer.setLabelsTextSize(28);//tama�o de las etiquetas
			mRenderer.setLegendTextSize(28);//tama�o de las categorias
		}else{
			mRenderer.setLabelsTextSize(18);//tama�o de las etiquetas
			mRenderer.setLegendTextSize(18);//tama�o de las categorias
		}//final if else
		//mRenderer.setZoomButtonsVisible(true);
		mRenderer.setStartAngle(180);
		mRenderer.setDisplayValues(true);
		    
		for(int i=0;i<tipoGasto.length;i++){
			String idioma = Locale.getDefault().getDisplayLanguage();
			if (idioma.equalsIgnoreCase("English")){
				mSeries.add(tipoGastoIngles[i]+" "+valores[i] ,valores[i]);
			}
			if (idioma.equalsIgnoreCase("Espa�ol")){
				mSeries.add(tipoGasto[i]+" "+valores[i] ,valores[i]);
			}
			
			
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);	     
			        
			mRenderer.addSeriesRenderer(renderer);
			mRenderer.setClickEnabled(true);
			        
			if (mChartView == null) {
	        LinearLayout layout = (LinearLayout) findViewById(R.id.grafica);
	        mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
			       
	       //a�adiendo un listener a cada trocito de la tarta
	       ////////////////////////////////////////////////////
	       /////////////////////////////////////////////////////
	       mChartView.setOnClickListener(new View.OnClickListener() {
	    	   @Override
	    	   public void onClick(View v) {
	    		   SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
				   if (seriesSelection == null) {

				   } else {
					   for (int i = 0; i < mSeries.getItemCount(); i++) {
						   mRenderer.getSeriesRendererAt(i).setHighlighted(i == seriesSelection.getPointIndex());
					   }//final del for
				            
					   mChartView.repaint();
				          
					   Sonidos.sonar(EstadisticaMensual.this,"botoncomun");
					   
				   	   Intent intent = new Intent(EstadisticaMensual.this,EstadisticaDeUnTipoGastoMes.class);
				   	   //se pasa el tipo de gasto y el a�o por el intent
				   	   intent.putExtra("TipoGasto", tipoGasto[seriesSelection.getPointIndex()]);
				   	   intent.putExtra("Anio",textAnio.getText().toString());
					   intent.putExtra("Mes",String.valueOf(mes));
				  	   startActivityForResult(intent,1);
				   }//final if selection == null
	    	   }///final onclick
	       });//final clase interna
			        
			        
	       layout.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT,
	    		   LayoutParams.MATCH_PARENT));
			} else {
			mChartView.repaint();
			}//final if mchartview==null
		}//final del bucle for		
	}//final rellenarLaTarta
	
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
			Sonidos.sonar(EstadisticaMensual.this,"botoncasa");
			
			Intent intent = new Intent(this,MainActivity.class);
			startActivity(intent);
			return true;
		}//final switch
		return false;
	}//final menuchoice
	
}//final clase EstadisticaMensual
