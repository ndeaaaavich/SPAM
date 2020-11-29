package personajes;

public enum Apariencia {

	PELO1("el pelo corto",0,0),
	PELO2("el pelo por los hombros",0,1),
	PELO3("el pelo estilo afro",0,2),
	TORSO1("un buzo",1,0),
	TORSO2("una remera de manga corta",1,1),
	TORSO3("una musculosa",1,2),
	PIERNAS1("unos pantalones",2,0),
	PIERNAS2("una pollera corta",2,1),
	PIERNAS3("unos pantalones cortos",2,2);
	
	private String descripcion;
	private int numPista;
	private int pista;
	
	private Apariencia(String descripcion, int numPista, int pista) {
		this.descripcion = descripcion;
		this.pista = pista;
		this.numPista = numPista;
	}
	
	public static String getDescripcion(int numPista, int pista) {
		for (int i = 0; i < Apariencia.values().length; i++) {
			if(numPista == Apariencia.values()[i].numPista && pista == Apariencia.values()[i].pista) {
				return Apariencia.values()[i].descripcion;
			}
		}
		return null;
	}
}
