import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.entities.NPC;
import org.quantumbot.api.entities.Player;
import org.quantumbot.api.map.Area;
import org.quantumbot.api.map.Tile;
import org.quantumbot.enums.Skill;
import org.quantumbot.events.*;
import org.quantumbot.events.containers.BankOpenEvent;
import org.quantumbot.events.ge.GEEvent;
import org.quantumbot.events.interactions.NPCInteractEvent;
import org.quantumbot.events.interactions.ObjectInteractEvent;
import org.quantumbot.interfaces.Logger;


public class FirstTimeRogues extends BotEvent {

    public FirstTimeRogues(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);
    }


    public static final Logger logger = Logger.get(Prepare.class);


    @Override
    public void step() throws InterruptedException {

        Player me = getBot().getPlayers().getLocal();

        Area roguesStartArea = new Area(3039, 4965, 3064, 4991, 1);
        Tile startDoor = new Tile(3056, 4991, 1);


        if(startDoor.equals(me)) {
            new WalkEvent(getBot(), roguesStartArea.getAvgCenterTile()).execute();
            sleep(2000);
        }else if(roguesStartArea.contains(me)){
            if(getBot().getWidgets().contains(widget -> widget.containsText("Hi again, what can I do for you?") || widget.containsText("Oh one last thing, if you happen to see my harmonica"))) {
                setComplete();
            }
            new NPCInteractEvent(getBot(), 3189).execute();
            sleep(2000);
            if (getBot().getDialogues().inDialogue()) {
                if(getBot().getWidgets().contains(widget -> widget.containsText("Hi again, what can I do for you?") || widget.containsText("Oh one last thing, if you happen to see my harmonica"))) {
                    new WalkEvent(getBot(), startDoor).execute();
                    setComplete();
                }
                new DialogueEvent(getBot(), "Yes actually, what've you got?", "Ok that sounds good!").execute();
                new DialogueEvent(getBot()).execute();
            }else if(getBot().getWidgets().contains(widget -> widget.containsText("Hi again, what can I do for you?") || widget.containsText("Oh one last thing, if you happen to see my harmonica"))) {
                new WalkEvent(getBot(), startDoor).execute();
                setComplete();
            }
        }

    }
    }
