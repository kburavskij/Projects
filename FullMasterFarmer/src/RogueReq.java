import org.quantumbot.api.QuantumBot;
import org.quantumbot.enums.EquipmentSlot;
import org.quantumbot.events.BotEvent;
import org.quantumbot.events.WorldHopEvent;
import org.quantumbot.events.containers.BankEvent;
import org.quantumbot.events.containers.EquipLoadoutEvent;
import org.quantumbot.events.containers.EquipmentLoadout;
import org.quantumbot.interfaces.Logger;
import org.quantumbot.utils.StringUtils;

public class RogueReq extends BotEvent {

    public RogueReq(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);
//        String[] gloryString = StringUtils.expandItemName("Amulet of glory(1~6)").toArray(new String[0]);
//        String[] wealthString = StringUtils.expandItemName("Ring of wealth (1~5)").toArray(new String[0]);
        reqSet = new EquipmentLoadout()
                .set(EquipmentSlot.WEAPON)
                .set(EquipmentSlot.RING)
                .set(EquipmentSlot.AMMO)
                .set(EquipmentSlot.CAPE)
                .set(EquipmentSlot.SHIELD)
                .set(EquipmentSlot.NECK, "Dodgy necklace")
                .set(EquipmentSlot.HEAD, "Rogue mask")
                .set(EquipmentSlot.TORSO, "Rogue top")
                .set(EquipmentSlot.LEGS, "Rogue trousers")
                .set(EquipmentSlot.FEET, "Rogue boots")
                .set(EquipmentSlot.HANDS, "Rogue gloves");
        reqWithdrawEvent = new BankEvent(getBot()).addReq(reqSet);
        reqLoadoutEvent = new EquipLoadoutEvent(getBot(),reqSet);
    }

    public static final Logger logger = Logger.get(RogueReq.class);

    private EquipmentLoadout reqSet;
    private EquipLoadoutEvent reqLoadoutEvent;
    private BankEvent reqWithdrawEvent;

    @Override
    public void step() throws InterruptedException {


        if (reqWithdrawEvent.isPendingOperation()){
            reqWithdrawEvent.execute();
        }else if(!getBot().getEquipment().isOnlyWearing(reqSet)){
            reqLoadoutEvent.execute();
            sleepUntil(5000, ()-> getBot().getEquipment().isOnlyWearing(reqSet));
        }else if(getBot().getEquipment().isOnlyWearing(reqSet)){
            setComplete();
        }

    }
}



