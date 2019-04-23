import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.entities.Entity;
import org.quantumbot.api.entities.Player;
import org.quantumbot.api.enums.Bank;
import org.quantumbot.api.map.Area;
import org.quantumbot.api.map.Tile;
import org.quantumbot.enums.Skill;
import org.quantumbot.events.*;
import org.quantumbot.events.containers.BankEvent;
import org.quantumbot.events.containers.InventoryInteractEvent;
import org.quantumbot.events.interactions.ObjectInteractEvent;
import org.quantumbot.interfaces.Logger;

public class TeaThieverEvent extends BotEvent {

    public TeaThieverEvent(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);
        emptyInv = new BankEvent(getBot()).setDepositCondition(() -> true);
    }

    Player me = getBot().getPlayers().getLocal();
    Tile teaStallTile = new Tile(3268, 3410, 0);
    Entity teaStall = getBot().getGameObjects().closest(o -> o.hasId(635));
    Area teaStallArea = new Area(3272, 3416, 3266, 3408);

    public static final Logger logger = Logger.get(TeaThieverEvent.class);
    private BankEvent emptyInv;

    @Override
    public void onStart() {
        super.onStart();
        new WorldHopEvent(getBot(), world -> world.isMembers() && world.isStandard());
    }

    @Override
    public void step() throws InterruptedException {

        logger.debug("Started tea");

        if(!getBot().getWorlds().getCurrentWorld().isStandard() || !getBot().getWorlds().getCurrentWorld().isMembers()){
            new WorldHopEvent(getBot(), world -> world.isMembers() && world.isStandard()).execute();
        }

        if (getBot().getClient().getSkillReal(Skill.THIEVING) <= 37) {

            if (getBot().getInventory().getAmount(22521) >= 28) {
                new InventoryInteractEvent(getBot(), d -> d.hasId(22521), "Open-all").execute();
            }


            if (!teaStallTile.equals(me)) {
                new WebWalkEvent(getBot(), teaStallTile).execute();
            }else if((getBot().getInventory().isFull() && getBot().getInventory().getAmount("Cup of tea")<=27) && (getBot().getInventory().contains("Jug of wine") || getBot().getInventory().contains("Stamina potion") || getBot().getInventory().contains("Ring of wealth") || getBot().getInventory().contains("Games necklace") || getBot().getInventory().contains("Amulet of glory") || getBot().getInventory().contains("Coins"))){
                emptyInv.execute();
            }else if(getBot().getInventory().getAmount("Cup of tea")>=24){
                new DropItemsEvent(getBot(), "Cup of tea").execute();
            }else if(teaStallTile.equals(me) && !me.isAnimating() && !getBot().getInventory().isFull()){
                logger.debug("Trying to pick");
                if(getBot().getPlayers().filter(p -> teaStallArea.contains(p) && !p.isMyself()).size() > 0){
                    logger.debug("Trying to hop");
                    new WorldHopEvent(getBot(), world -> world.isMembers() && world.isStandard()).execute();
                }
                new ObjectInteractEvent(getBot(), 635).executed();

                sleep(5000);
            }
        } else if (getBot().getClient().getSkillReal(Skill.THIEVING) >= 38) {
                setComplete();
        }
    }
}
