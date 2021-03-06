package arkanoid.entities.ball;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import arkanoid.ArkanoidField;
import arkanoid.collision.ReactionPaddleRebound;
import arkanoid.collision.ReactionRebound;
import arkanoid.entities.Entity;
import arkanoid.entities.paddle.AbstractPaddle;
import arkanoid.util.Speed2D;

/**
 * Модель абстрактного шарика
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public abstract class AbstractBall extends Entity {

	public AbstractBall(ArkanoidField field) {
		this(field, new Point2D.Double(0, 0), 0);
	}
	
	public AbstractBall(ArkanoidField field, Point2D.Double pos, int radius) {
	    
	    this(field, pos, radius, new Speed2D(0, 0));
	}
	
	public AbstractBall(ArkanoidField field, Point2D.Double pos, int radius, Speed2D speed) {
        
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
	public void setPosition(Point2D.Double pos) {
	    
	    super.setPosition(pos);
	}
	
	/**
	 * Задать позицию шарика, указав координаты его середины
	 * @param center Позиция центра шарика
	 */
	public void setCenter(Point2D.Double center) {
		
		setPosition(new Point2D.Double(center.x - _size.width/2, center.y - _size.height/2));
	}
	
	/**
	 * Получить позицию центра шарика
	 * @return Позиция центра шарика
	 */
	public Point2D.Double getCenter() {
		
		return new Point2D.Double(this.getPosition().x + _size.width/2, 
								 this.getPosition().y + _size.height/2);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		
		AbstractBall clone = (AbstractBall) super.clone();
		return clone;
	}
}
