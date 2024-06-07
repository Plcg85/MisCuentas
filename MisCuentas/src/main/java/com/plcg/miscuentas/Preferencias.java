package com.plcg.miscuentas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Preferencias extends Activity{

	//private TextView textRestaurar;
	private ToggleButton togleSonido,togleCopiaSeguridad;
	private Button restaurar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferencias);
		
		//obtengo los controles
		togleSonido = (ToggleButton)findViewById(R.id.idToogleSonido);
		togleCopiaSeguridad = (ToggleButton)findViewById(R.id.idToogleCopiaSeguridad);
		restaurar = (Button)findViewById(R.id.btnRestaurar);
		
		iniciarVistas();//se leen los valores en la base de datos y se aplican a las vistas
		
		
		//evento toggle sonido
		togleSonido.setOnClickListener(new ToggleButton.OnClickListener(){

			@Override
			public void onClick(View v) {
				if (togleSonido.isChecked()){
							
					DBAdapterPreferencias dbP = new DBAdapterPreferencias(Preferencias.this);
					dbP.open();
					dbP.activarSonido();
					dbP.close();
					Sonidos.sonar(Preferencias.this, "botoncomun");
				}else{
					DBAdapterPreferencias dbP = new DBAdapterPreferencias(Preferencias.this);
					dbP.open();
					dbP.desactivarSonido();
					dbP.close();
				}
			}//final onclick
		});//final clase interna
				
		
		//evento del boton gasto Corriente
		restaurar.setOnClickListener(new View.OnClickListener() {
											
			@Override
			public void onClick(View v) {
				crearMenuOpciones();
				
				Sonidos.sonar(Preferencias.this,"botoncomun");
				
			}//finalonClick

			private void crearMenuOpciones() {
				//creacion del cuadro de dialogo
				AlertDialog.Builder dialogoOpc = new AlertDialog.Builder(Preferencias.this);
				
				String idioma = Locale.getDefault().getDisplayLanguage();
				if (idioma.equalsIgnoreCase("English")){
					dialogoOpc.setTitle("!!! ATTENTION !!!");
			  		dialogoOpc.setMessage("You are about to restore a saved backup on your device, all existing data" +
			  				"will be replaced by the apllication of your backup ");
				}else{
					dialogoOpc.setTitle("!!! ATENCION !!!");
			  		dialogoOpc.setMessage("Esta a punto de restaurar una copia de seguridad guardada en su " +
			  				"dispositivo, todos los datos actuales de la aplicacion seran sustituidos por " +
			  				"los de su copia de seguridad ");
				}
		  		
		  		    	
		  		dialogoOpc.setPositiveButton("Si",new OnClickListener(){
		  		    		
		  			@Override
		  			public void onClick(DialogInterface dialog, int which) {
		  				restaurar();
		  			}//fin onClick

					private void restaurar() {
						try {
				            File sd = Environment.getExternalStorageDirectory();
				            File data = Environment.getDataDirectory();

				            if (sd.canWrite()) {
//				                String currentDBPath = "//data//" + "PackageName"
//				                        + "//databases//" + "DatabaseName";
				            	String currentDBPath = "data/com.plcg.miscuentas/databases/MisCuentas.db";
				                String backupDBPath = "/MisCuentas/MisCuentas.db";
				                File backupDB = new File(data, currentDBPath);
				                File currentDB = new File(sd, backupDBPath);

				                @SuppressWarnings("resource")
								FileChannel src = new FileInputStream(currentDB).getChannel();
				                @SuppressWarnings("resource")
								FileChannel dst = new FileOutputStream(backupDB).getChannel();
				                dst.transferFrom(src, 0, src.size());
				                src.close();
				                dst.close();
				                
				                Intent intent = new Intent(Preferencias.this,MainActivity.class);
				                startActivity(intent);
				                
				                Toast.makeText(getBaseContext(), backupDB.toString(),


				                		Toast.LENGTH_LONG).show();
				            	}
							} catch (Exception e) {
								Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)
				                .show();
						}
						
					}//final de restaurar
		  		});//fin clase interna
		  		dialogoOpc.setNegativeButton("No", new OnClickListener(){

		  			@Override
		  			public void onClick(DialogInterface dialog, int which) {
		  				dialog.cancel();
		  			}
		  		});
		  		dialogoOpc.show();
				
			}//final menu opciones
		});//final clase interna
		
		//evento toggle copia de seguridad
		togleCopiaSeguridad.setOnClickListener(new ToggleButton.OnClickListener(){

			@Override
			public void onClick(View v) {
				if (togleCopiaSeguridad.isChecked()){
									
					DBAdapterPreferencias dbP = new DBAdapterPreferencias(Preferencias.this);
					dbP.open();
					dbP.activarCopiaSeguridad();
					dbP.close();
					Sonidos.sonar(Preferencias.this, "botoncomun");
					
					String idioma = Locale.getDefault().getDisplayLanguage();
					if (idioma.equalsIgnoreCase("English")){
						Toast.makeText(Preferencias.this, "The copy is created in sdcard/MisCuentas/",
								Toast.LENGTH_LONG).show();
					}
					if(idioma.equalsIgnoreCase("Español")){
						Toast.makeText(Preferencias.this, "La copia se creara en sdcard/MisCuentas/",
								Toast.LENGTH_LONG).show();
					}
					
				}else{
					DBAdapterPreferencias dbP = new DBAdapterPreferencias(Preferencias.this);
					dbP.open();
					dbP.desactivarCopiaSeguridad();
					dbP.close();
					Sonidos.sonar(Preferencias.this, "botoncomun");
				}
			}//final onclick
		});//final clase interna
		
	}//final oncreate

	private void iniciarVistas() {
		//obtengo el valor de la variable sonido en la bd
		DBAdapterPreferencias dbP = new DBAdapterPreferencias(this);
		dbP.open();
		Boolean sonido = dbP.valorSonido();
		Boolean seguridad = dbP.valorCopiaSeguridad();
		dbP.close();
				
		//seteo el valor en el checkBox
		//checkSonido.setChecked(sonido);
		togleSonido.setChecked(sonido);
		togleCopiaSeguridad.setChecked(seguridad);
	}//final iniciarVistas
	
	
	
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
			Sonidos.sonar(Preferencias.this,"botoncasa");
			
			Intent i = new Intent(this,MainActivity.class);
			startActivity(i);
			return true;
		}//final switch
		return false;
	}//final menuchoice
}//final clase preferencias