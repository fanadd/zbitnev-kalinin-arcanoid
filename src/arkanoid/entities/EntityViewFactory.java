package arkanoid.entities;

import arkanoid.Entity;

/**
 * Фабрика представлений игровых объектов
 */
public class EntityViewFactory {

	public EntityView instantiateEntityView(Entity entity) {
		
		EntityView view = new EntityView(entity.getSprite()._sprite);
		return view;
	}
}
