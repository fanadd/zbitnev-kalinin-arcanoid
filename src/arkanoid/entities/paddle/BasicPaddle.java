package arkanoid.entities.paddle;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import arkanoid.ArkanoidField;

/**
 * Модель обычной ракетки.
 * @author Nikita Kalinin <nixorv@gmail.com>
 *
 */
public class BasicPaddle extends AbstractPaddle {

    public BasicPaddle(ArkanoidField field, Point2D.Double pos, Dimension dim) {
        
        super(field, pos, dim);
    }

    public BasicPaddle(ArkanoidField field) {
		
        super(field);
	}
    
    @Override
    public Object clone() throws CloneNotSupportedException {
    	
    	BasicPaddle clone = (BasicPaddle) super.clone();
    	return clone;
    }

}
