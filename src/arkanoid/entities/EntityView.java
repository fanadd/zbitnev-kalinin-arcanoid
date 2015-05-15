package arkanoid.entities;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.golden.gamedev.object.Sprite;

import arkanoid.Entity;
import arkanoid.ArkanoidFieldView;
import arkanoid.PublishingSprite;
import arkanoid.interaction.GenericEventListener;
import arkanoid.interaction.PositionChangeListener;
import arkanoid.interaction.SpeedChangeListener;
import arkanoid.util.Speed2D;

/**
 * Представление отдельного игрового объекта
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public class EntityView
		implements PositionChangeListener, SpeedChangeListener, GenericEventListener {

    protected final Entity ingameObject;
    
    protected ArkanoidFieldView _fieldView = null;
	protected PublishingSprite _sprite = null;
	com.golden.gamedev.object.Sprite _gtgeSprite = null;
	
	public void setImage(BufferedImage image) {
		_gtgeSprite.setImage(image);
	}
	
	/**
	 * Создает представление объекта на основе его модели и спрайта.
	 * Этот метод автоматически согласует слушателей и связывает спрайт с объектом представления, которому он принадлежит.
	 * @param obj Модель игрового объекта.
	 * @param sprite Спрайт, которым он будет отображен.
	 */
	EntityView(com.golden.gamedev.object.Sprite gtgeSprite, 
			Entity obj, PublishingSprite sprite, ArkanoidFieldView view) {
	    
	    if (sprite == null || obj == null) {
	        throw new NullPointerException();
	    }
	    
		if (gtgeSprite == null)
			throw new NullPointerException();
		_gtgeSprite = gtgeSprite;
		
	    this.ingameObject = obj;
	    this._sprite       = sprite;
	    this._fieldView    = view;
	    this._sprite.setLocation(_gtgeSprite.getX(), _gtgeSprite.getY());
	    this._sprite.setSpeed(_gtgeSprite.getHorizontalSpeed(), _gtgeSprite.getVerticalSpeed());
	    this._sprite.setObjectView(this);
	    obj.addPositionChangeListener(this);
	    obj.addSpeedChangeListener(this);
	    obj.addGenericEventListener(this);
	}
	
    /**
     * Необходимо использовать вместо прямого обращения к спрайту.
     * @param timeElapsed Прошедшее время.
     */
    public void update(long timeElapsed) {
        
    	_sprite.update(timeElapsed);
    	
    	if (_sprite.getX() != _gtgeSprite.getX() || _sprite.getY() != _gtgeSprite.getY()) {
    	    _gtgeSprite.setX(_sprite.getX());
    	    _gtgeSprite.setY(_sprite.getY());
    	}
    	
    	if (_sprite.getHorizontalSpeed() != _gtgeSprite.getHorizontalSpeed() 
    			|| _sprite.getVerticalSpeed() != _gtgeSprite.getVerticalSpeed()) {
    	    _gtgeSprite.setSpeed(_sprite.getHorizontalSpeed(), _sprite.getVerticalSpeed());
    	}
    }
    
    public void renderDeprecated(Graphics2D g) {
    	
    	_sprite.render(g);
    }
    
	public void render(Graphics2D g) {
		_gtgeSprite.render(g);
	}
    
	@Override
	public void positionChanged(Point2D.Double newpos) {
		
		_sprite.setLocation(newpos.x, newpos.y);
	}

	@Override
	public void speedChanged(Speed2D newspeed) {
		
		_sprite.setSpeed(newspeed.x(), newspeed.y());
	}
	
	/**
	 * Возвращает модель игрового объекта.
	 * @return IngameObject.
	 */
	public Entity getIngameObject() {
	    
	    return ingameObject;
	}
	
	/**
	 * Добавить спрайт, принадлежащий данному представлению объекта
	 * @param sprite Добавляемый спрайт
	 */
	public void setSprite(PublishingSprite sprite) {
		
		if (sprite == null) {
			throw new NullPointerException();
		}
		
		this._sprite = sprite;
	}
	
	/**
	 * Возвращает спрайт, принадлежащий данному представлению объекта.
	 * @return Спрайт.
	 */
	public PublishingSprite getSprite() {
	    return _sprite;
	}

	@Override
	public void destroyed() {
		this._fieldView.removeObjectView(this);
	}
}
