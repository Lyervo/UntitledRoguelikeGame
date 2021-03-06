/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InventoryUI;

import Item.Item;
import Res.Res;
import Trading.TradeXButton;
import World.World;
import java.awt.Point;
import java.awt.Rectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.TextField;

/**
 *
 * @author Timot
 */
public class XItemTextField extends TextField
{
    
    private int state;
    private String itemName;
    private XItemTextFieldConfirmButton confirmButton;
    private XItemTextFieldExitButton exitButton;
    
    private TrueTypeFont font;
    
    private Rectangle bounds;
    private boolean hover;
    
    private World world;
    
    private int index;
    
    private int itemPileId;
    

    
    public XItemTextField(GUIContext container, Font font, int x, int y, int width, int height,World world,Res res)
    {
        super(container, font, x, y, width, height);
        setBackgroundColor(Color.decode("#757161"));
        setBorderColor(Color.decode("#757161"));
        this.font = res.disposableDroidBB;
        confirmButton = new XItemTextFieldConfirmButton(x-(((res.disposableDroidBB.getWidth("Confirm")+20)-width)/2),y+res.disposableDroidBB.getHeight()+5,res.disposableDroidBB.getWidth("Confirm")+20,res.disposableDroidBB.getHeight()+10,"Confirm",Color.black,Color.decode("#757161"),Color.decode("#666355"),res.disposableDroidBB);
        exitButton = new XItemTextFieldExitButton(x-80,y-50,res.disposableDroidBB.getWidth("X")+10,res.disposableDroidBB.getWidth("X")+10,"X",Color.black,Color.decode("#757161"),Color.decode("#666355"),res.disposableDroidBB);
        bounds = new Rectangle(x,y,width,height);
        this.world = world;
    }
    
    @Override
    public void keyReleased(int key,char c)
    {
        if(key == Input.KEY_ENTER)
        {
            world.getxItemTextField().processInput(world, world.getInventory_ui());
            world.moved();
            setText("");
        }
    }
    
    @Override
    public void keyPressed(int key,char c)
    {
        if(getText().length()<=5)
        {
            if(Character.isDigit(c))
            {
                setText(getText()+c);
            }
        }
    }
    
    public void tick(boolean[] k,boolean[] m,Input input,World world,InventoryUI inventoryUI)
    {
        
        if(bounds.contains(new Point(input.getMouseX(),input.getMouseY())))
        {
            hover = true;
        }else
        {
            hover = false;
        }
        
//        if(m[10]&&!hover&&world.isxItemTextFieldActive())
//        {
//            world.deactivateXItemTextField();
//            setText("");
//            return;
//        }
        
        exitButton.tick(m, input, world);
        confirmButton.tick(m, input, world);
        
        
        
        if(k[Input.KEY_ESCAPE])
        {
            world.deactivateXItemTextField();
            setText("");
        }
        
    }
    
    public void processInput(World world,InventoryUI inventoryUI)
    {
        if(getText()!="")
        {
            if(state<=2||state==5)
            {
                world.getWm().getPlayerInventory().dropItem(world.getWm().getCurrentLocalMap().getPlayer().getX(),world.getWm().getCurrentLocalMap().getPlayer().getY(), itemName, world.getWm().getCurrentLocalMap(), Integer.parseInt(getText()));
            }else if(state==4)
            {
                world.getWm().getCurrentLocalMap().getPlayer().grabItemAt(0,0, itemPileId, index,itemName,Integer.parseInt(getText()));
            }
        }
        inventoryUI.refreshInventoryUI(world.getWm().getCurrentLocalMap());
        world.deactivateXItemTextField();
        setText("");
    }
    
    public void renderBackground(Graphics g)
    {
        g.setColor(Color.decode("#4d494d"));
        g.fillRect(x-80, y-50, 160+getWidth(), 120+getHeight());
        g.setColor(Color.white);
        font.drawString(x-((160+getWidth()-font.getWidth("Enter amount"))/2)-10, y-40, "Enter amount");
        exitButton.render(g);
        confirmButton.render(g);
        g.setColor(Color.black);
        g.drawRect(x-80, y-50, 160+getWidth(), 120+getHeight());
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public XItemTextFieldConfirmButton getConfirmButton() {
        return confirmButton;
    }

    public void setConfirmButton(XItemTextFieldConfirmButton confirmButton) {
        this.confirmButton = confirmButton;
    }

    public XItemTextFieldExitButton getExitButton() {
        return exitButton;
    }

    public void setExitButton(XItemTextFieldExitButton exitButton) {
        this.exitButton = exitButton;
    }

    public TrueTypeFont getFont() {
        return font;
    }

    public void setFont(TrueTypeFont font) {
        this.font = font;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public boolean isHover() {
        return hover;
    }

    public void setHover(boolean hover) {
        this.hover = hover;
    }

    public World getWorld()
    {
        return world;
    }

    public void setWorld(World world)
    {
        this.world = world;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public int getItemPileId()
    {
        return itemPileId;
    }

    public void setItemPileId(int itemPileId)
    {
        this.itemPileId = itemPileId;
    }
    
    
    
}