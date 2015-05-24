package arkanoid.collision;

import java.awt.geom.Point2D;

import math.geom2d.Vector2D;
import arkanoid.Entity;
import arkanoid.entities.ball.AbstractBall;
import arkanoid.entities.brick.AbstractBrick;
import arkanoid.entities.paddle.AbstractPaddle;
import arkanoid.util.Speed2D;

/**
 * Поведение упрогого отскока при столкновении.
 * @author Nikita Kalinin <nixorv@gmail.com>
 *
 */
public class ReactionRebound extends CollisionReaction {
	/**
	 * Экзмепляр синглтона.
	 */
	private static ReactionRebound _instance = null;
	
	protected ReactionRebound() {
	}
	
	/**
	 * Возвращает экземпляр поведения упрогого отскока.
	 * @return
	 */
	public static ReactionRebound getInstance() {
		
		if (_instance == null) {
			_instance = new ReactionRebound();
		}
		
		return _instance;
	}
	
	@Override
	public void invoke(CollidedObject from, CollidedObject to) {
		
		// Вектор скорости отражается по-разному в зависимости от геометрической формы
		// активного объекта и пассивного объекта
		Entity toobj = to.object();
		Entity fromobj = from.object();
		if ((fromobj instanceof AbstractBrick || fromobj instanceof AbstractPaddle) && toobj instanceof AbstractBall) {
			
			Point2D.Double newpos = to.oldPosition();
			if (to.collisionSide() == CollidedObject.SIDE_TOP) {
				
				newpos.y = fromobj.getPosition().y - toobj.getSize().height - 1;
				toobj.setPosition(newpos);
				toobj.setSpeed(toobj.getSpeed().flipVertical());
			}
			else if (to.collisionSide()  == CollidedObject.SIDE_BOTTOM) {
				
				newpos.y = fromobj.getPosition().y + fromobj.getSize().height + 1;
				toobj.setPosition(newpos);
				toobj.setSpeed(toobj.getSpeed().flipVertical());
			}
			else if (to.collisionSide() == CollidedObject.SIDE_RIGHT) {
				
				newpos.x = fromobj.getPosition().x + fromobj.getSize().width + 1;
				toobj.setPosition(newpos);
				toobj.setSpeed(toobj.getSpeed().flipHorizontal());
			}
			else if (to.collisionSide() == CollidedObject.SIDE_LEFT) {
				
				newpos.x = fromobj.getPosition().x - toobj.getSize().width;
				toobj.setPosition(newpos);
				toobj.setSpeed(toobj.getSpeed().flipHorizontal());
			}
		}
		else if (fromobj instanceof AbstractBall && toobj instanceof AbstractBall) {
			
			AbstractBall act = (AbstractBall)fromobj;
			AbstractBall pass = (AbstractBall)toobj;
			
			// Вычисляется точка столкновения
			double colx = (act.getCenter().x * pass.getRadius() 
					      + pass.getCenter().x * act.getRadius()) 
					   / (act.getRadius() + pass.getRadius());
			double coly = (act.getCenter().y * pass.getRadius() 
				      + pass.getCenter().y * act.getRadius()) 
				   / (act.getRadius() + pass.getRadius());
			
			// Пассивный объект "отодвигается" по линии столкновения (линия, соединяющая центры 
			// шаров) во избежание повторной коллизии
			Point2D.Double movevect = new Point2D.Double(pass.getCenter().x - colx,
													   pass.getCenter().y - coly);
			Point2D.Double newpos = new Point2D.Double(pass.getCenter().x + movevect.x,
													 pass.getCenter().y + movevect.y);
			pass.setCenter(newpos);
			
			// Вычисляется новая скорость для пассивного объекта
			Vector2D aspeed = new Vector2D(act.getSpeed().x(), act.getSpeed().y());
			Vector2D pspeed = new Vector2D(pass.getSpeed().x(), pass.getSpeed().y());
			Vector2D acenter = new Vector2D(act.getCenter().x, act.getCenter().y);
			Vector2D pcenter = new Vector2D(pass.getCenter().x, pass.getCenter().y);
			Vector2D newPSpeed = pspeed;
			Vector2D pminusa = pcenter.minus(acenter);
			newPSpeed = newPSpeed.minus(pminusa.times(
					pspeed.minus(aspeed).dot(pminusa) / Math.pow(pminusa.norm(), 2.0)));
			
			// Новая скорость назначается пассивному объекту
			pass.setSpeed(new Speed2D(newPSpeed.x(), newPSpeed.y()));
		}
	}
}
