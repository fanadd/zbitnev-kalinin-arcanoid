package arkanoid.entities.ball;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import arkanoid.Entity;
import arkanoid.ArkanoidField;
import arkanoid.collision.ReactionPaddleRebound;
import arkanoid.collision.ReactionRebound;
import arkanoid.entities.paddle.AbstractPaddle;
import arkanoid.util.Speed2D;

/**
 * Модель абстрактного шарика
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public abstract class AbstractBall extends Entity {

	public AbstractBall(ArkanoidField field) {
		this(field, new Point2D.Float(0, 0), 0);
	}
	
	public AbstractBall(ArkanoidField field, Point2D.Float pos, int radius) {
	    
	    this(field, pos, radius, new Speed2D(0, 0));
	}
	
	public AbstractBall(ArkanoidField field, Point2D.Float pos, int radius, Speed2D speed) {
        
        super(field, pos, new Dimension(2*radius, 2*radius), speed);
        this.addDefaultCollisionBehaviour(ReactionRebound.getInstance());
        this.addSpecificCollisionBehaviour(AbstractPaddle.class, ReactionPaddleRebound.getInstance(), true);
    }

	/**
	 * Возвращает радиус мяча.
	 * @return Радиус.
	 */
	public int getRadius() {
	    
	    if (this._size.width != this._size.height) {
	        throw new IllegalStateException("Dimensions of Ball are not the same.");
	    }
	    
	    return this._size.width;
	}
	
	/**
	 * Задает радиус мяча.
	 * @param radius
	 * @return 
	 */
	public void setRadius(int radius) {
	    
	    radius = Math.abs(radius);
	    this.setSize(new Dimension(2*radius, 2*radius));
	}
	
	/**
	 * Внимание! Этот метод способен задать неодинаковые ширину и высоту, если это потребуется,
	 * однако в этом случае вы не сможете использовать метод getRadius().
	 * @param dim Размеры.
	 */
	@Override
	public void setSize(Dimension dim) {
	    
	    super.setSize(dim);
	}
	
	/**
	 * Здесь должен быть жестко задан скаляр скорости мяча.
	 * @return Скорость, с которой должен двигаться мяч.
	 */
	public abstract float getDefaultSpeedScalar();
	
	@Override
	public void setPosition(Point2D.Float pos) {
	    
	    super.setPosition(pos);
	    _field.ballPositionChanged(this);
	}
	
	@Override
	public void positionChanged(Point2D.Float newpos) {

	    super.positionChanged(newpos);
	    _field.ballPositionChanged(this);
    }
	
	/**
	 * Задать позицию шарика, указав координаты его середины
	 * @param center Позиция центра шарика
	 */
	public void setCenter(Point2D.Float center) {
		
		setPosition(new Point2D.Float(center.x - _size.width/2, center.y - _size.height/2));
	}
	
	/**
	 * Получить позицию центра шарика
	 * @return Позиция центра шарика
	 */
	public Point2D.Float getCenter() {
		
		return new Point2D.Float(this._position.x + _size.width/2, 
								 this._position.y + _size.height/2);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		
		AbstractBall clone = (AbstractBall) super.clone();
		return clone;
	}
}
