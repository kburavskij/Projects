import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.entities.*;
import org.quantumbot.api.map.Tile;
import org.quantumbot.enums.Quest;
import org.quantumbot.events.BotEvent;
import org.quantumbot.events.DialogueEvent;
import org.quantumbot.events.DropItemsEvent;
import org.quantumbot.events.WebWalkEvent;
import org.quantumbot.events.containers.InventoryInteractEvent;
import org.quantumbot.events.interactions.*;

public class CooksSteps extends BotEvent {

    public CooksSteps(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);
    }

    @Override
    public void step() throws InterruptedException {


        Player me = getBot().getPlayers().getLocal();

        Entity hopperControls = getBot().getGameObjects().closest("Hopper controls");
        GameObject flourBin = getBot().getGameObjects().closest("Flour bin");
        Entity egg = getBot().getGroundItems().closest("Egg");
        InteractableObject cow = (InteractableObject) getBot().getGameObjects().closest("Dairy cow");
        InteractableObject wheat = (InteractableObject) getBot().getGameObjects().closest("Wheat");

        Tile cooksRoom = new Tile(3210, 3215, 0);

        Tile dairyCowTile = new Tile(3173, 3317, 0);
        Tile eggTile = new Tile(3174, 3302, 0);
        Tile wheatTile = new Tile(3162, 3292, 0);
        Tile putGrainTile = new Tile(3165, 3306, 2);
        Tile takeFlourTile = new Tile(3165, 3307, 0);

        boolean cooksAssistantStarted = getBot().getQuests().isStarted(Quest.COOKS_ASSISTANT);
        boolean cooksAssistant = getBot().getQuests().isComplete(Quest.COOKS_ASSISTANT);


        if (!cooksAssistant && !cooksAssistantStarted && !getBot().getInventory().contains("Bucket of milk", "Pot of flour", "Egg")) {
            new WebWalkEvent(getBot(), cooksRoom)
                    .then(new NPCInteractEvent(getBot(), "Cook", "Talk-to"))
                    .execute();
            sleepAnimating(2769, 5705);
            if (getBot().getDialogues().inDialogue()) {
                new DialogueEvent(getBot(), "What's wrong?", "I'm always happy to help a cook in distress.", "Actually, I know where to find this stuff.").execute();
                new DialogueEvent(getBot()).execute();
            }
        }

        if (!cooksAssistant && cooksAssistantStarted) {
            if (!getBot().getInventory().contains("Egg") && !getBot().getInventory().contains("Bucket of milk") && !getBot().getInventory().contains("Pot of flour")) {
                if(eggTile.distance(me) > 10){
                    new WebWalkEvent(getBot(), eggTile).execute();
                    sleepMoving(7982);
                } else if (eggTile.distance(me) <= 10 && egg!=null){
                    new EntityInteractEvent(getBot(), egg, "Take").execute();
                }
            } else if (getBot().getInventory().contains("Egg") && !getBot().getInventory().contains("Bucket of milk") && !getBot().getInventory().contains("Pot of flour")) {
                if(dairyCowTile.distance(me) > 10){
                    new WebWalkEvent(getBot(), dairyCowTile).execute();
                    sleepMoving(7982);
                } else if (dairyCowTile.distance(me) <= 10 && cow!=null){
                    new InteractEvent(getBot(), cow, "Milk").execute();
                    sleep(2549);
                    sleepAnimating(2769, 5705);
                }
            } else if (!getBot().getGameObjects().contains(x->x.hasName("Flour bin") && x.hasAction("Empty")) && getBot().getInventory().contains("Egg") && getBot().getInventory().contains("Bucket of milk") && !getBot().getInventory().contains("Pot of flour")) {
                if (getBot().getInventory().getAmount("Grain") <= 1 && !getBot().getInventory().contains("Grain") && !getBot().getInventory().contains("Pot of flour")) {
                    new WebWalkEvent(getBot(), wheatTile)
                            .then(new EntityInteractEvent(getBot(), wheat, "Pick"))
                            .execute();
                    sleepAnimating(2769, 5705);
                } else if (getBot().getInventory().contains("Grain") && getBot().getWidgets().contains(x->x.containsText("You pick some grain.")) && !getBot().getWidgets().contains(v->!v.containsText("There is already grain in the hopper."))) {
                    new WebWalkEvent(getBot(), putGrainTile)
                            .then(new InventoryInteractEvent(getBot(), "Grain", "Use"))
                            .execute();
                    new ObjectInteractEvent(getBot(), "Hopper").execute();
                    sleepMoving(7982);
                    sleep(2678);
                } else if (getBot().getWidgets().contains(x->x.containsText("You put the grain in the hopper.") && !x.containsText("You operate the hopper. The grain slides down the chute.") && !x.containsText("There is already grain in the hopper."))) {
                            new EntityInteractEvent(getBot(), hopperControls, "Operate")
                            .execute();
                    sleepAnimating(2769, 5705);
                } else if (getBot().getGameObjects().contains(x->x.hasName("Flour bin") && x.hasAction("Empty")) || getBot().getWidgets().contains(x->x.containsText("You operate the hopper. The grain slides down the chute.") || x.containsText("There is already grain in the hopper."))) {
                    new WebWalkEvent(getBot(), takeFlourTile)
                            .then(new EntityInteractEvent(getBot(), flourBin, "Empty"))
                            .execute();
                    sleepAnimating(2769, 5705);
                }
            } else if (getBot().getInventory().contains("Egg") && getBot().getInventory().contains("Bucket of milk") && getBot().getInventory().contains("Pot of flour")) {
                new WebWalkEvent(getBot(), cooksRoom)
                        .then(new NPCInteractEvent(getBot(), "Cook", "Talk-to"))
                        .execute();
                sleepAnimating(2769, 5705);
                if (getBot().getDialogues().inDialogue()) {
                    new DialogueEvent(getBot()).execute();
                }
            }
        } else if (cooksAssistant) {
            setComplete();
        }
    }
}
