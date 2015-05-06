package arkanoid.interaction;

import arkanoid.Speed2D;

/**
 * Интерфейс слушателя событий изменения скорости.
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public interface SpeedChangeListener {

	/**
	 * Скорость изменилась.
	 * @param newspeed Новое значение скорости
	 */
	public void speedChanged(Speed2D newspeed);
}
