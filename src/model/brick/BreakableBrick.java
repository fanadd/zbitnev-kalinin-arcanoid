package model.brick;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;

import model.GameField;
import model.Speed2D;
import model.collision.BehaviourDestroy;

/**
 * Модель разрушаемого кирпича.
 * @author Nikita Kalinin <nixorv@gmail.com>
 *
 */
public class BreakableBrick extends AbstractBrick {

	public BreakableBrick(GameField field) {
		
	    this(field, new Point2D.Float(0, 0), new Dimension(0, 0));
	}
	

	public BreakableBrick(GameField field, Float pos, Dimension dim, Speed2D speed) {
        
	    super(field, pos, dim, speed);
	    this.addDefaultCollisionBehaviour(BehaviourDestroy.getInstance());
    }


    public BreakableBrick(GameField field, Float pos, Dimension dim) {
        
        this(field, pos, dim, new Speed2D(0, 0));
    }


    /**
	 * Разрушает кирпич.
	 */
	@Override
	public void destroy() {
	    super.destroy();
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
	
		BreakableBrick clone = (BreakableBrick) super.clone();
		return clone;
	}
}
