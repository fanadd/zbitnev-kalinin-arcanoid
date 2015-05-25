package arkanoid;

import java.awt.Graphics2D;

import arkanoid.controller.GameController;
import arkanoid.entities.Entity;
import arkanoid.entities.paddle.BasicPaddle;

import com.golden.gamedev.Game;

/**
 * Режим игры
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public class Arkanoid extends Game {
    
	ArkanoidFieldView _fieldView;
	ArkanoidField _field;
	GameController _controller;

	@Override
	public void initResources() {

		// Инициализация уровня
		FieldFactory fieldFactory = new FieldFactory();
		_field = fieldFactory.createTestField(this.bsGraphics.getSize());
		
        // Инициализация представления уровня
	    FieldViewFactory fvFactory = new FieldViewFactory();
		_fieldView = fvFactory.instantiateFieldView(_field, bsLoader);
        
        // Контроллер и игрок.
		BasicPaddle paddle = null;
		for (Entity e : _field.getEntities()) {
			if (e instanceof BasicPaddle)
				paddle = (BasicPaddle)e;
		}
        Player player = new Player(paddle);
        _controller = new GameController(player, bsInput);
        
        // Инициализация закончена. Спрятать курсор мыши перед началом игры.
        this.hideCursor();
	}

	@Override
	public void render(Graphics2D arg0) {

		_fieldView.render(arg0);
		
		// TODO: Рендер кол-ва очков, другой инофрмации (сейчас игра на весь экран)
	}

	@Override
	public void update(long arg0) {
		
		// Апдейтим всё
		_field.update(arg0);
		_fieldView.update(arg0);
		_controller.update();
	}

}
