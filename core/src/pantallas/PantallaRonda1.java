package pantallas;

import com.badlogic.gdx.Gdx;

import elementos.Texto;
import personajes.Entidad;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import red.HiloCliente;
import utiles.Global;
import utiles.Render;
import utiles.Utiles;

import cuerpos.Cuerpo;
import menu.Hud;
import personajes.Guardia;
import personajes.Jugador;
import personajes.Ladron;
import personajes.NPC;

public class PantallaRonda1 extends PantallaRonda {

	public PantallaRonda1(Vector2 gravedad, String rutaMapa) {
		super(gravedad, rutaMapa);
	}

	public NPC[] npcs = new NPC[8];

	private float posSprX = 0, posSprY = 0;
	private Hud hud = new Hud("hud.png");
	private Vector2 posicion = new Vector2(0, 0);
	private Vector2 puntoLlegada;
	private Vector2 puntoSalida;
	private Interpolation interpol = Interpolation.circle;
	private float tiempo, duracion = 2.5f;

	@Override
	public void show() {
		// b2dr.setDrawBodies(false);
		// hilo cliente
		Utiles.hc = new HiloCliente(this);
		Utiles.hc.start();
		// guardia
		jugadorGuardia = new Guardia(new Cuerpo(mundo, 15, 15, BodyType.DynamicBody, 200, 160),
				"personajes/badlogic.jpg"/* , Utiles.hc */);
		stage.addActor(jugadorGuardia);
		stage.addActor(hud);
		hud.setearPopUp("botones/popup.png");

		// eventos
		mundo.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				Object o1 = contact.getFixtureA().getBody().getUserData();
				Object o2 = contact.getFixtureB().getBody().getUserData();
				// HAY QUE HACER TODAS LAS COMPROBACIONES 2 VECES UNA CON o1 Y OTRA CON o2
				try {
					if (o2 instanceof Cuerpo) {// contactos zonas
						if (o1 instanceof Jugador) {// comprueba si el objeto que choca es el ladron

							if (((Jugador) o1).getSala() != -1) {
								((Jugador) o1).salaAnterior = ((Jugador) o1).getSala();
							}

							((Jugador) o1).setSala(((Cuerpo) o2).getZona());// cambia la sala del ladron a la sala
																			// en la que está

							((Jugador) o1).cambiarSala = true;

						}
						if (o1 instanceof NPC) {// comprueba si el objeto que choca es el NPC
							((NPC) o1).setSala(((Cuerpo) o2).getZona());// cambia la sala del NPC a la sala
																		// en la que está
							if (((Cuerpo) o2).isRobado()) {// si en la sala ya se realizó un robo el atributo
															// robado del cuerpo que representa a la sala será true

								((NPC) o1).setRobado(true); // no se podrá robar en esta sala asi que se pone el
															// atributo robado del NPC en true
							}
						}
					}

					if (o1 instanceof Cuerpo) {// contactos zonas
						if (o2 instanceof Jugador) {// comprueba si el objeto que choca es el ladron

							if (((Jugador) o2).getSala() != -1) {
								((Jugador) o2).salaAnterior = ((Jugador) o2).getSala();
							}

							((Jugador) o2).setSala(((Cuerpo) o1).getZona());// cambia la sala del ladron a la sala
																			// en la que está

							((Jugador) o2).cambiarSala = true;

						}
						if (o2 instanceof NPC) {// comprueba si el objeto que choca es el NPC

							((NPC) o2).setSala(((Cuerpo) o1).getZona());// cambia la sala del NPC a la sala
																		// en la que está
							if (((Cuerpo) o1).isRobado()) {// si en la sala ya se realizó un robo el atributo
															// robado del cuerpo que representa a la sala será true

								((NPC) o2).setRobado(true); // no se podrá robar en esta sala asi que se pone el
															// atributo robado del NPC en true
							}
						}
					}

					if (o2 == null || o2 instanceof NPC) {
						if (o1 instanceof NPC) {
							((NPC) o1).setCambioDirec(true);
						}
					}
					if (o1 == null || o1 instanceof NPC) {
						if (o2 instanceof NPC) {
							((NPC) o2).setCambioDirec(true);
						}
					}
				} catch (Exception e) {
				}
			}

			@Override
			public void endContact(Contact contact) {
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}
		});
	}

	@Override
	public void render(float delta) {
		Render.limpiarPantalla();

		if (!Global.conexion || Utiles.hc.personajesRestantes > 0 || jugadorGuardia == null) {
			System.out.println(Utiles.hc.personajesRestantes);
		} else {

			if (Global.ronda == 1) {

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

				if (Global.guardia)
					arrestar();
				else
					roboNPC();

				adelantarCuerpos();

				hud.dibujarHud();

				Render.batch.setProjectionMatrix(camera.combined);
				Gdx.input.setInputProcessor(stage);
				Global.tiempo += Gdx.graphics.getRawDeltaTime();
			} else {
				Utiles.principal.setScreen(new PantallaRonda2(new Vector2(0, -9.8f), "mapas/ronda2.tmx"));
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
		
		  if(posGuardiaX != 0 && posGuardiaY != 0 && posLadronX != 0 && posLadronY != 0) {
	            jugadorGuardia.setPosition(posGuardiaX, posGuardiaY);
	            jugadorLadron.setPosition(posLadronX, posLadronY);
	        } 
		
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
		puntoLlegada = mapa.getVectorZonas()[(!Global.guardia) ? jugadorLadron.getSala() : jugadorGuardia.getSala()]
				.getPosition();
		puntoSalida = mapa.getVectorZonas()[(!Global.guardia) ? jugadorLadron.getSalaAnterior()
				: jugadorGuardia.getSalaAnterior()].getPosition();

		posicion.set(puntoLlegada);
		posicion.sub(puntoSalida);
		posicion.scl(interpol.apply(tiempo / duracion));
		posicion.add(puntoSalida);

		camera.position.set(posicion, 0);

		camera.update();
	}

	private void adelantarCuerpos() {
		for (int i = 0; i < npcs.length; i++) {
			if (jugadorGuardia.getPosition().dst(npcs[i].getPosition()) < 30 * Utiles.PPM) {
				if (jugadorGuardia.getPosition().y > npcs[i].getPosition().y) {
					jugadorGuardia.toBack();
				} else {
					jugadorGuardia.toFront();
				}
			}
			if (jugadorLadron.getPosition().dst(npcs[i].getPosition()) < 30 * Utiles.PPM) {
				if (jugadorLadron.getPosition().y > npcs[i].getPosition().y) {
					jugadorLadron.toBack();
				} else {
					jugadorLadron.toFront();
				}
			}
		}
		if (jugadorLadron.getPosition().dst(jugadorGuardia.getPosition()) < 30 * Utiles.PPM) {
			if (jugadorLadron.getPosition().y > jugadorGuardia.getPosition().y) {
				jugadorLadron.toBack();
			} else {
				jugadorLadron.toFront();
			}
		}
	}

	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------NPC
	// COSAS----------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	public void crearNPC(String[] mensajeParametrizado2, int[] apariencia) {
		npcs[Integer.parseInt(mensajeParametrizado2[2])] = new NPC(
				new Cuerpo(mundo, Float.parseFloat(mensajeParametrizado2[4]),
						Float.parseFloat(mensajeParametrizado2[5]), BodyType.DynamicBody,
						Float.parseFloat(mensajeParametrizado2[6]), Float.parseFloat(mensajeParametrizado2[7])),
				mensajeParametrizado2[3], apariencia, hud);
		stage.addActor(npcs[Integer.parseInt(mensajeParametrizado2[2])]);
	}

	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------JUGADORES
	// COSAS----------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	 private void roboNPC() {
	        int i = 0, resultadoRobo;
	        do {
	            if (jugadorLadron.getPosition().dst(npcs[i].getPosition()) < 30 * Utiles.PPM 
	             && !npcs[i].isRobado()
	             && jugadorLadron.getPosition().y - (jugadorLadron.getAlto() / 2) < npcs[i].getPosition().y + (npcs[i].getCuerpo().getAlto() / 2)
	             && jugadorLadron.getPosition().y + (jugadorLadron.getAlto() / 2) > npcs[i].getPosition().y - (npcs[i].getCuerpo().getAlto() / 2)
	             && (jugadorLadron.isDerecha() && npcs[i].isDerecha() && jugadorLadron.getPosition().x < npcs[i].getPosition().x
	             || !jugadorLadron.isDerecha() && !npcs[i].isDerecha() && jugadorLadron.getPosition().x > npcs[i].getPosition().x) ) {
	                if (jugadorLadron.getPosition().x > npcs[i].getPosition().x + (npcs[i].getCuerpo().getAncho() / 2)) {
	                    posSprX = npcs[i].getPosition().x + (npcs[i].getCuerpo().getAncho() / 2);
	                } else if (jugadorLadron.getPosition().x < npcs[i].getPosition().x - (npcs[i].getCuerpo().getAncho())) {
	                    posSprX = npcs[i].getPosition().x - (npcs[i].getCuerpo().getAncho()) - (npcs[i].getCuerpo().getAncho() / 2);
	                }
	                jugadorLadron.getSprRobo().setPosition(posSprX, npcs[i].getPosition().y - (npcs[i].getCuerpo().getAlto() / 2));
	                
	                Render.batch.begin();
	                jugadorLadron.getSprRobo().draw(Render.batch);
	                Render.batch.end();
	                if (jugadorLadron.isRobando()) {// compruebo que se este presionando la letra e         
	                    resultadoRobo = jugadorLadron.robar();
	                    if(resultadoRobo == 2) {
	                        npcs[i].detectarRobo();
	                    }
	                    else if(resultadoRobo == 0) { 
	                        Utiles.hc.enviarMensaje("ladron%robo%"+jugadorLadron.getSala()+"%"+i);
	                        //npcs[i].setEsperandoDialogo(true);
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
	            if (jugadorGuardia.getPosition().dst(npcs[i].getPosition()) < 30 * Utiles.PPM){
	                
	                
	                if(jugadorGuardia.getPosition().y < npcs[i].getPosition().y + (npcs[i].getAlto() / 2)
	                && jugadorGuardia.getPosition().y > npcs[i].getPosition().y - (npcs[i].getAlto() / 2)) {
	                            
	                    if( jugadorGuardia.getPosition().x - (jugadorGuardia.getAncho() / 2) > npcs[i].getPosition().x + (npcs[i].getCuerpo().getAncho()/2) ) {
	                        posSprX = npcs[i].getPosition().x + (npcs[i].getAncho() / 2);
	                    }else {
	                        posSprX = npcs[i].getPosition().x - (npcs[i].getAncho() + npcs[i].getAncho()/2);
	                    }
	                    posSprY = npcs[i].getPosition().y - (npcs[i].getAlto() / 2);
	                }
	                
	                if (jugadorGuardia.getPosition().x < npcs[i].getPosition().x + (npcs[i].getAncho() / 2) 
	                 && jugadorGuardia.getPosition().x > npcs[i].getPosition().x - (npcs[i].getAncho() / 2)) {
	                    
	                    if( jugadorGuardia.getPosition().y + (jugadorGuardia.getAlto() / 2) < npcs[i].getPosition().y - (npcs[i].getAlto()/2) ) {
	                        posSprY = npcs[i].getPosition().y - (npcs[i].getAlto() + npcs[i].getAlto()/2);
	                    }else {
	                        posSprY = npcs[i].getPosition().y + (npcs[i].getAlto() / 2);
	                    }
	                    posSprX = npcs[i].getPosition().x - (npcs[i].getWidth() /2);
	                }
	                jugadorGuardia.getSprArrestar().setPosition(posSprX, posSprY);
	                Render.batch.begin();
	                jugadorGuardia.getSprArrestar().draw(Render.batch);
	                Render.batch.end();
	                return (NPC) npcs[i].getCuerpo().getUserData();
	            }
	            i++;
	        }while (i < npcs.length);
	        return null;
	    }
	    private Ladron cercaniaLadron() {
	        if (jugadorGuardia.getPosition().dst(jugadorLadron.getPosition()) < 30 * Utiles.PPM ){
	            
	            if(jugadorGuardia.getPosition().y < jugadorLadron.getPosition().y + (jugadorLadron.getAlto() / 2)
	            && jugadorGuardia.getPosition().y > jugadorLadron.getPosition().y - (jugadorLadron.getAlto() / 2)) {
	                        
	                if( jugadorGuardia.getPosition().x - (jugadorGuardia.getAncho() / 2) > jugadorLadron.getPosition().x + (jugadorLadron.getCuerpo().getAncho()/2) ) {
	                    posSprX = jugadorLadron.getPosition().x + (jugadorLadron.getAncho() / 2);
	                }else {
	                    posSprX = jugadorLadron.getPosition().x - (jugadorLadron.getAncho() + jugadorLadron.getAncho()/2);
	                }
	                posSprY = jugadorLadron.getPosition().y - (jugadorLadron.getAlto() / 2);
	            }
	            
	            if (jugadorGuardia.getPosition().x < jugadorLadron.getPosition().x + (jugadorLadron.getAncho() / 2) 
	             && jugadorGuardia.getPosition().x > jugadorLadron.getPosition().x - (jugadorLadron.getAncho() / 2)) {
	                
	                if( jugadorGuardia.getPosition().y + (jugadorGuardia.getAlto() / 2) < jugadorLadron.getPosition().y - (jugadorLadron.getAlto()/2) ) {
	                    posSprY = jugadorLadron.getPosition().y - (jugadorLadron.getAlto() + jugadorLadron.getAlto()/2);
	                }else {
	                    posSprY = jugadorLadron.getPosition().y + (jugadorLadron.getAlto() / 2);
	                }
	                posSprX = jugadorLadron.getPosition().x - (jugadorLadron.getWidth() /2);
	            }
	            jugadorGuardia.getSprArrestar().setPosition(posSprX, posSprY);
	            Render.batch.begin();
	            jugadorGuardia.getSprArrestar().draw(Render.batch);
	            Render.batch.end();
	            return (Ladron) jugadorLadron.getCuerpo().getUserData();
	        }
	        return null;
	    }
}