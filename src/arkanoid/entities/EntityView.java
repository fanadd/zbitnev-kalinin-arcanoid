package arkanoid.entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import arkanoid.ArkanoidFieldView;
import arkanoid.Entity;

import com.golden.gamedev.object.Sprite;

/**
 * Представление отдельного игрового объекта
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public class EntityView {

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
}
