package viewmodel;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import arkanoid.Speed2D;

/**
 * Спрайт игрового объекта
 */
public class Sprite {

	com.golden.gamedev.object.Sprite _sprite;
	
	Sprite(com.golden.gamedev.object.Sprite gtgeSprite) {
		_sprite = gtgeSprite;
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
}
