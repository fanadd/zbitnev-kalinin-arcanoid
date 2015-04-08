package model.paddle;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;

import model.GameField;
import model.Entity;
import model.Speed2D;
import model.ball.Ball;

/**
 * Модель абстрактной ракетки.
 * @author Nikita Kalinin <nixorv@gmail.com>
 *
 */
public abstract class Paddle extends Entity {

    protected ArrayList<Ball> _balls = new ArrayList<>();

    public Paddle(GameField field, Float pos, Dimension dim) {
        
        super(field, pos, dim);
    }

    public Paddle(GameField field) {
		
        super(field);
	}

	/**
	 * Поместить шар на ракетку.
	 * @param b Шар.
	 */
	public void addBall(Ball b) {
        
	    if (b == null) {
	        throw new NullPointerException();
	    }
	    
	    b.setSpeed(new Speed2D(0, 0));
	    _balls.add(b);
	    this.fixBallsPosition();
    }
	
	/**
	 * Корректирует координаты мячей.
	 * Они не должны висеть над ракеткой.
	 * Они не должны по горизонтали вылазить за ракетку.
	 */
	protected void fixBallsPosition() {
	    
	    for (Ball b : _balls) {
            b.setPosition(new Point2D.Float(b.getPosition().x, this._position.y - b.getSize().height));
            
            if (b.getPosition().x < this._position.x) {
                b.setPosition(new Point2D.Float(this._position.x, b.getPosition().y));
            }
            if (b.getPosition().x > this._position.x + this._size.width) {
                b.setPosition(new Point2D.Float(this._position.x + this._size.width - b.getSize().width, b.getPosition().y));
            }
	    }
	}
    
	/**
	 * Убрать шар с ракетки.
	 * @param b Шар.
	 */
    public void removeBall(Ball b) {
        _balls.remove(b);
    }
    
    /**
     * Возвращает список шаров на ракетке.
     * @return Список шаров на ракетке.
     */
    public ArrayList<Ball> getBalls() {
        
        return (ArrayList<Ball>) _balls.clone();
    }
    
    /**
     * Возвращает скорость мяча при запуске его с ракетки или отскока от ракетки.
     * @param b Мяч.
     * @return Вектор скорости.
     */
    public Speed2D getFireSpeed(Ball ball) {
        
        // Найти два центра расчета вектора.
        Point2D.Float paddleLeftCenter = new Point2D.Float(this._position.x + (this._size.width / 5) * 2, this._position.y);
        Point2D.Float paddleRightCenter = new Point2D.Float(this._position.x + (this._size.width / 5) * 3, this._position.y);
        
        // Центр ракетки
        Point2D.Float paddleCenter = new Point2D.Float(this._position.x + this._size.width / 2, this._position.y);
        
        // Относительные координаты центра мяча в декартовой системе координат (точка B).
        // Считаем, что paddleCenter - это точка A(0, 0).
        Point2D.Float relBallCenter = new Point2D.Float(ball.getPosition().x + ball.getSize().width / 2 - paddleCenter.x,
                paddleCenter.y - ball.getPosition().y - ball.getSize().height / 2);
        
        // Если мяч между двумя центрами, направляем вектор вверх.
        if (relBallCenter.x <= this._size.width / 10 && relBallCenter.x >= -this._size.width / 10) {
            return new Speed2D(0, -ball.getDefaultSpeedScalar());
        }
        
        // В зависимости от трети ракетки, в которой располагается мяч, выбираем центр расчета вектора скорости.
        Point2D.Float paddleNewCenter = relBallCenter.x > this._size.width / 10 ? paddleRightCenter : paddleLeftCenter;
        
        // Рассчитываем относительное положение мяча от выбранного центра.
        relBallCenter = new Point2D.Float(relBallCenter.x + paddleCenter.x - paddleNewCenter.x, relBallCenter.y);
        
        // Коэффициенты уравнения точки пересечения прямой и окружности.
        double a = (Math.pow(relBallCenter.x, 2) + Math.pow(relBallCenter.y, 2)) / Math.pow(relBallCenter.x, 2);
        double b = 0;
        double c = -Math.pow(ball.getDefaultSpeedScalar(), 2);
        
        // Дискриминант.
        double D = Math.pow(b, 2) - 4*a*c;
        
        // Точки пересечения.
        Point2D.Float p1 = new Point2D.Float((float) ((-b + Math.sqrt(D)) / (2*a)), 0);
        Point2D.Float p2 = new Point2D.Float((float) ((-b - Math.sqrt(D)) / (2*a)), 0);
        
        // Находим y{1,2} у точек.
        p1.y = p1.x * relBallCenter.y / relBallCenter.x;
        p2.y = p2.x * relBallCenter.y / relBallCenter.x;
        
        // Нужная точка пересечения имеет положительную y-координату.
        Point2D.Float p = p1.y > 0 ? p1 : p2;
        
        // Находим горизонтальную и вертикальную сооставляющие вектора скорости.
        // y отрицательный, чтобы перейти в экранную систему координат.
        return new Speed2D(p.x, -Math.abs(p.y));
    }
    
    /**
     * Запускает шары с ракетки.
     */
    public void fireBalls() {
        
        while (!_balls.isEmpty()) {
            Ball b = _balls.get(0);
            b.setSpeed(getFireSpeed(b));
            _balls.remove(b);
        }
    }
    
    @Override
    public void setPosition(Point2D.Float pos) {
        
        if (this._position == null) {
            super.setPosition(pos);
            
        } else {
            float dx = pos.x - this._position.x;
            float dy = pos.y - this._position.y;
            
            super.setPosition(pos);
            
            for (Ball b : _balls) {
                b.setPosition(new Point2D.Float(b.getPosition().x + dx, b.getPosition().y + dy));
            }
        }
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
    	
    	Paddle clone = (Paddle) super.clone();
    	return clone;
    }
}
