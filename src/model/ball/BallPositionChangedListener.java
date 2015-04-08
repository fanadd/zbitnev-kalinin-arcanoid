package model.ball;

/**
 * Внутренний интерфейс слушателя событий об изменении позиции мяча.
 * @author Nikita Kalinin <nixorv@gmail.com>
 *
 */
public interface BallPositionChangedListener {
    
    /**
     * Изменилась позиция мяча.
     * @param ball Мяч, позиция которого изменилась.
     */
    public void ballPositionChanged(AbstractBall ball);
}
