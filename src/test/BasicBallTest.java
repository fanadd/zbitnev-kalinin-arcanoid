package test;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import model.GameField;
import model.ball.BasicBall;

import org.junit.Test;

public class BasicBallTest {
    
    GameField field = new GameField(new Dimension(100, 100));
    BasicBall ball = new BasicBall(field, new Point2D.Double(50, 50), 8);
    
    @Test
    public void testSetRadius() {
        
        ball.setRadius(10);
        assertTrue(ball.getSize().width == 20 && ball.getSize().height == 20);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetRectangleSize() {
        
        // Задаем различные ширину и высоту.
        ball.setSize(new Dimension(20, 14));
        assertTrue(ball.getSize().width == 20 && ball.getSize().height == 14);
        
        // Получить радиус не должно быть возможным.
        ball.getRadius(); // Exception expected.
    }
    
    @Test
    public void testSetCenter() {
        
        ball.setRadius(8);
        
        // Перемещаем мяч, задавая в качестве ориентира его центр.
        ball.setCenter(new Point2D.Double(20, 30));
        assertTrue(ball.getPosition().x == 12 && ball.getPosition().y == 22);
        
        // То же самое с различной шириной и высотой.
        ball.setSize(new Dimension(20, 14));
        ball.setCenter(new Point2D.Double(40, 50));
        assertTrue(ball.getPosition().x == 30 && ball.getPosition().y == 43);
    }
    
    @Test
    public void testGetCenter() {
        
        ball.setRadius(8);
        ball.setPosition(new Point2D.Double(50, 60));
        
        assertTrue(ball.getCenter().x == 58 && ball.getCenter().y == 68);
        
        ball.setSize(new Dimension(20, 14));
        ball.setPosition(new Point2D.Double(60, 30));
        
        assertTrue(ball.getCenter().x == 70 && ball.getCenter().y == 37);
    }
}
