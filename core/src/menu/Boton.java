package menu;

import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import utiles.Utiles;
import pantallas.PantallaMenu;
import pantallas.PantallaRonda1;

public class Boton extends Actor{

	private Sprite spr;
	public boolean touchdown;
	private PantallaMenu menu;
	public String filename;
	
	 public Boton(final String filename, Vector2 position, PantallaMenu menu){
	    	Texture tex = new Texture(filename);
	        this.menu = menu;
	    	this.filename = filename;
	    	spr = new Sprite(tex);
	    	float ancho = spr.getWidth()*Utiles.PPM;
	    	
	    	spr.setSize(ancho, spr.getHeight()*Utiles.PPM);
	    	spr.setPosition(position.x-ancho/2, position.y*Utiles.PPM);
	    	
	    	setBounds(spr.getX(),spr.getY(),spr.getWidth(),spr.getHeight());        
	        addListener(new InputListener(){
	        	@Override
	            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	        		touchdown = true;
	        		System.out.println("touchdown");
	            	return true; 
	            }
	        	
	        	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	        		touchdown = false;
	        	}
	        });
	    }
	 /*
	public Boton(String filename) {
		spr = new Sprite(new Texture(filename));
		
		spr.setPosition(-100,-100);
    	addListener(new InputListener(){
	        	@Override
	            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	        		touchdown = true;
	            	return true; 
	            }
	        	
	        	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	        		touchdown = false;
	        	}
	    });
	} */
	
	public void draw(Batch batch, float parentAlpha){
		Color color = getColor();
	    batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        spr.draw(batch);
    }
    
    @Override
	public void act(float delta) {
		if(touchdown){
			if(filename.equals("botones/boton 1.png")){
				Utiles.principal.setScreen(new PantallaRonda1(new Vector2(0, 0), ("mapas/escenario.tmx")));
			}else if(filename.equals("botones/boton 2.png")){
				
			}else if(filename.equals("botones/boton 3.png")){
				menu.getHud().moverPopUp(true);
				menu.cambiarModoBotones(Touchable.disabled);
			}
		}
		super.act(delta);
    }
    
    public Sprite getSpr() {
		return spr;
	}
    
    public void setArea(float x, float y) {
    	setBounds(x,y,spr.getWidth()*Utiles.PPM,spr.getHeight()*Utiles.PPM);  
    	System.out.println(x + "," + y + "," + spr.getWidth()*Utiles.PPM+ "," +spr.getHeight()*Utiles.PPM);
    }
}

