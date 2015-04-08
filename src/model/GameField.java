package model;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import view.IngameObjectView;
import model.ball.Ball;
import model.ball.BallPositionChangedListener;
import model.collision.CollidedObject;

/**
 * Модель игрового поля.
 * @author Nikita Kalinin <nixorv@gmail.com>
 *
 */
public class GameField implements BallPositionChangedListener {

	private ArrayList<Entity> _objects;
	private Dimension _dimensions;
	
    /**
     * Инициализирует поле заданного размера.
     * @param size Размер поля.
     */
    public GameField(Dimension size) {
    	
    	_objects = new ArrayList<>();
    	_dimensions = size;
    }
    
	/**
	 * Добавить объект на поле
	 * @param object Объект для добавления
	 */
	public void addObject(Entity object) {
		
		_objects.add(object);
	}
	
	/**
	 * Убрать объект с поля
	 * @param object Объект для удаления
	 */
	public void removeObject(Entity object) {
		
		_objects.remove(object);
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
    public void ballPositionChanged(Ball ball) {
        
        if (ball.getPosition().y < 0) {
            ball.setPosition(new Point2D.Float(ball.getPosition().x, 0));
            ball.setSpeed(ball.getSpeed().flipVertical());
        }
        
        if (ball.getPosition().x < 0 || ball.getPosition().x + ball.getSize().width > _dimensions.width) {
            if (ball.getPosition().x < 0) {
                ball.setPosition(new Point2D.Float(0, ball.getPosition().y));
            } else {
                ball.setPosition(new Point2D.Float(_dimensions.width - ball.getSize().width, ball.getPosition().y));
            }
            ball.setSpeed(ball.getSpeed().flipHorizontal());
        }
    }
    
    /**
     * Обработать столкновения
     * @param storage Словарь столкновений, где ключ - столкнувшийся объект, значение - 
     * список объектов, с которыми он столкнулся
     */
    public void collisionOccured(
			HashMap<CollidedObject, ArrayList<CollidedObject>> storage) {
		
    	// Вместо объектов, от которых принимается эффект (активные)
    	// передаётся их копия до начала обработки вообще всех столкновений
    	HashMap<CollidedObject, ArrayList<CollidedObject>> storage_copy = deepCopyStorage(storage);
    	
    	Iterator<CollidedObject> i, copyi, j, copyj;
    	i = storage.keySet().iterator();
    	copyi = storage_copy.keySet().iterator();
    	
    	while (i.hasNext() && copyi.hasNext()) {
    		
    		CollidedObject obj1 = i.next();
    		CollidedObject obj1copy = copyi.next();
    		j = storage.get(obj1).iterator();
    		copyj = storage_copy.get(obj1copy).iterator();
    		
    		while (j.hasNext() && copyj.hasNext()) {
    			
    			CollidedObject obj2 = j.next();
    			CollidedObject obj2copy = copyj.next();
    			obj1.object().processCollision(obj1, obj2copy);
    			obj2.object().processCollision(obj2, obj1copy);
    		}
    	}
	}
    
    /**
     * Порождает копию словаря коллизии вместе со всеми хранимыми объектами
     * @param storage Словарь коллизии
     * @return Копия словаря коллизии
     */
    private HashMap<CollidedObject, ArrayList<CollidedObject>> deepCopyStorage(
    		HashMap<CollidedObject, ArrayList<CollidedObject>> storage) {
    	
    	HashMap<CollidedObject, ArrayList<CollidedObject>> deepcopy = new HashMap<>();
    	
    	try {
    		
    		for (CollidedObject key : storage.keySet()) {
        		
        		CollidedObject key_copy = (CollidedObject) key.clone();
        		ArrayList<CollidedObject> values_copy = new ArrayList<>();
        		for (CollidedObject obj : storage.get(key)) {
        			values_copy.add((CollidedObject)obj.clone());
        		}
        		
        		deepcopy.put(key_copy, values_copy);
        	}
    	}
    	catch (CloneNotSupportedException exc) {
    		exc.printStackTrace();
    	}
    	
    	return deepcopy;
    }
}
