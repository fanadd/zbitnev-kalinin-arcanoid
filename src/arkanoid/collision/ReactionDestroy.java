package arkanoid.collision;


/**
 * Поведение разрушения при столкновении.
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public class ReactionDestroy extends CollisionReaction {

	/**
	 * Экзмепляр синглтона.
	 */
	private static ReactionDestroy _instance = null;
	
	protected ReactionDestroy() {
	}
	
	/**
	 * Возвращает экземпляр поведения разрушения
	 * @return
	 */
	public static ReactionDestroy getInstance() {
		
		if (_instance == null) {
			_instance = new ReactionDestroy();
		}
		
		return _instance;
	}
	
	@Override
	public void invoke(CollidedObject from, CollidedObject to) {
		
		to.getObject().destroy();
	}
}
