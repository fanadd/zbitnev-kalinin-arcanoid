package model.interaction;

import java.util.ArrayList;
import java.util.HashMap;

import model.Entity;
import model.collision.CollidedObject;


/**
 * Интерфейс слушателя событий о столкновениях игровых объектов.
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public interface CollisionListener {

	/**
	 * Произошла коллизия между игровыми объектами.
	 * @param storage Ключ -- столкнувшийся объект, Значение -- список объектов, с которыми он
	 *                столкнулся.
	 */
	void collisionOccured(HashMap<CollidedObject, ArrayList<CollidedObject>> storage);
}
