package test;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import model.Direction;
import model.GameField;
import model.Player;
import model.ball.BasicBall;
import model.paddle.BasicPaddle;

import org.junit.Test;

public class PlayerTest {

    GameField fieldModel = new GameField(new Dimension(300, 100));
    BasicPaddle paddle = new BasicPaddle(fieldModel, new Point2D.Double(0, 84), new Dimension(96, 16));
    Player player = new Player(paddle);
    
    @Test
    public void testFire() {
        
        // Размещаем мяч на ракетке, принадлежащей игроку, просим игрока запустить мячи со всех ракеток.
        BasicBall ball = new BasicBall(fieldModel, new Point2D.Double(20, 20), 8);
        paddle.addBall(ball);
        player.firePaddles();
        
        // Убеждаемся, что на ракетке нет мяча, а вектор его скорости имеет верное направление.
        assertTrue(paddle.getBalls().isEmpty());
        assertTrue(ball.getSpeed().x() < 0 || ball.getSpeed().y() < 0);
    }

    @Test
    public void testSetPositionX() {
        
        // Просим игрока переместить ракетку в указанную точку, смотрим, чтобы она не вылезла за поле.
        
        player.setPaddlesPositionX(1);
        assertTrue(paddle.getPosition().x == 1 && paddle.getPosition().y == 84);
        
        player.setPaddlesPositionX(270);
        assertTrue(paddle.getPosition().x == 204 && paddle.getPosition().y == 84);
        
        player.setPaddlesPositionX(204);
        assertTrue(paddle.getPosition().x == 204 && paddle.getPosition().y == 84);
        
        player.setPaddlesPositionX(0);
        assertTrue(paddle.getPosition().x == 0 && paddle.getPosition().y == 84);
        
        player.setPaddlesPositionX(-10);
        assertTrue(paddle.getPosition().x == 0 && paddle.getPosition().y == 84);
    }
    
    @Test
    public void testMovePaddles() {
        
        // Просим игрока передвинуть ракетку в сторону, смотрим, чтобы она не вылезла за границы поля.
        
        player.movePaddles(Direction.west());
        assertTrue(paddle.getPosition().x == 0 && paddle.getPosition().y == 84);
        
        player.movePaddles(Direction.east());
        assertTrue(paddle.getPosition().x == Math.round(paddle.getSize().width / 3 * 2) && paddle.getPosition().y == 84);
        
        player.setPaddlesPositionX(200);
        player.movePaddles(Direction.east());
        assertTrue(paddle.getPosition().x == 204 && paddle.getPosition().y == 84);
    }
}
