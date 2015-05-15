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
import model.brick.UnbreakableBrick;
import model.collision.CollidedObject;

import org.junit.Test;

import com.golden.gamedev.object.collision.CollisionRect;

public class BehaviourReboundTest {

    @Test
    public void testRebound() {
        
        GameModel model = new GameModel();
        GameField field = new GameField(new Dimension(120, 100));
        model.setField(field);
        
        UnbreakableBrick brick = new UnbreakableBrick(field, new Point2D.Double(50, 50), new Dimension(48, 24));
        BasicBall ballTop = new BasicBall(field, new Point2D.Double(70, 40), 8, new Speed2D(-0.1, 0.2));
        BasicBall ballBottom = new BasicBall(field, new Point2D.Double(70, 70), 8, new Speed2D(0.1, -0.2));
        BasicBall ballLeft = new BasicBall(field, new Point2D.Double(40, 50), 8, new Speed2D(0.1, 0.2));
        BasicBall ballRight = new BasicBall(field, new Point2D.Double(84, 50), 8, new Speed2D(-0.1, -0.2));
        
        // Имитируем столкновение.
        
        CollisionRect ballTopShape = new CollisionRect();
        ballTopShape.setBounds(70, 30, 16, 16);
        
        CollisionRect ballBottomShape = new CollisionRect();
        ballBottomShape.setBounds(70, 75, 16, 16);
        
        CollisionRect ballLeftShape = new CollisionRect();
        ballLeftShape.setBounds(30, 50, 16, 16);
        
        CollisionRect ballRightShape = new CollisionRect();
        ballRightShape.setBounds(100, 50, 16, 16);
        
        CollisionRect brickShape = new CollisionRect();
        brickShape.setBounds(50, 50, 48, 24);
        
        CollidedObject collidedBallTop = new CollidedObject(ballTop, new Point2D.Double(70, 30), CollidedObject.SIDE_BOTTOM, ballTopShape);
        CollidedObject collidedBallBottom = new CollidedObject(ballBottom, new Point2D.Double(70, 75), CollidedObject.SIDE_TOP, ballBottomShape);
        CollidedObject collidedBallLeft = new CollidedObject(ballLeft, new Point2D.Double(30, 50), CollidedObject.SIDE_RIGHT, ballLeftShape);
        CollidedObject collidedBallRight = new CollidedObject(ballRight, new Point2D.Double(100, 50), CollidedObject.SIDE_LEFT, ballRightShape);
        
        CollidedObject collidedBrickTop = new CollidedObject(brick, new Point2D.Double(50, 50), CollidedObject.SIDE_TOP, brickShape);
        CollidedObject collidedBrickBottom = new CollidedObject(brick, new Point2D.Double(50, 50), CollidedObject.SIDE_BOTTOM, brickShape);
        CollidedObject collidedBrickLeft = new CollidedObject(brick, new Point2D.Double(50, 50), CollidedObject.SIDE_LEFT, brickShape);
        CollidedObject collidedBrickRight = new CollidedObject(brick, new Point2D.Double(50, 50), CollidedObject.SIDE_RIGHT, brickShape);
        
        ArrayList<CollidedObject> collidedToBallTop = new ArrayList<>();
        collidedToBallTop.add(collidedBrickTop);
        
        ArrayList<CollidedObject> collidedToBallBottom = new ArrayList<>();
        collidedToBallBottom.add(collidedBrickBottom);
        
        ArrayList<CollidedObject> collidedToBallLeft = new ArrayList<>();
        collidedToBallLeft.add(collidedBrickLeft);
        
        ArrayList<CollidedObject> collidedToBallRight = new ArrayList<>();
        collidedToBallRight.add(collidedBrickRight);
        
        HashMap<CollidedObject, ArrayList<CollidedObject>> collisions = new HashMap<>();
        collisions.put(collidedBallTop, collidedToBallTop);
        collisions.put(collidedBallBottom, collidedToBallBottom);
        collisions.put(collidedBallLeft, collidedToBallLeft);
        collisions.put(collidedBallRight, collidedToBallRight);
        
        model.collisionOccured(collisions);
        
        // Проверяем, что мячи отлетели в правильных направлениях.
        assertTrue(ballTop.getSpeed().x() < 0 && ballTop.getSpeed().y() < 0);
        assertTrue(ballBottom.getSpeed().x() > 0 && ballBottom.getSpeed().y() > 0);
        assertTrue(ballLeft.getSpeed().x() < 0 && ballLeft.getSpeed().y() > 0);
        assertTrue(ballRight.getSpeed().x() > 0 && ballRight.getSpeed().y() < 0);
    }

}
