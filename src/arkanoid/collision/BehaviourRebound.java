package arkanoid.collision;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import arkanoid.Entity;
import arkanoid.Speed2D;
import arkanoid.ball.AbstractBall;
import arkanoid.brick.AbstractBrick;
import arkanoid.paddle.AbstractPaddle;

import com.golden.gamedev.object.collision.CollisionGroup;

import math.geom2d.Vector2D;

/**
 * Поведение упрогого отскока при столкновении.
 * @author Nikita Kalinin <nixorv@gmail.com>
 *
 */
public class BehaviourRebound extends CollisionBehaviour {
	/**
	 * Экзмепляр синглтона.
	 */
	private static BehaviourRebound _instance = null;
	
	protected BehaviourRebound() {
	}
	
	/**
	 * Возвращает экземпляр поведения упрогого отскока.
	 * @return
	 */
	public static BehaviourRebound getInstance() {
		
		if (_instance == null) {
			_instance = new BehaviourRebound();
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
			
			Point2D.Float newpos = to.oldPosition();
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
			float colx = (act.getCenter().x * pass.getRadius() 
					      + pass.getCenter().x * act.getRadius()) 
					   / (act.getRadius() + pass.getRadius());
			float coly = (act.getCenter().y * pass.getRadius() 
				      + pass.getCenter().y * act.getRadius()) 
				   / (act.getRadius() + pass.getRadius());
			
			// Пассивный объект "отодвигается" по линии столкновения (линия, соединяющая центры 
			// шаров) во избежание повторной коллизии
			Point2D.Float movevect = new Point2D.Float(pass.getCenter().x - colx,
													   pass.getCenter().y - coly);
			Point2D.Float newpos = new Point2D.Float(pass.getCenter().x + movevect.x,
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
