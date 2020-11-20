package cuerpos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animacion {
	
	protected Texture tex;
	protected Sprite spr ;
	
	private TextureRegion estoSeDibuja;
	private TextureRegion[] fila1, 
							fila2,
							fila3,
							fila4;
	private Animation[] animacionesDirec;
	private TextureRegion[][] divisiones;
	
	public Animacion(String ruta, int cantColumnas, int cantFilas,
					 float sprAncho, float sprAlto) {
		
		fila1 = new TextureRegion[cantColumnas];
		fila2 = new TextureRegion[cantColumnas];
		fila3 = new TextureRegion[cantColumnas];
		fila4 = new TextureRegion[cantColumnas];
		
		tex = new Texture(ruta);
		estoSeDibuja = new TextureRegion(tex, (int)tex.getWidth(), (int)tex.getHeight());
		divisiones = estoSeDibuja.split((int)tex.getWidth()/cantColumnas, (int)tex.getHeight()/cantFilas);
		
		for (int i = 0; i < divisiones[0].length; i++) {
			fila1[i] = divisiones[0][i];
			fila2[i] = divisiones[1][i];
			fila3[i] = divisiones[2][i];
			fila4[i] = divisiones[3][i];
		}	
		animacionesDirec = new Animation[]{new Animation(0.1f, fila1),
										   new Animation(0.1f, fila2),
										   new Animation(0.1f, fila3),
										   new Animation(0.1f, fila4)};
		
		
		estoSeDibuja =  (TextureRegion) animacionesDirec[0].getKeyFrame(0, true); //por default la primera fila que se muestra
		spr = new Sprite(estoSeDibuja);
		spr.setSize(sprAncho, sprAlto);
	}
	public TextureRegion getTexReg(int i, float duracion) {
		return estoSeDibuja = (TextureRegion) animacionesDirec[i].getKeyFrame(duracion, true);
	}
	public void setTexReg(TextureRegion TexReg) {
		spr.setRegion(TexReg);
	}
	public void setPosicion(float x, float y){
		spr.setPosition(x, y);
	}
	public Sprite getSprite(){
		return spr;
	}
}
