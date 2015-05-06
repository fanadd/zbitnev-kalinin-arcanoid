package arkanoid.entities;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import view.GameFieldView;
import view.PublishingSprite;
import arkanoid.Entity;
import arkanoid.Speed2D;
import arkanoid.interaction.GenericEventListener;
import arkanoid.interaction.PositionChangeListener;
import arkanoid.interaction.SpeedChangeListener;

/**
 * Представление отдельного игрового объекта
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public class EntityView
		implements PositionChangeListener, SpeedChangeListener, GenericEventListener {

    protected final Entity ingameObject;
    
    protected GameFieldView _fieldView = null;
	protected PublishingSprite _sprite = null;
	protected Point2D.Float _position = null;
	protected Speed2D _speed = null;
	protected ArrayList<PositionChangeListener> _positionListeners = new ArrayList<>();
	protected ArrayList<SpeedChangeListener> _speedListeners = new ArrayList<>();
	com.golden.gamedev.object.Sprite _gtgeSprite = null;
	
	private EntityView() {
		ingameObject = null;
	}
	
	EntityView(com.golden.gamedev.object.Sprite gtgeSprite) {
		
		this();
		if (gtgeSprite == null)
			throw new NullPointerException();
		_gtgeSprite = gtgeSprite;
	}
	
	public void setImage(BufferedImage image) {
		_gtgeSprite.setImage(image);
	}
	
	/**
	 * Создает представление объекта на основе его модели и спрайта.
	 * Этот метод автоматически согласует слушателей и связывает спрайт с объектом представления, которому он принадлежит.
	 * @param obj Модель игрового объекта.
	 * @param sprite Спрайт, которым он будет отображен.
	 */
	public EntityView(Entity obj, PublishingSprite sprite, GameFieldView view) {
	    
	    if (sprite == null || obj == null) {
	        throw new NullPointerException();
	    }
	    
	    this.ingameObject = obj;
	    this._sprite       = sprite;
	    this._fieldView    = view;
	    this._position     = obj.getPosition();
	    this._speed        = obj.getSpeed();
	    this._sprite.setLocation(_position.x, _position.y);
	    this._sprite.setSpeed(this._speed.x(), this._speed.y());
	    this._sprite.setObjectView(this);
	    addPositionChangeListener(obj);
	    addSpeedChangeListener(obj);
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
    	
    	if (_sprite.getX() != this._position.x || _sprite.getY() != this._position.y) {
    	    this._position = new Point2D.Float((float)_sprite.getX(), (float)_sprite.getY());
    	    for (PositionChangeListener l : _positionListeners) {
    	        l.positionChanged((Float) this._position.clone());
    	    }
    	}
    	
    	if (_sprite.getHorizontalSpeed() != this._speed.x() || _sprite.getVerticalSpeed() != this._speed.y()) {
    	    this._speed = new Speed2D(_sprite.getHorizontalSpeed(), _sprite.getVerticalSpeed());
    	    for (SpeedChangeListener l : _speedListeners) {
    	        l.speedChanged((Speed2D) this._speed.clone());
    	    }
    	}
    }
    
    public void renderDeprecated(Graphics2D g) {
    	
    	_sprite.render(g);
    }
    
	public void render(Graphics2D g) {
		_gtgeSprite.render(g);
	}
    
	@Override
	public void positionChanged(Point2D.Float newpos) {
		
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
	
	/**
	 * Добавить слушателя изменения позиции представления объекта
	 * @param l Новый слушатель
	 */
	public void addPositionChangeListener(PositionChangeListener l) {
		_positionListeners.add(l);
	}
	
	/**
	 * Удалить слушателя изменения позиции представления объекта
	 * @param l Удаляемый слушатель
	 */
	public void removePositionChangeListener(PositionChangeListener l) {
		_positionListeners.remove(l);
	}
	
	/**
	 * Добавить слушателя изменения скорости представления объекта
	 * @param l Новый слушатель
	 */
	public void addSpeedChangeListener(SpeedChangeListener l) {
		_speedListeners.add(l);
	}
	
	/**
	 * Удалить слушателя изменения скорости представления объекта
	 * @param l Удаляемый слушатель
	 */
	public void removeSpeedChangeListener(SpeedChangeListener l) {
		_speedListeners.remove(l);
	}

	@Override
	public void destroyed() {
		this._fieldView.removeObjectView(this);
	}
}
