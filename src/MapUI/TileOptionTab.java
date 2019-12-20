/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MapUI;

import Entity.Furniture;
import Entity.Pawn;
import Entity.Task;
import UI.OptionTab;
import Item.Item;
import Item.ItemPile;
import Res.Res;
import UI.Option;
import World.LocalMap;
import World.Tile;
import java.awt.Rectangle;
import java.util.ArrayList;
import javafx.util.Pair;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.TrueTypeFont;

/**
 *
 * @author Timot
 */
public class TileOptionTab extends OptionTab
{
    private Tile t;
    
    private Pawn p;
    
    private ItemPile ip;
    
    private Furniture furniture;
    
    private LocalMap lm;
    
    public TileOptionTab(int x, int y, GameContainer container, LocalMap lm, TrueTypeFont font,Res res,Tile t) {
        super(x, y, lm, container,font,res);
        this.t = t;
        p = lm.getPawnAt(t.getX(),t.getY());
        ip = lm.getItemPileAt(t.getX(), t.getY());
        furniture = lm.furnitureAt(t.getX(), t.getY());
        this.lm = lm;
        initOptions();
        initOptionTab();
    }
    

    @Override
    public void runOption(LocalMap lm)
    {
        switch (options.get(hoveringIndex).getActionType())
        {
            case 0:
                lm.getPlayer().walkTo(t.getX(),t.getY());
                break;
            case 1:
                if(p!=null)
                {
                    lm.getWm().getWorld().getAncestor().addText(p.getName()+" is standing there.");
                }
                if(ip==null)
                {
                    lm.getWm().getWorld().getAncestor().addText("There is nothing on the ground.");
                }else
                {
                    if(ip.getItems().size()==1)
                    {
                        lm.getWm().getWorld().getAncestor().addText("There is a "+ip.getItems().get(0).getName()+" on the ground.");
                
                    }else
                    {
                        lm.getWm().getWorld().getAncestor().addText("There is a "+ip.getItems().get(0).getName()+" and other "+(ip.getItems().size()-1)+" items on the ground.");
                
                    }
                }
                break;
            case 2:
                int id = ip.getId();
                int itemIndex = hoveringIndex;
                if (t.isVisit()) {
                    itemIndex--;
                }
                if (!t.isSolid()) {
                    itemIndex--;
                }
                lm.getPlayer().grabItemAt(t.getX(), t.getY(), id, itemIndex);
                
                break;
            case 3:
                if(furniture.withinDistance(1, lm.getPlayer()))
                {
                    lm.getWorld().spawnFurnitureInventoryWindow(furniture, 100, 100);
                }
                break;
            case 5:
                lm.getPlayer().setTask(new Task(0,0,options.get(hoveringIndex).getId(),0,"talk_to_target"));
                break;
        }
    }

    @Override
    public void initOptions()
    {
        optionBounds = new ArrayList<Rectangle>();
        options = new ArrayList<Option>();
        if(!t.isSolid())
        {
            options.add(new Option("Walk to",0));
        }
        ArrayList<Item> items = lm.getItemsAt(t.getX(), t.getY());
        if(t.getVisit()==2)
        {
            options.add(new Option("Look",1));
            if(items!=null)
            {
                for(Item i:items)
                {
                    options.add(new Option("Take "+i.getName(),2));
                }
            }
        }
        
        if(lm.getPawnAt(t.getX(), t.getY())!=null)
        {
            ArrayList<Pawn> pawns = lm.getPawnsAt(t.getX(),t.getY());
            for(Pawn p:pawns)
            {
                if(!p.isControl())
                {
                    options.add(new Option("Talk to "+p.getName(),5));
                    options.get(options.size()-1).setId(p.getId());
                }
            }
        }
        
        
        if(furniture!=null)
        {
            if(furniture.isFuelable()&&furniture.withinDistance(1, lm.getPlayer()))
            {
                options.add(new Option("Interact",3));
            }
        }
        
        setLongestIndex();
    }
    
}
