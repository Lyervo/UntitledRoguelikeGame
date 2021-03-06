/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CraftingUI;

import UI.Button;
import World.World;

import org.newdawn.slick.Image;


/**
 *
 * @author Timot
 */
public class RecipeScrollUpButton extends Button
{

    private CraftingUI craftingUI;

    public RecipeScrollUpButton(int x, int y, Image texture,CraftingUI craftingUI) {
        super(x, y, texture);
        this.craftingUI = craftingUI;
    }
    

    @Override
    public void onClick(boolean[] m, World world)
    {
        craftingUI.scrollUp();
    }
    
}
