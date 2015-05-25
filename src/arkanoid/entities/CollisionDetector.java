package arkanoid.entities;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import arkanoid.ArkanoidField;
import arkanoid.collision.CollidedObject;
import arkanoid.interaction.EntityEventListener;

import com.golden.gamedev.object.Sprite;
import com.golden.gamedev.object.SpriteGroup;
import com.golden.gamedev.object.collision.AdvanceCollisionGroup;
import com.golden.gamedev.object.collision.CollisionBounds;
import com.golden.gamedev.object.collision.CollisionGroup;
import com.golden.gamedev.object.collision.CollisionShape;

/**
 * Класс занимается отслеживанием столкновений сущностей на поле и рассылкой им сообщений.
 * @author Nikita Kalinin <nixorv@gmail.com>
 *
 */
public class CollisionDetector {
    
    private ArkanoidField _field = null;
    private HashMap<Class<?>, SpriteGroup> _spriteGroups = new HashMap<>();
    private ArrayList<CollisionManager> _managers = new ArrayList<>();
    private Class<?> _boundaryCollidableClass = null;
    private SpriteGroup _boundaryCollidableGroup = null;
    private CollisionBoundsManager _fieldBoundsManager = null; // Без _field нельзя создавать.
    
    public CollisionDetector(ArkanoidField field) {
        
        if (field == null) {
            throw new NullPointerException();
        }
        _field = field;
        _fieldBoundsManager = new CollisionBoundsManager();
        _field.addEntityEventListener(new AddRemoveListener());
    }
    
    /**
     * Задает классы, для экземпляры предков которых будут отслеживаться столкновения.
     * @param c1
     * @param c2
     */
    public void addCollidableSuperclasses(Class<?> c1, Class<?> c2) {
        
        if (c1 == null || c2 == null) {
            throw new NullPointerException();
        }
        
        for (AdvanceCollisionGroup mgr : _managers) {
            if (mgr.getGroup1().getName().equals(c1.getName()) && mgr.getGroup2().getName().equals(c2.getName())) {
                return;
            }
        }
        
        SpriteGroup g1 = getOrCreateSpriteGroup(c1);
        SpriteGroup g2 = getOrCreateSpriteGroup(c2);
        CollisionManager mgr = new CollisionManager();
        mgr.setCollisionGroup(g1, g2);
        _managers.add(mgr);
    }
    
    public void setBoundaryCollidableSuperclass(Class<?> c) {
        
        if (c == null) {
            throw new NullPointerException();
        }
        _boundaryCollidableClass = c;
        _boundaryCollidableGroup = new SpriteGroup(c.getName());
        _fieldBoundsManager.setCollisionGroup(_boundaryCollidableGroup, null);
    }
    
    private SpriteGroup getOrCreateSpriteGroup(Class<?> c) {
        
        if (_spriteGroups.containsKey(c)) {
            return _spriteGroups.get(c);
        }
        SpriteGroup grp = new SpriteGroup(c.getName());
        _spriteGroups.put(c, grp);
        return grp;
    }
    
    public void update(long timeElapsed) {
        
        // Проверить коллизии.
        HashMap<CollidedObject, ArrayList<CollidedObject>> collisions = new HashMap<>();
        for (CollisionManager mgr : _managers) {
            mgr.checkCollision();
            
            HashMap<CollidedObject, ArrayList<CollidedObject>> map = mgr.getCollidedStorage();
            // Если словарь столкновений не пуст, формируем один большой словарь столкновений
            if (!map.isEmpty()) {
                attachStorage(collisions, map);
                mgr.clearCollidedStorage();
            }
        }
        _fieldBoundsManager.checkCollision();
        
        // Если столкновения произошли -- сообщаем объектам об этом.
        if (!collisions.isEmpty()) {
            
            collisions = removeCouplingFromStorage(collisions);
            // Вместо объектов, от которых принимается эффект (активные)
            // передаётся их копия до начала обработки вообще всех столкновений
            HashMap<CollidedObject, ArrayList<CollidedObject>> storage_copy = deepCopyStorage(collisions);
            
            Iterator<CollidedObject> i, copyi, j, copyj;
            i = collisions.keySet().iterator();
            copyi = storage_copy.keySet().iterator();
            
            while (i.hasNext() && copyi.hasNext()) {
                
                CollidedObject obj1 = i.next();
                CollidedObject obj1copy = copyi.next();
                j = collisions.get(obj1).iterator();
                copyj = storage_copy.get(obj1copy).iterator();
                
                while (j.hasNext() && copyj.hasNext()) {
                    
                    CollidedObject obj2 = j.next();
                    CollidedObject obj2copy = copyj.next();
                    obj1.getObject().processCollision(obj1, obj2copy);
                    obj2.getObject().processCollision(obj2, obj1copy);
                }
            }
        }
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
     * Порождает копию словаря коллизии вместе со всеми хранимыми объектами
     * @param storage Словарь коллизии
     * @return Копия словаря коллизии
     */
    private HashMap<CollidedObject, ArrayList<CollidedObject>> deepCopyStorage(
            HashMap<CollidedObject, ArrayList<CollidedObject>> storage) {
        
        HashMap<CollidedObject, ArrayList<CollidedObject>> deepcopy = new HashMap<>();
        
        try {
            
            for (CollidedObject key : storage.keySet()) {
                
                CollidedObject key_copy = (CollidedObject) key.clone();
                ArrayList<CollidedObject> values_copy = new ArrayList<>();
                for (CollidedObject obj : storage.get(key)) {
                    values_copy.add((CollidedObject)obj.clone());
                }
                
                deepcopy.put(key_copy, values_copy);
            }
        }
        catch (CloneNotSupportedException exc) {
            exc.printStackTrace();
        }
        
        return deepcopy;
    }
    
    private Entity entityFor(Sprite s) {
        
        for (Entity e : _field.getEntities()) {
            if (e.getSprite()._sprite == s) {
                return e;
            }
        }
        return null;
    }
    
    /**
     * Обработчик столкновений GTGE.
     * @author Nikita Kalinin <nixorv@gmail.com>
     *
     */
    private class CollisionManager extends AdvanceCollisionGroup {

        private HashMap<CollidedObject, ArrayList<CollidedObject>> _storage = new HashMap<>();
        
        @Override
        public boolean isCollide(Sprite s1, Sprite s2, CollisionShape shape1, CollisionShape shape2) {
            
            boolean retval = super.isCollide(s1, s2, shape1, shape2);
            
            // Словарь столкновений будет формироваться в процессе детекции коллизий
            if (retval) {
            
                int obj1colside = -1, obj2colside = -1;
                switch (this.collisionSide) {
                case CollisionGroup.BOTTOM_TOP_COLLISION:
                    obj1colside = CollidedObject.SIDE_TOP;
                    obj2colside = CollidedObject.SIDE_BOTTOM;
                    break;
                case CollisionGroup.TOP_BOTTOM_COLLISION:
                    obj1colside = CollidedObject.SIDE_BOTTOM;
                    obj2colside = CollidedObject.SIDE_TOP;
                    break;
                case CollisionGroup.RIGHT_LEFT_COLLISION:
                    obj1colside = CollidedObject.SIDE_LEFT;
                    obj2colside = CollidedObject.SIDE_RIGHT;
                    break;
                case CollisionGroup.LEFT_RIGHT_COLLISION:
                    obj1colside = CollidedObject.SIDE_RIGHT;
                    obj2colside = CollidedObject.SIDE_LEFT;
                    break;
                default:
                    break;
                }
                
                Entity ent1 = entityFor(s1);
                Entity ent2 = entityFor(s2);
                
                CollidedObject obj1 = new CollidedObject(
                        ent1, 
                        new Point2D.Double((float)s1.getOldX(), (float)s1.getOldY()),
                        obj1colside, new Rectangle2D.Double(shape1.getX(), 
                                                            shape1.getY(), 
                                                            shape1.getWidth(), 
                                                            shape1.getHeight()));
                
                CollidedObject obj2 = new CollidedObject(
                        ent2, 
                        new Point2D.Double((float)s2.getOldX(), (float)s2.getOldY()),
                        obj2colside, new Rectangle2D.Double(shape2.getX(), 
                                                            shape2.getY(), 
                                                            shape2.getWidth(), 
                                                            shape2.getHeight()));
                
                if (!_storage.keySet().contains(obj1)) {
                    _storage.put(obj1, new ArrayList<CollidedObject>());
                }
                _storage.get(obj1).add(obj2);
            }
            
            return retval;
        }
        
        /**
         * Получить словарь столкновений объектов в текущем кадре. Объекты представлены в виде 
         * CollidedObject, содержащих дополнительную информацию о коллизиях
         * @return Словарь столкновений.
         */
        public HashMap<CollidedObject, ArrayList<CollidedObject>> getCollidedStorage() {
            return _storage;
        }
        
        /**
         * Очистить словарь столкновений объектов в текущем кадре.
         */
        public void clearCollidedStorage() {
            _storage.clear();
        }
        
        @Override
        public void collided(Sprite s1, Sprite s2) {
        }
    }
    
    private class CollisionBoundsManager extends CollisionBounds {

        public CollisionBoundsManager() {
            super(0, 0, _field.getSize().width, _field.getSize().height);
        }
        
        @Override
        public void collided(Sprite s) {
            
            Entity collidedEntity = entityFor(s);
            if (isCollisionSide(LEFT_COLLISION) || isCollisionSide(RIGHT_COLLISION)) {
                collidedEntity.setSpeed(collidedEntity.getSpeed().flipHorizontal());
            } else {
                collidedEntity.setSpeed(collidedEntity.getSpeed().flipVertical());
            }
        }
        
    }
    
    /**
     * Слушатель для синхронизации сталкиваемых объектов и их спрайтов в группах GTGE.
     * @author Nikita Kalinin <nixorv@gmail.com>
     *
     */
    private class AddRemoveListener implements EntityEventListener {

        @Override
        public void entityRemoved(Entity entity) {
            
            for (Class<?> c : _spriteGroups.keySet()) {
                if (c.isInstance(entity)) {
                    _spriteGroups.get(c).remove(entity.getSprite()._sprite);
                }
            }
            
            if (_boundaryCollidableClass != null && _boundaryCollidableClass.isInstance(entity)) {
                _boundaryCollidableGroup.remove(entity.getSprite()._sprite);
            }
        }

        @Override
        public void entityAdded(Entity entity) {
            
            for (Class<?> c : _spriteGroups.keySet()) {
                if (c.isInstance(entity)) {
                    _spriteGroups.get(c).add(entity.getSprite()._sprite);
                    entity.getSprite()._sprite.setActive(true);
                }
            }
            
            if (_boundaryCollidableClass != null && _boundaryCollidableClass.isInstance(entity)) {
                _boundaryCollidableGroup.add(entity.getSprite()._sprite);
            }
        }
        
    }
}
