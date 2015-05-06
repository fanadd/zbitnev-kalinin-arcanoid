package arkanoid;

import java.awt.image.BufferedImage;

import arkanoid.ball.BasicBall;
import arkanoid.brick.BreakableBrick;
import arkanoid.brick.UnbreakableBrick;
import arkanoid.entities.EntityView;
import arkanoid.paddle.BasicPaddle;

/**
 * Фабрика для создания представлений стандартных игровых объектов:
 * - простой мяч (basic ball)
 * - неразрушаемый кирпич (unbreakable brick)
 * - разрушаемый кирпич (breakable brick)
 * - простая ракетка (basic paddle)
 * 
 * @author Nikita Kalinin <nixorv@gmail.com>
 *
 */
public class DefaultObjectViewFactory {
    
    protected BufferedImage _basicBallImage = null;
    protected BufferedImage _breakableBrickImage = null;
    protected BufferedImage _unbreakableBrickImage = null;
    protected BufferedImage _basicPaddleImage = null;
    protected GameFieldView _view = null;
    
    public DefaultObjectViewFactory() {
        
    }
    
    /**
     * Создает фабрику.
     * @param basicBallImage
     * @param breakableBrickImage
     * @param unbreakableBrickImage
     * @param basicPaddleImage
     */
    public DefaultObjectViewFactory(BufferedImage basicBallImage, BufferedImage breakableBrickImage,
                                    BufferedImage unbreakableBrickImage, BufferedImage basicPaddleImage,
                                    GameFieldView view) {
        
    	_view = view;
        _basicBallImage = basicBallImage;
        _breakableBrickImage = breakableBrickImage;
        _unbreakableBrickImage = unbreakableBrickImage;
        _basicPaddleImage = basicPaddleImage;
    }
    
    /**
     * Возвращает true, если фабрика настроена и может порождать объекты.
     * @return Валидность фабрики.
     */
    public boolean is_valid() {
        
        boolean valid = true;
        
        valid &= _basicBallImage != null;
        valid &= _breakableBrickImage != null;
        valid &= _unbreakableBrickImage != null;
        valid &= _basicPaddleImage != null;
        
        return valid;
    }
    
    /**
     * Проверяет, валидна ли фабрика и выбрасывает исключение, если нет.
     */
    public void ensure_valid() {
        
        if (!is_valid()) {
            throw new NullPointerException("Fabric was initialized with null images.");
        }
    }
    
    /**
     * Задает изображение простого мяча.
     * @param i Изображение.
     */
    public void setBasicBallImage(BufferedImage i) {
        
        if (i == null) {
            throw new NullPointerException();
        }
        _basicBallImage = i;
    }
    
    /**
     * Задает изображение разрушаемого кирпича.
     * @param i Изображение.
     */
    public void setBreakableBrickImage(BufferedImage i) {
        
        if (i == null) {
            throw new NullPointerException();
        }
        _breakableBrickImage = i;
        ensure_valid();
    }
    
    
    /**
     * Задает изображение неразрушаемого кирпича.
     * @param i Изображение.
     */
    public void setUnbreakableBrickImage(BufferedImage i) {
        
        if (i == null) {
            throw new NullPointerException();
        }
        _unbreakableBrickImage = i;
        ensure_valid();
    }

    /**
     * Задает изображение простой ракетки.
     * @param i Изображение.
     */
    public void setBasicPaddleImage(BufferedImage i) {
        
        if (i == null) {
            throw new NullPointerException();
        }
        _basicPaddleImage = i;
    }
    
    /**
     * Возвращает изображение простого мяча.
     * @return Изображение.
     */
    public BufferedImage getBasicBallImage() {
        
        return _basicBallImage;
    }
    
    /**
     * Возвращает изображение разрушаемого кирпича.
     * @return Изображение.
     */
    public BufferedImage getBreakableBrickImage() {
        
        return _breakableBrickImage;
    }

    /**
     * Возвращает изображение неразрушаемого кирпича.
     * @return Изображение.
     */
    public BufferedImage getUnbreakableBrickImage() {
        
        return _unbreakableBrickImage;
    }
    
    /**
     * Возвращает изображение простой ракетки.
     * @return Изображение.
     */
    public BufferedImage getBasicPaddleImage() {
        
        return _basicPaddleImage;
    }
    
    /**
     * Создает представление для простого мяча.
     * @param ball Модель мяча.
     * @return Представление мяча.
     */
    public EntityView newBasicBallView(BasicBall ball) {
        
        ensure_valid();
        
        PublishingSprite ballSprite = new PublishingSprite();
        ballSprite.setImage(_basicBallImage);
        
        // Напоминание: этот конструктор сам установит объекты слушателями друг друга.
        EntityView ballView = new EntityView(ball, ballSprite, _view);
        
        return ballView;
    }
    
    /**
     * Создает представление разрушаемого кирпича.
     * @param brick Разрушаемый кирпич.
     * @return Представление разрушаемого кирпича.
     */
    public EntityView newBreakableBrickView(BreakableBrick brick) {
        
        ensure_valid();
        
        PublishingSprite brickSprite = new PublishingSprite();
        brickSprite.setImage(_breakableBrickImage);
        
        // Напоминание: этот конструктор сам установит объекты слушателями друг друга.
        EntityView brickView = new EntityView(brick, brickSprite, _view);
        
        return brickView;
    }
    
    /**
     * Создает представление неразрушаемого кирпича.
     * @param brick Неразрушаемый кирпич.
     * @return Представление неразрушаемого кирпича.
     */
    public EntityView newUnbreakableBrickView(UnbreakableBrick brick) {
        
        ensure_valid();
        
        PublishingSprite brickSprite = new PublishingSprite();
        brickSprite.setImage(_unbreakableBrickImage);
        
        // Напоминание: этот конструктор сам установит объекты слушателями друг друга.
        EntityView brickView = new EntityView(brick, brickSprite, _view);
        
        return brickView;
    }
    
    /**
     * Создает представление простой ракетки.
     * @param brick Ракетка.
     * @return Представление простой ракетки.
     */
    public EntityView newBasicPaddleView(BasicPaddle paddle) {
        
        ensure_valid();
        
        PublishingSprite paddleSprite = new PublishingSprite();
        paddleSprite.setImage(_basicPaddleImage);
        
        // Напоминание: этот конструктор сам установит объекты слушателями друг друга.
        EntityView paddleView = new EntityView(paddle, paddleSprite, _view);
        
        return paddleView;
    }
}
