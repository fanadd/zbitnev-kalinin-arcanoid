package arkanoid.entities;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import arkanoid.util.Speed2D;

/**
 * Спрайт игрового объекта
 */
public class Sprite implements Cloneable {

	com.golden.gamedev.object.Sprite _sprite;
	
	Sprite(com.golden.gamedev.object.Sprite gtgeSprite) {
		_sprite = gtgeSprite;
	}
	
	public Sprite(Dimension dim) {
		_sprite = new com.golden.gamedev.object.Sprite(dim.height, dim.width);
	}
	
	public void update(long timeElapsed) {
		_sprite.update(timeElapsed);
	}
	
	public Speed2D getSpeed() {
		return new Speed2D(_sprite.getHorizontalSpeed(), _sprite.getVerticalSpeed());
	}

	public void setSpeed(Speed2D speed) {
		
		if (speed == null) {
			throw new NullPointerException();
		}
		_sprite.setSpeed(speed.x(), speed.y());
	}
	
	public Point2D.Double getPosition() {
		return new Point2D.Double(_sprite.getX(), _sprite.getY());
	}
	
	public void setPosition(Point2D.Double position) {
		
		if (position == null) {
			throw new NullPointerException();
		}
		_sprite.setLocation(position.x, position.y);
	}
	
	public Dimension getSize() {
		return new Dimension(_sprite.getWidth(), _sprite.getHeight());
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		
		Sprite clone = (Sprite)super.clone();
		clone._sprite = new com.golden.gamedev.object.Sprite(_sprite.getImage());
		clone._sprite.setSpeed(_sprite.getHorizontalSpeed(), _sprite.getVerticalSpeed());
		clone._sprite.setX(_sprite.getX());
		clone._sprite.setY(_sprite.getY());
		
		return clone;
	}
}
