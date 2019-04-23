import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.containers.Bank;
import org.quantumbot.api.entities.Player;
import org.quantumbot.api.map.Area;
import org.quantumbot.api.map.Tile;
import org.quantumbot.enums.EquipmentSlot;
import org.quantumbot.events.BotEvent;
import org.quantumbot.events.CloseInterfacesEvent;
import org.quantumbot.events.DialogueEvent;
import org.quantumbot.events.WalkEvent;
import org.quantumbot.events.containers.*;
import org.quantumbot.interfaces.Logger;


public class Prepare extends BotEvent {



    public Prepare(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);
        emptyReq = new EquipmentLoadout();
        emptyReqLoadout = new EquipLoadoutEvent(getBot(),emptyReq).setStrict(true);
        emptyReqBank = new BankEvent(getBot()).addReq(emptyReq).setStrictEQ(true);
    }

    public static final Logger logger = Logger.get(Prepare.class);
    Area roguesStartArea = new Area(3039, 4965, 3064, 4991, 1);

    private EquipmentLoadout emptyReq;
    private EquipLoadoutEvent emptyReqLoadout;
    private BankEvent emptyReqBank;

    @Override
    public void step() throws InterruptedException {

        Player me;
        me = getBot().getPlayers().getLocal();



        if(getBot().getOwnedItems().getAmount("Rogue's equipment crate")<=4){
            logger.debug("Finding oportunities");
            if(roguesStartArea.contains(me) && getBot().getSettings().getRunEnergy()<=90 && !getBot().getInventory().contains("Stamina potion(4~1)") && getBot().getOwnedItems().contains("Stamina potion(4~1)")){
                logger.debug("Getting staminas");
                if(getBot().getOwnedItems().contains("Stamina potion(4~1)")) {
                    new BankEvent(getBot())
                            .addReq(2, "Stamina potion(4~1)")
                            .execute();
                    if (getBot().getBank().isDepositOpen()) {
                        new CloseInterfacesEvent(getBot()).execute();
                    }
                }
            }else if(getBot().getInventory().contains("Stamina potion(4~1)") && getBot().getSettings().getRunEnergy()<=91){
                logger.debug("Drinking pots");
                new InventoryInteractEvent(getBot(), "Stamina potion(4~1)", "Drink").execute();
                    sleep(1500);
            }else if(!getBot().getInventory().isEmpty()){
                logger.debug("Emptying inventory");
                new BankEvent(getBot())
                        .setDepositCondition(() -> true)
                        .execute();
                if(getBot().getBank().isDepositOpen()) {
                    new CloseInterfacesEvent(getBot()).execute();
                }
            }else if (!getBot().getOwnedItems().contains("Stamina potion(4~1)")){
                new RoguesDenEvent(getBot(),true).execute();
            }else if(roguesStartArea.contains(me) && getBot().getEquipment().isOnlyWearing(emptyReq) && getBot().getSettings().getRunEnergy()>=91 && getBot().getInventory().isEmpty()){
                logger.debug("Entering Maze");
                new MazeOne(getBot(),true).execute();
            }else if (roguesStartArea.contains(me) && !getBot().getEquipment().isOnlyWearing(emptyReq)){
                if(emptyReqBank.isPendingOperation()){
                    emptyReqBank.execute();
                }else if(!getBot().getEquipment().isOnlyWearing(emptyReq)) {
                    emptyReqLoadout.execute();
                }
            }
        }else if(getBot().getOwnedItems().getAmount("Rogue's equipment crate")>=5){
            setComplete();
        }

        if(getBot().getInventory().contains("Mystic jewel")){
            new CacheEvent(getBot(), true).execute();
        }

    }
}

