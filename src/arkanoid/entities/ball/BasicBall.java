package arkanoid.entities.ball;

import java.awt.geom.Point2D;

import arkanoid.ArkanoidField;
import arkanoid.util.Speed2D;

/**
 * Модель обычного шарика
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public class BasicBall extends AbstractBall {

	public BasicBall(ArkanoidField field) {
		
	    super(field);
	}

	public BasicBall(ArkanoidField field, Point2D.Double pos, int radius) {
	    
	    super(field, pos, radius);
	}
	
	public BasicBall(ArkanoidField field, Point2D.Double pos, int radius, Speed2D speed) {
	    
	    super(field, pos, radius, speed);
	}

    @Override
    public float getDefaultSpeedScalar() {
        
        return (float) 0.2;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
    	
    	BasicBall clone = (BasicBall) super.clone();
    	return clone;
    }
}
