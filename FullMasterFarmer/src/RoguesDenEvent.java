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
import org.quantumbot.events.interactions.NPCInteractEvent;
import org.quantumbot.events.interactions.ObjectInteractEvent;
import org.quantumbot.events.interactions.WidgetInteractEvent;
import org.quantumbot.interfaces.Logger;


public class RoguesDenEvent extends BotEvent {


    public RoguesDenEvent(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);
        emptyReq = new EquipmentLoadout();
//        emptyReqLoadout = new EquipLoadoutEvent(getBot(),emptyReq);
    }

    NPC brian = getBot().getNPCs().closest(n -> n.hasName("Brian O'Richard"));


    public static final Logger logger = Logger.get(RoguesDenEvent.class);

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

    Area roguesStartArea = new Area(3039, 4965, 3064, 4991, 1);
    Tile startDoor = new Tile(3056, 4991, 1);

    private EquipmentLoadout emptyReq;
//    private EquipLoadoutEvent emptyReqLoadout;

    @Override
    public void step() throws InterruptedException {


        Player me;
        me = getBot().getPlayers().getLocal();


        if (!getBot().getWorlds().getCurrentWorld().isStandard() || !getBot().getWorlds().getCurrentWorld().isMembers()) {
            new WorldHopEvent(getBot(), world -> world.isMembers() && world.isStandard()).execute();
        }

        if (getBot().getClient().getSkillReal(Skill.THIEVING) >= 50 && getBot().getClient().getSkillReal(Skill.AGILITY) >= 50 && getBot().getOwnedItems().getAmount("Rogue's equipment crate") <= 4) {

//            logger.debug("Trying to figure");



            if (!getBot().getOwnedItems().contains("Stamina potion(4)") || !getBot().getOwnedItems().contains("Games necklace(1~8)") && !getBot().getInventory().contains("Mystic jewel")) {
//                logger.debug("From Rogues Den to GE");
                new GEEvent(getBot())
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
                            .sell(0, wildSell, "Wildblood seed")
                            .buy(2, 2000, "Games necklace(8)")
                            .buy(1, 15000, "Amulet of glory(6)")
                            .buy(10, 6000, "Stamina potion(4)")
                            .execute();
                } else if (!roguesStartArea.contains(me) && getBot().getOwnedItems().contains("Games necklace(1~8)") && !getBot().getInventory().contains("Mystic jewel")) {
//                logger.debug("Attempting to go to Rogues Den");
                    if (!getBot().getInventory().contains("Games necklace(1~8)")) {
                        new BankEvent(getBot())
                                .addReq(1, "Games necklace(1~8)")
                                .execute();
                    } else if (getBot().getInventory().contains("Games necklace(1~8)")) {
                        new CloseInterfacesEvent(getBot()).execute();
                        new WebWalkEvent(getBot(), new Tile(2906, 3537, 0)).execute();
                        if (me.distance(new Tile(2906, 3537, 0)) <= 5) {
                            if (new ObjectInteractEvent(getBot(), "Trapdoor", "Enter").executed()) {
                                new WebWalkEvent(getBot(), new Tile(3046, 4971, 1)).execute();
                            }
                        }
                    }
                }

            if (new Tile(3014, 5033, 1).equals(me)) {
                new MazeThree(getBot(), true).execute();
            }else if (new Tile(2972, 5093, 1).equals(me)) {
                new MazeTwo(getBot(), true).execute();
            }else if (roguesStartArea.contains(me) && (!getBot().getInventory().contains("Mystic jewel") || getBot().getEquipment().isOnlyWearing(emptyReq))) {
//                logger.debug("Preparing inside Rogues Den");
                new Prepare(getBot(), true).execute();
            }


            } else if (getBot().getClient().getSkillReal(Skill.THIEVING) >= 50 && getBot().getClient().getSkillReal(Skill.AGILITY) >= 50 && getBot().getOwnedItems().getAmount("Rogue's equipment crate") >= 5) {
                setComplete();
            }

        if (getBot().getInventory().contains("Mystic jewel") && !new Tile(3014, 5033, 1).equals(me) && !new Tile(2972, 5093, 1).equals(me)) {
            new CacheEvent(getBot(),true).execute();
        }

    }
}
