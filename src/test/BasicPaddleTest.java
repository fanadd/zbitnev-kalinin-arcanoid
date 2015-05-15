package test;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import model.GameField;
import model.Speed2D;
import model.ball.BasicBall;
import model.paddle.BasicPaddle;

import org.junit.Test;

public class BasicPaddleTest {

    GameField field = new GameField(new Dimension(300, 200));
    BasicPaddle paddle = new BasicPaddle(field, new Point2D.Double(100, 100), new Dimension(96, 16));
    
    @Test
    public void testAddBall() {
        
        // Размещаем мячи вокруг ракетки.
        
        BasicBall ball1 = new BasicBall(field, new Point2D.Double(10, 0), 8);
        BasicBall ball2 = new BasicBall(field, new Point2D.Double(110, 0), 8);
        BasicBall ball3 = new BasicBall(field, new Point2D.Double(180, 0), 8);
        BasicBall ball4 = new BasicBall(field, new Point2D.Double(220, 0), 8);
        
        BasicBall ball5 = new BasicBall(field, new Point2D.Double(10, 100), 8);
        BasicBall ball6 = new BasicBall(field, new Point2D.Double(220, 100), 8);
        
        BasicBall ball7  = new BasicBall(field, new Point2D.Double(10, 150), 8);
        BasicBall ball8  = new BasicBall(field, new Point2D.Double(110, 150), 8);
        BasicBall ball9  = new BasicBall(field, new Point2D.Double(180, 150), 8);
        BasicBall ball10 = new BasicBall(field, new Point2D.Double(220, 150), 8);
        
        // Добавляем их на ракетку. Мячи не должны вылазить за ракетку по горизонтали
        // и должны лежать на ней.
        
        paddle.addBall(ball1);
        assertTrue(ball1.getPosition().x == 100 && ball1.getPosition().y == 84);
        paddle.removeBall(ball1);
        
        paddle.addBall(ball2);
        assertTrue(ball2.getPosition().x == 110 && ball2.getPosition().y == 84);
        paddle.removeBall(ball2);
        
        paddle.addBall(ball3);
        assertTrue(ball3.getPosition().x == 180 && ball3.getPosition().y == 84);
        paddle.removeBall(ball3);
        
        paddle.addBall(ball4);
        assertTrue(ball4.getPosition().x == 180 && ball4.getPosition().y == 84);
        paddle.removeBall(ball4);
        
        paddle.addBall(ball5);
        assertTrue(ball5.getPosition().x == 100 && ball5.getPosition().y == 84);
        paddle.removeBall(ball5);
        
        paddle.addBall(ball6);
        assertTrue(ball6.getPosition().x == 180 && ball6.getPosition().y == 84);
        paddle.removeBall(ball6);
        
        paddle.addBall(ball7);
        assertTrue(ball7.getPosition().x == 100 && ball7.getPosition().y == 84);
        paddle.removeBall(ball7);
        
        paddle.addBall(ball8);
        assertTrue(ball8.getPosition().x == 110 && ball8.getPosition().y == 84);
        paddle.removeBall(ball8);
        
        paddle.addBall(ball9);
        assertTrue(ball9.getPosition().x == 180 && ball9.getPosition().y == 84);
        paddle.removeBall(ball9);
        
        paddle.addBall(ball10);
        assertTrue(ball10.getPosition().x == 180 && ball10.getPosition().y == 84);
        paddle.removeBall(ball10);
    }

    @Test
    public void testGetFireSpeed() {
        
        // Размещаем мячи на ракетке в трех разных зонах.
        BasicBall ball1 = new BasicBall(field, new Point2D.Double(110, 0), 8);
        BasicBall ball2 = new BasicBall(field, new Point2D.Double(140, 0), 8);
        BasicBall ball3 = new BasicBall(field, new Point2D.Double(180, 0), 8);
        Speed2D speed = null;
        
        paddle.addBall(ball1);
        paddle.addBall(ball2);
        paddle.addBall(ball3);
        
        // Рассчитываем вектора скорости при запуске мачей с ракетки и проверяем их направления.
        speed = paddle.getFireSpeed(ball1);
        assertTrue(speed.x() < 0 && speed.y() < 0);
        
        speed = paddle.getFireSpeed(ball2);
        assertTrue(Math.abs(speed.x()) < 0.00001 && speed.y() < 0);
        
        speed = paddle.getFireSpeed(ball3);
        assertTrue(speed.x() > 0 && speed.y() < 0);
        
        paddle.removeBall(ball1);
        paddle.removeBall(ball2);
        paddle.removeBall(ball3);
    }
    
    @Test
    public void fireBalls() {
        
        // Размещаем мячи на ракетке в трех разных зонах и запускаем их.
        BasicBall ball1 = new BasicBall(field, new Point2D.Double(110, 0), 8);
        BasicBall ball2 = new BasicBall(field, new Point2D.Double(140, 0), 8);
        BasicBall ball3 = new BasicBall(field, new Point2D.Double(180, 0), 8);
        
        paddle.addBall(ball1);
        paddle.addBall(ball2);
        paddle.addBall(ball3);
        
        paddle.fireBalls();
        
        // Проверяем направления векторов скорости.
        assertTrue(ball1.getSpeed().x() < 0 && ball1.getSpeed().y() < 0);
        assertTrue(Math.abs(ball2.getSpeed().x()) < 0.00001 && ball2.getSpeed().y() < 0);
        assertTrue(ball3.getSpeed().x() > 0 && ball3.getSpeed().y() < 0);
        
        paddle.removeBall(ball1);
        paddle.removeBall(ball2);
        paddle.removeBall(ball3);
    }
    
    @Test
    public void testSetPosition() {
        
        BasicBall ball = new BasicBall(field, new Point2D.Double(140, 0), 8);
        paddle.addBall(ball);
        
        // Смещаем ракетку и убеждаемся, что мяч переместился вместе с ней.
        paddle.setPosition(new Point2D.Double(10, 150));
        assertTrue(ball.getPosition().x == 50 && ball.getPosition().y == 134);
    }
}
