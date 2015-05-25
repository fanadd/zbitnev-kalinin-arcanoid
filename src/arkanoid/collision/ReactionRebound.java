package arkanoid.collision;

import java.awt.geom.Point2D;

import math.geom2d.Vector2D;
import arkanoid.entities.Entity;
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
		Entity toObj = to.getObject();
		Entity fromObj = from.getObject();
		if ((fromObj instanceof AbstractBrick || fromObj instanceof AbstractPaddle) && toObj instanceof AbstractBall) {
			
			Point2D.Double newpos = to.getOldPosition();
			if (to.getCollisionSide() == CollidedObject.SIDE_TOP) {
				
				newpos.y = fromObj.getPosition().y - toObj.getDimension().height - 1;
				toObj.setPosition(newpos);
				toObj.setSpeed(toObj.getSpeed().flipVertical());
			}
			else if (to.getCollisionSide()  == CollidedObject.SIDE_BOTTOM) {
				
				newpos.y = fromObj.getPosition().y + fromObj.getDimension().height + 1;
				toObj.setPosition(newpos);
				toObj.setSpeed(toObj.getSpeed().flipVertical());
			}
			else if (to.getCollisionSide() == CollidedObject.SIDE_RIGHT) {
				
				newpos.x = fromObj.getPosition().x + fromObj.getDimension().width + 1;
				toObj.setPosition(newpos);
				toObj.setSpeed(toObj.getSpeed().flipHorizontal());
			}
			else if (to.getCollisionSide() == CollidedObject.SIDE_LEFT) {
				
				newpos.x = fromObj.getPosition().x - toObj.getDimension().width;
				toObj.setPosition(newpos);
				toObj.setSpeed(toObj.getSpeed().flipHorizontal());
			}
		}
		else if (fromObj instanceof AbstractBall && toObj instanceof AbstractBall) {
			
			AbstractBall act = (AbstractBall)fromObj;
			AbstractBall pass = (AbstractBall)toObj;
			
			// Вычисляется точка столкновения
			double colx = (act.getCenter().x * pass.getRadius() 
					      + pass.getCenter().x * act.getRadius()) 
					   / (act.getRadius() + pass.getRadius());
			double coly = (act.getCenter().y * pass.getRadius() 
				      + pass.getCenter().y * act.getRadius()) 
				   / (act.getRadius() + pass.getRadius());
			
			// Пассивный объект "отодвигается" по линии столкновения (линия, соединяющая центры 
			// шаров) во избежание повторной коллизии
			Point2D.Double moveVector = new Point2D.Double(pass.getCenter().x - colx,
													   pass.getCenter().y - coly);
			Point2D.Double newPos = new Point2D.Double(pass.getCenter().x + moveVector.x,
													 pass.getCenter().y + moveVector.y);
			pass.setCenter(newPos);
			
			// Вычисляется новая скорость для пассивного объекта
			Vector2D actSpeed = new Vector2D(act.getSpeed().x(), act.getSpeed().y());
			Vector2D passSpeed = new Vector2D(pass.getSpeed().x(), pass.getSpeed().y());
			Vector2D actCenter = new Vector2D(act.getCenter().x, act.getCenter().y);
			Vector2D passCenter = new Vector2D(pass.getCenter().x, pass.getCenter().y);
			Vector2D newPassSpeed = passSpeed;
			Vector2D passMinusAct = passCenter.minus(actCenter);
			newPassSpeed = newPassSpeed.minus(passMinusAct.times(
					passSpeed.minus(actSpeed).dot(passMinusAct) / Math.pow(passMinusAct.norm(), 2.0)));
			
			// Новая скорость назначается пассивному объекту
			pass.setSpeed(new Speed2D(newPassSpeed.x(), newPassSpeed.y()));
		}
	}
}
