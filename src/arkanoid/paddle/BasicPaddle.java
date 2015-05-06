package arkanoid.paddle;

import java.awt.Dimension;
import java.awt.geom.Point2D.Float;

import arkanoid.GameField;
import arkanoid.util.Speed2D;

/**
 * Модель обычной ракетки.
 * @author Nikita Kalinin <nixorv@gmail.com>
 *
 */
public class BasicPaddle extends AbstractPaddle {

    public BasicPaddle(GameField field, Float pos, Dimension dim) {
        
        super(field, pos, dim);
    }

    public BasicPaddle(GameField field) {
		
        super(field);
	}
    
    @Override
    public Object clone() throws CloneNotSupportedException {
    	
    	BasicPaddle clone = (BasicPaddle) super.clone();
    	return clone;
    }

}
