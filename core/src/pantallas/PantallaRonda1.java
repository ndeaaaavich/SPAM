package pantallas;
import com.badlogic.gdx.Gdx;

import personajes.Entidad;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import red.HiloCliente;
import utiles.Global;
import utiles.Render;
import utiles.Utiles;

import menu.Hud;
import personajes.Guardia;
import personajes.Ladron;
import personajes.NPC;

public class PantallaRonda1 extends PantallaRonda {

	public PantallaRonda1(Vector2 gravedad, String rutaMapa) {
		super(gravedad, rutaMapa);
	}

	public NPC[] npcs = new NPC[8];

	private float posSprX = 0, posSprY = 0;
	private Hud hud = new Hud("hud.png",this);
	private Vector2 posicion = new Vector2(0, 0), puntoLlegada, puntoSalida;
	private Interpolation interpol = Interpolation.circle;
	private float tiempo, duracion = 2.5f;

	@Override
	public void show() {
		// hilo cliente
		if(Global.ronda == 1) {
			Utiles.hc = new HiloCliente(this);
			Utiles.hc.start();
		}else {
			Utiles.hc.setApp(this);
		}
		Global.terminaRonda = false;
		// guardia
		jugadorGuardia = new Guardia("personajes/guardia.png");
		stage.addActor(jugadorGuardia);
		stage.addActor(hud);

		hud.setearPopUp("botones/popup.png");
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Render.limpiarPantalla();

		if (!Global.conexion || Utiles.hc.personajesRestantes > 0 || jugadorGuardia == null) {
			System.out.println(Utiles.hc.personajesRestantes);
		} else {
			
			if (!Global.terminaRonda) {

				if (!Global.empiezaJuego) {
					Global.empiezaJuego = true;
					Utiles.hc.enviarMensaje("Empieza");
				}

				update(delta);
				
				tmr.setView(camera);
				tmr.render();
				b2dr.render(mundo, camera.combined);

				stage.act();
				stage.draw();
				
				if (Global.guardia) {
					arrestar();
				}else{
					roboNPC();
				}
				adelantarCuerpos();
				
				hud.dibujarHud();

				Render.batch.setProjectionMatrix(camera.combined);
				Global.tiempo += Gdx.graphics.getRawDeltaTime();
				
			} else {
				Utiles.principal.setScreen(new PantallaRonda1(new Vector2(0, 0), ("mapas/escenario.tmx")));
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------CAMARA-------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	private void update(float delta) {
		mundo.step(1 / 60f, 6, 2);
		
		if ((!Global.guardia) ? jugadorLadron.cambiarSala : jugadorGuardia.cambiarSala) { // necesito sumar tiempo
																							// mientras se hace la
																							// transición
			tiempo += delta;

			camaraUpdate();

			if (tiempo > duracion) {
				if (!Global.guardia)
					jugadorLadron.cambiarSala = false;
				else
					jugadorGuardia.cambiarSala = false;
				tiempo = 0;
			}
		}
		camera.update();
	}

	private void camaraUpdate() {
		puntoLlegada = mapa.getVectorZonas()[(!Global.guardia) ? jugadorLadron.getSala() : jugadorGuardia.getSala()].getPosition();
		puntoSalida = mapa.getVectorZonas()[(!Global.guardia) ? jugadorLadron.getSalaAnterior(): jugadorGuardia.getSalaAnterior()].getPosition();

		posicion.set(puntoLlegada);
		posicion.sub(puntoSalida);
		posicion.scl(interpol.apply(tiempo / duracion));
		posicion.add(puntoSalida);

		camera.position.set(posicion, 0);

		camera.update();
	}

	private void adelantarCuerpos() {
		for (int i = 0; i < npcs.length; i++) {
			if (jugadorGuardia.getSprPosition().dst(npcs[i].getSprPosition()) < 30 * Utiles.PPM) {
				if (jugadorGuardia.getSprPosition().y > npcs[i].getSprPosition().y) {
					jugadorGuardia.toBack();
				} else {
					jugadorGuardia.toFront();
				}
			}
			if (jugadorLadron.getSprPosition().dst(npcs[i].getSprPosition()) < 30 * Utiles.PPM) {
				if (jugadorLadron.getSprPosition().y > npcs[i].getSprPosition().y) {
					jugadorLadron.toBack();
				} else {
					jugadorLadron.toFront();
				}
			}
		}
		if (jugadorLadron.getSprPosition().dst(jugadorGuardia.getSprPosition()) < 30 * Utiles.PPM) {
			if (jugadorLadron.getSprPosition().y > jugadorGuardia.getSprPosition().y) {
				jugadorLadron.toBack();
			} else {
				jugadorLadron.toFront();
			}
		}
	}

	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------NPC COSAS----------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	public void crearNPC(String[] mensajeParametrizado2, int[] apariencia) {
		npcs[Integer.parseInt(mensajeParametrizado2[2])] = new NPC(mensajeParametrizado2[3], apariencia, hud, Integer.parseInt(mensajeParametrizado2[9]));
		stage.addActor(npcs[Integer.parseInt(mensajeParametrizado2[2])]);
	}

	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------JUGADORES COSAS----------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	 private void roboNPC() {
	        int i = 0, resultadoRobo;
	        do {
	            if (jugadorLadron.getSprPosition().dst(npcs[i].getSprPosition()) < 30 * Utiles.PPM 
	             && !npcs[i].isRobado()
	             && jugadorLadron.getSprPosition().y < npcs[i].getSprPosition().y + npcs[i].getAlto()
	             && jugadorLadron.getSprPosition().y + jugadorLadron.getAlto() > npcs[i].getSprPosition().y
	             && (jugadorLadron.isDerecha() && npcs[i].isDerecha() && jugadorLadron.getSprPosition().x < npcs[i].getSprPosition().x
	             || !jugadorLadron.isDerecha() && !npcs[i].isDerecha() && jugadorLadron.getSprPosition().x > npcs[i].getSprPosition().x) ) {
	               
	            	if (jugadorLadron.getSprPosition().x > npcs[i].getSprPosition().x + npcs[i].getAncho()) {
	            		
	                    posSprX = npcs[i].getSprPosition().x + npcs[i].getAncho();
	                    
	                } else if (jugadorLadron.getSprPosition().x + jugadorLadron.getAncho() < npcs[i].getSprPosition().x) {
	                	
	                    posSprX = npcs[i].getSprPosition().x - npcs[i].getAncho();
	                    
	                }
	                jugadorLadron.getSprRobo().setPosition(posSprX, npcs[i].getSprPosition().y);
	                
	                Render.batch.begin();
	                jugadorLadron.getSprRobo().draw(Render.batch);
	                Render.batch.end();
	                if (jugadorLadron.isRobando()) {// compruebo que se este presionando la letra e         
	                    resultadoRobo = jugadorLadron.robar();
	                    if(resultadoRobo == 2) {
	                        npcs[i].detectarRobo();
	                    }else if(resultadoRobo == 0) { 
	                        Utiles.hc.enviarMensaje("ladron%robo%"+jugadorLadron.getSala()+"%"+i);
	                    }
	                }
	            }
	            i++;
	        } while (i < npcs.length);
	    }
	    private void arrestar() {
	        Entidad entidad;
	        
	        entidad = cercaniaLadron();
	        if(entidad == null) {
	            entidad = cercaniaNPC();
	        }
	        if(jugadorGuardia.isArrestando()) {
	            jugadorGuardia.arrestar(entidad);
	        }
	    }
	    private NPC cercaniaNPC() {
	        int i = 0;
	        do {
	            if (jugadorGuardia.getSprPosition().dst(npcs[i].getSprPosition()) < 30 * Utiles.PPM){
	                
	                
	                if(jugadorGuardia.getSprPosition().y + (jugadorGuardia.getAlto() / 2) < npcs[i].getSprPosition().y + npcs[i].getAlto()
	                && jugadorGuardia.getSprPosition().y + (jugadorGuardia.getAlto() / 2) > npcs[i].getSprPosition().y) {
	                            
	                    if( jugadorGuardia.getSprPosition().x > npcs[i].getSprPosition().x + npcs[i].getAncho() ) {
	                        posSprX = npcs[i].getSprPosition().x + npcs[i].getAncho();
	                    }else {
	                        posSprX = npcs[i].getSprPosition().x - npcs[i].getAncho();
	                    }
	                    posSprY = npcs[i].getSprPosition().y;
	                }
	                
	                if (jugadorGuardia.getSprPosition().x + (jugadorGuardia.getAncho()/2) < npcs[i].getSprPosition().x + npcs[i].getAncho() 
	                 && jugadorGuardia.getSprPosition().x + (jugadorGuardia.getAncho()/2) > npcs[i].getSprPosition().x) {
	                    
	                    if( jugadorGuardia.getSprPosition().y + (jugadorGuardia.getAlto() / 2) < npcs[i].getSprPosition().y) {
	                        posSprY = npcs[i].getSprPosition().y - (npcs[i].getAlto() / 2);
	                    }else {
	                        posSprY = npcs[i].getSprPosition().y + (npcs[i].getAlto() / 2);
	                    }
	                    posSprX = npcs[i].getSprPosition().x;
	                }
	                jugadorGuardia.getSprArrestar().setPosition(posSprX, posSprY);
	                Render.batch.begin();
	                jugadorGuardia.getSprArrestar().draw(Render.batch);
	                Render.batch.end();
	                return (NPC) npcs[i].getUserData();
	            }
	            i++;
	        }while (i < npcs.length);
	        return null;
	    }
	    private Ladron cercaniaLadron() {
	        if (jugadorGuardia.getSprPosition().dst(jugadorLadron.getSprPosition()) < 30 * Utiles.PPM ){
	            
	            if(jugadorGuardia.getSprPosition().y + (jugadorGuardia.getAlto() / 2) < jugadorLadron.getSprPosition().y + jugadorLadron.getAlto()
	            && jugadorGuardia.getSprPosition().y + (jugadorGuardia.getAlto() / 2) > jugadorLadron.getSprPosition().y) {
	                        
	                if( jugadorGuardia.getSprPosition().x > jugadorLadron.getSprPosition().x + jugadorLadron.getAncho() ) {
	                    posSprX = jugadorLadron.getSprPosition().x + jugadorLadron.getAncho();
	                }else {
	                    posSprX = jugadorLadron.getSprPosition().x - jugadorLadron.getAncho();
	                }
	                posSprY = jugadorLadron.getSprPosition().y;
	            }
	            
	            if (jugadorGuardia.getSprPosition().x + (jugadorGuardia.getAncho()/2) < jugadorLadron.getSprPosition().x + jugadorLadron.getAncho() 
	             && jugadorGuardia.getSprPosition().x + (jugadorGuardia.getAncho()/2) > jugadorLadron.getSprPosition().x ) {
	                
	                if( jugadorGuardia.getSprPosition().y + (jugadorGuardia.getAlto() / 2) < jugadorLadron.getSprPosition().y) {
	                    posSprY = jugadorLadron.getSprPosition().y - (jugadorLadron.getAlto() / 2);
	                }else {
	                    posSprY = jugadorLadron.getSprPosition().y + (jugadorLadron.getAlto() / 2);
	                }
	                posSprX = jugadorLadron.getSprPosition().x;
	            }
	            jugadorGuardia.getSprArrestar().setPosition(posSprX, posSprY);
	            Render.batch.begin();
	            jugadorGuardia.getSprArrestar().draw(Render.batch);
	            Render.batch.end();
	            return (Ladron) jugadorLadron.getUserData();
	        }
	        return null;
	    }
}