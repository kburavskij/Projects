import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.entities.NPC;
import org.quantumbot.api.entities.Player;
import org.quantumbot.api.map.Area;
import org.quantumbot.api.map.Tile;
import org.quantumbot.events.BotEvent;
import org.quantumbot.events.DialogueEvent;
import org.quantumbot.events.WalkEvent;
import org.quantumbot.events.containers.InventoryInteractEvent;
import org.quantumbot.events.interactions.GroundItemInteractEvent;
import org.quantumbot.events.interactions.NPCInteractEvent;
import org.quantumbot.events.interactions.ObjectInteractEvent;
import org.quantumbot.interfaces.Logger;

import java.util.Random;

public class MazeThree extends BotEvent {

    public MazeThree(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);
    }

    public static final Logger logger = Logger.get(MazeTwo.class);
    Area roguesStartArea = new Area(3039, 4965, 3064, 4991, 1);
    Tile startDoor = new Tile(3056, 4991, 1);

    @Override
    public void step() throws InterruptedException {


        Player me;
        me = getBot().getPlayers().getLocal();



        if (new Tile(3014,5033,1).equals(me)){
            logger.debug("MazeThree");
            new WalkEvent(getBot(),new Tile(3010, 5033,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(3010,5033,1).equals(me)){
            new ObjectInteractEvent(getBot(),"Grill","Open").execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(3009,5033,1).equals(me)){
            new WalkEvent(getBot(),new Tile(3008, 5033,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(3008,5033,1).equals(me)){
            new WalkEvent(getBot(),new Tile(3004, 5033,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(3004,5033,1).equals(me)){
            new WalkEvent(getBot(),new Tile(3000, 5034,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(3000,5034,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2998, 5049,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        } else if (new Tile(2998,5049,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2998, 5053,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(2998,5053,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2992, 5067,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(2992,5067,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2992, 5071,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(2992,5071,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2992, 5075,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(2992,5075,1).equals(me)){
            new WalkEvent(getBot(),new Tile(3008, 5070,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(3008,5070,1).equals(me)){
            new GroundItemInteractEvent(getBot(),i->i.hasAction("Take") && i.hasName("Flash powder") && i.getTile().equals(new Tile(3009,5070, 1))).execute();
        }else if(getBot().getInventory().contains("Flash powder") && me.distance(new Tile(3008,5070,1)) <= 10){
            if(new InventoryInteractEvent(getBot(),"Flash powder", "Use")
                    .then(new NPCInteractEvent(getBot(),npc -> npc.hasName("Rogue Guard") && npc.distance(new Tile(3017,5060,1))<=7))
                    .executed()){
                new WalkEvent(getBot(), new Tile(3028,5055,1)).execute();
            }
        }else if (new Tile(3028,5055,1).equals(me)){
            new WalkEvent(getBot(),new Tile(3028, 5051,1)).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(3028,5051,1).equals(me)){
            new WalkEvent(getBot(),new Tile(3028, 5047,1)).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        } else if (new Tile(3028,5047,1).equals(me)){
            logger.debug("Finishing with CRACK");
            new ObjectInteractEvent(getBot(),"Wall safe","Crack").execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }
        if(me.distance(startDoor)<=8){
            logger.debug("Completing Maze 3");
            setComplete();
        }

    }
}



