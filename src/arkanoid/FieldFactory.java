package arkanoid;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import arkanoid.collision.ReactionRebound;
import arkanoid.entities.ball.BasicBall;
import arkanoid.entities.brick.BreakableBrick;
import arkanoid.entities.brick.UnbreakableBrick;
import arkanoid.entities.paddle.BasicPaddle;
import arkanoid.util.Speed2D;

/**
 * Фабрика игровых полей, способна конструировать игровое поле.
 */
public class FieldFactory {

	/**
	 * Создать тестовое игровое поле указанного размера.
	 * @param size Размеры создаваемого поля.
	 */
	public GameField createTestField(Dimension size) {
		
		// Построение уровня
		// TODO: Загрузка уровня из файла (пока уровень захардкоден)
		GameField newField = new GameField(size);
		BasicBall newball = new BasicBall(newField, new Point2D.Float(40, 160), 8, new Speed2D(0.03, -0.01));
		BreakableBrick newbrick = new BreakableBrick(newField, new Point2D.Float(180, 120), new Dimension(48, 24));
        BreakableBrick newbrick2 = new BreakableBrick(newField, new Point2D.Float(228, 120), new Dimension(48, 24));
        UnbreakableBrick newbrick3 = new UnbreakableBrick(newField, new Point2D.Float(276, 120), new Dimension(48, 24));
        BasicPaddle paddle = new BasicPaddle(newField, new Point2D.Float(0, 584), new Dimension(96, 16));
        
        // Тестирование столкновения множества шаров
        BasicBall ball01 = new BasicBall(newField, new Point2D.Float((float) 213.3975, 250), 16, new Speed2D(0.043, -0.025));
        BasicBall ball02 = new BasicBall(newField, new Point2D.Float(400, 200), 16, new Speed2D(-0.05, 0));
        ball01.addDefaultCollisionBehaviour(ReactionRebound.getInstance());
        ball02.addDefaultCollisionBehaviour(ReactionRebound.getInstance());
        
        paddle.addBall(newball);
        
        newField.addObject(newball);
        newField.addObject(newbrick);
        newField.addObject(newbrick2);
        newField.addObject(newbrick3);
        newField.addObject(paddle);
        newField.addObject(ball01);
        newField.addObject(ball02);
        
        return newField;
	}
}
