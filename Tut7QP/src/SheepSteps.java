import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.entities.Entity;
import org.quantumbot.api.entities.GroundItem;
import org.quantumbot.api.entities.NPC;
import org.quantumbot.api.entities.Player;
import org.quantumbot.api.map.Area;
import org.quantumbot.api.map.Tile;
import org.quantumbot.enums.Quest;
import org.quantumbot.events.BotEvent;
import org.quantumbot.events.DialogueEvent;
import org.quantumbot.events.MakeXEvent;
import org.quantumbot.events.WebWalkEvent;
import org.quantumbot.events.interactions.EntityInteractEvent;
import org.quantumbot.events.interactions.GroundItemInteractEvent;
import org.quantumbot.events.interactions.InteractEvent;
import org.quantumbot.events.interactions.NPCInteractEvent;

public class SheepSteps extends BotEvent {

    public SheepSteps(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);
    }

    Tile farmerBarn = new Tile(3191, 3272, 0);
    Area sheepArea = new Area(3193, 3275, 3211, 3259);
    Tile spinRoom = new Tile(3209, 3213,1);

    Player me = getBot().getPlayers().getLocal();

    Entity shears = getBot().getGroundItems().closest("Shears");
    Entity sheep = getBot().getNPCs().nearby(x->x.hasAction("Shear") && x.distance(me) > 2 && x.hasId(2800) || x.hasId(2801));
    Entity spinningWheel = getBot().getGameObjects().nearby(x->x.hasAction("Spin") && x.hasName("Spinning wheel"));

    boolean sheepShearStarted = getBot().getQuests().isStarted(Quest.SHEEP_SHEARER);
    boolean sheepShear = getBot().getQuests().isComplete(Quest.SHEEP_SHEARER);

    private boolean isSheep(NPC npc) {
        return npc.hasName("Sheep") && npc.hasAction("Shear") && !npc.hasAction("Talk-to");
    }
    @Override
    public void step() throws InterruptedException {

        if (!sheepShear && !sheepShearStarted && !getBot().getInventory().contains("Shears") && !getBot().getInventory().contains("Ball of wool")
        ) {
            if (!getBot().getInventory().contains("Shears") && shears!=null) {
                new WebWalkEvent(getBot(), farmerBarn)
                        .then(new GroundItemInteractEvent(getBot(), "Shears", "Take"))
                        .execute();
                sleepAnimating(2769, 5705);
            } else if (getBot().getInventory().contains("Shears")) {
                new WebWalkEvent(getBot(), farmerBarn)
                        .then(new NPCInteractEvent(getBot(), "Fred the Farmer", "Talk-to"))
                        .execute();
                sleepAnimating(2769, 5705);
                if (getBot().getDialogues().inDialogue()) {
                    new DialogueEvent(getBot(), "I'm looking for a quest.", "Yes okay. I can do that.", "Of course!", "I'm something of an expert actually!").execute();
                    new DialogueEvent(getBot()).execute();
                }
            }
        }else if(!sheepShear && sheepShearStarted && !getBot().getInventory().contains("Shears") && shears!=null && getBot().getInventory().getAmount("Wool") <= 19 && !getBot().getInventory().contains("Ball of wool")){
            new WebWalkEvent(getBot(), farmerBarn)
                    .then(new GroundItemInteractEvent(getBot(), "Shears", "Take"))
                    .execute();
            sleepAnimating(2769, 5705);
        }else if(!sheepShear && sheepShearStarted && getBot().getInventory().contains("Shears") && !sheepArea.contains(me) && getBot().getInventory().getAmount("Wool") <= 19 && !getBot().getInventory().contains("Ball of wool")){
            new WebWalkEvent(getBot(),sheepArea.getRandomTile()).execute();
            sleepAnimating(2769, 5705);
        }else if(!sheepShear && sheepShearStarted && getBot().getInventory().contains("Shears") && sheepArea.contains(me) && getBot().getInventory().getAmount("Wool") <= 19 && !getBot().getInventory().contains("Ball of wool")){
            if (new NPCInteractEvent(getBot(), this::isSheep, "Shear").executed()) {
                if (sleepUntil(4000, () -> me.isAnimating() || !me.isMoving())) {
                    sleep(600);
                }
            }
        }else if(!sheepShear && me.distance(spinRoom) > 3 && sheepShearStarted && getBot().getInventory().getAmount("Wool") >= 20 && !getBot().getInventory().contains("Ball of wool")){
            new WebWalkEvent(getBot(), spinRoom).execute();
            sleepAnimating(2769, 5705);
        }else if(!sheepShear && me.distance(spinRoom) < 3 && sheepShearStarted && getBot().getInventory().getAmount("Wool") >= 1 && getBot().getInventory().getAmount("Ball of wool") <= 19) {
            new EntityInteractEvent(getBot(), spinningWheel)
                    .then(new MakeXEvent(getBot(), 1))
                    .execute();
//            sleepAnimating(2769, 5705);
            sleepUntil(20000, () -> me.isAnimating() || !me.isMoving());
        }else if(!sheepShear && sheepShearStarted && getBot().getInventory().getAmount("Ball of wool") >=20){
            new WebWalkEvent(getBot(), farmerBarn)
                    .then(new NPCInteractEvent(getBot(), "Fred the Farmer", "Talk-to"))
                    .execute();
            sleepAnimating(2769, 5705);
            if (getBot().getDialogues().inDialogue()) {
                new DialogueEvent(getBot()).execute();
            }
        } else if (sheepShear){
            setComplete();
        }
    }
}