package arkanoid;

import java.awt.Dimension;
import java.util.ArrayList;

import arkanoid.entities.CollisionDetector;
import arkanoid.entities.ball.AbstractBall;
import arkanoid.entities.brick.AbstractBrick;
import arkanoid.entities.paddle.AbstractPaddle;
import arkanoid.interaction.EntityEventListener;

/**
 * Модель игрового поля.
 * @author Nikita Kalinin <nixorv@gmail.com>
 *
 */
public class ArkanoidField {

	private ArrayList<Entity> _objects = new ArrayList<>();;
	private ArrayList<EntityEventListener> _entityEventListeners = new ArrayList<>();
	private Dimension _dimensions;
	private CollisionDetector _collisionDetector = null;
	
    /**
     * Инициализирует поле заданного размера.
     * @param size Размер поля.
     */
    public ArkanoidField(Dimension size) {
    	
    	_dimensions = size;
    	_collisionDetector = new CollisionDetector(this);
    	_collisionDetector.addCollidableSuperclasses(AbstractBall.class, AbstractBrick.class);
        _collisionDetector.addCollidableSuperclasses(AbstractBall.class, AbstractPaddle.class);
    	_collisionDetector.addCollidableSuperclasses(AbstractBall.class, AbstractBall.class);
    	_collisionDetector.setBoundaryCollidableSuperclass(AbstractBall.class);
    }
    
	/**
	 * Добавить объект на поле
	 * @param object Объект для добавления
	 */
	public void addObject(Entity object) {
		
		_objects.add(object);
		fireEntityAdded(object);
	}
	
	/**
	 * Убрать объект с поля
	 * @param object Объект для удаления
	 */
	public void removeObject(Entity object) {
		
		_objects.remove(object);
		fireEntityRemoved(object);
	}
	
	/** 
	 * Получить размеры игрового поля (в пикселях).
	 * @return Размеры поля.
	 */
	public Dimension getSize() {
		
		return _dimensions;
	}

	public ArrayList<Entity> getEntities() {

		return new ArrayList<Entity>(_objects);
	}
	
	public void update(long timeElapsed) {
		
		_collisionDetector.update(timeElapsed);
	}
	
	/**
	 * Добавить слушателя событий добавления/удаления игровых объектов.
	 * @param listener Добавляемый слушатель.
	 */
	public void addEntityEventListener(EntityEventListener listener) {
		
		if (listener == null) {
			throw new NullPointerException();
		}
		_entityEventListeners.add(listener);
	}
	
	/**
	 * Удалить слушателя с событий добавления/удаления игровых объектов.
	 * @param listener Удаляемый слушатель.
	 */
	public void removeEntityEventListener(EntityEventListener listener) {
		
		_entityEventListeners.remove(listener);
	}
	
	private void fireEntityAdded(Entity e) {
	    
	    for (EntityEventListener l : _entityEventListeners) {
            l.entityAdded(e);
        }
	}
	
	private void fireEntityRemoved(Entity e) {
	    
	    for (EntityEventListener l : _entityEventListeners) {
            l.entityRemoved(e);
        }
	}
}
