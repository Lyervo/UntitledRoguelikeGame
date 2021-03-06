/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Item;

import Entity.Entity;
import Entity.Pawn;
import Entity.Status;

import World.LocalMap;
import World.World;
import java.util.ArrayList;
import org.newdawn.slick.Input;

/**
 *
 * @author Timot
 */
public class Inventory
{
    private ArrayList<Item> items;
    
    private Entity owner;
    
    private Equipment equipment;
    
    private Crafting crafting;
    
    public Inventory(Entity owner,ItemLibrary itemLibrary)
    {
        this.owner = owner;
        items = new ArrayList<Item>();
        equipment = new Equipment(this);
        crafting = new Crafting(this,itemLibrary);
        
        
    }
    
    public Inventory(Entity owner,ArrayList<Item> items)
    {
        this.owner = owner;
        this.items = new ArrayList<Item>(items);
     
        
    }
    
    
    public void tick(boolean[] k,boolean[] m,Input input,World world)
    {
        boolean removed = false;
        for(int i=items.size()-1;i>=0;i--)
        {
            items.get(i).tick(k, m, input, world);
            if(items.get(i).getDurability() <= 0)
            {
                if(items.get(i).getExpire().size()!=0)
                {
                    for(int j=0;j<items.get(i).getExpire().size();j++)
                    {
                        addItem(world.getItemLibrary().getItemByTrueName(items.get(i).getExpire().get(j)));
                    }
                }
                items.remove(i);
                removed = true;
                
            }
        }
        equipment.tick(k, m, input, world);
        if(removed)
        {
            world.getInventory_ui().refreshInventoryUI(world.getWm().getCurrentLocalMap());
            world.getEquipment_ui().refreshUI();
        }
    }
    
   
    
    public void dropItem(int x,int y,String itemName,LocalMap lm,int amount)
    {

        for(int index = items.size()-1;index>=0;index--)
        {
            if(items.get(index).getTrueName().equals(itemName))
            {
                if(amount>0&&amount<=items.get(index).getStack()&&items.get(index).isStackable())
                {
                    if(lm.getItemPileAt(owner.getX(), owner.getY())==null)
                    {
                        items.get(index).addStack(-amount);

                        Item i = new Item(items.get(index));
                        i.setStack(amount);
                        lm.getItemPiles().add(new ItemPile(lm.getItemPiles().size(),owner.getX(),owner.getY(),i));

                    }else
                    {

                        items.get(index).addStack(-amount);

                        Item i = new Item(items.get(index));
                        i.setStack(amount);
                        lm.getItemPileAt(owner.getX(), owner.getY()).addItem(new Item(i));
                    }
                }else
                {
                    if(lm.getItemPileAt(owner.getX(), owner.getY())==null)
                    {
                        lm.getItemPiles().add(new ItemPile(lm.getItemPiles().size(),owner.getX(),owner.getY(),items.get(index)));
                    }else
                    {  
                        lm.getItemPileAt(owner.getX(), owner.getY()).addItem(items.get(index));
                    }
                    items.remove(index);
                    return;
                }
            }
        }
        
      
    }
    
    public void dropItem(int x,int y,int index,LocalMap lm,int amount)
    {

       
            
                if(amount>0&&amount<=items.get(index).getStack()&&items.get(index).isStackable())
                {
                    if(lm.getItemPileAt(owner.getX(), owner.getY())==null)
                    {
                        items.get(index).addStack(-amount);

                        Item i = new Item(items.get(index));
                        i.setStack(amount);
                        lm.getItemPiles().add(new ItemPile(lm.getItemPiles().size(),owner.getX(),owner.getY(),i));

                    }else
                    {

                        items.get(index).addStack(-amount);

                        Item i = new Item(items.get(index));
                        i.setStack(amount);
                        lm.getItemPileAt(owner.getX(), owner.getY()).addItem(new Item(i));
                    }
                }else
                {
                    if(lm.getItemPileAt(owner.getX(), owner.getY())==null)
                    {
                        lm.getItemPiles().add(new ItemPile(lm.getItemPiles().size(),owner.getX(),owner.getY(),items.get(index)));
                    }else
                    {  
                        lm.getItemPileAt(owner.getX(), owner.getY()).addItem(items.get(index));
                    }
                    items.remove(index);
                    return;
                }
          
        
      
    }
    
    public void addItem(Item item)
    {
        Item itemA = new Item(item);
        
        if(itemA.isStackable())
        {
            boolean stacked = false;
            for(int i=0;i<items.size();i++)
            {
                if(itemA.getTrueName().equals(items.get(i).getTrueName())&&itemA.getOwnership().equals(items.get(i).getOwnership()))
                {
                    items.get(i).addStack(itemA.getStack());
                    stacked = true;
                }
            }
            if(!stacked)
            {
                items.add(itemA);
            }
        }else
        {
            items.add(itemA);
        }
        
        
       
    }
    
    public void transferItem(Inventory inventory,String itemName,int amount)
    {
        for(int i=items.size()-1;i>=0;i--)
        {
            if(items.get(i).getTrueName().equals(itemName))
            {
                if(items.get(i).isStackable())
                {
                    Item transferItem = new Item(items.get(i));
                    
                    if(items.get(i).getStack()>=amount)
                    {
                        transferItem.setStack(amount);
                        items.get(i).addStack(-amount);
                    }else if(amount==-1)
                    {
                        transferItem.setStack(items.get(i).getStack());
                        items.get(i).setStack(0);
                    }else
                    {
                            transferItem.setStack(items.get(i).getStack());
                            items.get(i).setStack(0);
                    
                    }
                    
                    inventory.addItem(transferItem);
                    
                    if(items.get(i).getStack()==0)
                    {
                        items.remove(i);
                    }
                    
                }else
                {
                    Item transferItem = new Item(items.get(i));
                    inventory.addItem(transferItem);
                    items.remove(i);
                    amount--;
                    if(amount<=0)
                    {
                        break;
                    }
                }
            }
        }
    }
    
    
    
    
    public void transferItem(Inventory inventory,int index,int amount)
    {
        System.out.println(((Pawn)inventory.getOwner()).getName());
        if (items.get(index).isStackable())
        {
            Item transferItem = new Item(items.get(index));

            if (amount == -1)
            {
                transferItem.setStack(items.get(index).getStack());
                items.get(index).setStack(0);
            } else if (items.get(index).getStack() >= amount)
            {
                transferItem.setStack(amount);
                items.get(index).addStack(-amount);
            } else
            {
                transferItem.setStack(items.get(index).getStack());
                items.get(index).setStack(0);

            }

            inventory.addItem(transferItem);

            if (items.get(index).getStack() == 0)
            {
                items.remove(index);
            }

        } else
        {
            Item transferItem = new Item(items.get(index));
            inventory.addItem(transferItem);
            items.remove(index);
            amount--;
            if (amount <= 0)
            {
                return;
            }
        }
           
    }
    
    public void transferItem(Inventory inventory,Item item,int amount)
    {
        for(int i=items.size()-1;i>=0;i--)
        {
            if(items.get(i).equals(item))
            {
                if(items.get(i).isStackable())
                {
                    Item transferItem = new Item(items.get(i));
                    
                    if(items.get(i).getStack()>=amount)
                    {
                        transferItem.setStack(amount);
                        items.get(i).addStack(-amount);
                    }else if(amount==-1)
                    {
                        transferItem.setStack(items.get(i).getStack());
                        items.get(i).setStack(0);
                    }else
                    {
                            transferItem.setStack(items.get(i).getStack());
                            items.get(i).setStack(0);
                    
                    }
                    
                    inventory.addItem(transferItem);
                    
                    if(items.get(i).getStack()==0)
                    {
                        items.remove(i);
                    }
                    
                }else
                {
                    Item transferItem = new Item(items.get(i));
                    inventory.addItem(transferItem);
                    items.remove(i);
                    amount--;
                    if(amount<=0)
                    {
                        break;
                    }
                }
            }
        }
    }
    
    
    public void removeItem(Item item)
    {
        
        if(item.isStackable())
        {
            boolean stacked = false;
            for(int i=0;i<items.size();i++)
            {
                if(item.equals(items.get(i)))
                {
                    items.get(i).decreaseStack();
                    if(items.get(i).getStack()<=0)
                    {
                        items.remove(i);
                    }
                }
            }
            
        }else
        {
            items.remove(item);
        }
        
        
       
    }
    
    public void removeItem(Item item,int amount)
    {
        
        
        items.remove(item);
        
        
        
       
    }
    
    
   

    public void debugInit(ItemLibrary itemLibrary)
    {
        addItem(new Item(itemLibrary.getItemByTrueName("Steel Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Raw meat")));
        addItem(new Item(itemLibrary.getItemByTrueName("Raw meat")));
        addItem(new Item(itemLibrary.getItemByTrueName("Raw meat")));
        addItem(new Item(itemLibrary.getItemByTrueName("Raw meat")));
        addItem(new Item(itemLibrary.getItemByTrueName("Steel Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Steel Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Steel Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Bronze Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Bronze Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Bronze Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Iron Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Iron Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Iron Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Steel Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Steel Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Steel Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Steel Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Bronze Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Bronze Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Bronze Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Iron Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Iron Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Iron Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Steel Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Steel Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Steel Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Steel Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Bronze Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Bronze Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Bronze Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Iron Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Iron Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Iron Bar")));
        addItem(new Item(itemLibrary.getItemByTrueName("Woodworking Guide I")));
        addItem(new Item(itemLibrary.getItemByTrueName("Iron Sword")));
        addItem(new Item(itemLibrary.getItemByTrueName("Wooden Shield")));
        addItem(new Item(itemLibrary.getItemByTrueName("Wood")));
        addItem(new Item(itemLibrary.getItemByTrueName("Wood")));
        addItem(new Item(itemLibrary.getItemByTrueName("Wood")));
        addItem(new Item(itemLibrary.getItemByTrueName("Crafting Knife")));
        addItem(new Item(itemLibrary.getItemByTrueName("Bronze Axe")));
        for(int i=0;i<20;i++)
        {
            addItem(new Item(itemLibrary.getItemByTrueName("Wood")));
        }
        
        addItem(new Item(itemLibrary.getItemByTrueName("Tree Leaves")));
        for(int i=0;i<750;i++)
        {
            if(i==10||i==20)
            {
                addItem(new Item(itemLibrary.getItemByTrueName("Poison")));
            }else if(i==0||i==3)
            {
                addItem(new Item(itemLibrary.getItemByTrueName("Antidote")));
            }else
            {
                addItem(new Item(itemLibrary.getItemByTrueName("Healing Potion")));
            }
        }
        
    }
    
    public void consumeItem(int index,World world,boolean player)
    {
        if(items.get(index).isConsumable())
        {
            if(player)
            {
                for (int i = 0; i < items.get(index).getEffects().size(); i++)
                {
                    world.getWm().getPlayer().getStatus().add(new Status(items.get(index).getEffects().get(i)));

                }
                if (!items.get(index).getPostConsume().isEmpty())
                {
                    for (int i=0;i<items.get(index).getPostConsume().size();i++)
                    {
                        addItem(world.getItemLibrary().getItemByTrueName(items.get(index).getPostConsume().get(i)));
                    }
                }
                world.getItemLibrary().identify(items.get(index).getTrueName());
                removeItem(items.get(index));

                world.moved();
            }else
            {
                
            }
        }else
        {
            return;
        }
                
                
    }
    
    public int getItemCount(String name)
    {
        int count = 0;
        for(Item i:items)
        {
            if(i.getTrueName().equals(name))
            {
                if(i.isStackable())
                {
                    count += i.getStack();
                }else
                {
                    count++;
                }
            }
        }
        return count;
        
    }
    
    
    public Item getItemByName(String name)
    {
        for(Item i:items)
        {
            if(i.getTrueName().equals(name))
            {
                return i;
            }
        }
        return null;
    }
            
    public boolean hasItemType(int type)
    {
        for(Item i:items)
        {
            
            if(i.getProperties().contains(type))
            {
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<Item> getItemsOfType(int type)
    {
        ArrayList<Item> result = new ArrayList<Item>();
        for(Item i:items)
        {
            if(i.getProperties().contains(type))
            {
                result.add(i);
            }
        }
        return result;
    }
    
    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public Entity getOwner() {
        return owner;
    }

    public void setOwner(Entity owner) {
        this.owner = owner;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Crafting getCrafting() {
        return crafting;
    }

    public void setCrafting(Crafting crafting) {
        this.crafting = crafting;
    }
    
    
    
    
    
}
