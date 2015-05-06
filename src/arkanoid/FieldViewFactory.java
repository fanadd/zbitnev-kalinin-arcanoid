package arkanoid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.golden.gamedev.engine.BaseLoader;
import com.golden.gamedev.object.background.ImageBackground;

import arkanoid.ball.BasicBall;
import arkanoid.brick.BreakableBrick;
import arkanoid.brick.UnbreakableBrick;
import arkanoid.paddle.BasicPaddle;
import view.DefaultObjectViewFactory;
import view.GameFieldView;

/**
 * Фабрика представлений игрового поля.
 */
public class FieldViewFactory {

	/**
	 * Получить представления для экземпляра игрового поля.
	 * @param field Экземпляр игрового поля.
	 * @param bsLoader Загрузщик изображений. TODO устранить.
	 */
	public GameFieldView instantiateFieldView(GameField field, BaseLoader bsLoader) {
		
		GameFieldView newFieldView = new GameFieldView();
		
		// Загрузка изображений для представлений игровых объектов
		BufferedImage bgImage               = bsLoader.getImage("default/gfx/misc/bg-blue.png");
	    BufferedImage basicBallImage        = bsLoader.getImage("default/gfx/balls/basic.png");
	    BufferedImage breakableBrickImage   = bsLoader.getImage("default/gfx/bricks/breakable.png");
	    BufferedImage unbreakableBrickImage = bsLoader.getImage("default/gfx/bricks/unbreakable.png");
	    BufferedImage basicPaddleImage      = bsLoader.getImage("default/gfx/paddles/basic.png");
		DefaultObjectViewFactory viewfact = new DefaultObjectViewFactory(basicBallImage, breakableBrickImage,
		        unbreakableBrickImage, basicPaddleImage, newFieldView);
		
		for (Entity e : field.getEntities()) {
			
			if (e instanceof BasicBall) {
				newFieldView.addObjectView(viewfact.newBasicBallView((BasicBall)e));
			}
			else if (e instanceof BreakableBrick) {
				newFieldView.addObjectView(viewfact.newBreakableBrickView((BreakableBrick)e));
			}
			else if (e instanceof UnbreakableBrick) {
				newFieldView.addObjectView(viewfact.newUnbreakableBrickView((UnbreakableBrick)e));
			}
			else if (e instanceof BasicPaddle) {
				newFieldView.addObjectView(viewfact.newBasicPaddleView((BasicPaddle)e));
			}
		}
		
		// Задать фон уровня.
		BufferedImage fieldBg = new BufferedImage(field.getSize().width, field.getSize().height, 
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = fieldBg.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, field.getSize().width, field.getSize().height);
		newFieldView.setBackground(new ImageBackground(fieldBg));
		
		return newFieldView;
	}
}
