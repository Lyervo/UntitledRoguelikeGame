/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CraftingUI;

import UI.DescBox;
import Entity.Furniture;
import Res.Res;
import World.World;
import java.awt.Point;
import java.awt.Rectangle;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

/**
 *
 * @author Timot
 */
public class StationUI extends DescBox
{
    private String name, desc;
    private int index;
    
    private Image texture;
    
    private Rectangle bounds;
    
    public StationUI(Furniture furniture,int index,Res res)
    {
        super(furniture.getName(),furniture.getDesc(),res.disposableDroidBB);
        this.name = furniture.getName();
        this.desc = furniture.getDesc();
        if(furniture.getTexture()!=null)
        {
            this.texture = furniture.getTexture();
        }else
        {
            this.texture = furniture.getSprites().getSubImage(32, 32, 32, 32);
        }
        this.index = index;
        this.bounds = new Rectangle(16+(35*index),29,32,32);
    }
    
    public void render(Graphics g,Input input,int x,int y)
    {
        texture.draw(16+(35*index)+x,29+y);
    }
    
    public void tick(boolean[] k,boolean[] m,Input input,World world,int x,int y)
    {
        tickDesc(bounds.contains(new Point(input.getMouseX()-x,input.getMouseY()-y))&&world.getZ()==world.getCraftingWindow().getZ());
    }
}
