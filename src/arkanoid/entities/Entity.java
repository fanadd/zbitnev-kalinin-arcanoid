package arkanoid.entities;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import arkanoid.ArkanoidField;
import arkanoid.collision.CollidedObject;
import arkanoid.collision.CollisionReaction;
import arkanoid.collision.SpecialBehaviours;
import arkanoid.util.Speed2D;

/**
 * Класс игрового объекта.
 * @author Nikita Kalinin <nixorv@gmail.com>
 *
 */
public abstract class Entity implements Cloneable {
    
    protected Boolean _isDestroyed = false;
	protected Dimension _size = null;
	protected ArrayList<CollisionReaction> _defaultColBehaviour = new ArrayList<>();
	protected HashMap<Class<?>, SpecialBehaviours> _specialColBehaviours = new HashMap<>();
	protected ArkanoidField _field = null;
	protected Sprite _sprite = null;

	/**
	 * Создает игровой объект, координаты (0, 0), нулевая скорость, нулевой размер.
	 * @param field Игровое поле.
	 */
	public Entity(ArkanoidField field) {
		
	    this(field, new Point2D.Double(0, 0), new Dimension(0, 0));
	}
	
	/**
	 * Создает игровой объект с нулевой скоростью.
	 * @param field Игровое поле.
	 * @param pos Позиция объекта.
	 * @param dim Размеры объекта.
	 */
	public Entity(ArkanoidField field, Point2D.Double pos, Dimension dim) {
	    
	    this(field, pos, dim, new Speed2D(0, 0));
	}
	
	/**
	 * Создает игровой объект.
	 * @param field Игровое поле.
	 * @param pos Позиция объекта.
	 * @param dim Размеры объекта.
	 * @param speed Скорость объекта.
	 */
	public Entity(ArkanoidField field, Point2D.Double pos, Dimension dim, Speed2D speed) {
	    
	    if (field == null || pos == null || dim == null || speed == null) {
	        throw new NullPointerException();
	    }
	    
	    this._sprite = new Sprite(dim);
	    this._field = field;
	    this.setSize(dim);
        this.setPosition(pos);
	    this.setSpeed(speed);
	}
	
	/**
	 * Возвращает поле, на котором находится объект.
	 * @return Игровое поле.
	 */
	public ArkanoidField getField() {
	    
	    return this._field;
	}
	
	/**
	 * Получить скорость.
	 * @return Текущая скорость.
	 */
	public Speed2D getSpeed() {

		return _sprite.getSpeed();
	}
	
	/**
	 * Установить скорость.
	 * @param speed Новая скорость.
	 */
	public void setSpeed(Speed2D speed) {

		_sprite.setSpeed(speed);
	}
	
	/**
	 * Получить позицию.
	 * @return Текущая позиция.
	 */
	public Point2D.Double getPosition() {

		return _sprite.getPosition();
	}
	
	/**
	 * Задать позицию.
	 * @param pos Новая позиция.
	 */
	public void setPosition(Point2D.Double pos) {

	    if (pos == null) {
	        throw new NullPointerException();
	    }
		_sprite.setPosition(pos);
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
	public void move(double deltaX, double deltaY) {
		
		this.setPosition(new Point2D.Double(this.getPosition().x + deltaX, this.getPosition().y + deltaY));
	}
	
	/**
	 * Обрабатывает столкновение с другим объектом.
	 * @param curr Текущий объект
	 * @param other Объект, столкнувшийся с данным.
	 */
	void processCollision(CollidedObject curr, CollidedObject other) {

		// Вызываем специализированные коллизии, если таковые имеются
	    boolean foundSpecial = false;
	    
	    Iterator i = _specialColBehaviours.entrySet().iterator();
	    while (i.hasNext()) {
	        Map.Entry<Class<?>, SpecialBehaviours> entry = (Map.Entry)i.next();
	        if (entry.getValue()._flagCheckDerived) {
	            if (entry.getKey().isInstance(other.object())) {
	                foundSpecial = true;
	                for (CollisionReaction cb : entry.getValue()._behaviours) {
	                    cb.invoke(other, curr);
	                }
	            }
	        } else if (entry.getKey().equals(other.object().getClass())) {
                foundSpecial = true;
                for (CollisionReaction cb : entry.getValue()._behaviours) {
                    cb.invoke(other, curr);
                }
            }
	    }
		
		// Если их нет, тогда вызываем коллизию по умолчанию
		// Если и она не определена, то ничего не происходит
	    if (!foundSpecial) {
    		for (CollisionReaction cb : _defaultColBehaviour) {
    			cb.invoke(other, curr);
    		}
	    }
	}
	
	/**
	 * Получить список поведений по умолчанию при столкновении
	 * @return Список объектов поведения
	 */
	protected ArrayList<CollisionReaction> getDefaultCollisionBehaviours() {
		
		return _defaultColBehaviour;
	}
	
	/**
	 * Добавить поведение по умолчанию при столкновении
	 * @param behaviour Добавляемое поведение
	 */
	protected void addDefaultCollisionBehaviour(CollisionReaction behaviour) {
	
		_defaultColBehaviour.add(behaviour);
	}
	
	/**
	 * Удалить поведение по умолчанию при столкновении
	 * @param behaviour Поведение для удаления
	 */
	protected void removeDafaultCollisionBehavior(CollisionReaction behaviour) {
		
		// TODO Method stub
	}
	
	/**
	 * Получить словарь специальных поведений при столкновении
	 * @return Ключ -- класс объектов, значение -- флаги и список поведений, определённых при столкновении 
	 *         с этим объектом
	 */
	protected HashMap<Class<?>, SpecialBehaviours> getSpecificCollisionBehaviours() {
		
		return _specialColBehaviours;
	}
	
	/**
	 * Добавить специальное поведение при столкновении
	 * @param c Класс объектов
	 * @param cb Поведение, определяемое при столкновении с этим классом объектов
	 * @param checkDerived Если true, то будут также проверяться наследники класса объектов.
	 *                     Игнорируется, если для класса уже задано какое-либо поведение.
	 */
	protected void addSpecificCollisionBehaviour(Class<?> c, CollisionReaction cb, boolean checkDerived) {
		
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
	protected void addSpecificCollisionBehaviour(Class<?> c, CollisionReaction cb) {
	    
	    this.addSpecificCollisionBehaviour(c, cb, false);
	}
	
	/**
	 * Удалить специальное поведение при столкновении
	 * @param c Класс объектов
	 * @param cb Поведение, определённое при столкновении с этим классом объектов
	 */
	protected void removeSpecificCollisionBehaviour(Class<?> c, CollisionReaction cb) {
		
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
	protected void cleanDefaultCollisionBehaviours() {
		
		_defaultColBehaviour.clear();
	}
	
	/**
	 * Очистить списки специальных поведений при столкновении для всех классов объектов
	 */
	protected void cleanAllSpecificCollisionBehaviours() {
		
		_specialColBehaviours.clear();
	}
	
	/**
	 * Очистить список специальных поведений при столкновений для класса объектов
	 * @param cl Класс объектов, для которых очищается список поведений
	 */
	protected void cleanSpecificCollisionBehaviours(Class<?> cl) {
		
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
	}
	
	/**
	 * Возвращает true, если объект считается уничтоженным.
	 * @return Уничтожен ли объект.
	 */
	public boolean isDestroyed() {
	    
	    return _isDestroyed;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		
		Entity clone = (Entity)super.clone();
		clone._isDestroyed = this._isDestroyed;
		clone._field = this._field;    // ссылка на поле просто копируется, да
		clone._sprite = (Sprite)this._sprite.clone();
		clone._size = (Dimension) this._size.clone();
		
		return clone;
	}

	public Sprite getSprite() {

		return _sprite;
	}
}
