package arkanoid.collision;


/**
 * Поведение столкнувшегося объекта; синглтон.
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public abstract class CollisionReaction {
	
	protected CollisionReaction() {
	}
	
	/**
	 * Осуществить поведение пассивного объекта в ответ на столкновение.
	 * @param from Активный объект (не изменяется).
	 * @param to Пассивный объект (изменяет состояние в ответ на столкновение).
	 */
	public void invoke(CollidedObject from, CollidedObject to) {
	}
}
