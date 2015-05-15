package arkanoid.entities.brick;

import java.awt.Dimension;
import java.awt.geom.Point2D.Float;

import arkanoid.ArkanoidField;
import arkanoid.util.Speed2D;

/**
 * Модель неразрушаемого кирпича.
 * @author Nikita Kalinin <nixorv@gmail.com>
 *
 */
public class UnbreakableBrick extends AbstractBrick {

	public UnbreakableBrick(ArkanoidField field, Float pos, Dimension dim, Speed2D speed) {
        
	    super(field, pos, dim, speed);
    }

    public UnbreakableBrick(ArkanoidField field, Float pos, Dimension dim) {
        
        super(field, pos, dim);
    }

    public UnbreakableBrick(ArkanoidField field) {
		
        super(field);
	}
    
    @Override
    public Object clone() throws CloneNotSupportedException {
    	
    	UnbreakableBrick clone = (UnbreakableBrick) super.clone();
    	return clone;
    }

}
