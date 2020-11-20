package mapas;

import com.badlogic.gdx.maps.MapObject;


import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import utiles.Global;
import utiles.Utiles;
import cuerpos.Cuerpo;

public class Mapa{

	private World mundo;
	private TiledMap tileMap;
	
	private Cuerpo[] zonas;
	
	private int i, x, p, f; 
	private Vector2[] PowerUpsPosition, 
					  PlataformasInicioPosition, PlataformasFinalPosition, 
					  PlataformasSize;
	//PlataformasSize.x ancho, PlataformasSize.y Alto
	private String[] PowerUps;
	
	public Mapa(TiledMap tileMap, World mundo) {
		this.tileMap = tileMap;
		this.mundo = mundo;
		cargarObjetosMapa();
	}
	
	private void cargarObjetosMapa() {
		if(Global.ronda == 1) {
			 zonas = new Cuerpo[tileMap.getLayers().get("zona").getObjects().getCount()];
		}
		if(Global.ronda == 2) {
			PowerUps = new String[tileMap.getLayers().get("powerUps").getObjects().getCount()];
			PowerUpsPosition = new Vector2[PowerUps.length];
			
			PlataformasInicioPosition = new Vector2[tileMap.getLayers().get("PlataformasMovilesInicio").getObjects().getCount()];
			PlataformasFinalPosition = new Vector2[PlataformasInicioPosition.length];
			PlataformasSize = new Vector2[PlataformasInicioPosition.length];
		}
		for (int j = 0; j < tileMap.getLayers().getCount(); j++) {
			for(MapObject objeto : tileMap.getLayers().get(j).getObjects().getByType(RectangleMapObject.class)) {
				
				Rectangle rec = ((RectangleMapObject)objeto).getRectangle(); 

				if(tileMap.getLayers().get(j) == tileMap.getLayers().get("zona")){
					//en este if se crean las zonas, osea lo que diferencia una sala de otra
					zonas[i] = new Cuerpo(mundo, (rec.getWidth()) , (rec.getHeight()) , 
									      BodyType.StaticBody , 
									      rec.getX()+rec.getWidth()/2 , rec.getY()+rec.getHeight()/2);
					zonas[i].setArea(i);
					i++;
					
				}else if(tileMap.getLayers().get(j) == tileMap.getLayers().get("powerUps")){
					//en este if se optienen las posiciones en orden de los powerUps
					PowerUps[x] = objeto.getName();
					PowerUpsPosition[x] = new Vector2( rec.getX() , rec.getY());
					x++;
					
				}else if(tileMap.getLayers().get(j) == tileMap.getLayers().get("PlataformasMovilesInicio")){
					//en este if se optienen las posiciones en orden de las plataformas
					PlataformasInicioPosition[Integer.parseInt(objeto.getName())] = new Vector2( rec.getX() , rec.getY());
					PlataformasSize[p] = new Vector2(rec.getWidth() , rec.getHeight());
					p++;
					
				}else if(tileMap.getLayers().get(j) == tileMap.getLayers().get("PlataformasMovilesFinal")){
					//en este if se optienen las posiciones en orden de las plataformas
					PlataformasFinalPosition[Integer.parseInt(objeto.getName())] = new Vector2( rec.getX() , rec.getY());
					f++;
					
				}else {
					//en el else se hacen el respto de cuerpos que del tiledMap
					new Cuerpo(mundo, rec.getWidth() , rec.getHeight() , 
						       BodyType.StaticBody , 
						       (rec.getX()+rec.getWidth()/ 2) , (rec.getY()+rec.getHeight()/ 2) );
					
				}
			}
		}
	}
	public Vector2[] getPlataformasFinalPosition() {
		return PlataformasFinalPosition;
	}
	public Vector2[] getPlataformasInicioPosition() {
		return PlataformasInicioPosition;
	}
	public Vector2[] getPlataformasSize() {
		return PlataformasSize;
	}
	public World getMundo() {
		return mundo;
	}
	public Cuerpo[] getVectorZonas() {
		return zonas;
	}
	public String[] getPowerUps() {
		return PowerUps;
	}
	public Vector2[] getPowerUpsPosition() {
		return PowerUpsPosition;
	}
}
