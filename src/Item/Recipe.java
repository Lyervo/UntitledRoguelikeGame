/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Item;

import java.util.ArrayList;
import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.newdawn.slick.Image;

/**
 *
 * @author Timot
 */
public class Recipe
{
    private String name,genericName;
    private int amount;
    private ArrayList<IngredientRequirement> ingredients;
    private ArrayList<Integer> stations;
    private ArrayList<Pair<String,Integer>> requirements;
    private boolean learnt;
    private String type;
    private Image texture;
    private int id,turns;
    
    
    private ArrayList<Pair<String,Integer>> byProducts;
    
    private Image template_texture;
    
    public Recipe(JSONObject jsonObj,ItemLibrary itemLibrary,int id)
    {
        this.id = id;
        ingredients = new ArrayList<IngredientRequirement>();
        stations = new ArrayList<Integer>();
        requirements = new ArrayList<Pair<String,Integer>>();
        
        name = (String)jsonObj.get("name");
        if(name.startsWith("<"))
        {
            genericName = name;
        }
        
        amount = Integer.parseInt((String)jsonObj.get("amount"));
        turns = Integer.parseInt((String)jsonObj.get("turn"));

        JSONArray ingArr = (JSONArray)jsonObj.get("ingredients");
        if(ingArr!=null)
        {
            for(int i=0;i<ingArr.size();i++)
            {
                JSONObject ingObj = (JSONObject)ingArr.get(i);
              
                ingredients.add(new IngredientRequirement((String)ingObj.get("name"),Integer.parseInt((String)ingObj.get("consumed"))));
            }
        }
        
        JSONArray staArr = (JSONArray)jsonObj.get("station");
        if(staArr!=null)
        {
            for(int i=0;i<staArr.size();i++)
            {
                JSONObject staObj = (JSONObject)staArr.get(i);
                stations.add(Integer.parseInt((String)staObj.get("type")));
            }
        }
        
        JSONArray reqArr = (JSONArray)jsonObj.get("requirements");
        if(reqArr!=null)
        {
            for(int i=0;i<reqArr.size();i++)
            {
                JSONObject reqObj = (JSONObject)reqArr.get(i);
                requirements.add(new Pair((String)reqObj.get("type"),Integer.parseInt((String)reqObj.get("level"))));
            }
        }
        
        byProducts = new ArrayList<Pair<String,Integer>>();
        JSONArray proArr = (JSONArray)jsonObj.get("by_product");
        if(proArr!=null)
        {
            for(int i=0;i<proArr.size();i++)
            {
                JSONObject proObj = (JSONObject)proArr.get(i);
                byProducts.add(new Pair((String)proObj.get("name"),Integer.parseInt((String)proObj.get("amount"))));
            }
        }

        
       if((String)jsonObj.get("template_texture")!=null)
       {
           
           template_texture = new Image(itemLibrary.getRes().getTextureByName((String)jsonObj.get("template_texture")).getTexture());
       }
        
        texture = itemLibrary.getItemByTrueName(name).getTexture();
        
        learnt = true;
        
    }
    
    public void setRecipeGeneric(Item item,ItemLibrary itemLibrary)
    {
        
        for(int i=0;i<ingredients.size();i++)
        {
            if(ingredients.get(i).getItem().startsWith("<"))
            {
                if(itemLibrary.getItemTypeByType(item.getGenericType()).getName().equals(ingredients.get(i).getItem()))
                {
                    ingredients.get(i).setItem(item.getInGameName());
                    ingredients.get(i).setGenericType(item.getGenericType());
                }
            }else
            {
                if(itemLibrary.getItemByTrueName(ingredients.get(i).getItem()).getGenericType()==item.getGenericType())
                {
                    ingredients.get(i).setItem(item.getInGameName());
                    ingredients.get(i).setGenericType(item.getGenericType());
                }
            }
        }
        
    }
    
    public IngredientRequirement getIngredientByGenericType(int type)
    {
        for(int i=0;i<ingredients.size();i++)
        {
            if(ingredients.get(i).getGenericType() == type)
            {
                return ingredients.get(i);
            }
        }
        return null;
    }
    

    public String getName() {
        return name;
    }
    
    public String getRecipeName()
    {
        if(genericName==null)
        {
            return name;
        }else
        {
            return genericName;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ArrayList<IngredientRequirement> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<IngredientRequirement> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Integer> getStations() {
        return stations;
    }

    public void setStations(ArrayList<Integer> stations) {
        this.stations = stations;
    }

    public ArrayList<Pair<String, Integer>> getRequirements() {
        return requirements;
    }

    public void setRequirements(ArrayList<Pair<String, Integer>> requirements) {
        this.requirements = requirements;
    }

    public boolean isLearnt() {
        return learnt;
    }

    public void setLearnt(boolean learnt) {
        this.learnt = learnt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public ArrayList<Pair<String, Integer>> getByProducts() {
        return byProducts;
    }

    public void setByProducts(ArrayList<Pair<String, Integer>> byProducts) {
        this.byProducts = byProducts;
    }

    public Image getTemplate_texture() {
        return template_texture;
    }

    public void setTemplate_texture(Image template_texture) {
        this.template_texture = template_texture;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }
    
    
    
    
}
