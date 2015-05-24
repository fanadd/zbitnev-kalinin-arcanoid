package arkanoid;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import arkanoid.collision.CollidedObject;
import arkanoid.entities.EntityView;
import arkanoid.entities.EntityViewFactory;
import arkanoid.entities.PublishingCollisionManager;
import arkanoid.entities.ball.AbstractBall;
import arkanoid.entities.brick.AbstractBrick;
import arkanoid.entities.paddle.AbstractPaddle;
import arkanoid.interaction.CollisionListener;
import arkanoid.interaction.EntityEventListener;

import com.golden.gamedev.object.CollisionManager;
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
	private ArrayList<CollisionListener> _collisionListners;
	private EntityEventHandler _entityEventHandler = new EntityEventHandler();
	
	public ArkanoidFieldView(ArkanoidField field) {
		
		if (field == null)
			throw new NullPointerException();
		
		_field = field;
		_field.addEntityEventListener(_entityEventHandler);
		
    	_collisionListners = new ArrayList<>();
		SpriteGroup balls = new SpriteGroup("balls");
		SpriteGroup bricks = new SpriteGroup("bricks");
		SpriteGroup paddles = new SpriteGroup("paddles");
		_gtgeField.addGroup(balls);
		_gtgeField.addGroup(bricks);
		_gtgeField.addGroup(paddles);
		
		// Добавить на поле менеджеры коллизий для обработки столкновений
		_gtgeField.addCollisionGroup(balls, paddles, new PublishingCollisionManager(_field));
		_gtgeField.addCollisionGroup(balls, bricks, new PublishingCollisionManager(_field));
		_gtgeField.addCollisionGroup(balls, balls, new PublishingCollisionManager(_field));
		
		for (Entity e : _field.getEntities()) {
			this.addObjectView(_viewFactory.instantiateEntityView(e, this));
		}
	}

	public void update(long timeElapsed) {
	    
		_gtgeField.update(timeElapsed);
	    
	    // Формируем словарь столкновений
	    CollisionManager[] mgrs = _gtgeField.getCollisionGroups();
	    HashMap<CollidedObject, ArrayList<CollidedObject>> collisions = new HashMap<>();
	    for (int i = 0; i < mgrs.length; i++) {
	    	
	    	HashMap<CollidedObject, ArrayList<CollidedObject>> map = 
	    			((PublishingCollisionManager)mgrs[i]).getCollidedStorage();
	    	
	    	// Если словарь столкновений не пуст, формируем один большой словарь столкновений
	    	if (!map.isEmpty()) {
	    		attachStorage(collisions, map);
	    		((PublishingCollisionManager)mgrs[i]).clearCollidedStorage();
	    	}
	    }
	    
	    // Если столкновения произошли -- посылаем сигнал модели
	    if (!collisions.isEmpty()) {
	    	
	    	collisions = removeCouplingFromStorage(collisions);
	    	for (CollisionListener l : _collisionListners) {
	    		l.collisionOccured(collisions);
	    	}
	    }
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
	    if (ov.getIngameObject() instanceof AbstractBall) {
	        getBallsGroup().add(ov.getSprite());
	    } else if (ov.getIngameObject() instanceof AbstractBrick) {
	        getBricksGroup().add(ov.getSprite());
	    } else if (ov.getIngameObject() instanceof AbstractPaddle) {
	        getPaddlesGroup().add(ov.getSprite());
	    }
	}
	
	/**
	 * Удаляет представление объекта с этого представления поля и из группы спрайтов.
	 */
	private void removeObjectView(EntityView ov) {
	    
	    _objectViews.remove(ov);
	    if (ov.getIngameObject() instanceof AbstractBall) {
            getBallsGroup().remove(ov.getSprite());
        } else if (ov.getIngameObject() instanceof AbstractBrick) {
            getBricksGroup().remove(ov.getSprite());
        } else if (ov.getIngameObject() instanceof AbstractPaddle) {
            getPaddlesGroup().remove(ov.getSprite());
        }
	}
	
	/**
	 * Возвращает список представлений объектов на этом поле.
	 * @return Список.
	 */
	public ArrayList<EntityView> getObjectViews() {
	    
	    return (ArrayList<EntityView>) _objectViews.clone();
	}
	
    /**
     * Добавить слушателя событий о произошедших на поле столкновениях
     * @param l Добавляемый слушатель
     */
    public void addCollisionListener(CollisionListener l) {
    	_collisionListners.add(l);
    }
    
    /**
     * Удалить слушателя событий о произошедших на поле столкновениях
     * @param l Удаляемый слушатель
     */
    public void removeCollisionListener(CollisionListener l) {
    	_collisionListners.remove(l);
    }
    
    /**
     * Копирует сообщения о столкновениях из одного словаря в другой
     * @param to Словарь, который будет дополнен новыми сообщениями
     * @param from Словарь, из которого будут скопированы сообщения
     */
    private void attachStorage(HashMap<CollidedObject, ArrayList<CollidedObject>> to,
    		HashMap<CollidedObject, ArrayList<CollidedObject>> from) {
    	
    	for (CollidedObject obj : from.keySet()) {
    		
    		// Если такого ключа не содержится -- просто добавляем новую запись в словарь
        	// Если такой ключ есть -- копируем значения из списка
    		if (!to.containsKey(obj)) {
    			to.put(obj, from.get(obj));
    		}
    		else {
    			
    			for (CollidedObject listobj : from.get(obj)) {
    				
    				if (!to.get(obj).contains(listobj)) {
    					to.get(obj).add(listobj);
    				}
    			}
    		}
    	}
    }
    
    /**
     * Просеять словарь столкновений и удалить дублирующиеся ассоциации
     * @param st Словарь столкновений
     */
    private HashMap<CollidedObject, ArrayList<CollidedObject>> 
    	removeCouplingFromStorage(HashMap<CollidedObject, ArrayList<CollidedObject>> st) {
    	
    	HashMap<CollidedObject, ArrayList<CollidedObject>> newst = new HashMap<>();
    	
    	for (CollidedObject key : st.keySet()) {	
    		for (CollidedObject val : st.get(key)) {
    			
    			// Если в словарь уже не добавлена "обратная" ассоциация
    			if (!newst.containsKey(val) || !newst.get(val).contains(key)) {
    				
    				if (!newst.containsKey(key)) {
    					newst.put(key, new ArrayList<CollidedObject>());
    				}
    				if (!newst.get(key).contains(val)) {
    				    newst.get(key).add(val);
    				}
    			}
    		}
    	}
    	
    	return newst;
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
