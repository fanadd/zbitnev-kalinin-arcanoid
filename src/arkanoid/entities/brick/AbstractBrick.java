package arkanoid.entities.brick;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import arkanoid.ArkanoidField;
import arkanoid.entities.Entity;
import arkanoid.util.Speed2D;

/**
 * Модель абстрактного кирпича.
 * @author Nikita Kalinin <nixorv@gmail.com>
 *
 */
public abstract class AbstractBrick extends Entity {

	public AbstractBrick(ArkanoidField field) {
		
	    super(field);
	}

    public AbstractBrick(ArkanoidField field, Point2D.Double pos, Dimension dim, Speed2D speed) {
        
        super(field, pos, dim, speed);
    }

    public AbstractBrick(ArkanoidField field, Point2D.Double pos, Dimension dim) {
        
        super(field, pos, dim);
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
    	
    	AbstractBrick clone = (AbstractBrick) super.clone();
    	return clone;
    }
}
