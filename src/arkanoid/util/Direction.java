package arkanoid.util;

/**
 * Предоставляет направление движения по игровому полю
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 * 
 */
public class Direction {
    
    // ------------------------------------------------------------------------
    private final int angle;  // угол поворота в декартовой системе координат
                              // соответствующий текущему направлению
    
    // ------------------------------------------------------------------------
    private Direction(int angle) {
        
        while (angle < 0) {
            angle += 360;
        }
        
        while (angle >= 360) {
            angle -= 360;
        }
        
        this.angle = angle;
    }
    
    /**
     * Получить северное направление (вверх)
     * @return Северное направление
     */
    public static Direction north() {
        return new Direction(90);
    }
    
    /**
     * Получить южное направление (вниз)
     * @return Южное направление
     */
    public static Direction south() {
        return new Direction(270);
    }
    
    /**
     * Получить восточное направление (направо)
     * @return Восточное направление
     */
    public static Direction east() {
        return new Direction(0);
    }
    
    /**
     * Получить западное направление (налево)
     * @return Западное направление
     */
    public static Direction west() {
        return new Direction(180);
    }
    
    // ------------------------------------------------------------------------
    /**
     * Получить направление, противоположное данному
     * @return Противоположное направление
     */
    public Direction opposite() {
        return new Direction(this.angle - 180);
    }
    
    /**
     * Получить направление, полученное поворотом данного на 90 градусов 
     * по часовой стрелке. Например, СЕВЕР -> ВОСТОК, ЮГ -> ЗАПАД.
     * @return Направление, развёрнутое на 90 градусов по часовой
     */
    public Direction turnClockwise() {
        return new Direction(this.angle - 90);
    }
    
    /**
     * Получить направление, полученное поворотом данного на 90 градусов 
     * против часовой стрелки. Например, СЕВЕР -> ЗАПАД, ЮГ -> ВОСТОК.
     * @return Направление, развёрнутое на 90 градусов против часовой
     */
    public Direction turnAnticlockwise() {
        return new Direction(this.angle + 90);
    }
    
    // ------------------------------------------------------------------------
    @Override
    public int hashCode() {
        return angle;
    }
    
    @Override
    public boolean equals(Object obj) {
        int otherangle = ((Direction)obj).angle;
        return this.angle == otherangle;
    }
    
    @Override
    public Direction clone() {
        return new Direction(this.angle);
    }
}