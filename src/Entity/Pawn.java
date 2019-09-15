/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Camera.Camera;
import Item.Inventory;
import Item.ItemLibrary;
import World.LocalMap;
import World.Tile;
import World.World;
import java.util.ArrayList;
import java.util.Arrays;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import rlforj.los.IFovAlgorithm;


/**
 *
 * @author Timot
 */
public class Pawn extends Entity
{
    private String name;
    private boolean control;
    private Path path;
    private int step;
    
    private FOVBoard fovBoard;
    
    private Entity target;
    private Task task;
    private AStarPathFinder pf;
    private IFovAlgorithm fov;
    
    private long current;
    
    private Inventory inventory;
    
    private Meter hp;
    
    private ArrayList<Status> status;
    
    
    public Pawn(int id, int x, int y,Image texture,IFovAlgorithm fov,String name,ItemLibrary itemLibrary)
    {
        super(id, x, y,texture);
        hp = new Meter("HP",100,50,0);
        this.fov = fov;
        this.control = false;
        step = 1;
        task = new Task(x,y,-1,-1,0);
        inventory = new Inventory(this,itemLibrary);
        this.name = name;
        current = System.currentTimeMillis();
        status = new ArrayList<Status>();
        
    }
    
    public void initPawn(LocalMap lm,AStarPathFinder pf)
    {
        this.pf = pf;
        fovBoard = new FOVBoard(lm.getTiles());
        if(control)
        {
            fov.visitFieldOfView(lm, x, y, 7);
        }
    }
    
    
    public void pawnAction(boolean[] k,boolean[] m,Input input,World world)
    {
        if (world.isMoved())
        {
            fovBoard.resetVision(x, y, 7);
            fov.visitFieldOfView(fovBoard, x, y, 7);

            if (fovBoard.playerInVision(x, y, 7, world.getWm().getCurrentLocalMap()))
            {
                task = new Task(x, y, -1, -1, 1);
            }

        }

        if (path == null && task.getType() == 1)
        {
            step = 1;
            path = pf.findPath(null, x, y, world.getWm().getCurrentLocalMap().getPawns().get(0).getX(), world.getWm().getCurrentLocalMap().getPawns().get(0).getY());
        }else if (path != null)
        {
            if (step == path.getLength() - 1 || path.getLength() <= 1)
            {
                path = null;
            } else if (world.isMoved()) {
                doPath(world.getWm().getCurrentLocalMap());
            }
        }
    }

    @Override
    public void tick(boolean[] k, boolean[] m,Input input, World world)
    {
        if(control&&!world.isMoved())
        {
            if(path==null)
            {
                if(task.getType()==2)
                {
                    if(world.getWm().getCurrentLocalMap().getItemPileAt(x, y)!=null)
                    {
                        if(world.getWm().getCurrentLocalMap().getItemPileAt(x, y).getId() == task.getId())
                        {
                           
                            world.getAncestor().addText(world.getWm().getCurrentLocalMap().getItemPileAt(x, y).getItems().get(task.getIndex()).getName()+" added to inventory.");
                            
                            world.getWm().getCurrentLocalMap().getItemPileAt(x, y).takeFrom(world.getWm().getPlayerInventory(),task.getIndex(),world.getWm().getCurrentLocalMap(),-1);
                            
                            world.getInventory_ui().refreshInventoryUI(world.getWm().getCurrentLocalMap());
                            task = new Task(x,y,-1,-1,0);

                            world.moved();
                        }
                        
                    }
                }else
                {
                    playerKeyboardControl(k,input,world);
                }
            }else if(m[19]||k[255])
            {
                //k[255] will be true whenever a key is pressed
                task = new Task(x,y,-1,-1,0);
                path = null;
                step = 1;
                current = System.currentTimeMillis();
                
            }
            else
            {
                    if(System.currentTimeMillis()-current>=250)
                    {
                        current = System.currentTimeMillis();
                        doPath(world.getWm().getCurrentLocalMap());
                        world.moved();
                        if(step==path.getLength())
                        {
                            path = null;
                            step = 1;
                        }
                    
                    }
            }
            
            
            
            if(world.isMoved())
            {
                
                for(Tile[] ts:world.getWm().getCurrentLocalMap().getTiles())
                {
                    for(Tile t: ts)
                    {
                        t.resetVisit();
                    }
                }
                
                fov.visitFieldOfView(world.getWm().getCurrentLocalMap(), x, y, 7);
                
            }
            
        }else
        {
            pawnAction(k,m,input,world);
        }
        
        if(world.isMoved())
        {
            
            processStatus(k,m,input,world);
        }
        
    }
    
    public void processStatus(boolean[] k,boolean[] m,Input input,World world)
    {
        for(int i=0;i<status.size();i++)
        {
            if(status.get(i).getEffect().getDuration()>0)
            {
                status.get(i).tick(world, world.getWm().getCurrentLocalMap(),this);
                System.out.println(status.get(i).getEffect().getType());
            }
            if(status.get(i).getEffect().getDuration()==0)
            {
                status.remove(i);
            }
        }
    }
    
    
    public void grabItemAt(int x,int y,int id,int index)
    {
        task = new Task(x,y,id,index,2);
        if(x!=this.x||y!=this.y)
        {
            calcPath(x,y);
        }else
        {
            path = null;
        }
    }
    
    public void checkVision()
    {
        
    }
    
    public void calcPath(int x,int y)
    {
        if(path==null)
        {
            step = 1;
            path = pf.findPath(null,this.x,this.y,x,y);
            if(path==null)
            {
                return;
            }
            
        }
            
    }
    
    
    @Override
    public void render(Camera cam,LocalMap map)
    {
        if(map.getTiles()[y][x].isVisit())
        {
            texture.draw(x*32+cam.getXofs(),y*32+cam.getYofs());
        }
        
    }
    
    
    public void doPath(LocalMap lm)
    {
        if(step<path.getLength())
        {
            if(path.getX(step)>x&&path.getY(step)>y)
            {
                move(1,1,lm);
            }else if(path.getX(step)>x&&path.getY(step)<y)
            {
                move(1,-1,lm);
            }else if(path.getX(step)<x&&path.getY(step)>y)
            {
                move(-1,1,lm);
            }else if(path.getX(step)<x&&path.getY(step)<y)
            {
                move(-1,-1,lm);
            }else if(path.getX(step)>x&&path.getY(step)==y)
            {
                move(1,0,lm);
            }else if(path.getX(step)<x&&path.getY(step)==y)
            {
                move(-1,0,lm);
            }else if(path.getX(step)==x&&path.getY(step)>y)
            {
                move(0,1,lm);
            }else if(path.getX(step)==x&&path.getY(step)<y)
            {
                move(0,-1,lm);
              
            }
            step++;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isControl() {
        return control;
    }

    public void setControl(boolean control) {
        this.control = control;
    }


    public void playerKeyboardControl(boolean[] k,Input input,World world)
    {
        if (k[Input.KEY_A]&&!world.getWm().getCurrentLocalMap().isObstacle(x-1, y)) {
            move(-1, 0,world.getWm().getCurrentLocalMap());
            world.moved();
        } else if (k[Input.KEY_D]&&!world.getWm().getCurrentLocalMap().isObstacle(x+1, y)) {
            move(1, 0,world.getWm().getCurrentLocalMap());
            world.moved();
        } else if (k[Input.KEY_W]&&!world.getWm().getCurrentLocalMap().isObstacle(x, y-1)) {
            move(0, -1,world.getWm().getCurrentLocalMap());
            world.moved();
        } else if (k[Input.KEY_S]&&!world.getWm().getCurrentLocalMap().isObstacle(x, y+1)) {
            move(0, 1,world.getWm().getCurrentLocalMap());
            world.moved();
        }else if(k[Input.KEY_Q]&&!world.getWm().getCurrentLocalMap().isObstacle(x-1, y-1))
        {
            move(-1,-1,world.getWm().getCurrentLocalMap());
            world.moved();
        }else if(k[Input.KEY_E]&&!world.getWm().getCurrentLocalMap().isObstacle(x+1, y-1))
        {
            move(+1,-1,world.getWm().getCurrentLocalMap());
            world.moved();
        }else if(k[Input.KEY_Z]&&!world.getWm().getCurrentLocalMap().isObstacle(x-1, y+1))
        {
            move(-1,+1,world.getWm().getCurrentLocalMap());
            world.moved();
        }else if(k[Input.KEY_C]&&!world.getWm().getCurrentLocalMap().isObstacle(x+1, y+1))
        {
            move(+1,+1,world.getWm().getCurrentLocalMap());
            world.moved();
        }else if(k[Input.KEY_SPACE])
        {
            world.moved();
        }
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public FOVBoard getFovBoard() {
        return fovBoard;
    }

    public void setFovBoard(FOVBoard fovBoard) {
        this.fovBoard = fovBoard;
    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public IFovAlgorithm getFov() {
        return fov;
    }

    public void setFov(IFovAlgorithm fov) {
        this.fov = fov;
    }

    public AStarPathFinder getPf() {
        return pf;
    }

    public void setPf(AStarPathFinder pf) {
        this.pf = pf;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Meter getHp() {
        return hp;
    }

    public void setHp(Meter hp) {
        this.hp = hp;
    }

    public ArrayList<Status> getStatus() {
        return status;
    }

    public void setStatus(ArrayList<Status> status) {
        this.status = status;
    }

    
    
    
    
}
