package model.ball;

import java.awt.geom.Point2D;

import model.GameField;
import model.Speed2D;
import model.swarm.CanBeInSwarm;

/**
 * Модель обычного шарика
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public class BasicBall extends AbstractBall implements CanBeInSwarm {

	public BasicBall(GameField field) {
		
	    super(field);
	}

	public BasicBall(GameField field, Point2D.Float pos, int radius) {
	    
	    super(field, pos, radius);
	}
	
	public BasicBall(GameField field, Point2D.Float pos, int radius, Speed2D speed) {
	    
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
