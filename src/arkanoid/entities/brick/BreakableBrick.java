package arkanoid.entities.brick;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import arkanoid.ArkanoidField;
import arkanoid.collision.ReactionDestroy;
import arkanoid.util.Speed2D;

/**
 * Модель разрушаемого кирпича.
 * @author Nikita Kalinin <nixorv@gmail.com>
 *
 */
public class BreakableBrick extends AbstractBrick {

	public BreakableBrick(ArkanoidField field) {
		
	    this(field, new Point2D.Double(0, 0), new Dimension(0, 0));
	}
	

	public BreakableBrick(ArkanoidField field, Point2D.Double pos, Dimension dim, Speed2D speed) {
        
	    super(field, pos, dim, speed);
	    this.addDefaultCollisionBehaviour(ReactionDestroy.getInstance());
    }


    public BreakableBrick(ArkanoidField field, Point2D.Double pos, Dimension dim) {
        
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
