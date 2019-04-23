import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.entities.NPC;
import org.quantumbot.api.entities.Player;
import org.quantumbot.api.enums.Bank;
import org.quantumbot.api.map.Area;
import org.quantumbot.api.map.Tile;
import org.quantumbot.enums.EquipmentSlot;
import org.quantumbot.enums.Skill;
import org.quantumbot.events.*;
import org.quantumbot.events.containers.*;
import org.quantumbot.events.ge.GEEvent;
import org.quantumbot.events.interactions.InteractEvent;
import org.quantumbot.events.interactions.NPCInteractEvent;
import org.quantumbot.interfaces.Logger;
import org.quantumbot.interfaces.Message;
import org.quantumbot.interfaces.Painter;
import org.quantumbot.listeners.MessageListener;
import org.quantumbot.utils.StringUtils;
import org.quantumbot.utils.Timer;

import java.awt.*;
import java.util.Random;


public class MasterFarmerEvent extends BotEvent implements MessageListener{

    public MasterFarmerEvent(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);

        reqSet = new EquipmentLoadout()
                .set(EquipmentSlot.NECK, "Dodgy necklace")
                .set(EquipmentSlot.HEAD, "Rogue mask")
                .set(EquipmentSlot.TORSO, "Rogue top")
                .set(EquipmentSlot.LEGS, "Rogue trousers")
                .set(EquipmentSlot.FEET, "Rogue boots")
                .set(EquipmentSlot.HANDS, "Rogue gloves");

        reqWithdrawEvent = new BankEvent(getBot()).addReq(reqSet);

        mfPereq = new BankEvent(getBot(), Bank.DRAYNOR_VILLAGE_BANK)
                .addReq(3, "Dodgy necklace")
                .addReq(10, "Jug of wine");

        dodgiesReq = new EquipmentLoadout()
                .set(EquipmentSlot.NECK, "Dodgy necklace");
        dodgiesLoadoutEvent = new EquipLoadoutEvent(getBot(),dodgiesReq);

        dodgiesWithdrawEvent = new BankEvent(getBot()).addReq(dodgiesReq);

        wealthBuy = new GEEvent(getBot())
                .buy(2, 15000, "Ring of wealth (5)");

        gloryBuy = new GEEvent(getBot())
                .buy(2, 15000, "Amulet of glory(6)");

        totalShopping = new GEEvent(getBot())
                .buy(1000, 10, "Jug of wine")
                .buy(2, 2000, "Games necklace(8)")
                .buy(50, 2000, "Dodgy necklace")
                .sell(0, 10000, "Ring of wealth")
                .sell(0, 10000, "Amulet of glory")
                .sell(0, 200, "Monkfish")
                .sell(0, torstolSell, "Torstol seed")
                .sell(0, dwarfSell, "Dwarf weed seed")
                .sell(0, lantadSell, "Lantadyme seed")
                .sell(0, cadanSell, "Cadantine seed")
                .sell(0, snapSell, "Snapdragon seed")
                .sell(0, kwuarmSell, "Kwuarm seed")
                .sell(0, avantSell, "Avantoe seed")
                .sell(0, ranarrSell, "Ranarr seed")
                .sell(0, snapeSell, "Snape grass seed")
                .sell(0, toadSell, "Toadflax seed")
                .sell(0, watermelonSell, "Watermelon seed")
                .sell(0, limpSell, "Limpwurt seed")
                .sell(0, wildSell, "Wildblood seed");
    }


    public static final Logger logger = Logger.get(MasterFarmerEvent.class);



    Integer[] worlds = {60, 28, 59, 36, 68, 76, 52, 51, 11, 75, 4, 95, 12, 67, 3, 27, 44, 25, 41, 34, 58, 50, 33, 10, 9, 2};

    int torstolSell = getBot().getPriceGrabber().getSellPrice("Torstol seed") - 2000;
    int dwarfSell = getBot().getPriceGrabber().getSellPrice("Dwarf weed seed") - 100;
    int lantadSell = getBot().getPriceGrabber().getSellPrice("Lantadyme seed") - 100;
    int cadanSell = getBot().getPriceGrabber().getSellPrice("Cadantine seed") - 100;
    int snapSell = getBot().getPriceGrabber().getSellPrice("Snapdragon seed") - 2000;
    int kwuarmSell = getBot().getPriceGrabber().getSellPrice("Kwuarm seed") - 100;
    int avantSell = getBot().getPriceGrabber().getSellPrice("Avantoe seed") - 100;
    int ranarrSell = getBot().getPriceGrabber().getSellPrice("Ranarr seed") - 2000;
    int snapeSell = getBot().getPriceGrabber().getSellPrice("Snape grass seed") - 100;
    int toadSell = getBot().getPriceGrabber().getSellPrice("Toadflax seed") - 100;
    int watermelonSell = getBot().getPriceGrabber().getSellPrice("Watermelon seed") - 30;
    int limpSell = getBot().getPriceGrabber().getSellPrice("Limpwurt seed") - 30;
    int wildSell = getBot().getPriceGrabber().getSellPrice("Wildblood seed") - 30;

    private EquipmentLoadout dodgiesReq;
    private EquipLoadoutEvent dodgiesLoadoutEvent;
    private BankEvent dodgiesWithdrawEvent;

    private EquipmentLoadout reqSet;
    private BankEvent reqWithdrawEvent;
    private BankEvent mfPereq;

    private GEEvent wealthBuy;
    private GEEvent gloryBuy;
    private GEEvent totalShopping;


    boolean isAttemptingPick;
    boolean isStunned;

    int success;
    int failed;
    long start = System.currentTimeMillis();
    int ranarr;
    int snap;


    @Override
    public void onMessage(Message m) {

        if (m.getMessage().contains("You attempt to pick")) {
            success++;
            isAttemptingPick = false;
        }

//        if (m.getMessage().contains("You fail to pick")) {
//            failed++;
//        }
            if (m.getMessage().contains("You're stunned")) {
            isStunned = true;
        }

//        if (m.getMessage().contains("You steal 1 ranarr")) {
//            ranarr+=2;
//        }else if (m.getMessage().contains("You steal 2 ranarr")) {
//            ranarr+=4;
//        }
//
//        if (m.getMessage().contains("You steal 1 snapdragon")) {
//            snap+=2;
//        }else if (m.getMessage().contains("You steal 2 snapdragon")) {
//            snap+=4;
//        }
    }
//
//    @Override
//    public void onPaint(Graphics2D g) {
//        g.drawString("Succeeded:" + success, 50,50);
//        g.drawString("Failed:" + failed, 50,60);
//        g.drawString("Time: "+ StringUtils.formatTime(System.currentTimeMillis() - start),50,70);
//        g.drawString("Ranarrs':" + ranarr, 50,80);
//        g.drawString("Snapdragons':" + snap, 50,90);
//    }

    @Override
    public void step() throws InterruptedException {

        if(getBot().getOwnedItems().contains("Rogue mask") && getBot().getOwnedItems().contains("Rogue top") && getBot().getOwnedItems().contains("Rogue trousers") && getBot().getOwnedItems().contains("Rogue boots") && getBot().getOwnedItems().contains("Rogue gloves") && getBot().getOwnedItems().contains("Dodgy necklace") && !getBot().getEquipment().isOnlyWearing(reqSet)){
            new RogueReq(getBot(),true).execute();
        }
        else if(!getBot().getOwnedItems().contains("Rogue mask") && !getBot().getOwnedItems().contains("Rogue top") && !getBot().getOwnedItems().contains("Rogue trousers") && !getBot().getOwnedItems().contains("Rogue boots") && !getBot().getOwnedItems().contains("Rogue gloves") && getBot().getOwnedItems().contains("Dodgy necklace") && !getBot().getEquipment().isOnlyWearing(dodgiesReq)){
            if(!getBot().getEquipment().isOnlyWearing(dodgiesReq)){
                dodgiesLoadoutEvent.execute();
                sleepUntil(5000, ()-> getBot().getEquipment().isOnlyWearing(dodgiesReq));
            }
        }

        Player me = getBot().getPlayers().getLocal();
        Area mfArea = new Area(3087, 3243, 3073, 3257);
        NPC mf = getBot().getNPCs().closest(npc -> npc.hasName("Master Farmer"));



        if (!getBot().getWorlds().getCurrentWorld().isStandard() || !getBot().getWorlds().getCurrentWorld().isMembers()) {
            new WorldHopEvent(getBot(), worlds -> worlds.isStandard() && worlds.isMembers()).execute();
        }

        String[] seeds = {"Potato seed", "Onion seed", "Cabbage seed", "Tomato seed", "Sweetcorn seed", "Strawberry seed", "Marigold seed", "Nasturtium seed", "Rosemary seed", "Woad seed", "Redberry seed", "Cadavaberry seed", "Dwellberry seed", "Jangerberry seed", "Guam seed", "Marrentill seed", "Tarromin seed"};


        if (getBot().getOwnedItems().getAmount("Jug of wine") <= 9 || getBot().getOwnedItems().getAmount("Dodgy necklace") <= 2 || getBot().getOwnedItems().getAmount("Torstol seed") >= 20 || getBot().getOwnedItems().getAmount("Snapdragon seed") >= 20 || getBot().getOwnedItems().getAmount("Ranarr seed") >= 20) {
            logger.debug("Buyin some booze");
            if(!getBot().getOwnedItems().contains("Ring of wealth (1~5)")){
               wealthBuy.execute();
            }else if(!getBot().getOwnedItems().contains("Amulet of glory(1~6)")){
                gloryBuy.execute();
            }else if (totalShopping.isPendingOperation()) {
                if(totalShopping.executed() && !totalShopping.isPendingOperation()) {
                    new BankEvent(getBot(), Bank.GRAND_EXCHANGE)
                            .setDepositCondition(() -> true)
                            .then(new WorldHopEvent(getBot(), w -> w.isStandard() && w.isMembers()))
                            .execute();
                }
            }


        } else if (mfPereq.isPendingOperation() || (getBot().getInventory().isFull() && !getBot().getInventory().contains(seeds) && !getBot().getInventory().contains("Jug"))) {
            logger.debug("Trying to bank");
                mfPereq.execute();
        } else if (!mfArea.contains(me)) {
            logger.debug("I'm far, need to walk");
            new WebWalkEvent(getBot(), new Tile(3080, 3250, 0)).execute();
        } else if (getBot().getOwnedItems().getAmount("Jug of wine") >= 10 && getBot().getInventory().contains("Jug of wine")) {
            logger.debug("Got wines, check");

            if (getBot().getClient().getSkillBoosted(Skill.HITPOINTS) < 4) {
                logger.debug("I'm healthy, check");
                if(!getBot().getBank().isOpen()) {
                    new InventoryInteractEvent(getBot(), "Jug of wine", "Drink").execute();
                }else if(getBot().getBank().isOpen()){
                    new CloseInterfacesEvent(getBot()).execute();
                }
            }else if (isStunned  && getBot().getInventory().contains(seeds) && getBot().getInventory().contains("Jug")) {
                if (sleepUntil(600, () -> !isStunned))
                    sleep(100);
                new DropItemsEvent(getBot(), d -> d.hasName(seeds)).execute();
                new DropItemsEvent(getBot(), "Jug").execute();
            }else if (mfArea.contains(me) && getBot().getClient().getSkillBoosted(Skill.HITPOINTS) > 3 && getBot().getInventory().contains("Jug of wine") && !getBot().getInventory().isFull()) {
                isAttemptingPick = true;
                isStunned = false;

                if (sleepUntil(600, () -> !isAttemptingPick))
                    sleep(100);
                    new InteractEvent(getBot(), mf).execute();

            }
        }

        if (getBot().getClient().getSkillReal(Skill.AGILITY) >= 50 && getBot().getClient().getSkillReal(Skill.THIEVING) >= 50 && !getBot().getOwnedItems().contains("Rogue mask") && !getBot().getOwnedItems().contains("Rogue top") && !getBot().getOwnedItems().contains("Rogue trousers") && !getBot().getOwnedItems().contains("Rogue boots") && !getBot().getOwnedItems().contains("Rogue gloves")) {
            setComplete();
        }



    }


}



