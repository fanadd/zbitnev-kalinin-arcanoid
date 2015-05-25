package arkanoid.collision;

import java.awt.geom.Point2D;

import arkanoid.entities.ball.AbstractBall;
import arkanoid.entities.paddle.AbstractPaddle;

/**
 * Поведение отскока от ракетки при столкновении.
 * @author Nikita Kalinin <nixorv@gmail.com>
 *
 */
public class ReactionPaddleRebound extends CollisionReaction {
	/**
	 * Экзмепляр синглтона.
	 */
	private static ReactionPaddleRebound _instance = null;
	
	protected ReactionPaddleRebound() {
	}
	
	/**
	 * Возвращает экземпляр поведения отражения от ракетки.
	 * @return
	 */
	public static ReactionPaddleRebound getInstance() {
		
		if (_instance == null) {
			_instance = new ReactionPaddleRebound();
		}
		
		return _instance;
	}
	
	@Override
	public void invoke(CollidedObject from, CollidedObject to) {
		
		if (from.object() instanceof AbstractPaddle && to.object() instanceof AbstractBall) {
		
			to.object().setPosition(new Point2D.Double(to.object().getPosition().x, 
									 		          from.object().getPosition().y 
									 		          - to.object().getDimension().height));
			to.object().setSpeed(((AbstractPaddle)(from.object())).getFireSpeed((AbstractBall)to.object()));
		}
	}
}
