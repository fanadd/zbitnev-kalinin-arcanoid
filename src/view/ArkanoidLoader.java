package view;

import java.awt.Dimension;
import com.golden.gamedev.GameLoader;

/**
 * Класс инициализации игры.
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public class ArkanoidLoader extends GameLoader {

    public static void main(String[] args) {
        
    	ArkanoidLoader loader = new ArkanoidLoader();
    	loader.setup(new ScreenGame(), 
    			new Dimension(800, 600), 
    			false);
    	loader.start();
    }

}
