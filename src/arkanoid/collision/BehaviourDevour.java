package arkanoid.collision;

import arkanoid.Entity;

/**
 * Поведение поглощения при столкновении. 
 * @author Nikita Kalinin <nixorv@gmail.com>
 *
 */
public class BehaviourDevour extends CollisionBehaviour {
	/**
	 * Экзмепляр синглтона.
	 */
	private static BehaviourDevour _instance = null;
	
	protected BehaviourDevour() {
	}
	
	/**
	 * Возвращает экземпляр поведения поглощения.
	 * @return
	 */
	public static BehaviourDevour getInstance() {
		
		if (_instance == null) {
			_instance = new BehaviourDevour();
		}
		
		return _instance;
	}
	
	@Override
	public void invoke(CollidedObject from, CollidedObject to) {
		// TODO
	}
}
