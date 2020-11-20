package utiles;

import java.util.ArrayList;

import java.util.EventListener;
import java.util.Random;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;

import main.Principal;
import red.HiloCliente;



public class Utiles {
	
	public static Scanner s = new Scanner(System.in);
	public static Random r = new Random();
	
	public static float PPM = 0.01f;
	public static Principal principal;
	public static HiloCliente hc;
	
	public static float ancho = Gdx.graphics.getWidth()*PPM;
	public static float alto = Gdx.graphics.getHeight()*PPM;
	
	private static ArrayList<EventListener> listeners = new ArrayList<EventListener>();
	
	public static void addListener(EventListener listener) {
		listeners.add(listener);
	}
	public static ArrayList<EventListener> getListeners() {
		return listeners;
	}
}
