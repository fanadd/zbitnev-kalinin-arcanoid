package arkanoid.controller;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import arkanoid.Player;
import arkanoid.util.Direction;

import com.golden.gamedev.engine.BaseInput;

/**
 * Слушает ввод и управляет игрой.
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public class GameController {

	private Player _player;
	private BaseInput _input;
	
	/**
	 * Создаёт контроллер ввода
	 * @param player Модель игрока, которой будет управлять контроллер.
	 * @param input Менеджер ввода.
	 */
	public GameController(Player player, BaseInput input) {
		
		if (player == null || input == null) {
		    throw new NullPointerException();
		}
		
		_player = player;
		_input = input;
	}
	
	/**
	 * Проверяет состояние ввода и управляет моделью игрока.
	 */
	public void update() {
		
	    // Управление мышью.
	    if (_input.getMouseDX() != 0) {
	        _player.setPaddlesPositionX(_input.getMouseX());
	    }
	    
	    if (_input.isMousePressed(MouseEvent.BUTTON1)) {
	        _player.firePaddles();
	    }

	    // Управление с клавиатуры.
	    if (_input.isKeyPressed(KeyEvent.VK_LEFT)) {
	        _player.movePaddles(Direction.west());
	    }
	    
	    if (_input.isKeyPressed(KeyEvent.VK_RIGHT)) {
	        _player.movePaddles(Direction.east());
	    }
	    
	    if (_input.isKeyPressed(KeyEvent.VK_SPACE)) {
	        _player.firePaddles();
	    }
	}
}
