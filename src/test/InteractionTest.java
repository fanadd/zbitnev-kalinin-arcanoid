package test;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import model.GameField;
import model.Speed2D;
import model.ball.BasicBall;

import org.junit.BeforeClass;
import org.junit.Test;

import view.DefaultObjectViewFactory;
import view.GameFieldView;
import view.IngameObjectView;

public class InteractionTest {

    BufferedImage b = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    DefaultObjectViewFactory viewfact = new DefaultObjectViewFactory(b, b, b, b, new GameFieldView());
    
    BasicBall ballModel = new BasicBall(new GameField(new Dimension(100, 100)));
    IngameObjectView ballView = viewfact.newBasicBallView(ballModel);
    
    @Test
    public void testSpeedChanged() {
        
        // Изменилась скорость спрайта.
        ballView.getSprite().setSpeed(5, 7);
        ballView.update(0);
        assertTrue(ballModel.getSpeed().equals(new Speed2D(5, 7)));
        
        // Изменилась скорость в модели.
        ballModel.setSpeed(new Speed2D(2, 3));
        assertTrue(ballView.getSprite().getHorizontalSpeed() == 2
                && ballView.getSprite().getVerticalSpeed() == 3);
    }

    @Test
    public void testPositionChanged() {
        
        ballModel.setSpeed(new Speed2D(0, 0));
        
        // Изменилась позиция спрайта.
        ballView.getSprite().setLocation(10, 15);
        ballView.update(0);
        assertTrue(ballModel.getPosition().equals(new Point2D.Double(10, 15)));
        
        // Изменилась позиция в модели.
        ballModel.setPosition(new Point2D.Double(4, 7));
        assertTrue(ballView.getSprite().getX() == 4
                && ballView.getSprite().getY() == 7);
    }
}
