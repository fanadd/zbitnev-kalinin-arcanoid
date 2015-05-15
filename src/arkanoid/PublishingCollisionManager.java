package arkanoid;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

import arkanoid.collision.CollidedObject;

import com.golden.gamedev.object.Sprite;
import com.golden.gamedev.object.collision.AdvanceCollisionGroup;
import com.golden.gamedev.object.collision.CollisionGroup;
import com.golden.gamedev.object.collision.CollisionShape;

/**
 * Менеджер столкновений, сообщающий о коллизиях между объектами
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public class PublishingCollisionManager extends AdvanceCollisionGroup {

	protected HashMap<CollidedObject, ArrayList<CollidedObject>> _storage = new HashMap<>();
	
	@Override
	public void collided(Sprite arg0, Sprite arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isCollide(Sprite s1, Sprite s2, CollisionShape shape1, CollisionShape shape2) {
		
		boolean retval = super.isCollide(s1, s2, shape1, shape2);
		
		// Словарь столкновений будет формироваться в процессе детекции коллизий
		if (retval) {
		
			int obj1colside = -1, obj2colside = -1;
			switch (this.collisionSide) {
			case CollisionGroup.BOTTOM_TOP_COLLISION:
				obj1colside = CollidedObject.SIDE_TOP;
				obj2colside = CollidedObject.SIDE_BOTTOM;
				break;
			case CollisionGroup.TOP_BOTTOM_COLLISION:
				obj1colside = CollidedObject.SIDE_BOTTOM;
				obj2colside = CollidedObject.SIDE_TOP;
				break;
			case CollisionGroup.RIGHT_LEFT_COLLISION:
				obj1colside = CollidedObject.SIDE_LEFT;
				obj2colside = CollidedObject.SIDE_RIGHT;
				break;
			case CollisionGroup.LEFT_RIGHT_COLLISION:
				obj1colside = CollidedObject.SIDE_RIGHT;
				obj2colside = CollidedObject.SIDE_LEFT;
				break;
			default:
				break;
			}
			
			CollidedObject obj1 = new CollidedObject(
					((PublishingSprite)s1).getObjectView().getIngameObject(), 
					new Point2D.Double((float)s1.getOldX(), (float)s1.getOldY()),
					obj1colside, new Rectangle2D.Double(shape1.getX(), 
													    shape1.getY(), 
													    shape1.getWidth(), 
													    shape1.getHeight()));
			
			CollidedObject obj2 = new CollidedObject(
					((PublishingSprite)s2).getObjectView().getIngameObject(), 
					new Point2D.Double((float)s2.getOldX(), (float)s2.getOldY()),
					obj2colside, new Rectangle2D.Double(shape2.getX(), 
						    							shape2.getY(), 
						    							shape2.getWidth(), 
						    							shape2.getHeight()));
			
			if (!_storage.keySet().contains(obj1)) {
				_storage.put(obj1, new ArrayList<CollidedObject>());
			}
			_storage.get(obj1).add(obj2);
		}
		
		return retval;
	}
	
	/**
	 * Получить словарь столкновений объектов в текущем кадре. Объекты представлены в виде 
	 * CollidedObject, содержащих дополнительную информацию о коллизиях
	 * @return Словарь столкновений.
	 */
	public HashMap<CollidedObject, ArrayList<CollidedObject>> getCollidedStorage() {
		return _storage;
	}
	
	/**
	 * Очистить словарь столкновений объектов в текущем кадре.
	 */
	public void clearCollidedStorage() {
		_storage.clear();
	}
}
