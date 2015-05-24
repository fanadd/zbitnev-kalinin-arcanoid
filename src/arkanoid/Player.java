package arkanoid;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import arkanoid.entities.paddle.AbstractPaddle;
import arkanoid.util.Direction;

/**
 * Модель игрока, управляющего ракеткой.
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public class Player {

    protected ArrayList<AbstractPaddle> _paddles = new ArrayList<>();
    
	/**
	 * Инициализировать игрока
	 * @param paddle Подконтрольная игроку ракетка
	 */
	public Player(AbstractPaddle paddle) {
		_paddles.add(paddle);
	}
	
	public Player() {
		
	}

	/**
	 * Получить контролируемые ракетки
	 * @return Список контролируемых ракеток
	 */
	public ArrayList<AbstractPaddle> getPaddles() {
		
		return (ArrayList<AbstractPaddle>) _paddles.clone();
	}
	
	/**
	 * Добавить ракетку под контроль игрока
	 * @param paddle Ракетка для добавления
	 */
	public void addPaddle(AbstractPaddle paddle) {
		
	    if (paddle == null) {
	        throw new NullPointerException();
	    }
	    
	    _paddles.add(paddle);
	}
	
	/**
	 * Убрать ракетку из-под контроля игрока
	 * @param paddle Ракетка
	 */
	public void removePaddle(AbstractPaddle paddle) {
		
	    _paddles.remove(paddle);
	}
	
	/**
	 * Переместить все свои ракетки в указанную позицию по горизонтали
	 * @param pos Позиция
	 */
	public void setPaddlesPositionX(int x) {
		
	    for (AbstractPaddle p : _paddles) {
	        int actualx;
	        if (x > p.getField().getSize().width - p.getSize().width) {
	            actualx = p.getField().getSize().width - p.getSize().width;
	        } else if (x < 0) {
	            actualx = 0;
	        } else {
	            actualx = x;
	        }
	        p.setPosition(new Point2D.Double(actualx, p.getPosition().y));
	    }
	}
	
	/**
	 * Переместить все свои ракетки в указанном направлении.
	 * Величину сдвига жёстко задана внутри класса.
	 * @param dir Направление перемещения
	 */
	public void movePaddles(Direction dir) {
		
	    for (AbstractPaddle p : _paddles) {
	        long delta = Math.round(p.getSize().width / 3.0 * 2.0);
	        delta = dir.equals(Direction.west()) ? -delta : delta;
            if (p.getPosition().x + p.getSize().width + delta > p.getField().getSize().width) {
                p.setPosition(new Point2D.Double(p.getField().getSize().width - p.getSize().width, p.getPosition().y));
            } else if (p.getPosition().x + delta < 0) {
                p.setPosition(new Point2D.Double(0, p.getPosition().y));
            } else {
                p.move(new Point2D.Double(delta, 0));
            }
        }
	}
	
	/**
	 * Заставляет подконтрольные ракетки задать скорость мячам.
	 */
	public void firePaddles() {
	    
	    for (AbstractPaddle p : _paddles) {
	        p.fireBalls();
	    }
	}
}
