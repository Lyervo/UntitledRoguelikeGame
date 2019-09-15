/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Item;

import Res.Res;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Timot
 */
public class ItemLibrary
{
    private String seed;
    
    private ArrayList<Item> potions;
    private ArrayList<Item> equipments;
    private ArrayList<Item> items;
    
    private Res res;
    
    private boolean[] identify;
    
    private ArrayList<Recipe> recipes;
    
    private ArrayList<ItemType> types;
    
    
    public ItemLibrary(String seed,Res res)
    {
        this.res = res;
        this.seed = seed;
        this.potions = new ArrayList<Item>();
        this.equipments = new ArrayList<Item>();
        this.items = new ArrayList<Item>();
        this.recipes = new ArrayList<Recipe>();
        this.types = new ArrayList<ItemType>();
        initItems();
        initRecipes();
        initItemType();
    }
    
    
    public void initItemType()
    {
        try {
            
            File jsonFile = new File("res/data/item_type.json");
            Scanner jsonReader;
            jsonReader = new Scanner(jsonFile);
            String jsonString = "";
            while(jsonReader.hasNext())
            {
                jsonString += jsonReader.nextLine();
            }
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(jsonString);
            JSONArray jsonArr = (JSONArray)obj;
            
            for(int i=0;i<jsonArr.size();i++)
            {
                JSONObject jsonObj = (JSONObject)jsonArr.get(i);
                types.add(new ItemType(jsonObj,res));
            }
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ItemLibrary.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException e)
        {
            Logger.getLogger(ItemLibrary.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    
    public void initRecipes()
    {
        
        try {
            
            File jsonFile = new File("res/data/recipe.json");
            Scanner jsonReader;
            jsonReader = new Scanner(jsonFile);
            String jsonString = "";
            while(jsonReader.hasNext())
            {
                jsonString += jsonReader.nextLine();
            }
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(jsonString);
            JSONArray jsonArr = (JSONArray)obj;
            
            for(int i=0;i<jsonArr.size();i++)
            {
                System.out.println("int "+i);
                JSONObject jsonObj = (JSONObject)jsonArr.get(i);
                recipes.add(new Recipe(jsonObj,this,i));
            }
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ItemLibrary.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException e)
        {
            Logger.getLogger(ItemLibrary.class.getName()).log(Level.SEVERE, null, e);
        }
            
    }
    
    
    public void initItems()
    {
        
        try {
            
            File jsonFile = new File("res/data/item.json");
            Scanner jsonReader = new Scanner(jsonFile);
            String jsonString = "";
            while(jsonReader.hasNext())
            {
                jsonString += jsonReader.nextLine();
            }
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(jsonString);
            JSONArray jsonArr = (JSONArray)obj;
            
            for(int i=0;i<jsonArr.size();i++)
            {
                JSONObject jsonObj = (JSONObject)jsonArr.get(i);
                if(jsonObj.get("category").equals("potion"))
                {
                    potions.add(new Item(jsonObj,this));
                    if(jsonObj.get("texture")!=null)
                    {
                        potions.get(potions.size()-1).setTexture(res.getTextureByName((String)jsonObj.get("texture")));
                    }
                }else if(jsonObj.get("category").equals("potion"))
                {
                    equipments.add(new Item(jsonObj,this));
                    if(jsonObj.get("texture")!=null)
                    {
                        equipments.get(equipments.size()-1).setTexture(res.getTextureByName((String)jsonObj.get("texture")));
                    }
                }else
                {
                    items.add(new Item(jsonObj,this));
                    if(jsonObj.get("texture")!=null)
                    {
                        items.get(items.size()-1).setTexture(res.getTextureByName((String)jsonObj.get("texture")));
                    }
                }
                    
                
            }
        
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ItemLibrary.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ItemLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        try {
            
            ArrayList<Pair<String,Pair<Color,Color>>> colors = new ArrayList<Pair<String,Pair<Color,Color>>>();
            File colorNameFile = new File("res/data/potion_colors.json");
            Scanner colorNameReader = new Scanner(colorNameFile);
            
            String colorJsonString = "";
            
            while(colorNameReader.hasNext())
            {
                colorJsonString +=colorNameReader.next();
            }
            
            JSONParser colorNameParser = new JSONParser();
            Object colorObj = colorNameParser.parse(colorJsonString);
            JSONArray colorArr = (JSONArray)colorObj;
            
            for(int i=0;i<colorArr.size();i++)
            {
                JSONObject jsonObj = (JSONObject)colorArr.get(i);
                
                colors.add(new Pair((String)jsonObj.get("name"),new Pair(Color.decode((String)jsonObj.get("primary")),Color.decode((String)jsonObj.get("secondary")))));
            }
            
            Collections.shuffle(colors);
            
            
            Random dice = new Random();
            
            identify = new boolean[potions.size()];
            Arrays.fill(identify, false);
            
            for(int i=0;i<potions.size();i++)
            {
                
                Image temp = new Image(res.empty_potion.getTexture());
                
                temp = paintPotion(temp,colors.get(i).getValue());
                
                if(potions.get(i).getTexture()==null)
                {
                    potions.get(i).setTexture(temp);
                    potions.get(i).setName(colors.get(i).getKey()+" Potion");
                    if(potions.get(i).getUnidentified_desc()!=null)
                    {
                        String new_desc = potions.get(i).getUnidentified_desc().replace("<color>", colors.get(i).getKey());
                        potions.get(i).setUnidentified_desc(new_desc);
                    }
                }
          
                
                
            }
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ItemLibrary.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ItemLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public Image paintPotion(Image image,Pair<Color,Color> colors)
    {
        try {
            Graphics g = image.getGraphics();
            g.setColor(colors.getValue());
            g.fillRect(12, 16, 2, 2);
            g.fillRect(10, 18, 2, 4);
            g.fillRect(12, 22, 2, 2);
            g.fillRect(14, 24, 4, 2);
            g.fillRect(18, 16, 2, 2);
            g.fillRect(20, 18, 2, 4);
            g.fillRect(18, 22, 2, 2);
            
            g.setColor(colors.getKey());
            g.fillRect(12, 10, 2, 2);
            g.fillRect(14, 12, 2, 2);
            g.fillRect(12, 18, 8, 4);
            g.fillRect(14, 22, 4, 2);

            g.flush();
        } catch (SlickException ex) {
            Logger.getLogger(ItemLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }
    
    
    //this will get the item by it's type name instead of it's randomly generated name
    public Item getItemByTrueName(String trueName)
    {
        for(Item i:potions)
        {
          
            if(i.getTrueName().equals(trueName))
            {
                return i;
            }
        }
        for(Item i:equipments)
        {
          
            if(i.getTrueName().equals(trueName))
            {
                return i;
            }
        }
        for(Item i:items)
        {
          
            if(i.getTrueName().equals(trueName))
            {
                return i;
            }
        }
        
        return null;
    }
    
    public boolean checkIdentified(String trueName)
    {
        for(int i=0;i<potions.size();i++)
        {
            if(potions.get(i).getTrueName().equals(trueName))
            {
                
                return identify[i];
            }
        }
        
        return false;
    }
    
    public void identify(String trueName)
    {
        
        for(int i=0;i<potions.size();i++)
        {
            if(potions.get(i).getTrueName().equals(trueName))
            {
                identify[i] = true;
            }
        }
        
    }
    
    public Recipe getRecipeById(int id)
    {
        for(Recipe r:recipes)
        {
            if(r.getId() == id)
            {
                return r;
            }
        }
        return null;
    }
    public ItemType getItemTypeByName(String name)
    {
        for(ItemType i:types)
        {
            if(i.getName().equals(name))
            {
                return i;
            }
        }
        return null;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public ArrayList<Item> getPotions() {
        return potions;
    }

    public void setPotions(ArrayList<Item> potions) {
        this.potions = potions;
    }

    public Res getRes() {
        return res;
    }

    public void setRes(Res res) {
        this.res = res;
    }

    public boolean[] getIdentify() {
        return identify;
    }

    public void setIdentify(boolean[] identify) {
        this.identify = identify;
    }

    public ArrayList<Item> getEquipments() {
        return equipments;
    }

    public void setEquipments(ArrayList<Item> equipments) {
        this.equipments = equipments;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    public ArrayList<ItemType> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<ItemType> types) {
        this.types = types;
    }
    
    
    
    
}
