package test;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import model.GameField;
import model.GameModel;
import model.Speed2D;
import model.ball.BasicBall;
import model.brick.BreakableBrick;
import model.collision.CollidedObject;
import model.paddle.BasicPaddle;

import org.junit.Test;

import com.golden.gamedev.object.collision.CollisionRect;

public class BehaviourPaddleReboundTest {

    @Test
    public void testRebound() {
        
        GameModel model = new GameModel();
        GameField field = new GameField(new Dimension(100, 100));
        model.setField(field);
        
        BasicPaddle paddle = new BasicPaddle(field, new Point2D.Double(0, 80), new Dimension(96, 16));
        
        // Размещаем три мяча в три активные области ракетки.
        BasicBall ballLeft = new BasicBall(field, new Point2D.Double(5, 64), 8, new Speed2D(0, -0.1));
        BasicBall ballCenter = new BasicBall(field, new Point2D.Double(40, 64), 8, new Speed2D(0, -0.1));
        BasicBall ballRight = new BasicBall(field, new Point2D.Double(80, 64), 8, new Speed2D(0, -0.1));
        
        // Имитируем столкновение.
        
        CollisionRect ballLeftShape = new CollisionRect();
        ballLeftShape.setBounds(5, 64, 16, 16);
        
        CollisionRect ballCenterShape = new CollisionRect();
        ballCenterShape.setBounds(40, 64, 16, 16);
        
        CollisionRect ballRightShape = new CollisionRect();
        ballRightShape.setBounds(80, 64, 16, 16);
        
        CollisionRect paddleShape = new CollisionRect();
        paddleShape.setBounds(0, 80, 96, 16);
        
        CollidedObject collidedBallLeft = new CollidedObject(ballLeft, new Point2D.Double(5, 64), CollidedObject.SIDE_BOTTOM, ballLeftShape);
        CollidedObject collidedBallCenter = new CollidedObject(ballCenter, new Point2D.Double(40, 64), CollidedObject.SIDE_BOTTOM, ballCenterShape);
        CollidedObject collidedBallRight = new CollidedObject(ballRight, new Point2D.Double(80, 64), CollidedObject.SIDE_BOTTOM, ballRightShape);
        CollidedObject collidedPaddle = new CollidedObject(paddle, new Point2D.Double(0, 80), CollidedObject.SIDE_TOP, paddleShape);
        
        ArrayList<CollidedObject> collidedToBallLeft = new ArrayList<>();
        collidedToBallLeft.add(collidedPaddle);
        
        ArrayList<CollidedObject> collidedToBallCenter = new ArrayList<>();
        collidedToBallCenter.add(collidedPaddle);
        
        ArrayList<CollidedObject> collidedToBallRight = new ArrayList<>();
        collidedToBallRight.add(collidedPaddle);
        
        HashMap<CollidedObject, ArrayList<CollidedObject>> collisions = new HashMap<>();
        collisions.put(collidedBallLeft, collidedToBallLeft);
        collisions.put(collidedBallCenter, collidedToBallCenter);
        collisions.put(collidedBallRight, collidedToBallRight);
        
        model.collisionOccured(collisions);
        
        // Проверяем, что мячи получили верные направления векторов скорости.
        assertTrue(ballLeft.getSpeed().x() < 0 && ballLeft.getSpeed().y() < 0);
        assertTrue(Math.abs(ballCenter.getSpeed().x()) < 0.00001 && ballCenter.getSpeed().y() < 0);
        assertTrue(ballRight.getSpeed().x() > 0 && ballRight.getSpeed().y() < 0);
    }
}
