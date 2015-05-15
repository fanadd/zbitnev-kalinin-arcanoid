package arkanoid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.golden.gamedev.engine.BaseLoader;

import arkanoid.entities.EntityViewFactory;


/**
 * Фабрика представлений игрового поля.
 */
public class FieldViewFactory {

	private EntityViewFactory _factory = new EntityViewFactory();
	/**
	 * Получить представления для экземпляра игрового поля.
	 * @param field Экземпляр игрового поля.
	 * @param bsLoader Загрузщик изображений. TODO устранить.
	 */
	public ArkanoidFieldView instantiateFieldView(ArkanoidField field, BaseLoader bsLoader) {
		
		ArkanoidFieldView newFieldView = new ArkanoidFieldView(field);
		
		// Задать фон уровня.
		BufferedImage fieldBg = new BufferedImage(field.getSize().width, field.getSize().height, 
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = fieldBg.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, field.getSize().width, field.getSize().height);
		newFieldView.setBackground(fieldBg);
		
		return newFieldView;
	}
}
