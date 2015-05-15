package test;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import model.GameField;
import model.GameModel;
import model.ball.BasicBall;
import model.brick.BreakableBrick;
import model.collision.CollidedObject;

import org.junit.Test;

import com.golden.gamedev.object.collision.CollisionRect;

public class BehaviourDestroyTest {

    @Test
    public void testDestroy() {
        
        GameModel model = new GameModel();
        GameField field = new GameField(new Dimension(100, 100));
        model.setField(field);
        
        BreakableBrick brick = new BreakableBrick(field, new Point2D.Double(50, 50), new Dimension(48, 24));
        BasicBall ball = new BasicBall(field, new Point2D.Double(50, 50), 8);
        
        // Имитируем столкновение.
        
        CollisionRect ballShape = new CollisionRect();
        ballShape.setBounds(50, 33, 16, 16);
        
        CollisionRect brickShape = new CollisionRect();
        brickShape.setBounds(50, 50, 48, 24);
        
        CollidedObject collidedBall = new CollidedObject(ball, new Point2D.Double(50, 33), CollidedObject.SIDE_BOTTOM, ballShape);
        CollidedObject collidedBrick = new CollidedObject(brick, new Point2D.Double(50, 50), CollidedObject.SIDE_TOP, brickShape);
        
        ArrayList<CollidedObject> collidedToBall = new ArrayList<>();
        collidedToBall.add(collidedBrick);
        
        HashMap<CollidedObject, ArrayList<CollidedObject>> collisions = new HashMap<>();
        collisions.put(collidedBall, collidedToBall);
        
        model.collisionOccured(collisions);
        
        // Проверяем, что кирпич разрушен.
        assertTrue(brick.isDestroyed());
    }

}
