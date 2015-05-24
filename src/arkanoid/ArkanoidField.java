package arkanoid;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import arkanoid.entities.CollisionDetector;
import arkanoid.entities.ball.AbstractBall;
import arkanoid.entities.ball.BallPositionChangedListener;
import arkanoid.entities.brick.AbstractBrick;
import arkanoid.entities.paddle.AbstractPaddle;
import arkanoid.interaction.EntityEventListener;

/**
 * Модель игрового поля.
 * @author Nikita Kalinin <nixorv@gmail.com>
 *
 */
public class ArkanoidField implements BallPositionChangedListener {

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
    }
    
	/**
	 * Добавить объект на поле
	 * @param object Объект для добавления
	 */
	public void addObject(Entity object) {
		
		_objects.add(object);
		for (EntityEventListener l : _entityEventListeners) {
			l.entityAdded(object);
		}
	}
	
	/**
	 * Убрать объект с поля
	 * @param object Объект для удаления
	 */
	public void removeObject(Entity object) {
		
		_objects.remove(object);
		for (EntityEventListener l : _entityEventListeners) {
			l.entityRemoved(object);
		}
	}
	
	/** 
	 * Получить размеры игрового поля (в пикселях).
	 * @return Размеры поля.
	 */
	public Dimension getSize() {
		
		return _dimensions;
	}

	/**
	 * Реализация этого метода отражает мяч от границ поля.
	 */
    @Override
    public void ballPositionChanged(AbstractBall ball) {
        
        if (ball.getPosition().y < 0) {
            ball.setPosition(new Point2D.Double(ball.getPosition().x, 0));
            ball.setSpeed(ball.getSpeed().flipVertical());
        }
        
        if (ball.getPosition().x < 0 || ball.getPosition().x + ball.getSize().width > _dimensions.width) {
            if (ball.getPosition().x < 0) {
                ball.setPosition(new Point2D.Double(0, ball.getPosition().y));
            } else {
                ball.setPosition(new Point2D.Double(_dimensions.width - ball.getSize().width, ball.getPosition().y));
            }
            ball.setSpeed(ball.getSpeed().flipHorizontal());
        }
    }

	public ArrayList<Entity> getEntities() {

		return new ArrayList<Entity>(_objects);
	}
	
	public void update(long timeElapsed) {
		
		for (Entity e : _objects) {
			if (e instanceof AbstractBall) {
				ballPositionChanged((AbstractBall)e);
			}
		}
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
}
