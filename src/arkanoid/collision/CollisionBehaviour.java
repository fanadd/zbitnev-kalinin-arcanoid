package arkanoid.collision;

import arkanoid.Entity;

/**
 * Поведение столкнувшегося объекта; синглтон.
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public class CollisionBehaviour {

	/**
	 * Экзмепляр синглтона.
	 */
	private static CollisionBehaviour _instance = null;
	
	protected CollisionBehaviour() {
	}
	
	/**
	 * Возвращает экзмепляр поведения объектов. Обязательно переопределяется в классах-наследниках.
	 * @return Экземпляр поведения.
	 */
	public static CollisionBehaviour getInstance() {
		
		if (_instance == null) {
			_instance = new CollisionBehaviour();
		}
		
		return _instance;
	}
	
	/**
	 * Осуществить поведение пассивного объекта в ответ на столкновение.
	 * @param from Активный объект (не изменяется).
	 * @param to Пассивный объект (изменяет состояние в ответ на столкновение).
	 */
	public void invoke(CollidedObject from, CollidedObject to) {
		
	}
}
