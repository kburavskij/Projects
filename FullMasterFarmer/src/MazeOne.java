import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.entities.NPC;
import org.quantumbot.api.entities.Player;
import org.quantumbot.api.map.Area;
import org.quantumbot.api.map.Tile;
import org.quantumbot.events.BotEvent;
import org.quantumbot.events.DialogueEvent;
import org.quantumbot.events.WalkEvent;
import org.quantumbot.events.containers.InventoryInteractEvent;
import org.quantumbot.events.interactions.*;
import org.quantumbot.interfaces.Logger;


public class MazeOne extends BotEvent {

    public MazeOne(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);
    }

    Tile startDoor = new Tile(3056, 4991, 1);
    Area roguesStartArea = new Area(3039, 4965, 3064, 4990, 1);
    NPC pets = getBot().getNPCs().closest(npc -> npc.hasName("Rocky") || npc.hasName("Giant Squirrel"));

    public static final Logger logger = Logger.get(MazeOne.class);

    @Override
    public void step() throws InterruptedException {

        Player me = getBot().getPlayers().getLocal();

        if(getBot().getWidgets().contains(widget -> widget.containsText("Sorry but I can't allow you to take your follower in"))){
            new InteractEvent(getBot(), pets, "Pick-up").execute();
        }
            if(!startDoor.equals(me) && getBot().getInventory().isEmpty() && getBot().getSettings().getRunEnergy()>= 90){
            logger.debug("Inside the zone MazeOne");
            new WalkEvent(getBot(), startDoor).execute();
        } else if (startDoor.distance(me)<=2 && getBot().getInventory().isEmpty() && getBot().getSettings().getRunEnergy()>= 90){
            if(getBot().getWidgets().contains(widget -> widget.containsText("And where do you think you're going?"))) {
                new FirstTimeRogues(getBot(),true).execute();
            }else if(getBot().getInventory().isEmpty() && getBot().getSettings().getRunEnergy()>= 90){
                new ObjectInteractEvent(getBot(), "Doorway", "Open").execute();
            }
            }else if (new Tile(3056,4992,1).equals(me)){
            new WalkEvent(getBot(),new Tile(3051, 4997,1)).execute();
        }else if (new Tile(3051,4997,1).equals(me)){
            new ObjectInteractEvent(getBot(),"Contortion Bars", "Enter").execute();
        }else if (new Tile(3048,4997,1).equals(me)){
            new WalkEvent(getBot(),new Tile(3039, 4997,1)).execute();
        }else if (new Tile(3039,4997,1).equals(me)){
            new WalkEvent(getBot(),new Tile(3037, 5002,1)).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(3037,5002,1).equals(me)){
            new WalkEvent(getBot(),new Tile(3028, 5002,1)).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(3028,5002,1).equals(me)){
            new WalkEvent(getBot(),new Tile(3025, 5001,1)).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(3025,5001,1).equals(me)){
            new ObjectInteractEvent(getBot(),"Grill", "Open").execute();
        }else if (new Tile(3023,5001,1).equals(me)){
            new WalkEvent(getBot(),new Tile(3014, 5003,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(3014,5003,1).equals(me)){
            new WalkEvent(getBot(),new Tile(3012, 5001,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        } else if (new Tile(3012,5001,1).equals(me)){
            new WalkEvent(getBot(),new Tile(3009, 5003,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(3009,5003,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2994, 5004,1)).execute();
        }else if (new Tile(2994,5004,1).equals(me)){
            new ObjectInteractEvent(getBot(),"Ledge", "Climb").execute();
        }else if (new Tile(2988,5004,1).equals(me) || new Tile(2988,5005,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2970, 5018,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(2970,5018,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2969, 5018,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(2967,5018,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2958, 5030,1)).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(2959,5028,1).equals(me) || new Tile(2958,5028,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2958, 5030,1)).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(2958,5030,1).equals(me)){
            new ObjectInteractEvent(getBot(),"Ledge", "Climb").execute();
        }else if (new Tile(2958,5035,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2962, 5046,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(2962,5046,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2962, 5054,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        } else if (new Tile(2962,5054,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2965, 5057,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(2965,5057,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2958, 5063,1)).execute();
        }else if (new Tile(2958,5063,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2957, 5067,1)).execute();
        } else if (new Tile(2957,5067,1).equals(me)){
            new ObjectInteractEvent(getBot(),"Passageway", "Enter").execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(2957,5072,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2957, 5076,1)).execute();
        }else if (new Tile(2957,5076,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2956, 5087,1)).execute();
        }else if (new Tile(2956,5087,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2955, 5094,1)).execute();
        } else if (new Tile(2955,5094,1).equals(me)){
            new ObjectInteractEvent(getBot(),"Passageway", "Enter").execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(2955,5098,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2963, 5105,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(2963,5105,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2963, 5101,1)).setAccuracy(0).execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if (new Tile(2963,5101,1).equals(me)){
            new WalkEvent(getBot(),new Tile(2972, 5098,1)).execute();
            sleep(2000);
            new ObjectInteractEvent(getBot(), "Passageway","Enter").execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
            sleep(2000);
            new ObjectInteractEvent(getBot(),"Grill", "Open").execute();
            sleepDuring(2000, ()-> me.isMoving() && me.isAnimating());
        }else if(new Tile(2972, 5093,1).equals(me)){
            logger.debug("MazeTwo");
            new MazeTwo(getBot(),true).execute();
        }else if((!getBot().getInventory().isEmpty() || getBot().getSettings().getRunEnergy()<= 85) && roguesStartArea.contains(me)){
            setComplete();
        }

    }
}



