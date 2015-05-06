package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import view.collision.PublishingCollisionManager;
import arkanoid.Entity;
import arkanoid.ball.AbstractBall;
import arkanoid.brick.AbstractBrick;
import arkanoid.collision.CollidedObject;
import arkanoid.interaction.CollisionListener;
import arkanoid.paddle.AbstractPaddle;

import com.golden.gamedev.object.CollisionManager;
import com.golden.gamedev.object.PlayField;
import com.golden.gamedev.object.Sprite;
import com.golden.gamedev.object.SpriteGroup;

/**
 * Игровое поле арканоида. Содержит все обекты игры, ответственнен за обновление, рендеринг и
 * проверку стоклновений 
 * @author Gregory Zbitnev <zbitnev@hotmail.com>
 *
 */
public class GameFieldView extends PlayField {
	
	private ArrayList<IngameObjectView> _objectViews = new ArrayList<>();
	private ArrayList<CollisionListener> _collisionListners;
	
	public GameFieldView() {
		
    	_collisionListners = new ArrayList<>();
		SpriteGroup balls = new SpriteGroup("balls");
		SpriteGroup bricks = new SpriteGroup("bricks");
		SpriteGroup paddles = new SpriteGroup("paddles");
		this.addGroup(balls);
		this.addGroup(bricks);
		this.addGroup(paddles);
		
		// Добавить на поле менеджеры коллизий для обработки столкновений
		this.addCollisionGroup(balls, paddles, new PublishingCollisionManager());
		this.addCollisionGroup(balls, bricks, new PublishingCollisionManager());
		this.addCollisionGroup(balls, balls, new PublishingCollisionManager());
	}

	@Override
	public void update(long timeElapsed) {
	    
	    super.update(timeElapsed);
	    for (IngameObjectView ov : _objectViews) {
	        ov.update(timeElapsed);
	    }
	    
	    // Формируем словарь столкновений
	    CollisionManager[] mgrs = this.getCollisionGroups();
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
	    
	    return this.getGroup("balls");
	}
	
	/**
	 * Возвращает группу спрайтов кирпичей.
	 * @return Группа спрайтов.
	 */
	public SpriteGroup getBricksGroup() {
	    
	    return this.getGroup("bricks");
	}
	
	/**
	 * Возвращает группу спрайтов ракеток.
	 * @return Группа спрайтов.
	 */
	public SpriteGroup getPaddlesGroup() {
	    
	    return this.getGroup("paddles");
	}
	
	/**
	 * Добавляет представление объекта на это поле. Этот метод добавляет объект в соответствующую группу спрайтов.
	 * @param ov Представление.
	 */
	public void addObjectView(IngameObjectView ov) {
	    
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
	 * @param ov Представление.
	 */
	public void removeObjectView(IngameObjectView ov) {
	    
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
	public ArrayList<IngameObjectView> getObjectViews() {
	    
	    return (ArrayList<IngameObjectView>) _objectViews.clone();
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
    				newst.get(key).add(val);
    			}
    		}
    	}
    	
    	return newst;
    }
}
