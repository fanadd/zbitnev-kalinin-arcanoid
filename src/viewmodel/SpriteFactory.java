package viewmodel;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import arkanoid.Entity;

import com.golden.gamedev.object.PlayField;

public class SpriteFactory {
	
	private PlayField _field = null;
	private HashMap<Class<?>, String> _spriteUrls = new HashMap<>();
	
	public SpriteFactory(PlayField field) {
		
		if (field == null) {
			throw new NullPointerException();
		}
		_field = field;
		
		// TODO Инициализация словаря спрайтов
	}
	
	public Sprite createSpriteFor(Entity entity) {
	
		com.golden.gamedev.object.Sprite gtgeSprite 
			= getGtgeSprite(entity.getClass());
		_field.add(gtgeSprite);
		return new Sprite(gtgeSprite);
	}
	
	private com.golden.gamedev.object.Sprite getGtgeSprite(Class<?> entityClass) {
		
		if (!_spriteUrls.containsKey(entityClass)) {
			throw new IllegalArgumentException("No sprite url defined for class " 
					+ entityClass.toString());
		}
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(_spriteUrls.get(entityClass)));
		} catch (IOException e) {
		}
		
		return new com.golden.gamedev.object.Sprite(img);
	}
}
