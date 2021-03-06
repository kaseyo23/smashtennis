package com.kaseyo23.object;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * @class Actor
 * Actor del juego basado en un AnimatedSprite,
 * que se encarga de cambiar los indices de las 'tiles'
 * en funcion del movimiento (hacia la izquierda, hacia
 * la derecha o parado). 
 */
public abstract class Actor extends AnimatedSprite {
	////CONSTANTES
	
	/**
	 * Constantes estaticas que indican los tipos
	 * de actores posibles (jugador o maquina).
	 */
	public static final int PLAYER = 0, MAQUINA = 1;
	
	/**
	 * Constantes que indica el tiempo que esta en cada tile
	 * cuando se mueve hacia un lado u otro
	 */
	protected final long[] ACTOR_ANIMATE = new long[]{100, 100, 100, 100};
	
	/**
	 * Constantes con los indices de las tiles
	 */
	protected final int ACTOR_STAND_INDEX = 0;
	protected final int ACTOR_RIGHT_START_INDEX = 5;
	protected final int ACTOR_RIGHT_END_INDEX = 8;
	protected final int ACTOR_LEFT_START_INDEX = 1;
	protected final int ACTOR_LEFT_END_INDEX = 4;
	
	/**
	 * Tolerancia o umbral que debe superar el movimiento
	 * para cambiar a los tiles a moverse hacia uno u otro lado
	 */
	protected final float ACTOR_TOLERANCIA = 0.25f;
	
	//// ATRIBUTOS
	
	/**
	 * Puntero a la camara
	 */
	protected Camera camera;
	
	/**
	 * Entero que representa el sentido anterior
	 * del movimiento. Puede ser:
	 * -  0. Estaba quieto.
	 * -  1. Se movia hacia la derecha.
	 * - -1. Se movia hacia la izquierda
	 */
	protected int sentidoAnterior;
	
	////CONSTRUCTOR
	
	/**
	 * Constructor del actor
	 * @param pX Posicion en X inicial
	 * @param pY Posicion en Y inicial
	 * @param vbo Puntero al VertexBuffer
	 * @param camera Puntero a la camara
	 * @param region Textura con tiles del actor
	 */
	public Actor(float pX, float pY, VertexBufferObjectManager vbo,
					Camera camera, ITiledTextureRegion region) {
		super(pX, pY, region, vbo);
		this.camera = camera;
		this.sentidoAnterior = 0;
		this.setScale(2.5f);
	}
	
	//// GETTERS
	
	/**
	 * Devuelve el tipo de actor
	 * @return PLAYER o MAQUINA
	 */
	public abstract int getTipo();
	
	////HELPERS
	
	/**
	 * Metodo privado que recibe la diferencia entre la posicion actual
	 * y a la que se mueve para animar y settear el indice del tile actual
	 * @param diff Diferencia de movimiento
	 */
	protected void updateSprite(float diff) {
		if(Math.abs(diff) <= ACTOR_TOLERANCIA) {
			//Si no nos movemos y antes nos moviamos
			//paramos el sprite
			if(sentidoAnterior != 0) {
				stopAnimation();
				setCurrentTileIndex(ACTOR_STAND_INDEX);
				sentidoAnterior = 0;
			}
		} else if(diff > 0) { //Nos movemos a la derecha
			//Si el sentido anterior es distinto
			//al actual...
			if(sentidoAnterior < 1){
				sentidoAnterior = 1;
				animate(ACTOR_ANIMATE, ACTOR_RIGHT_START_INDEX, ACTOR_RIGHT_END_INDEX, true);
			}
		} else if(diff < 0) { //Nos movemos a la izquierda
			if(sentidoAnterior > -1) {
				sentidoAnterior = -1;
				animate(ACTOR_ANIMATE, ACTOR_LEFT_START_INDEX, ACTOR_LEFT_END_INDEX, true);
			}
		}
	}
}
