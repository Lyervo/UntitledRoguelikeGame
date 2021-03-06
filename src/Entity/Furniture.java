 /* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Camera.Camera;
import Item.Inventory;
import Item.Item;
import World.LocalMap;
import World.World;
import java.util.ArrayList;

import org.newdawn.slick.Input;

/**
 *
 * @author Timot
 */
public class Furniture extends Entity
{

    private int fuel;
    private String name;
    private String desc;
    private ArrayList<Integer> properties;
    //works the same way as item properties, each integer represents a property
    //1 - fuelable
    //2 - is a fire source
    //3 - flammable
    //4 - woodworking station
    
    private int fuelAnim;
    
    private ArrayList<Item> items;
    
    public Furniture(int id,int x,int y,FurnitureTemplate ft)
    {
        super(id,x,y,ft.getTexture());
        this.properties = new ArrayList<Integer>(ft.getProperties());
        this.name = ft.getName();
        this.desc = ft.getDesc();
        items = new ArrayList<Item>();
        initFurniture();
    }
    
    public Furniture(int id,int x,int y,FurnitureTemplate ft,boolean autoAnim)
    {
        super(id,x,y,ft.getSpriteSheet(),autoAnim);
        this.properties = new ArrayList<Integer>(ft.getProperties());
        this.name = ft.getName();
        this.desc = ft.getDesc();
        items = new ArrayList<Item>();
        initFurniture();
    }
    
    
    public void initFurniture()
    {
        if(properties.contains(1))
        {
            fuel = 0;
            fuelAnim = 3;
        }
    }
    
    public void addFuel(Item item)
    {
        fuel += item.getEffectByName("fuel").getValue();
        
    }

    @Override
    public void tick(boolean[] k, boolean[] m, Input input, World world)
    {
        if(world.isMoved())
        {
            if(properties.contains(1))
            {
                if(fuel>=1)
                {
                    fuel--;
                }
                
                if(fuel<=0)
                {
                    fuelAnim = 3;
                }else if(fuel <= 2)
                {
                    fuelAnim = 2;
                }else if(fuel <= 5)
                {
                    fuelAnim = 1;
                }else if(fuel >=6)
                {
                    fuelAnim = 0;
                }
                
            }
        }
    }
    
    @Override
    public void render(Camera cam,LocalMap map,boolean animate)
    {
        if(map.getTiles()[this.y][this.x].isVisit())
        {
            if(sprites == null)
            {
                texture.draw(x*cam.getTile_size()+cam.getXofs(),y*cam.getTile_size()+cam.getYofs(),cam.getTile_size(),cam.getTile_size());
            }else
            {
                if(autoAnimate)
                {
//                    if(animate)
//                    {
//                        animatingIndex++;
//                        if(animatingIndex>=sprites.getHorizontalCount()-1)
//                        {
//                            animatingIndex = 0;
//                        }
//
//
//                        
//                    }
//                    
//                    sprites.getSprite(animatingIndex, fuelAnim).draw(x*cam.getTile_size()+cam.getXofs(),y*cam.getTile_size()+cam.getYofs(),cam.getTile_size(),cam.getTile_size());
//

                }
            }
        }
    }
    
    public int getStationType()
    {
        for(int i=0;i<properties.size();i++)
        {
            if(properties.get(i)>20&&properties.get(i)<50)
            {
                return properties.get(i);
            }
        }
        return -1;
    }
    
    public void addItem(int index,Inventory inventory)
    {
        items.add(new Item(inventory.getItems().get(index)));
        inventory.getItems().remove(index);
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ArrayList<Integer> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<Integer> properties) {
        this.properties = properties;
    }
    
    public boolean isFuelable()
    {
        return properties.contains(1);
    }

    public int getFuelAnim() {
        return fuelAnim;
    }

    public void setFuelAnim(int fuelAnim) {
        this.fuelAnim = fuelAnim;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    @Override
    public boolean hasItem(String name) {
       return true;
    }
    
    

}
