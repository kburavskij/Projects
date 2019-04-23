import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.entities.NPC;
import org.quantumbot.api.entities.Player;
import org.quantumbot.api.map.Area;
import org.quantumbot.api.map.Tile;
import org.quantumbot.events.BotEvent;
import org.quantumbot.events.CloseInterfacesEvent;
import org.quantumbot.events.DialogueEvent;
import org.quantumbot.events.WalkEvent;
import org.quantumbot.events.containers.BankOpenEvent;
import org.quantumbot.events.containers.InventoryInteractEvent;
import org.quantumbot.events.interactions.NPCInteractEvent;
import org.quantumbot.events.interactions.ObjectInteractEvent;
import org.quantumbot.interfaces.Logger;


public class CacheEvent extends BotEvent {

    public CacheEvent(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);
    }


    public static final Logger logger = Logger.get(CacheEvent.class);


    @Override
    public void step() throws InterruptedException {

        Player me = getBot().getPlayers().getLocal();

        Area roguesStartArea = new Area(3039, 4965, 3064, 4991, 1);
        Tile startDoor = new Tile(3056, 4991, 1);


        if(getBot().getInventory().contains("Mystic jewel")) {
            new InventoryInteractEvent(getBot(), "Mystic jewel", "Activate").execute();
            if (getBot().getDialogues().inDialogue()) {
                new DialogueEvent(getBot(), "Yes I'd like to leave!").execute();
                new DialogueEvent(getBot()).execute();
            }
        }else if(roguesStartArea.contains(me) && !getBot().getBank().isCached()) {
            new WalkEvent(getBot(), new Tile(3040, 4969, 1)).execute();
            if (new Tile(3040, 4969, 1).equals(me)) {
                new ObjectInteractEvent(getBot(), "Bank chest", "Use").execute();
                sleep(2500);
            }
            if(getBot().getBank().isOpen()) {
                sleep(2500);
                new CloseInterfacesEvent(getBot()).execute();
            }
            if (getBot().getDialogues().inDialogue()) {
                new DialogueEvent(getBot()).execute();
            }
        }else if(!getBot().getBank().isCached()) {
            if(!getBot().getBank().isOpen()) {
                new BankOpenEvent(getBot()).execute();
                sleep(2500);
            }
            if(getBot().getBank().isOpen()) {
                sleep(2500);
                new CloseInterfacesEvent(getBot()).execute();
            }
        }else if(getBot().getBank().isCached()) {
                setComplete();
            }
        }

    }
