import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.entities.*;
import org.quantumbot.api.map.Area;
import org.quantumbot.api.map.Tile;
import org.quantumbot.events.BotEvent;
import org.quantumbot.events.DialogueEvent;
import org.quantumbot.events.WalkEvent;
import org.quantumbot.events.containers.InventoryInteractEvent;
import org.quantumbot.events.interactions.*;
import org.quantumbot.interfaces.Logger;

import java.util.Random;

public class MazeTwo extends BotEvent {

    public MazeTwo(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);
    }

    public static final Logger logger = Logger.get(MazeTwo.class);
    Area roguesStartArea = new Area(3039, 4965, 3064, 4991, 1);
    Tile startDoor = new Tile(3056, 4991, 1);


    @Override
    public void step() throws InterruptedException {

        Player me;
        me = getBot().getPlayers().getLocal();


        if(me.distance(startDoor)<=8){
            setComplete();
        }
        if (new Tile(2972,5093,1).equals(me)){
            logger.debug("MazeTwo");
            new WalkEvent(getBot(),new Tile(2972, 5087,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(2972,5087,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2980, 5086,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(2980,5086,1).equals(me)){
            new ObjectInteractEvent(getBot(),"Ledge", "Climb").execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(2991,5087,1).equals(me) || new Tile(2991,5090,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2992, 5088,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(2992,5088,1).equals(me)){
            new ObjectInteractEvent(getBot(),o->o.hasId(7249) && o.hasName("Wall") && o.hasAction("Search") && o.getTile().equals(new Tile(2993, 5087, 1))).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(2993,5088,1).equals(me)){
            new ObjectInteractEvent(getBot(),o->o.hasId(7249) && o.hasName("Wall") && o.hasAction("Search") && o.getTile().equals(new Tile(2994, 5087, 1))).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(2994,5088,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2998, 5088,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(2998,5088,1).equals(me)){
            new WalkEvent(getBot(),new Tile(3000, 5087,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(3000,5087,1).equals(me)){
            new WalkEvent(getBot(),new Tile(3001, 5087,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(3003,5087,1).equals(me)){
            new WalkEvent(getBot(),new Tile(3017, 5081,1)).execute();
        }else if (new Tile(3017,5081,1).distance(me)<=2 && getBot().getInventory().getAmount("Tile")<=0){
            new GroundItemInteractEvent(getBot(), "Tile", "Take").execute();
        }else if(getBot().getInventory().contains("Tile")){
            new WalkEvent(getBot(),new Tile(3023, 5082,1)).execute();
            if(new ObjectInteractEvent(getBot(),"Door", "Open")
                    .executed()){
                getBot().getMouse().click(430,235);
                sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
            }
        }else if (new Tile(3024,5082,1).equals(me)){
            new ObjectInteractEvent(getBot(),"Grill", "Open").execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Area(3036,5079, 3031,5078,1).contains(me)){
            new ObjectInteractEvent(getBot(),o->o.hasAction("Open") && o.hasName("Grill") && o.getTile().equals(new Tile(3032,5078,1))).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Area(3032,5077,3035,5076,1).contains(me)){
            new ObjectInteractEvent(getBot(),o->o.hasAction("Open") && o.hasName("Grill") && o.getTile().equals(new Tile(3036,5076,1))).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Area(3037,5075,3039,5079,1).contains(me)){
            new ObjectInteractEvent(getBot(),o->o.hasAction("Open") && o.hasName("Grill") && o.getTile().equals(new Tile(3039,5079,1))).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Area(3040,5079,3042,5075,1).contains(me)){
            new ObjectInteractEvent(getBot(),o->o.hasAction("Open") && o.hasName("Grill") && o.getTile().equals(new Tile(3042,5076,1))).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Area(3044,5077,3043,5069,1).contains(me)){
            new ObjectInteractEvent(getBot(),o->o.hasAction("Open") && o.hasName("Grill") && o.getTile().equals(new Tile(3044,5069,1))).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Area(3044,5068,3040,5067,1).contains(me)){
            new ObjectInteractEvent(getBot(),o->o.hasAction("Open") && o.hasName("Grill") && o.getTile().equals(new Tile(3041,5068,1))).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Area(3040,5069,3042,5071,1).contains(me)){
            new ObjectInteractEvent(getBot(),o->o.hasAction("Open") && o.hasName("Grill") && o.getTile().equals(new Tile(3040,5070,1))).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Area(3037,5071,3039,5069,1).contains(me)){
            new ObjectInteractEvent(getBot(),o->o.hasAction("Open") && o.hasName("Grill") && o.getTile().equals(new Tile(3038,5069,1))).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Area(3039,5066,3037,5069,1).contains(me)){
            new WalkEvent(getBot(),new Tile(3028, 5033,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(3028,5033,1).equals(me)){
            new WalkEvent(getBot(),new Tile(3015, 5033,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(3015,5033,1).equals(me)){
            new ObjectInteractEvent(getBot(),"Grill","Open").execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if(new Tile(3014,5033,1).equals(me)){
            logger.debug("MazeThree");
            new MazeThree(getBot(), true).execute();
        }



    }
}



