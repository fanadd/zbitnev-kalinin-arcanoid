package model.brick;

import java.awt.Dimension;
import java.awt.geom.Point2D.Float;

import model.GameField;
import model.Entity;
import model.Speed2D;

/**
 * Модель абстрактного кирпича.
 * @author Nikita Kalinin <nixorv@gmail.com>
 *
 */
public abstract class AbstractBrick extends Entity {

	public AbstractBrick(GameField field) {
		
	    super(field);
	}

    public AbstractBrick(GameField field, Float pos, Dimension dim, Speed2D speed) {
        
        super(field, pos, dim, speed);
    }

    public AbstractBrick(GameField field, Float pos, Dimension dim) {
        
        super(field, pos, dim);
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
    	
    	AbstractBrick clone = (AbstractBrick) super.clone();
    	return clone;
    }
}
