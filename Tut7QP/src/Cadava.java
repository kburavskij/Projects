import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.entities.Entity;
import org.quantumbot.api.entities.Player;
import org.quantumbot.api.map.Area;
import org.quantumbot.enums.Quest;
import org.quantumbot.events.BotEvent;
import org.quantumbot.events.WebWalkEvent;
import org.quantumbot.events.interactions.EntityInteractEvent;

public class Cadava extends BotEvent {

    public Cadava(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);
    }


    Player me = getBot().getPlayers().getLocal();
    Area berries = new Area(3263, 3365, 3272, 3370);
    Area romeoArea = new Area(3206, 3432, 3218, 3422);

    Entity berryBush = getBot().getGameObjects().closest(23625, 23626);
    boolean romeoJuliet = getBot().getQuests().isStarted(Quest.ROMEO_JULIET);

    @Override
    public void step() throws InterruptedException {


        if (!romeoJuliet && !getBot().getInventory().contains("Cadava berries")) {
            if (!getBot().getInventory().contains("Cadava berries") && !berries.contains(me)) {
                new WebWalkEvent(getBot(), berries.getRandomTile()).execute();
                sleepMoving(4897);
            } else if (!getBot().getInventory().contains("Cadava berries") && berryBush.distance()<10 && berryBush!=null ) {
                new EntityInteractEvent(getBot(), berryBush).execute();
                sleepAnimating(3486, 9772);
            } else if (getBot().getInventory().contains("Cadava berries") && romeoArea.contains(me)) {
                setComplete();
            }
        } else if (!romeoJuliet && getBot().getInventory().contains("Cadava berries")) {
            new WebWalkEvent(getBot(), romeoArea.getRandomTile()).execute();
            sleepMoving(14897);
            setComplete();
        }else if (romeoJuliet){
            setComplete();
        }

    }
}
