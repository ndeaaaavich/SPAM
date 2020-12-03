package personajes;

public enum SpriteInfo {

	UNO("personajes/1.png",new int[]{0,0,0},false),
	DOS("personajes/2.png",new int[]{1,1,1},false),
	TRES("personajes/3.png",new int[]{2,2,2},false),
	CUATRO("personajes/4.png",new int[]{0,2,1},false),
	CINCO("personajes/5.png",new int[]{2,0,1},false),
	SEIS("personajes/6.png",new int[]{1,1,2},false),
	SIETE("personajes/7.png",new int[]{0,1,0},false),
	OCHO("personajes/8.png",new int[]{1,0,2},false),
	NUEVE("personajes/9.png",new int[]{2,2,0},false);
	
	private String filename;
	private int[] apariencia = new int[3];
	private boolean ladron;
	
	SpriteInfo(String filename, int[] apariencia, boolean ladron) {
		this.filename = filename;
		this.apariencia = apariencia;
	}
	
	public static int getIndiceLadron() {
        int indice = 0;
        for (int i = 0; i < SpriteInfo.values().length; i++) {
            if (SpriteInfo.values()[i].ladron == true){
                indice = i;
            }
        }
        return indice;
    }
	
	public static void setLadron(int indice) {
		SpriteInfo.values()[indice].ladron = true;
	}
	
	public int[] getApariencia() {
		return apariencia;
	}
	
	public String getFilename() {
		return filename;
	}
	
}
