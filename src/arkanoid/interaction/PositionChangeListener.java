package arkanoid.interaction;

import java.awt.geom.Point2D;

/**
 * Интерфейс слушателя событий изменения позиции.
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public interface PositionChangeListener {

	/**
	 * Позиция изменилась.
	 * @param newpos Новая позиция.
	 */
	public void positionChanged(Point2D.Float newpos);
}
