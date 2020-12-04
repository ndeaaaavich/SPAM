package pantallas;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import personajes.Entidad;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import elementos.Texto;
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
	
	private int cantRobos = 0, numPista = 0;
	private int[] chancePista = new int[3];
	private Hud hud = new Hud("hud.png",this); 
	private Vector2 posicion = new Vector2(0, 0), puntoLlegada, puntoSalida;
	private Interpolation interpol = Interpolation.circle;
	private float posSprX = 0, posSprY = 0, tiempo, duracion = 2.5f, tiempoPantallaFinal, a = 1;
	private Texto textoFin;
	private SpriteBatch sprBatchFin = new SpriteBatch();
	private ShapeRenderer renderer = new ShapeRenderer();
	
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
		Global.tiempo = 0;
		// guardia
		jugadorGuardia = new Guardia("personajes/guardia.png");
		stage.addActor(jugadorGuardia);
		stage.addActor(hud);

		hud.setearPopUp("botones/popup.png");
		Gdx.input.setInputProcessor(stage);
		
		do {
			for (int i = 0; i < chancePista.length; i++) {
				chancePista[i] = Utiles.r.nextInt(5);
			}
		} while ( (chancePista[0] == chancePista[1]) || 
				  (chancePista[1] == chancePista[2]) || 
				  (chancePista[0] == chancePista[2]) );
		textoFin = new Texto("fonts/Early GameBoy.ttf", 50, Color.WHITE, false);
		textoFin.setPosition( Utiles.ancho / 2, 
							  Utiles.alto / 2);
		
	}
	@Override
	public void render(float delta) {
		Render.limpiarPantalla();	
		
		if (!Global.conexion || Utiles.hc.personajesRestantes > 0 || jugadorGuardia == null || Global.terminaJuego) {
			System.out.println(Utiles.hc.personajesRestantes);
			if(Global.terminaJuego) {
				tiempoPantallaFinal += Gdx.graphics.getRawDeltaTime();
				if(Global.guardia && tiempoPantallaFinal < 3) { 
					textoFin.setTexto( (Global.puntajeGuardia > Global.puntajeLadron)?"Ganaste":"Perdiste");
				}else if( (!Global.guardia) && tiempoPantallaFinal < 3){
					textoFin.setTexto( (Global.puntajeGuardia > Global.puntajeLadron)?"Perdiste":"Ganaste");
				}else {
					Utiles.hc.enviarMensaje("desconectarCliente");
					Global.empiezaJuego = false;
					Global.terminaJuego = false;
					Utiles.principal.setScreen(Utiles.pantallaMenu);
				}
				sprBatchFin.begin();
				textoFin.draw(sprBatchFin);
				sprBatchFin.end();
			}
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
				hud.dibujarHud();
				
				if (Global.guardia) {
					arrestar();
				}else{
					roboNPC();
				}
				adelantarCuerpos();

				Render.batch.setProjectionMatrix(camera.combined);
				Global.tiempo += Gdx.graphics.getRawDeltaTime();
				
				if (a > 0f) {
					Gdx.gl.glEnable(GL20.GL_BLEND);
					
					a -= 0.01;
					renderer.begin(ShapeType.Filled);
					renderer.setColor(0, 0, 0, a);
					renderer.rect(0, 0, Utiles.ancho/Utiles.PPM, Utiles.alto/Utiles.PPM);
					renderer.end();
				
					Gdx.gl.glDisable(GL20.GL_BLEND);
				}
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
	                    	System.out.println("Pantallaronda1");
	                    	if(cantRobos==chancePista[0] || cantRobos==chancePista[1] || cantRobos==chancePista[2]) {
	                    		Utiles.hc.enviarMensaje("ladron%robo%"+jugadorLadron.getSala()+"%"+i+"%"+numPista);

	                    		System.out.println("cantRobos: " + cantRobos); //esto no se lo mando al servidor
	                    		System.out.println("pista: " + numPista); 
	                    		numPista += 1;
	                    	}else {
	                    		Utiles.hc.enviarMensaje("ladron%robo%"+jugadorLadron.getSala()+"%"+i+"%-2");

	                    		System.out.println("cantRobos: " + cantRobos);
	                    		System.out.println("no hubo pista");
							}
	                        cantRobos += 1;
	                    }

                    	System.out.println("----------------");
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