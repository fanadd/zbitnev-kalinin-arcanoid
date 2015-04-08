package model;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import model.collision.CollidedObject;
import model.collision.CollisionBehaviour;
import model.collision.SpecialBehaviours;
import model.interaction.GenericEventListener;
import model.interaction.PositionChangeListener;
import model.interaction.SpeedChangeListener;

/**
 * Класс игрового объекта.
 * @author Nikita Kalinin <nixorv@gmail.com>
 *
 */
public abstract class Entity implements Cloneable, PositionChangeListener, SpeedChangeListener {
    
    protected Boolean _isDestroyed = false;
    
	protected Point2D.Float _position = null;
	protected Dimension _size = null;
	protected Speed2D _speed = null;
	protected ArrayList<CollisionBehaviour> _defaultColBehaviour = new ArrayList<>();
	protected HashMap<Class<?>, SpecialBehaviours> _specialColBehaviours 
		= new HashMap<>();
	protected GameField _field = null;
	protected ArrayList<PositionChangeListener> _positionListeners = new ArrayList<>();
	protected ArrayList<SpeedChangeListener> _speedListeners = new ArrayList<>();
	protected ArrayList<GenericEventListener> _geneventListeners = new ArrayList<>();

	/**
	 * Создает игровой объект, координаты (0, 0), нулевая скорость, нулевой размер.
	 * @param field Игровое поле.
	 */
	public Entity(GameField field) {
		
	    this(field, new Point2D.Float(0, 0), new Dimension(0, 0));
	}
	
	/**
	 * Создает игровой объект с нулевой скоростью.
	 * @param field Игровое поле.
	 * @param pos Позиция объекта.
	 * @param dim Размеры объекта.
	 */
	public Entity(GameField field, Point2D.Float pos, Dimension dim) {
	    
	    this(field, pos, dim, new Speed2D(0, 0));
	}
	
	/**
	 * Создает игровой объект.
	 * @param field Игровое поле.
	 * @param pos Позиция объекта.
	 * @param dim Размеры объекта.
	 * @param speed Скорость объекта.
	 */
	public Entity(GameField field, Point2D.Float pos, Dimension dim, Speed2D speed) {
	    
	    if (field == null || pos == null || dim == null || speed == null) {
	        throw new NullPointerException();
	    }
	    
	    this._field = field;
	    this.setSize(dim);
        this.setPosition(pos);
	    this.setSpeed(speed);
	}
	
	/**
	 * Возвращает поле, на котором находится объект.
	 * @return Игровое поле.
	 */
	public GameField getField() {
	    
	    return this._field;
	}
	
	/**
	 * Получить скорость.
	 * @return Текущая скорость.
	 */
	public Speed2D getSpeed() {

		return (Speed2D) _speed.clone();
	}
	
	/**
	 * Установить скорость.
	 * @param speed Новая скорость.
	 */
	public void setSpeed(Speed2D speed) {

		this._speed = speed;
		for (SpeedChangeListener l : _speedListeners) {
			l.speedChanged(this._speed);
		}
	}
	
	/**
	 * Получить позицию.
	 * @return Текущая позиция.
	 */
	public Point2D.Float getPosition() {

		return (Point2D.Float) _position.clone();
	}
	
	/**
	 * Задать позицию.
	 * @param pos Новая позиция.
	 */
	public void setPosition(Point2D.Float pos) {

	    if (pos == null) {
	        throw new NullPointerException();
	    }
		_position = pos;
		for (PositionChangeListener l : _positionListeners) {
			l.positionChanged(this._position);
		}
	}
	
	/**
	 * Задает размер объекта в пикселях.
	 * @param dim
	 */
	public void setSize(Dimension dim) {
	    
	    if (dim == null) {
	        throw new NullPointerException();
	    }
	    _size = dim;
	}
	
	/**
	 * Возвращает размер объекта в пикселях.
	 * @return
	 */
	public Dimension getSize() {
	    
	    return (Dimension) _size.clone();
	}
	
	/**
	 * Сдвинуть объект.
	 * @param delta Величина изменения позиции
	 */
	public void move(Point2D.Float delta) {
		
		this.setPosition(new Point2D.Float(this.getPosition().x + delta.x, this.getPosition().y + delta.y));
	}
	
	/**
	 * Обрабатывает столкновение с другим объектом.
	 * @param curr Текущий объект
	 * @param other Объект, столкнувшийся с данным.
	 */
	public void processCollision(CollidedObject curr, CollidedObject other) {

		// Вызываем специализированные коллизии, если таковые имеются
	    boolean foundSpecial = false;
	    
	    Iterator i = _specialColBehaviours.entrySet().iterator();
	    while (i.hasNext()) {
	        Map.Entry<Class<?>, SpecialBehaviours> entry = (Map.Entry)i.next();
	        if (entry.getValue()._flagCheckDerived) {
	            if (entry.getKey().isInstance(other.object())) {
	                foundSpecial = true;
	                for (CollisionBehaviour cb : entry.getValue()._behaviours) {
	                    cb.invoke(other, curr);
	                }
	            }
	        } else if (entry.getKey().equals(other.object().getClass())) {
                foundSpecial = true;
                for (CollisionBehaviour cb : entry.getValue()._behaviours) {
                    cb.invoke(other, curr);
                }
            }
	    }
		
		// Если их нет, тогда вызываем коллизию по умолчанию
		// Если и она не определена, то ничего не происходит
	    if (!foundSpecial) {
    		for (CollisionBehaviour cb : _defaultColBehaviour) {
    			cb.invoke(other, curr);
    		}
	    }
	}
	
	/**
	 * Получить список поведений по умолчанию при столкновении
	 * @return Список объектов поведения
	 */
	public ArrayList<CollisionBehaviour> getDefaultCollisionBehaviours() {
		
		return _defaultColBehaviour;
	}
	
	/**
	 * Добавить поведение по умолчанию при столкновении
	 * @param behaviour Добавляемое поведение
	 */
	public void addDefaultCollisionBehaviour(CollisionBehaviour behaviour) {
	
		_defaultColBehaviour.add(behaviour);
	}
	
	/**
	 * Удалить поведение по умолчанию при столкновении
	 * @param behaviour Поведение для удаления
	 */
	public void removeDafaultCollisionBehavior(CollisionBehaviour behaviour) {
		
		// TODO Method stub
	}
	
	/**
	 * Получить словарь специальных поведений при столкновении
	 * @return Ключ -- класс объектов, значение -- флаги и список поведений, определённых при столкновении 
	 *         с этим объектом
	 */
	public HashMap<Class<?>, SpecialBehaviours> getSpecificCollisionBehaviours() {
		
		return _specialColBehaviours;
	}
	
	/**
	 * Добавить специальное поведение при столкновении
	 * @param c Класс объектов
	 * @param cb Поведение, определяемое при столкновении с этим классом объектов
	 * @param checkDerived Если true, то будут также проверяться наследники класса объектов.
	 *                     Игнорируется, если для класса уже задано какое-либо поведение.
	 */
	public void addSpecificCollisionBehaviour(Class<?> c, CollisionBehaviour cb, boolean checkDerived) {
		
		if (!c.isInstance(Entity.class)) {
			// TODO: Выброс исключения, ибо нечего
		}
		
		if (!_specialColBehaviours.containsKey(c)) {
			
			SpecialBehaviours newsb = new SpecialBehaviours(cb);
			newsb._flagCheckDerived = checkDerived;
			_specialColBehaviours.put(c, newsb);
		}
		else {
			_specialColBehaviours.get(c)._behaviours.add(cb);
		}
	}
	
	/**
     * Добавить специальное поведение при столкновении
     * @param c Класс объектов
     * @param cb Поведение, определяемое при столкновении с этим классом объектов
     */
	public void addSpecificCollisionBehaviour(Class<?> c, CollisionBehaviour cb) {
	    
	    this.addSpecificCollisionBehaviour(c, cb, false);
	}
	
	/**
	 * Удалить специальное поведение при столкновении
	 * @param c Класс объектов
	 * @param cb Поведение, определённое при столкновении с этим классом объектов
	 */
	public void removeSpecificCollisionBehaviour(Class<?> c, CollisionBehaviour cb) {
		
		if (!c.isInstance(Entity.class)) {
			// TODO: Выброс исключения, ибо нечего
		}
		
		if (_specialColBehaviours.containsKey(c)) {
			_specialColBehaviours.get(c)._behaviours.remove(cb);
			if (_specialColBehaviours.get(c)._behaviours.isEmpty()) {
			    _specialColBehaviours.remove(c);
			}
		}
	}
	
	/**
	 * Очистить список поведений при столкновении по умолчанию
	 */
	public void cleanDefaultCollisionBehaviours() {
		
		_defaultColBehaviour.clear();
	}
	
	/**
	 * Очистить списки специальных поведений при столкновении для всех классов объектов
	 */
	public void cleanAllSpecificCollisionBehaviours() {
		
		_specialColBehaviours.clear();
	}
	
	/**
	 * Очистить список специальных поведений при столкновений для класса объектов
	 * @param cl Класс объектов, для которых очищается список поведений
	 */
	public void cleanSpecificCollisionBehaviours(Class<?> cl) {
		
		if (!cl.isInstance(Entity.class)) {
			// TODO: Выброс исключения, ибо нечего
		}
		
		if (_specialColBehaviours.containsKey(cl)) {
			_specialColBehaviours.remove(cl);
		}
	}
	
	/**
	 * Уничтожает объект.
	 * По умолчанию ничего не освобождает.
	 */
	public void destroy() {
		
	    this._isDestroyed = true;
	    this._field.removeObject(this);
	    for (GenericEventListener l : _geneventListeners) {
	    	l.destroyed();
	    }
	}
	
	/**
	 * Возвращает true, если объект считается уничтоженным.
	 * @return Уничтожен ли объект.
	 */
	public boolean isDestroyed() {
	    
	    return _isDestroyed;
	}
	
	//-------------------------------------------------------------------------------------------//
	
	@Override
	public void positionChanged(Point2D.Float newpos) {
		
	    this._position = newpos;
	}

	@Override
	public void speedChanged(Speed2D newspeed) {
		
		this._speed = newspeed;
	}
	
	/**
	 * Добавить слушателя изменения позиции объекта.
	 * @param l Новый слушатель.
	 */
	public void addPositionChangeListener(PositionChangeListener l) {
		_positionListeners.add(l);
	}
	
	/**
	 * Удалить слушателя изменения позиции объекта.
	 * @param l Удаляемый слушатель.
	 */
	public void removePositionChangeListener(PositionChangeListener l) {
		_positionListeners.remove(l);
	}
	
	/**
	 * Добавить слушателя изменения скорости объекта.
	 * @param l Новый слушатель.
	 */
	public void addSpeedChangeListener(SpeedChangeListener l) {
		_speedListeners.add(l);
	}
	
	/**
	 * Удалить слушателя изменения скорости объекта.
	 * @param l Удаляемый слушатель.
	 */
	public void removeSpeedChangeListener(SpeedChangeListener l) {
		_speedListeners.remove(l);
	}
	
	/**
	 * Добавить слушателя событий жизни объекта.
	 * @param l Добавляемый слушатель.
	 */
	public void addGenericEventListener(GenericEventListener l) {
		
		if (l == null) {
			return;
		}

		_geneventListeners.add(l);
	}
	
	/**
	 * Удалить слушателя событий жизни объекта.
	 * @param l Удаляемый слушатель.
	 */
	public void removeGenericEventListener(GenericEventListener l) {
		_geneventListeners.remove(l);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		
		Entity clone = (Entity)super.clone();
		clone._isDestroyed = this._isDestroyed;
		clone._field = this._field;    // ссылка на поле просто копируется, да
		clone._position = (Point2D.Float) this._position.clone();
		clone._size = (Dimension) this._size.clone();
		clone._speed = (Speed2D) this._speed.clone();
		
		return clone;
	}
}
