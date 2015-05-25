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
		
		if (from.getObject() instanceof AbstractPaddle && to.getObject() instanceof AbstractBall) {
		
			to.getObject().setPosition(new Point2D.Double(to.getObject().getPosition().x, 
									 		          from.getObject().getPosition().y 
									 		          - to.getObject().getDimension().height));
			to.getObject().setSpeed(((AbstractPaddle)(from.getObject())).getFireSpeed((AbstractBall)to.getObject()));
		}
	}
}
