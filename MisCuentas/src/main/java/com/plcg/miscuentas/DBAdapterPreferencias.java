package com.plcg.miscuentas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapterPreferencias{
	static final String KEY_ROWID = "_id";
	static final String KEY_SONIDOS = "sonidos";
	static final String KEY_COPIASEGURIDAD = "seguridad";
	
	static final String TAG = "DBAdapter";
	
	static final String DATABASE_NAME = "BaseDatosPreferencias";
	static final String DATABASE_TABLE = "preferencias";
	static final int DATABASE_VERSION = 1;
	
	static final String DATABASE_CREATE = "create table preferencias(_id integer primary key autoincrement,"
			+"sonidos BOOLEAN,seguridad BOOLEAN);";
	
	final Context context;
	
	DatabaseHelper DBHelper;
	SQLiteDatabase db;
	
	public DBAdapterPreferencias(Context ctx){
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
			}catch (SQLException e){//fin try
				e.printStackTrace();
			}
		}//fin onCreate

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG,"Upgrading database from version "+ oldVersion + "to "
					+ newVersion +",which will destroy all old data");
			db.execSQL("DROP TABLE IF EXIST sonidos");
			onCreate(db);
			
		}//fin onUpgrade
		
	}//fin clase DatabaseHelper
	
	//--abre la base de datos--//
	public DBAdapterPreferencias open() throws SQLException{
		db = DBHelper.getWritableDatabase();
		return this;
	}//fin open
	
	//--cierra la base de datos--//
	public void close(){
		DBHelper.close();
	}//fin close

	public int devuelveNumeroPreferencias() {
		//campos a los que accedemos
		String[] campos = new String[]{"sonidos"};
		//se hace la busqueda 
		Cursor c = db.query(DATABASE_TABLE, campos,null,null,null,null,null );
		
		return c.getCount();
	}

	public void inicializarPreferencias() {
		ContentValues contenedor = new ContentValues();
		contenedor.put(KEY_SONIDOS, true);
		contenedor.put(KEY_COPIASEGURIDAD, true);
		db.insert(DATABASE_TABLE, null, contenedor);
	}//iniciar preferencias

	public Boolean valorSonido() {
		boolean sonido = true; //devuelve el valor de la configuracion de sonido
		
		//campos a los que se accede
		String campos[]=new String[]{"sonidos"};
		
		//se hace la busqueda
		Cursor c = db.query(DATABASE_TABLE, campos,null,null,null,null,null);
		c.moveToFirst();
		if (c.getInt(0) == 1){
			sonido = true;
		}else{
			sonido = false;
		}
		
		return sonido;
	}//final valor Sonido
	
	public Boolean valorCopiaSeguridad() {
		boolean seguridad = true; //devuelve el valor de la configuracion de copia de Seguridad
		
		//campos a los que se accede
		String campos[]=new String[]{"seguridad"};
		
		//se hace la busqueda
		Cursor c = db.query(DATABASE_TABLE, campos,null,null,null,null,null);
		c.moveToFirst();
		if (c.getInt(0) == 1){
			seguridad = true;
		}else{
			seguridad = false;
		}
		
		return seguridad;
	}//final valor Sonido

	public void activarSonido() {
		db.execSQL("UPDATE preferencias SET sonidos=1 where sonidos=0");
	}//final activar Sonido
	
	public void activarCopiaSeguridad() {
		db.execSQL("UPDATE preferencias SET seguridad=1 where seguridad=0");
	}//final activar Sonido

	public void desactivarSonido() {
		db.execSQL("UPDATE preferencias SET sonidos=0 where sonidos=1");
	}//final desactivarSonido
	
	public void desactivarCopiaSeguridad() {
		db.execSQL("UPDATE preferencias SET seguridad=0 where seguridad=1");
	}//final desactivarSonido
	
}//fin clase DBAdapter
