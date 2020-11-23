package red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import eventos.InterfaceRobable;
import utiles.Global;
import utiles.Utiles;
import pantallas.PantallaRonda;
import pantallas.PantallaRonda1;
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
	private float posYant = 0, posXant = 0;
	
	public HiloCliente(PantallaRonda app) {
		this.app = app;
		
		try {
			ipServer = InetAddress.getByName("255.255.255.255");
			conexion = new DatagramSocket();
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
		enviarMensaje("Conexion");
		System.out.println("Solicitud de conexion");
	}
	
	public void setApp(PantallaRonda app) {
		this.app = app;
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
			}// else if(msg.equals("TerminaJuego")) {
//				Global.terminaJuego = true;
//			}
		} else {
			if(mensajeParametrizado[0].equals("OK")) {
				ipServer = dp.getAddress();
				Global.guardia = (Integer.parseInt(mensajeParametrizado[1]) == 1)?true:false;
		
			}else if(mensajeParametrizado[0].equals("actualizar")) {
				if(mensajeParametrizado[3].equals("G")) {
					posX = Float.parseFloat(mensajeParametrizado[1]);
					posY = Float.parseFloat(mensajeParametrizado[2]);
					app.jugadorGuardia.setPosition(posX, posY);
				}else if(mensajeParametrizado[3].equals("L") && Global.empiezaJuego){//si empiezaJuego = true me aseguro que el ladron exista
					posX = Float.parseFloat(mensajeParametrizado[1]);
					posY = Float.parseFloat(mensajeParametrizado[2]);
					app.jugadorLadron.setPosition(posX, posY);
				}
				
			}else if(mensajeParametrizado[0].equals("npcs")) {
				
				//--------------------------------movimiento de los npc------------------------------------------
				if(mensajeParametrizado[1].equals("posicion") && personajesRestantes==0) {//Num de movimiento
					((PantallaRonda1)app).npcs[ Integer.parseInt(mensajeParametrizado[2]) ].setPosicion(Float.parseFloat(mensajeParametrizado[3]), 
																										Float.parseFloat(mensajeParametrizado[4]));
				}else if(mensajeParametrizado[1].equals("esperandoDialogo")) {
					((PantallaRonda1)app).npcs[Integer.parseInt(mensajeParametrizado[2])].setEsperandoDialogo(true);
					
				}else if (mensajeParametrizado[1].equals("crear")){
				//--------------------------------creacion de los npc--------------------------------------------
					int[] apariencia = new int[]{Integer.parseInt(mensajeParametrizado[8]), 
												 Integer.parseInt(mensajeParametrizado[9]),
												 Integer.parseInt(mensajeParametrizado[10])
												};
					runnableNPC(mensajeParametrizado, apariencia);
				}
				
			}else if (mensajeParametrizado[0].equals("ladron")) {
				if(mensajeParametrizado[1].equals("robo")) {
					for(int i = 0; i<Utiles.getListeners().size();i++) {
						((InterfaceRobable) Utiles.getListeners().get(i)).salaRobada(Integer.parseInt(mensajeParametrizado[2]));
					}
				}else if(mensajeParametrizado[1].equals("gano")) {
					Global.puntajeLadron++;
					Global.ronda = Integer.parseInt(mensajeParametrizado[2]);
				}else if(mensajeParametrizado[1].equals("perdio")) {
					Global.puntajeGuardia++;
					Global.ronda = Integer.parseInt(mensajeParametrizado[2]);
				}else {
					String[] mensajeParametrizado2 = mensajeParametrizado.clone();
					runnableLadron(mensajeParametrizado2);
				}
			}else if (mensajeParametrizado[0].equals("guardia")) {
				if(mensajeParametrizado[1].equals("npcDialogo")) {
					((PantallaRonda1)app).npcs[ Integer.parseInt(mensajeParametrizado[2]) ].setEsperandoDialogo(true);
				}else if(mensajeParametrizado[1].equals("gano")) {
					Global.puntajeGuardia++;
					Global.ronda = Integer.parseInt(mensajeParametrizado[2]);
				}else if(mensajeParametrizado[1].equals("perdio")) {
					Global.puntajeLadron++;
					Global.ronda = Integer.parseInt(mensajeParametrizado[2]);
				}
			}else if (mensajeParametrizado[0].equals("sala") && Global.empiezaJuego) {
				if(mensajeParametrizado[1].equals("anterior")) {
					if(Global.guardia) {
						app.jugadorGuardia.salaAnterior = Integer.parseInt(mensajeParametrizado[2]);
					}else {
						app.jugadorLadron.salaAnterior = Integer.parseInt(mensajeParametrizado[2]);
					}
				}else if(mensajeParametrizado[1].equals("cambiar")) {
					System.out.println("a");
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
			}
		//	Utiles.hs.enviarMensaje("sala%anterior%" + ((Jugador) o1).getSala(), 
		//			Utiles.hs.getClientes()[0].getIp(), 
		//			Utiles.hs.getClientes()[0].getPuerto());
//			Utiles.hs.enviarMensaje("sala%" + ((Jugador) o1).getSala(), 
//					Utiles.hs.getClientes()[0].getIp(), 
//					Utiles.hs.getClientes()[0].getPuerto());
//			Utiles.hs.enviarMensaje("sala%cambiar%" + true, 
//					Utiles.hs.getClientes()[0].getIp(), 
//					Utiles.hs.getClientes()[0].getPuerto());
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
	        	app.jugadorLadron = new Ladron(SpriteInfo.values()[Integer.parseInt(mensajeParametrizado2[1])].getFilename());
	    		app.jugadorLadron.setPosition(Float.parseFloat(mensajeParametrizado2[2]), Float.parseFloat(mensajeParametrizado2[3]));
	    		
	    		app.stage.addActor(app.jugadorLadron);
				app.stage.setKeyboardFocus( (Global.guardia)? app.jugadorGuardia : app.jugadorLadron );
				SpriteInfo.setLadron( Integer.parseInt(mensajeParametrizado2[1]) );
				System.out.println("Se creo el ladron " + personajesRestantes);
				personajesRestantes-=1;
		    }	
		});
	}
}
