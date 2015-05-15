package arkanoid.entities;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.golden.gamedev.object.Sprite;

import arkanoid.Entity;
import arkanoid.ArkanoidFieldView;
import arkanoid.interaction.GenericEventListener;
import arkanoid.util.Speed2D;

/**
 * Представление отдельного игрового объекта
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public class EntityView implements GenericEventListener {

    protected final Entity ingameObject;
    
    protected ArkanoidFieldView _fieldView = null;
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
			Entity obj, ArkanoidFieldView view) {
	    
	    if (obj == null) {
	        throw new NullPointerException();
	    }
	    
		if (gtgeSprite == null)
			throw new NullPointerException();
		_gtgeSprite = gtgeSprite;
		
	    this.ingameObject = obj;
	    this._fieldView    = view;
	    obj.addGenericEventListener(this);
	}
    
	public void render(Graphics2D g) {
		_gtgeSprite.render(g);
	}
	
	/**
	 * Возвращает модель игрового объекта.
	 * @return IngameObject.
	 */
	public Entity getIngameObject() {
	    
	    return ingameObject;
	}
	
	/**
	 * Возвращает спрайт, принадлежащий данному представлению объекта.
	 * @return Спрайт.
	 */
	public Sprite getSprite() {
	    return _gtgeSprite;
	}

	@Override
	public void destroyed() {
		this._fieldView.removeObjectView(this);
	}
}
