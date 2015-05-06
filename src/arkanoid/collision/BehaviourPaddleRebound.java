package arkanoid.collision;

import java.awt.geom.Point2D;

import arkanoid.Entity;
import arkanoid.ball.AbstractBall;
import arkanoid.paddle.AbstractPaddle;

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
		
		if (from.object() instanceof AbstractPaddle && to.object() instanceof AbstractBall) {
		
			to.object().setPosition(new Point2D.Float(to.object().getPosition().x, 
									 		          from.object().getPosition().y 
									 		          - to.object().getSize().height));
			to.object().setSpeed(((AbstractPaddle)(from.object())).getFireSpeed((AbstractBall)to.object()));
		}
	}
}