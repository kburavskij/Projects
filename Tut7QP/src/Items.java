import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.map.Tile;
import org.quantumbot.enums.Quest;
import org.quantumbot.events.BotEvent;
import org.quantumbot.events.WebWalkEvent;
import org.quantumbot.events.interactions.GroundItemInteractEvent;

public class Items extends BotEvent {

    public Items(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);
    }

    Tile cooksRoom = new Tile(3210, 3215,0);
    Tile cooksBasement = new Tile(3216, 9624,0);

    boolean cooksAssistantStarted = getBot().getQuests().isStarted(Quest.COOKS_ASSISTANT);

    @Override
    public void step() throws InterruptedException {

        if(!getBot().getInventory().contains("Bucket", "Pot") && !cooksAssistantStarted){
            if(!getBot().getInventory().contains("Bucket")){
                new WebWalkEvent(getBot(), cooksBasement)
                        .then(new GroundItemInteractEvent(getBot(), "Bucket", "Take"))
                        .execute();
                sleepAnimating(2769, 5705);
            }if(!getBot().getInventory().contains("Pot")){
                new WebWalkEvent(getBot(), cooksRoom)
                        .then(new GroundItemInteractEvent(getBot(), "Pot", "Take"))
                        .execute();
                sleepAnimating(2769, 5705);
            }
        } else if(getBot().getInventory().contains("Bucket", "Pot") ){
            setComplete();
        }
        }
}
