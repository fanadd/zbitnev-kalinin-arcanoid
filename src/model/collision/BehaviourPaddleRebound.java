package model.collision;

import java.awt.geom.Point2D;

import model.Entity;
import model.ball.AbstractBall;
import model.paddle.Paddle;

/**
 * Поведение отскока от ракетки при столкновении.
 * @author Nikita Kalinin <nixorv@gmail.com>
 *
 */
public class BehaviourPaddleRebound extends CollisionBehaviour {
	/**
	 * Экзмепляр синглтона.
	 */
	private static BehaviourPaddleRebound _instance = null;
	
	protected BehaviourPaddleRebound() {
	}
	
	/**
	 * Возвращает экземпляр поведения отражения от ракетки.
	 * @return
	 */
	public static BehaviourPaddleRebound getInstance() {
		
		if (_instance == null) {
			_instance = new BehaviourPaddleRebound();
		}
		
		return _instance;
	}
	
	@Override
	public void invoke(CollidedObject from, CollidedObject to) {
		
		if (from.object() instanceof Paddle && to.object() instanceof AbstractBall) {
		
			to.object().setPosition(new Point2D.Float(to.object().getPosition().x, 
									 		          from.object().getPosition().y 
									 		          - to.object().getSize().height));
			to.object().setSpeed(((Paddle)(from.object())).getFireSpeed((AbstractBall)to.object()));
		}
	}
}
