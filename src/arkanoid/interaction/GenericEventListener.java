package arkanoid.interaction;

import arkanoid.Entity;

/**
 * Интерфейс слушателя событий добавления и удаления игрового объекта с поля.
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 */
public interface GenericEventListener {
	
	/**
	 * Объект был удалён с поля.
	 * @param entity
	 */
	public void entityRemoved(Entity entity);
	
	/**
	 * Объект был добавлен на поле.
	 * @param entity
	 */
	public void entityAdded(Entity entity);
}
