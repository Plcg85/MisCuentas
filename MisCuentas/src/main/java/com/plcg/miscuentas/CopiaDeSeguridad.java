package com.plcg.miscuentas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.os.Environment;

public class CopiaDeSeguridad {
	
	public static void CrearCopiaSeguridad(Context context){
		
		DBAdapterPreferencias dbP = new DBAdapterPreferencias(context);
		dbP.open();
		boolean copia = dbP.valorCopiaSeguridad();
		dbP.close();
		
		if (copia==true){
			//copio la base de datos a la sd
			try {
				File sd = Environment.getExternalStorageDirectory();
				File data = Environment.getDataDirectory();
					    
					if (sd.canWrite()) {
					    	
						String packageName  = "com.plcg.miscuentas";
							
						//misCuentas
					    String sourceDBName = "MisCuentas.db";
						String targetDBName = "MisCuentas";
					    String currentDBPath = "data/" + packageName + "/databases/" + sourceDBName;
					    String backupDBPath = targetDBName + ".db";
					    File currentDB = new File(data, currentDBPath);
					    File backupDB = new File(sd+"/MisCuentas/", backupDBPath);
					    @SuppressWarnings("resource")
						FileChannel src = new FileInputStream(currentDB).getChannel();
					    @SuppressWarnings("resource")
						FileChannel dst = new FileOutputStream(backupDB).getChannel();
					    dst.transferFrom(src, 0, src.size());
					    src.close();
					    dst.close();
					  	
					}//fin if se puede acceder a sd
			} catch (Exception e) {
				File f = new File(Environment.getExternalStorageDirectory()+"/MisCuentas/");
				if(f.mkdir()){
					CopiaDeSeguridad.CrearCopiaSeguridad(context);
				}
			}
		}//final si la copia esta activada
		if (copia==false){
		}//final si no esta activada la copia de seguridad
	}//final CrearCopiaSeguridad
	
}//final clase
