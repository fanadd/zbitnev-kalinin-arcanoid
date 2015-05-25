package arkanoid;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import arkanoid.entities.Entity;
import arkanoid.entities.EntityView;
import arkanoid.entities.EntityViewFactory;
import arkanoid.interaction.EntityEventListener;

import com.golden.gamedev.object.PlayField;
import com.golden.gamedev.object.SpriteGroup;
import com.golden.gamedev.object.background.ImageBackground;

/**
 * Игровое поле арканоида. Содержит все обекты игры, ответственнен за обновление, рендеринг и
 * проверку стоклновений 
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public class ArkanoidFieldView {
	
	private EntityViewFactory _viewFactory = new EntityViewFactory();
	private PlayField _gtgeField = new PlayField();
	private ArkanoidField _field = null;
	private ArrayList<EntityView> _objectViews = new ArrayList<>();
	private EntityEventHandler _entityEventHandler = new EntityEventHandler();
	
	public ArkanoidFieldView(ArkanoidField field) {
		
		if (field == null)
			throw new NullPointerException();
		
		_field = field;
		_field.addEntityEventListener(_entityEventHandler);
		
		for (Entity e : _field.getEntities()) {
			this.addObjectView(_viewFactory.instantiateEntityView(e, this));
		}
	}

	public void update(long timeElapsed) {
	    
		_gtgeField.update(timeElapsed);
	}

	/**
	 * Возвращает группу спрайтов мячей.
	 * @return Группа спрайтов.
	 */
	public SpriteGroup getBallsGroup() {
	    
	    return _gtgeField.getGroup("balls");
	}
	
	/**
	 * Возвращает группу спрайтов кирпичей.
	 * @return Группа спрайтов.
	 */
	public SpriteGroup getBricksGroup() {
	    
	    return _gtgeField.getGroup("bricks");
	}
	
	/**
	 * Возвращает группу спрайтов ракеток.
	 * @return Группа спрайтов.
	 */
	public SpriteGroup getPaddlesGroup() {
	    
	    return _gtgeField.getGroup("paddles");
	}
	
	/**
	 * Добавляет представление объекта на это поле.
	 * Этот метод добавляет объект в соответствующую группу спрайтов.
	 */
	private void addObjectView(EntityView ov) {
	    
	    _objectViews.add(ov);
	    _gtgeField.add(ov.getSprite());
	}
	
	/**
	 * Удаляет представление объекта с этого представления поля и из группы спрайтов.
	 */
	private void removeObjectView(EntityView ov) {
	    
	    _objectViews.remove(ov);
	    _gtgeField.getExtraGroup().remove(ov.getSprite());
	}
	
	/**
	 * Возвращает список представлений объектов на этом поле.
	 * @return Список.
	 */
	public ArrayList<EntityView> getObjectViews() {
	    
	    return (ArrayList<EntityView>) _objectViews.clone();
	}
    
    /**
     * Задать фоновое изображение для игрового поля.
     * @param img Фоновое изображение.
     */
    public void setBackground(BufferedImage img) {
    	_gtgeField.setBackground(new ImageBackground(img));
    }

	public void render(Graphics2D g) {
		_gtgeField.render(g);
	}
	
	private class EntityEventHandler implements EntityEventListener {
		
		@Override
		public void entityRemoved(Entity entity) {
			
			EntityView removed = null;
			for (EntityView ev : _objectViews) {
				if (ev.getIngameObject() == entity) {
					removed = ev;
				}
			}
			ArkanoidFieldView.this.removeObjectView(removed);
		}

		@Override
		public void entityAdded(Entity entity) {
			
			ArkanoidFieldView.this.addObjectView(
					_viewFactory.instantiateEntityView(entity, ArkanoidFieldView.this)
			);
		}
		
	}
}
