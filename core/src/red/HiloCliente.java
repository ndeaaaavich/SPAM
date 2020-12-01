package red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;

import utiles.Global;
import utiles.Utiles;
import pantallas.*;
import personajes.EstadoMovimiento;
import personajes.Ladron;
import personajes.SpriteInfo;

public class HiloCliente extends Thread {
	
	private DatagramSocket conexion;
	private InetAddress ipServer;
	private int puerto = 42069;
	
	private boolean fin = false;
	private PantallaRonda app;
	public int tope = 9;
	public int personajesRestantes = tope;
	private float posY = 0, posX = 0;
	
	public HiloCliente(PantallaRonda app) {
		this.app = app;
		
		try {
			ipServer = InetAddress.getByName("255.255.255.255");
			conexion = new DatagramSocket();
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
		if(Global.ronda == 1) {
			System.out.println("Solicitud de conexion");
			enviarMensaje("Conexion");
		}
	}
	
	public void setApp(PantallaRonda app) {
		this.app = app;
		if(Global.guardia) enviarMensaje("Entidades");
		personajesRestantes = tope;
	}
	
	public void enviarMensaje(String msg) {
		byte[] data = msg.getBytes();
		DatagramPacket dp = new DatagramPacket(data, data.length,ipServer,puerto);
		try {
			conexion.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		do {
			byte[] data = new byte[1024];
			DatagramPacket dp = new DatagramPacket(data, data.length);
			try {
				conexion.receive(dp);
			} catch (IOException e) {
				e.printStackTrace();
			}
			procesarMensaje(dp);
		}while(!fin);
	}

	private void procesarMensaje(DatagramPacket dp) {
		String msg = (new String(dp.getData())).trim();
		
		String[] mensajeParametrizado = msg.split("%");
		
		if(mensajeParametrizado.length<2) {
			if(msg.equals("ConexionLista")) {
				Global.conexion = true;
			} else if(msg.equals("TerminaJuego")) {
				Global.terminaJuego = true;
			}
		} else {
			if(mensajeParametrizado[0].equals("OK")) {
				ipServer = dp.getAddress();
				Global.guardia = (Integer.parseInt(mensajeParametrizado[1]) == 1)?true:false;
				enviarMensaje("Entidades");
				
			}else if(mensajeParametrizado[0].equals("actualizar")  && Global.empiezaJuego && !Global.terminaRonda) {
				if(mensajeParametrizado[3].equals("G") && app.jugadorGuardia != null) {
					
					if(mensajeParametrizado[1].equals("estado")){
						app.jugadorGuardia.setEstado(EstadoMovimiento.values()[Integer.parseInt(mensajeParametrizado[2])]);
					}else {
						posX = Float.parseFloat(mensajeParametrizado[1]);
						posY = Float.parseFloat(mensajeParametrizado[2]);
						app.jugadorGuardia.setPosition(posX, posY);
					}
				}else if(mensajeParametrizado[3].equals("L") && personajesRestantes == 0){
					
					if(mensajeParametrizado[1].equals("estado")){
						app.jugadorLadron.setEstado(EstadoMovimiento.values()[Integer.parseInt(mensajeParametrizado[2])]);
					}else {
						posX = Float.parseFloat(mensajeParametrizado[1]);
						posY = Float.parseFloat(mensajeParametrizado[2]);
						app.jugadorLadron.setPosition(posX, posY);
					}
				}
				
			}else if(mensajeParametrizado[0].equals("npcs")) {
				
				//--------------------------------movimiento de los npc------------------------------------------
				if(mensajeParametrizado[1].equals("posicion") && personajesRestantes==0) {//Num de movimiento
					((PantallaRonda1)app).npcs[ Integer.parseInt(mensajeParametrizado[2]) ].setPosicion(Float.parseFloat(mensajeParametrizado[3]), 
																										Float.parseFloat(mensajeParametrizado[4]));
					
				}else if(mensajeParametrizado[1].equals("estado") && personajesRestantes==0) {
					((PantallaRonda1)app).npcs[ Integer.parseInt(mensajeParametrizado[2]) ].setEstado(EstadoMovimiento.values()
							 																		  [Integer.parseInt(mensajeParametrizado[3])] );

				}else if(mensajeParametrizado[1].equals("esperandoDialogo")) {
					((PantallaRonda1)app).npcs[Integer.parseInt(mensajeParametrizado[2])].setEsperandoDialogo(true);
					
				}else if(mensajeParametrizado[1].equals("salaRobada")) {
					((PantallaRonda1)app).npcs[Integer.parseInt(mensajeParametrizado[2])].setRobado(true);
					((PantallaRonda1)app).npcs[Integer.parseInt(mensajeParametrizado[2])].setSalaRobada(true);
					
				}else if(mensajeParametrizado[1].equals("sala") && personajesRestantes==0) {
					((PantallaRonda1)app).npcs[Integer.parseInt(mensajeParametrizado[2])].setSala(Integer.parseInt(mensajeParametrizado[3]));
				}else if(mensajeParametrizado[1].equals("derecha") && personajesRestantes==0){
					((PantallaRonda1)app).npcs[Integer.parseInt(mensajeParametrizado[2])].setDerecha(Boolean.parseBoolean(mensajeParametrizado[2]));
				}else if (mensajeParametrizado[1].equals("crear")){
				//--------------------------------creacion de los npc--------------------------------------------
					int[] apariencia = new int[]{Integer.parseInt(mensajeParametrizado[6]), 
												 Integer.parseInt(mensajeParametrizado[7]),
												 Integer.parseInt(mensajeParametrizado[8])
												};
					runnableNPC(mensajeParametrizado, apariencia);
				}
				
			}else if (mensajeParametrizado[0].equals("ladron")) {
				if(mensajeParametrizado[1].equals("gano")) {
					Global.puntajeLadron++;
					Global.ronda = Integer.parseInt(mensajeParametrizado[2]);
					Global.terminaRonda = true;
				}else if(mensajeParametrizado[1].equals("perdio")) {
					Global.puntajeGuardia++;
					Global.ronda = Integer.parseInt(mensajeParametrizado[2]);
					Global.terminaRonda = true;
				}else if(mensajeParametrizado[1].equals("desconectado")) {
					Global.terminaJuego = true;
				}else {
					String[] mensajeParametrizado2 = mensajeParametrizado.clone();
					runnableLadron(mensajeParametrizado2);
				}
			}else if (mensajeParametrizado[0].equals("guardia")) {
				if(mensajeParametrizado[1].equals("npcDialogo")) {
					((PantallaRonda1)app).npcs[ Integer.parseInt(mensajeParametrizado[2]) ].setEsperandoDialogo(true);
					((PantallaRonda1)app).npcs[ Integer.parseInt(mensajeParametrizado[2]) ].setPista(Integer.parseInt(mensajeParametrizado[3]),Integer.parseInt(mensajeParametrizado[4]));				
				}else if(mensajeParametrizado[1].equals("gano")) {
					Global.puntajeGuardia++;
					Global.ronda = Integer.parseInt(mensajeParametrizado[2]);
					Global.terminaRonda = true;
				}else if(mensajeParametrizado[1].equals("perdio")) {
					Global.puntajeLadron++;
					Global.ronda = Integer.parseInt(mensajeParametrizado[2]);
					Global.terminaRonda = true;
				}else if(mensajeParametrizado[1].equals("desconectado")) {
					System.out.println(Utiles.pantallaMenu.viewport.getWorldWidth() + ""
							   + " " + Utiles.pantallaMenu.viewport.getWorldHeight());
					
					Utiles.pantallaMenu.viewport.setWorldSize(Utiles.pantallaMenu.viewport.getWorldWidth() * 2, 
							  								  Utiles.pantallaMenu.viewport.getWorldHeight() * 2);
					Global.terminaJuego = true;
				}
			}else if (mensajeParametrizado[0].equals("sala") && personajesRestantes==0) {
				if(mensajeParametrizado[1].equals("anterior")) {
					if(Global.guardia) {
						app.jugadorGuardia.salaAnterior = Integer.parseInt(mensajeParametrizado[2]);
					}else {
						app.jugadorLadron.salaAnterior = Integer.parseInt(mensajeParametrizado[2]);
					}
				}else if(mensajeParametrizado[1].equals("cambiar")) {
					if(Global.guardia) {
						app.jugadorGuardia.cambiarSala = Boolean.parseBoolean(mensajeParametrizado[2]);
					}else {
						app.jugadorLadron.cambiarSala = Boolean.parseBoolean(mensajeParametrizado[2]);
					}
				}else {
					if(Global.guardia) {
						app.jugadorGuardia.setSala(Integer.parseInt(mensajeParametrizado[1]));
					}else {
						app.jugadorLadron.setSala(Integer.parseInt(mensajeParametrizado[1]));
					}
				}
			}/*else if (mensajeParametrizado[0].equals("plataforma") && app instanceof PantallaRonda2) {
				((PantallaRonda2)app).plataformaMovilSprite[Integer.parseInt(mensajeParametrizado[1])].setPosition(Float.parseFloat(mensajeParametrizado[2]) ,
			 			 																					       Float.parseFloat(mensajeParametrizado[3]) );
			}else if (mensajeParametrizado[0].equals("powerUps") && app instanceof PantallaRonda2) {
				if (mensajeParametrizado[1].equals("desactivar")){
					((PantallaRonda2)app).powerUp[Integer.parseInt(mensajeParametrizado[2])].setActivo(false);
				}
				if (mensajeParametrizado[1].equals("activar")){
					((PantallaRonda2)app).powerUp[Integer.parseInt(mensajeParametrizado[2])].setActivo(true);
				}
			}*/
		}
	}

	private void runnableNPC(final String[] mensajeParametrizado2, final int[] apariencia) {
		Gdx.app.postRunnable(new Runnable(){
		    @Override
		     public void run(){
		    	((PantallaRonda1)app).crearNPC(mensajeParametrizado2, apariencia);
		     	System.out.println("Se creo el npc " + personajesRestantes);
		     	personajesRestantes-=1;
		    }
		});
	}
	
	private void runnableLadron(final String[] mensajeParametrizado2) {
		final HiloCliente hc = this;
		
		Gdx.app.postRunnable(new Runnable(){
		    @Override
		     public void run(){
	        	app.jugadorLadron = new Ladron(SpriteInfo.values()[Integer.parseInt(mensajeParametrizado2[1])].getFilename(), 
	        								   Float.parseFloat(mensajeParametrizado2[2]), 
	        								   Float.parseFloat(mensajeParametrizado2[3]),
	        								   Integer.parseInt(mensajeParametrizado2[4]));
	    		app.stage.addActor(app.jugadorLadron);
				app.stage.setKeyboardFocus( (Global.guardia)? app.jugadorGuardia : app.jugadorLadron );
				SpriteInfo.setLadron( Integer.parseInt(mensajeParametrizado2[1]) );
				System.out.println("Se creo el ladron " + personajesRestantes);
				personajesRestantes-=1;
		    }	
		});
	}
}
