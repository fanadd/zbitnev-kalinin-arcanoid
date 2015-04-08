package model.interaction;

import model.Entity;

/**
 * Интерфейс слушателя событий создания, удаления.
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public interface GenericEventListener {
	
	/**
	 * Объект был уничтожен.
	 */
	void destroyed();
}
