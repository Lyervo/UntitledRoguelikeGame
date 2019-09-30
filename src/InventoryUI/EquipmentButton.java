/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InventoryUI;

import UI.Button;
import World.World;
import org.newdawn.slick.Image;

/**
 *
 * @author Timot
 */
public class EquipmentButton extends Button
{

    public EquipmentButton(int x, int y, Image texture)
    {
        super(x, y, texture);
    }

    @Override
    public void onClick(boolean[] m, World world)
    {
        world.getEquipmentWindow().setDisplay();
    }
    
}
