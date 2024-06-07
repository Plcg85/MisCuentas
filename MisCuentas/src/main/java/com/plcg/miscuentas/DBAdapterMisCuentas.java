package com.plcg.miscuentas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapterMisCuentas {
	static final String KEY_ROWID = "_id";
	static final String KEY_CONCEPTO = "concepto";
	static final String KEY_CANTIDAD = "cantidad";
	static final String KEY_DIA = "dia";
	static final String KEY_MES = "mes";
	static final String KEY_ANIO = "anio";
	static final String KEY_TIPOGASTOCORRIENTE = "tipogastocorriente";
	static final String TAG = "DBAdapter";
	/////////////////////////////////////////////////////////////////////////////////////////////////
	static final String KEY_CONCEPTO_GASTOS_FIJOS = "conceptogastosfijos";
	static final String KEY_CANTIDADFIJAMENSUALGASTO= "cantidadfijamensualgasto";
	static final String KEY_TIPOGASTOFIJO = "tipogastofijo";
	/////////////////////////////////////////////////////////////////////////////////////////////////
	static final String KEY_CONCEPTO_INGRESOS_FIJOS = "conceptoingresosfijos";
	static final String KEY_CANTIDADFIJAMENSUAL_INGRESOS_FIJOS = "cantidadfijamensualingresosfijos";
	//////////////////////////////////////////////////////////////////////////////////////////////////
	static final String KEY_CONCEPTO_INGRESOS_PUNTUALES = "conceptoingresospuntuales";
	static final String KEY_CANTIDAD_INGRESOS_PUNTUALES = "cantidadingresospuntuales";
	static final String KEY_DIA_INGRESOS_PUNTUALES = "diaingresospuntuales";
	static final String KEY_MES_INGRESOS_PUNTUALES = "mesingresospuntuales";
	static final String KEY_ANIO_INGRESOS_PUNTUALES = "anioingresospuntuales";
	//////////////////////////////////////////////////////////////////////////////////////////////////
	
	static final String DATABASE_NAME = "MisCuentas.db";
	static final String DATABASE_TABLE_GASTOS_CORRIENTES = "gastospuntuales";
	static final String DATABASE_TABLE_GASTOS_FIJOS = "gastosfijos";
	static final String DATABASE_TABLE_INGRESOS_FIJOS = "ingresosfijos";
	static final String DATABASE_TABLE_INGRESOS_PUNTUALES = "ingresospuntuales";

	static final int DATABASE_VERSION = 1;
	
	//////////////////////////////////
	//////////////////////////////////
	/////creacion de las tablas
	static final String DATABASE_CREATE = "create table gastospuntuales(_id integer primary key autoincrement,"
			+"concepto TEXT,cantidad REAL,dia INTEGER,mes INTEGER, anio INTEGER,tipogastocorriente TEXT);";
	static final String DATABASE_CREATE_1 = "create table gastosFijos(_id integer primary key autoincrement,"
			+"conceptogastosfijos TEXT,cantidadfijamensualgasto REAL,tipogastofijo TEXT);";
	static final String DATABASE_CREATE_2 = "create table ingresosFijos(_id integer primary key autoincrement,"
			+"conceptoingresosfijos TEXT,cantidadfijamensualingresosfijos REAL);";
	static final String DATABASE_CREATE_3 = "create table ingresospuntuales(_id integer primary key autoincrement,"
			+"conceptoingresospuntuales TEXT,cantidadingresospuntuales REAL,diaingresospuntuales INTEGER," +
			" mesingresospuntuales INTEGER, anioingresospuntuales INTEGER);";
	/////final creacion de las tablas
	//////////////////////////////////
	//////////////////////////////////
	final Context context;
	
	DatabaseHelper DBHelper;
	SQLiteDatabase db;
	
	public DBAdapterMisCuentas(Context ctx){
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}//fin DBAdapter
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
		DatabaseHelper(Context context){
			super(context,DATABASE_NAME,null,DATABASE_VERSION);
		}//fin constructor
		
		public void onCreate(SQLiteDatabase db){
			try{
				db.execSQL(DATABASE_CREATE);
				db.execSQL(DATABASE_CREATE_1);
				db.execSQL(DATABASE_CREATE_2);
				db.execSQL(DATABASE_CREATE_3);
			}catch (SQLException e){//fin try
				e.printStackTrace();
			}
		}//fin onCreate

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG,"Upgrading database from version "+ oldVersion + "to "
					+ newVersion +",which will destroy all old data");
			db.execSQL("DROP TABLE IF EXIST gastos");
			onCreate(db);
			
		}//fin onUpgrade
		
	}//fin clase DatabaseHelper
	
	//--abre la base de datos--//
	public DBAdapterMisCuentas open() throws SQLException{
		db = DBHelper.getWritableDatabase();
		return this;
	}//fin open
	
	//--cierra la base de datos--//
	public void close(){
		DBHelper.close();
	}//fin close
	
	public void nuevoGasto(double cantidad,String concepto,int dia,int mes,int anio,String tipogasto){
		ContentValues contenedorValores = new ContentValues();
		contenedorValores.put(KEY_CONCEPTO,concepto);
		contenedorValores.put(KEY_CANTIDAD,cantidad);
		contenedorValores.put(KEY_DIA, dia);
		contenedorValores.put(KEY_MES, mes);
		contenedorValores.put(KEY_ANIO, anio);
		contenedorValores.put(KEY_TIPOGASTOCORRIENTE, tipogasto);
		db.insert(DATABASE_TABLE_GASTOS_CORRIENTES, null,contenedorValores);
	}//fin nuevoGastoFijo
	
	public void eliminarGasto(String concepto,String dia,String mes, String anio){
		String[] args = new String[]{concepto,dia,mes,anio};
		db.execSQL("DELETE FROM gastospuntuales WHERE concepto=? and dia=? and mes=? and anio=?" ,args);
	}//final eliminarGastoFijo

	public double devuelveTotalGastosCorrientesMes(int mes,int anio) {
		String[]campos = new String[]{"cantidad","mes","anio"};
		Cursor c = db.query(DATABASE_TABLE_GASTOS_CORRIENTES,campos,null,null,null,null,null);

		double total = 0;//aqui sumo el valor total de todos los ingresos fijos
		mes = mes +1;//esto es porque el telefono a enero le da valor 0
		
		if (c.moveToFirst()){
			do{
				if(c.getInt(1)==mes){
					if (c.getInt(2)==anio){
						total = total + c.getDouble(0);
					}
				}
			}while(c.moveToNext());
		}//fin if
		
		c.close();//cierro el cursor
		return total;
	}//final devuelve total gastos corrientesmes
	

	public boolean compruebaSiEsta(String conceptod,int diad,int mesd,int aniod) {
		boolean esta=false;
		//campos a los que accedemos
		String[] campos = new String[]{"concepto","cantidad","dia","mes","anio"};
		//condicion que se busca en la base de datos
		String[] args = new String[]{conceptod};
		//se hace la busqueda 
		Cursor c = db.query(DATABASE_TABLE_GASTOS_CORRIENTES, campos,"concepto=?",args,null,null,null );
		if (c.moveToFirst()){//si se puede mover al
			do{   //primer elemento del cursor
				if (c.getInt(2)==1){//si coincide el dia
					if (c.getInt(3)==mesd){//si coincide el mes
						if (c.getInt(4)==aniod){//si coincide el año
							esta = true;
							Log.w("Pedro ","coincide el concepto"+conceptod);
						}
					}
				}
			}while (c.moveToNext());
					
		}//fin del if
		return esta;
	}//final comprueba si esta
	
	public boolean compruebaSiEstaEnIngresosPuntuales(String conceptod, int diad,
			int mesd, int aniod) {
		boolean esta=false;
		//campos a los que accedemos
		String[] campos = new String[]{"conceptoingresospuntuales","cantidadingresospuntuales","diaingresospuntuales","mesingresospuntuales","anioingresospuntuales"};
		//condicion que se busca en la base de datos
		String[] args = new String[]{conceptod};
		//se hace la busqueda 
		Cursor c = db.query(DATABASE_TABLE_INGRESOS_PUNTUALES, campos,"conceptoingresospuntuales=?",args,null,null,null );
		if (c.moveToFirst()){//si se puede mover al
			do{   //primer elemento del cursor
				if (c.getInt(2)==1){//si coincide el dia
					if (c.getInt(3)==mesd){//si coincide el mes
						if (c.getInt(4)==aniod){//si coincide el año
							esta = true;
							Log.w("Pedro ","coincide el concepto"+conceptod);
						}
					}
				}
			}while (c.moveToNext());
					
		}//fin del if
		return esta;
	}//final compruebaSiEstaEnIngresosPuntuales

	public String[] devuelveListaConceptos() {
		//campos a los que accedemos
		String[] campos = new String[]{"concepto","cantidad","dia","mes","anio"};
		//se hace la busqueda 
		Cursor c = db.query(DATABASE_TABLE_GASTOS_CORRIENTES, campos,null,null,null,null,null );
		String[] conceptos = new String[c.getCount()];
		int contador = 0;
		if (c.moveToFirst()){//si se puede mover al
			do{   //primer elemento del cursor
				conceptos[contador]=c.getString(0);
				contador=contador+1;
			}while (c.moveToNext());
					
		}//fin del if
		Set<String> unst = new HashSet<String>(Arrays.asList(conceptos));
		String[] conceptosSinRepetir = new String[unst.size()];
		unst.toArray(conceptosSinRepetir);
		return conceptosSinRepetir;
	}

	public DatoGastoHoy[] listaGastoHoy(int di,int me,int an) {
		//--campos a los que accederemos--//
		String[] campos = new String[]{"concepto","cantidad","dia","mes","anio","tipogastocorriente"};
		//condicion que se busca en la base de datos
		String dia1 = String.valueOf(di).toString();
		String mes1 = String.valueOf(me).toString();
		String anio1 = String.valueOf(an).toString();
		String[] args = new String[]{dia1,mes1,anio1};
		//--se hace la busqueda--//
		Cursor c = db.query(DATABASE_TABLE_GASTOS_CORRIENTES, campos,"dia=? and mes=? and anio=?",args,null,null,null );
		//--crear array de objetos gastos--//
		DatoGastoHoy[] datos = new DatoGastoHoy[c.getCount()];
						
		//arraylis de datos habitaciones
		ArrayList<DatoGastoHoy> datosarraylist = new ArrayList<DatoGastoHoy>();
						
		if (c.moveToFirst()){//si se puede mover al
			do{   //primer elemento del cursor
				datosarraylist.add(new DatoGastoHoy(c.getString(0),c.getString(1),c.getString(5)));
			}while (c.moveToNext());
							
			//se transfiere el contenido del arraylist al array de datos
			datosarraylist.toArray(datos);
							
		}//fin del if
					
		c.close();
		return datos;
	}

	public ElementoGastoCompuesto[] devuelveArrayElementoGastoCompuesto() {
		ElementoGastoCompuesto[] datos;
		ArrayList<ElementoGastoCompuesto> datosarraylist;
		//Campos de la base de datos a los que queremos acceder
    	String[] campos = new String[]{"concepto","cantidad","dia","mes","anio","tipogastocorriente"};
		
    	//hacer la busqueda
    	Cursor c = db.query("gastospuntuales",campos,null,null,null,null,null);
    	//contar el numero de elementos que ha dado la busqueda para inicializar el array
    	c.getCount();
        datos = new ElementoGastoCompuesto[c.getCount()];
		datosarraylist=new ArrayList<ElementoGastoCompuesto>();

		if (c.moveToLast()){//comprobar que la tabla no esta vacia
		do{
			datosarraylist.add(new ElementoGastoCompuesto(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5)));
		}while(c.moveToPrevious());
		
		//transferimos el valor de arraylist al array de datos
    	datosarraylist.toArray(datos);
		}
		return datos;
	}//final ElementoGastoCompuesto
	
	public ElementoGastoCompuesto[] devuelveArrayElementoGastoCompuestoDandoAnioYTipoGasto(String anibuscado,String tipogastobuscado) {
		ElementoGastoCompuesto[] datos;
		ArrayList<ElementoGastoCompuesto> datosarraylist;
		
		//Campos de la base de datos a los que queremos acceder
    	String[] campos = new String[]{"concepto","cantidad","dia","mes","anio","tipogastocorriente"};
    	
    	//se busca que coincidan en la base de datos el anio y el concepto
    	String[] args = new String[]{anibuscado,tipogastobuscado};
    	
    	//hacer la busqueda
		Cursor c = db.query(DATABASE_TABLE_GASTOS_CORRIENTES, campos,"anio=? and tipogastocorriente=?",args,null,null,null );
    	
    	//contar el numero de elementos que ha dado la busqueda para inicializar el array
    	c.getCount();
        datos = new ElementoGastoCompuesto[c.getCount()];
		datosarraylist=new ArrayList<ElementoGastoCompuesto>();

		if (c.moveToFirst()){//comprobar que la tabla no esta vacia
		do{
			datosarraylist.add(new ElementoGastoCompuesto(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5)));
		}while(c.moveToNext());
		
		//transferimos el valor de arraylist al array de datos
    	datosarraylist.toArray(datos);
		}
		return datos;
	}//final devuelveArrayElementoGastoCompuestoDandoAnioYConcepto
	
	public ElementoGastoCompuesto[] devuelveArrayElementoGastoCompuestoDandoAnioYTipoGastoYMes(
			String anibuscado, String tipogastobuscado, String mesbuscado) {
		ElementoGastoCompuesto[] datos;
		ArrayList<ElementoGastoCompuesto> datosarraylist;
		
		//Campos de la base de datos a los que queremos acceder
    	String[] campos = new String[]{"concepto","cantidad","dia","mes","anio","tipogastocorriente"};
    	
    	//se busca que coincidan en la base de datos el a�o y el concepto
    	String[] args = new String[]{anibuscado,tipogastobuscado,mesbuscado};
    	
    	//hacer la busqueda
		Cursor c = db.query(DATABASE_TABLE_GASTOS_CORRIENTES, campos,"anio=? and tipogastocorriente=? and mes=?",args,null,null,null );
    	
    	//contar el numero de elementos que ha dado la busqueda para inicializar el array
    	c.getCount();
        datos = new ElementoGastoCompuesto[c.getCount()];
		datosarraylist=new ArrayList<ElementoGastoCompuesto>();

		if (c.moveToFirst()){//comprobar que la tabla no esta vacia
		do{
			datosarraylist.add(new ElementoGastoCompuesto(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5)));
		}while(c.moveToNext());
		
		//transferimos el valor de arraylist al array de datos
    	datosarraylist.toArray(datos);
		}
		return datos;
	}//final devuelveArrayElementoGastoCompuestoDandoAnioYTipoGastoYMes
	
	public ElementoGastoCompuesto[] devuelveArrayElementoGastoCompuestoDeUnDia(
			int diaEleg,int mesEleg,int anioEleg) {
		ElementoGastoCompuesto[] datos;
		ArrayList<ElementoGastoCompuesto> datosarraylist;
		//Campos de la base de datos a los que queremos acceder
    	String[] campos = new String[]{"concepto","cantidad","dia","mes","anio","tipogastocorriente"};
    	//valores que se busca coincidencia
    	String diaElegido = String.valueOf(diaEleg);
    	String mesElegido = String.valueOf(mesEleg);
    	String anioElegido = String.valueOf(anioEleg);
    	String[] args = new String[]{diaElegido,mesElegido,anioElegido};
		//se hace la busqueda 
		Cursor c = db.query(DATABASE_TABLE_GASTOS_CORRIENTES, campos,"dia=? and mes=? and anio=?",args,null,null,null );
    	//contar el numero de elementos que ha dado la busqueda para inicializar el array
    	c.getCount();
        datos = new ElementoGastoCompuesto[c.getCount()];
		datosarraylist=new ArrayList<ElementoGastoCompuesto>();

		if (c.moveToLast()){//comprobar que la tabla no esta vacia
		do{
			datosarraylist.add(new ElementoGastoCompuesto(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5)));
		}while(c.moveToPrevious());
		
		//transferimos el valor de arraylist al array de datos
    	datosarraylist.toArray(datos);
		}
		return datos;
	}//final devuelveArrayElementoGastoCompuesto

	public double devuelveTotalGastosCorrientesHoy(int dia, int mes, int anio) {
		String[]campos = new String[]{"cantidad","dia","mes","anio"};
		Cursor c = db.query(DATABASE_TABLE_GASTOS_CORRIENTES,campos,null,null,null,null,null);
		
		mes = mes +1;
		double total = 0;//aqui sumo el valor total de todos los ingresos fijos
		
		if (c.moveToFirst()){
			do{
				if (c.getInt(1)==dia){
					if(c.getInt(2)==mes){
						if (c.getInt(3)==anio){
							total = total + c.getDouble(0);
						}
					}
				}
			}while(c.moveToNext());
		}//fin if
		
		c.close();//cierro el cursor
		return total;
	}//final devuelve total gastos corrientes hoy

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	public void nuevoGastoFijo(double cantidad,String concepto,String tipoGastoFijo){
		ContentValues contenedorValores = new ContentValues();
		contenedorValores.put(KEY_CONCEPTO_GASTOS_FIJOS,concepto);
		contenedorValores.put(KEY_CANTIDADFIJAMENSUALGASTO,cantidad);
		contenedorValores.put(KEY_TIPOGASTOFIJO,tipoGastoFijo);
		db.insert(DATABASE_TABLE_GASTOS_FIJOS, null,contenedorValores);
	}//fin nuevoGastoFijo
	
	public void eliminarGastoFijo(String concepto){
		String[] args = new String[]{concepto};
		db.execSQL("DELETE FROM gastosFijos WHERE conceptogastosfijos=? " ,args);
	}//final eliminarGastoFijo

	public double devuelveTotalGastosFijos() {
		String[]campos = new String[]{"cantidadfijamensualgasto"};
		Cursor c = db.query(DATABASE_TABLE_GASTOS_FIJOS,campos,null,null,null,null,null);
		
		double total = 0;//aqui sumo el valor total de todos los ingresos fijos
		if (c.moveToFirst()){
			do{
				total = total + c.getDouble(0);
			}while(c.moveToNext());
		}//fin if
		
		c.close();//cierro el cursor
		return total;
	}

	//devuelve un array de string con los datos de los gastos fijos mensuales
	public String[] devuelveArrayDeGastosFijos() {
		String[] campos = new String[]{"conceptogastosfijos","cantidadfijamensualgasto"};
		Cursor c = db.query(DATABASE_TABLE_GASTOS_FIJOS,campos,null,null,null,null,null,null);
		String[] conceptos = new String[c.getCount()];
		int contador = 0;
		if (c.moveToFirst()){
			do{
				conceptos[contador]=c.getString(0);
				contador=contador+1;
			}while(c.moveToNext());
		}//fin if
		return conceptos;
	}


	public double devuelveCantidadGastosFijos(String concepto) {
		double cantidad=0;
		//campos a los que accedemos
		String[] campos = new String[]{"conceptogastosfijos","cantidadfijamensualgasto"};
		//condicion que se busca en la base de datos
		String[] args = new String[]{concepto};
		//se hace la busqueda 
		Cursor c = db.query(DATABASE_TABLE_GASTOS_FIJOS, campos,"conceptogastosfijos=?",args,null,null,null );
		if (c.moveToFirst()){
			do{
				cantidad = c.getDouble(1);
			}while(c.moveToNext());
		}//fin if
		return cantidad;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void nuevoIngresoFijo(double cantidad,String concepto){
		ContentValues contenedorValores = new ContentValues();
		contenedorValores.put(KEY_CONCEPTO_INGRESOS_FIJOS,concepto);
		contenedorValores.put(KEY_CANTIDADFIJAMENSUAL_INGRESOS_FIJOS,cantidad);
		db.insert(DATABASE_TABLE_INGRESOS_FIJOS, null,contenedorValores);
	}//fin nuevoGastoFijo
	
	public void eliminarIngresoFijo(String concepto){
		String[] args = new String[]{concepto};
		db.execSQL("DELETE FROM ingresosfijos WHERE conceptoingresosfijos=? " ,args);
	}//final eliminarIngresoFijo
	
	public double devuelveTotalIngresosFijos(){
		String[]campos = new String[]{"cantidadfijamensualingresosfijos"};
		Cursor c = db.query(DATABASE_TABLE_INGRESOS_FIJOS,campos,null,null,null,null,null);
		
		double total = 0;//aqui sumo el valor total de todos los ingresos fijos
		if (c.moveToFirst()){
			do{
				total = total + Double.parseDouble((c.getString(0)));
			}while(c.moveToNext());
		}//fin if
		
		c.close();//cierro el cursor
		return total;
	}//final devuelveTotalIngresosFijos
	
	//devuelve un array de string con los datos de los ingresos fijos mensuales
		public String[] devuelveArrayDeIngresosFijos() {
			String[] campos = new String[]{"conceptoingresosfijos","cantidadfijamensualingresosfijos"};
			Cursor c = db.query(DATABASE_TABLE_INGRESOS_FIJOS,campos,null,null,null,null,null,null);
			String[] conceptos = new String[c.getCount()];
			int contador = 0;
			if (c.moveToFirst()){
				do{
					conceptos[contador]=c.getString(0);
					contador=contador+1;
				}while(c.moveToNext());
			}//fin if
			return conceptos;
		}

		public double devuelveCantidadIngresosFijos(String concepto) {
			double cantidad=0;
			//campos a los que accedemos
			String[] campos = new String[]{"conceptoingresosfijos","cantidadfijamensualingresosfijos"};
			//condicion que se busca en la base de datos
			String[] args = new String[]{concepto};
			//se hace la busqueda 
			Cursor c = db.query(DATABASE_TABLE_INGRESOS_FIJOS, campos,"conceptoingresosfijos=?",args,null,null,null );
			if (c.moveToFirst()){
				do{
					cantidad = c.getDouble(1);
				}while(c.moveToNext());
			}//fin if
			return cantidad;
		}
	//////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////////
		public void nuevoIngresoPuntual(double cantidad,String concepto,int dia,int mes, int anio){
			ContentValues contenedorValores = new ContentValues();
			contenedorValores.put(KEY_CONCEPTO_INGRESOS_PUNTUALES,concepto);
			contenedorValores.put(KEY_CANTIDAD_INGRESOS_PUNTUALES,cantidad);
			contenedorValores.put(KEY_DIA_INGRESOS_PUNTUALES,dia);
			contenedorValores.put(KEY_MES_INGRESOS_PUNTUALES, mes);
			contenedorValores.put(KEY_ANIO_INGRESOS_PUNTUALES,anio);
			db.insert(DATABASE_TABLE_INGRESOS_PUNTUALES, null,contenedorValores);
		}//fin nuevoGastoFijo
		
		public void eliminarIngresoPuntual(String concepto,String dia,String mes,String anio){
			String[] args = new String[]{concepto,dia,mes,anio};
			db.execSQL("DELETE FROM ingresospuntuales WHERE conceptoingresospuntuales=? and diaingresospuntuales=? and mesingresospuntuales=? and anioingresospuntuales=?" ,args);
		}//final eliminarGastoFijo

		public double devuelveTotalIngresosPuntualesDelMes(int mes,int anio) {
			String[]campos = new String[]{"cantidadingresospuntuales","mesingresospuntuales","anioingresospuntuales"};
			Cursor c = db.query(DATABASE_TABLE_INGRESOS_PUNTUALES,campos,null,null,null,null,null);
			
			double total = 0;//aqui sumo el valor total de todos los ingresos fijos
			mes = mes +1;//esto es porque el telefono a enero le da valor 0
			
			if (c.moveToFirst()){
				do{
					if(c.getInt(1)==mes){
						if (c.getInt(2)==anio){
							total = total + c.getDouble(0);
						}
					}
				}while(c.moveToNext());
			}//fin if
			
			c.close();//cierro el cursor
			return total;
		}//final devuelve total ingresos puntuales del mes
		
		public double devuelveTotalIngresosPuntualesDelAnio(int anio) {
			String[]campos = new String[]{"cantidadingresospuntuales","mesingresospuntuales","anioingresospuntuales"};
			Cursor c = db.query(DATABASE_TABLE_INGRESOS_PUNTUALES,campos,null,null,null,null,null);
			
			double total = 0;//aqui sumo el valor total de todos los ingresos fijos
			
			if (c.moveToFirst()){
				do{
					if (c.getInt(2)==anio){
						total = total + c.getDouble(0);
					}
				}while(c.moveToNext());
			}//fin if
			
			c.close();//cierro el cursor
			return total;
		}//final devuelve total ingresos puntuales del mes
		
		public double devuelveTotalIngresosPuntualesDelDia(int dia,int mes,int anio) {
			String[]campos = new String[]{"cantidadingresospuntuales","diaingresospuntuales","mesingresospuntuales","anioingresospuntuales"};
			Cursor c = db.query(DATABASE_TABLE_INGRESOS_PUNTUALES,campos,null,null,null,null,null);
			
			double total = 0;//aqui sumo el valor total de todos los ingresos puntuales
			mes = mes +1;//esto es porque el telefono a enero le da valor 0
			
			if (c.moveToFirst()){
				do{
					if(c.getInt(1)==dia){
						if (c.getInt(2)==mes){
							if(c.getInt(3)==anio){
								total = total + c.getDouble(0);
							}
						}
					}
				}while(c.moveToNext());
			}//fin if
			
			c.close();//cierro el cursor
			return total;
		}//final devuelve total ingresos puntuales del mes

		public ElementoGastoCompuesto[] devuelveArrayElementoGastoCompuestoIngresosPuntuales() {
			ElementoGastoCompuesto[] datos;
			ArrayList<ElementoGastoCompuesto> datosarraylist;
			//Campos de la base de datos a los que queremos acceder
	    	String[] campos = new String[]{"conceptoingresospuntuales","cantidadingresospuntuales","diaingresospuntuales","mesingresospuntuales","anioingresospuntuales"};
			
	    	//hacer la busqueda
	    	Cursor c = db.query("ingresospuntuales",campos,null,null,null,null,null);
	    	//contar el numero de elementos que ha dado la busqueda para inicializar el array
	    	c.getCount();
	        datos = new ElementoGastoCompuesto[c.getCount()];
			datosarraylist=new ArrayList<ElementoGastoCompuesto>();
			if (c.moveToLast()){//comprobar que la tabla no esta vacia
	    		do{
	    			datosarraylist.add(new ElementoGastoCompuesto(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),"ingreso"));
	    		}while(c.moveToPrevious());
	    		
	    		//transferimos el valor de arraylist al array de datos
	        	datosarraylist.toArray(datos);
			}
			return datos;
		}//final elementogastocompuesto
		
		public ElementoGastoCompuesto[] devuelveArrayElementoGastoCompuestoIngresosPuntualesDeUnDia(
				int diaEleg, int mesEleg, int anioEleg) {
			ElementoGastoCompuesto[] datos;
			ArrayList<ElementoGastoCompuesto> datosarraylist;
			//Campos de la base de datos a los que queremos acceder
	    	String[] campos = new String[]{"conceptoingresospuntuales","cantidadingresospuntuales","diaingresospuntuales","mesingresospuntuales","anioingresospuntuales"};
	    	//valores que se busca coincidencia
	    	String diaElegido = String.valueOf(diaEleg);
	    	String mesElegido = String.valueOf(mesEleg);
	    	String anioElegido = String.valueOf(anioEleg);
	    	String[] args = new String[]{diaElegido,mesElegido,anioElegido};
			//se hace la busqueda 
			Cursor c = db.query(DATABASE_TABLE_INGRESOS_PUNTUALES, campos,"diaingresospuntuales=? and mesingresospuntuales=? and anioingresospuntuales=?",args,null,null,null );
	    	//contar el numero de elementos que ha dado la busqueda para inicializar el array
	    	c.getCount();
	        datos = new ElementoGastoCompuesto[c.getCount()];
			datosarraylist=new ArrayList<ElementoGastoCompuesto>();
			if (c.moveToLast()){//comprobar que la tabla no esta vacia
	    		do{
	    			datosarraylist.add(new ElementoGastoCompuesto(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),"ingreso"));
	    		}while(c.moveToPrevious());
	    		
	    		//transferimos el valor de arraylist al array de datos
	        	datosarraylist.toArray(datos);
			}
			return datos;
		}//final devuelveArrayElementoGastoCompuestoIngresosPuntualesDeUnDia

		public String devuelveTipoGastoFijo(String conceptoBuscado) {
			String tipoGasto="";
			//campos a los que accedemos
			String[] campos = new String[]{"conceptogastosfijos","tipogastofijo"};
			//se hace la busqueda 
			Cursor c = db.query(DATABASE_TABLE_GASTOS_FIJOS, campos,null,null,null,null,null );
						
			if (c.moveToFirst()){
				do{
					if(c.getString(0).equalsIgnoreCase(conceptoBuscado)){
						tipoGasto = c.getString(1);
					}
				}while(c.moveToNext());
			}//fin if
			return tipoGasto;
		}

		///este metodo devuelve el total de � indicando el a�o y el tipo de gasto
		public Double devuelveTotalDandoTipoGastoYAnio(String tipogasto, int anio) {
			Double total=0.0;//valor total que devuelve
			//campos a los que accederemos en la base de datos
			String[]campos = new String[]{"cantidad","anio","tipogastocorriente"};
			//condiciones  que se buscan en la base de datos
			String an = String.valueOf(anio);
			String[] args = new String[]{an,tipogasto};
			//--se hace la busqueda--//
			Cursor c = db.query(DATABASE_TABLE_GASTOS_CORRIENTES, campos,"anio=? and tipogastocorriente=?",args,null,null,null );
			if (c.moveToFirst()){
				do{
					total = total + c.getDouble(0);
				}while(c.moveToNext());
			}//fin if
			total = Math.rint(total*100)/100;//redondeo decimales a solo 2 cifras
			return total;
		}//final del metodo devuelveTotalDandoTipoGastoYAnio
		
		
		///este metodo devuelve el total de � indicando el a�o y el tipo de gasto
		public Double devuelveTotalDandoTipoGastoYAnioYMes(String tipogasto,
				int anio, int mes) {
			Double total=0.0;//valor total que devuelve
			//campos a los que accederemos en la base de datos
			String[]campos = new String[]{"cantidad","anio","tipogastocorriente","mes"};
			//condiciones  que se buscan en la base de datos
			String an = String.valueOf(anio);
			String me = String.valueOf(mes);
			String[] args = new String[]{an,tipogasto,me};
			//--se hace la busqueda--//
			Cursor c = db.query(DATABASE_TABLE_GASTOS_CORRIENTES, campos,"anio=? and tipogastocorriente=? and mes=?",args,null,null,null );
			if (c.moveToFirst()){
				do{
					total = total + c.getDouble(0);
				}while(c.moveToNext());
			}//fin if
			total = Math.rint(total*100)/100;//redondeo decimales a solo 2 cifras
			return total;
		}//final devuelveTotalDandoTipoGastoYAnioYMes


		public String[] devuelveListaAnios() {
			
			ArrayList<String>listaAnios = new ArrayList<String>();
			
			//campos a los que se accede
			String[] campos = new String[]{"anio"};
			//--se hace la busqueda--//
			Cursor c = db.query(DATABASE_TABLE_GASTOS_CORRIENTES, campos,null,null,null,null,null );
					
			if (c.moveToFirst()){
				do{
					listaAnios.add(c.getString(0));
				}while(c.moveToNext());
			}//fin if
			
			//limpiar la lista
			Set<String> s = new LinkedHashSet<String>(listaAnios);
			listaAnios.clear();
			listaAnios.addAll(s);
			
			String[] datos = new String[listaAnios.size()];
			listaAnios.toArray(datos);
			return datos;
		}//final devuelveListaAnios;

		public int devuelveMesDeInicioDadoUnAnio(String ani) {
			int mes=15;
			//campos a los que se accede
			String[] campos = new String[]{"anio","mes"};
			//--se hace la busqueda--//
			Cursor c = db.query(DATABASE_TABLE_GASTOS_CORRIENTES, campos,null,null,null,null,null );
			if (c.moveToFirst()){
				do{
					if (c.getString(0).equals(ani)){
						if(c.getInt(1)<=mes){
							mes=c.getInt(1);
						}
					}
				}while(c.moveToNext());
			}//fin if
			return mes;
		}//final devuelveMesDeInicioDadoUnAnio

		public int devuelveMesFinalDadoUnAnio(String ani) {
			int mes=0;
			//campos a los que se accede
			String[] campos = new String[]{"anio","mes"};
			//--se hace la busqueda--//
			Cursor c = db.query(DATABASE_TABLE_GASTOS_CORRIENTES, campos,null,null,null,null,null );
			if (c.moveToFirst()){
				do{
					if (c.getString(0).equals(ani)){
						if(c.getInt(1)>=mes){
							mes=c.getInt(1);
						}
					}
				}while(c.moveToNext());
			}//fin if
			return mes;
		}//final devuelveMesFinalDadoUnAnio

		/*cuenta el numero de registros en la tabla gastos corrientes*/
		public int contadorDeRegistros() {
			//campos a los que se accede
			String[] campos = new String[]{"anio","mes"};
			//--se hace la busqueda--//
			Cursor c = db.query(DATABASE_TABLE_GASTOS_CORRIENTES, campos,null,null,null,null,null );
			return c.getCount();
		}

}//fin clase DBAdapterMisCuentas
