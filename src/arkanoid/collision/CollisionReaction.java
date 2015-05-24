package arkanoid.collision;


/**
 * Поведение столкнувшегося объекта; синглтон.
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public class CollisionReaction {

	/**
	 * Экзмепляр синглтона.
	 */
	private static CollisionReaction _instance = null;
	
	protected CollisionReaction() {
	}
	
	/**
	 * Возвращает экзмепляр поведения объектов. Обязательно переопределяется в классах-наследниках.
	 * @return Экземпляр поведения.
	 */
	public static CollisionReaction getInstance() {
		
		if (_instance == null) {
			_instance = new CollisionReaction();
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
