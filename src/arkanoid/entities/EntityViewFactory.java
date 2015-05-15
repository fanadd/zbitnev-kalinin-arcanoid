package arkanoid.entities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import arkanoid.ArkanoidFieldView;
import arkanoid.Entity;
import arkanoid.PublishingSprite;
import arkanoid.entities.ball.BasicBall;
import arkanoid.entities.brick.BreakableBrick;
import arkanoid.entities.brick.UnbreakableBrick;
import arkanoid.entities.paddle.BasicPaddle;

/**
 * Фабрика представлений игровых объектов
 */
public class EntityViewFactory {

	private static final HashMap<Class<?>, String> entityImageUrl 
		= new HashMap<Class<?>, String>();
	static {
		entityImageUrl.put(BreakableBrick.class, "default/gfx/bricks/breakable.png");
		entityImageUrl.put(UnbreakableBrick.class, "default/gfx/bricks/unbreakable.png");
		entityImageUrl.put(BasicBall.class, "default/gfx/balls/basic.png");
		entityImageUrl.put(BasicPaddle.class, "default/gfx/paddles/basic.png");
	}
	
	public EntityView instantiateEntityView(Entity entity, ArkanoidFieldView fieldView) {
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(entityImageUrl.get(entity.getClass())));
		} catch (IOException e) {
		}
		
		// TODO Убрать зависимость от игрового поля
		// TODO Не использовать больше паблишинг спрайт
		PublishingSprite pubSprite = new PublishingSprite();
		pubSprite.setImage(img);
		EntityView view = new EntityView(entity.getSprite()._sprite, entity, pubSprite, fieldView);
		view._gtgeSprite.setImage(img);

		return view;
	}
}
