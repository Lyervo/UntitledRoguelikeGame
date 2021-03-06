/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Camera.Camera;
import Item.ItemPile;
import World.LocalMap;
import World.World;
import java.util.ArrayList;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SpriteSheet;


/**
 *
 * @author Timot
 */
public abstract class Entity
{
    protected int x,y;
    protected Image texture;
    protected SpriteSheet sprites;
    protected boolean autoAnimate;
    protected int id;
    protected int direction;
    protected int animatingIndex;
    protected int animatingTimer;
    
    static int ID = 0;
    
    
    public Entity(int id,int x,int y,Image texture)
    {
        this.x = x;
        this.y = y;
        this.id = ID+1;
        ID++;
        this.texture = texture;
        this.direction = 0;
    }
    
    public Entity(int id,int x,int y,SpriteSheet sprites,boolean autoAnimate)
    {
        this.x = x;
        this.y = y;
        this.id = ID + 1;
        ID++;
        this.sprites = sprites;
        this.autoAnimate = autoAnimate;
        this.direction = 0;
    }
    
    public abstract void tick(boolean[] k,boolean[] m,Input input,World world);
    
    public boolean hasItem(String name)
    {
        return false;
    }
    
    public boolean hasItem(int type)
    {
        return false;
    }
    
    public boolean hasItem(String name,int amount)
    {
        return false;
    }
    
    public void render(Camera cam,LocalMap map,boolean animate)
    {
        if(sprites == null)
        {
            texture.draw(x*cam.getTile_size()+cam.getXofs(),y*cam.getTile_size()+cam.getYofs());
        }else
        {
            if(autoAnimate)
            {
                if(animate)
                {
                    animatingIndex++;
                    if(animatingIndex>=3)
                    {
                        animatingIndex = 0;
                    }
                    
                    
                   
                }
                sprites.getSprite(animatingIndex, 0).draw(x*cam.getTile_size()+cam.getXofs(),y*cam.getTile_size()+cam.getYofs());
                
                
            }
        }
    }
    
    public void move(int x,int y,LocalMap lm)
    {
        this.x += x;
        this.y += y;
        
    }
    
    public boolean withinDistance(int distance,Entity entity)
    {
        return ((Math.abs(this.x-entity.getX()))<=distance&&(Math.abs(this.y-entity.getY()))<=distance);
    }
    
    public int distanceBetween(Entity p)
    {
        
        int px = Math.abs(x - p.getX());
        int py = Math.abs(y - p.getY());
        return px + py;
        
    }
    
    
    
    public int distanceBetween(int x,int y)
    {
        
        int px = Math.abs(this.x - x);
        int py = Math.abs(this.y - y);
        return px + py;
        
    }
    
    public void setPos(int x,int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public int getClosestEntity(ArrayList<Entity> entities)
    {
        int min = Integer.MAX_VALUE;
        int id = 0;
        for(Entity e:entities)
        {
            if(e.distanceBetween(this)<min)
            {
                min = e.distanceBetween(this);
                id = e.getId();
            }
        }
        return id;
    }
    
    public int getClosestItemPile(ArrayList<ItemPile> entities)
    {
        int min = Integer.MAX_VALUE;
        int id = 0;
        for(ItemPile e:entities)
        {
            if(e.distanceBetween(this)<min)
            {
                min = e.distanceBetween(this);
                id = e.getId();
            }
        }
        return id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Image getTexture() {
        return texture;
    }

    public void setTexture(Image texture) {
        this.texture = texture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SpriteSheet getSprites() {
        return sprites;
    }

    public void setSprites(SpriteSheet sprites) {
        this.sprites = sprites;
    }

    public boolean isAutoAnimate() {
        return autoAnimate;
    }

    public void setAutoAnimate(boolean autoAnimate) {
        this.autoAnimate = autoAnimate;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getAnimatingIndex() {
        return animatingIndex;
    }

    public void setAnimatingIndex(int animatingIndex) {
        this.animatingIndex = animatingIndex;
    }

    public int getAnimatingTimer() {
        return animatingTimer;
    }

    public void setAnimatingTimer(int animatingTimer) {
        this.animatingTimer = animatingTimer;
    }

    

    
}
