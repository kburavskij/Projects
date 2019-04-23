import org.quantumbot.api.Script;
import org.quantumbot.api.Sleeper;
import org.quantumbot.api.entities.GameObject;
import org.quantumbot.api.entities.NPC;
import org.quantumbot.api.entities.Player;
import org.quantumbot.api.entities.WallObject;
import org.quantumbot.api.ge.GrandExchange;
import org.quantumbot.api.input.Mouse;
import org.quantumbot.api.map.Area;
import org.quantumbot.api.map.Tile;
import org.quantumbot.api.widgets.Widget;
import org.quantumbot.client.script.ScriptManifest;
import org.quantumbot.enums.Skill;
import org.quantumbot.events.*;
import org.quantumbot.events.containers.*;
import org.quantumbot.events.ge.GEBuyEvent;
import org.quantumbot.events.ge.GEEvent;
import org.quantumbot.events.ge.GESellEvent;
import org.quantumbot.events.interactions.NPCInteractEvent;
import org.quantumbot.events.interactions.ObjectInteractEvent;
import org.quantumbot.interfaces.Logger;


@ScriptManifest(author = "Gwanz", name = "Blaster", version = 1, description = "Runs Blast Furnace farm", image = "")
public class BlastFurnace extends Script {

    int coalBagAmount;
    boolean shopping;
    private static final Logger logger = Logger.get(BlastFurnace.class);
//    private int getCofferCoins() {
//        return getBot().getClient().getVarp(VARP_COFFER) >> 1;
//    }


    public void onLoop() throws InterruptedException {

        if (!getBot().getWorlds().getCurrentWorld().hasId(352, 355, 358, 386, 387, 494, 516)) {
            new WorldHopEvent(getBot(), 352, 355, 358, 386, 387, 494, 516).execute();
            sleep(573);
        }

        Player me = getBot().getPlayers().getLocal();
        Tile belt = new Tile(1942, 4967, 0);
        Tile disp1 = new Tile(1939, 4964, 0);
        Tile disp2 = new Tile(1941, 4962, 0);
        Tile dispMain = new Tile(1939, 4963, 0);
        Area disp = new Area(disp1, disp2);
        Tile chest1 = new Tile(1938, 4958, 0);
        Tile chest2 = new Tile(1950, 4957, 0);
        Area chest = new Area(chest1, chest2);
        Tile chestMain = new Tile(1948, 4957, 0);
        Tile ge1 = new Tile(3167, 3487, 0);
        Tile ge2 = new Tile(3159, 3486, 0);
        Area ge = new Area(ge1, ge2);
        Tile trapdoor = new Tile(3141, 3504, 0);
        Tile travel = new Tile(2909, 10173, 0);
        Tile stairs = new Tile(2931, 10196, 0);
        Tile entrance = new Tile(1940, 4958, 0);
        Widget cofferAdena = getBot().getWidgets().get(474, 3, 3);




        if (!getBot().getBank().isCached()){
            new BankEvent(getBot()).execute();
            sleep(2665);
            new BankOpenEvent(getBot()).execute();
        }

        if((disp1.isOnScreen(getBot()) || disp1.distance(me) <= 40) && getBot().getClient().isInGame()){
             shopping = false;
        }else {
            shopping = true;
        }




        if( shopping==false && getBot().getBank().isCached() && getBot().getClient().isInGame()){

            if (getBot().getWidgets().contains(h -> h.containsText("You must ask the foreman")) && getBot().getClient().getSkillReal(Skill.SMITHING) <= 59) {
                logger.debug("Darau");
                new BankEvent(getBot()).addReq(2500, "Coins").execute();
                new NPCInteractEvent(getBot(), "Blast Furnace Foreman", "Pay")
                        .then(new DialogueEvent(getBot(), "Yes"))
                        .then(new DialogueEvent(getBot()))
                        .execute();
                new BankEvent(getBot()).addReq(1, 1, "Coal bag")
                        .execute();
            }

            if (getBot().getWidgets().contains(a -> a.containsText("Congratulations, you just advanced"))) {
                new DialogueEvent(getBot()).execute();
                logger.debug("Continue pressed");
            }

            if (getBot().getWidgets().contains(o -> o.containsText("You should collect your bars before making any more.", "You don't have any free inventory space."))) {
            new BankEvent(getBot()).setDepositCondition(() -> getBot().getInventory().isFull())
//                    .then(new CloseInterfacesEvent(getBot()))
                    .then(new ObjectInteractEvent(getBot(), "Bar dispenser", "Take"))
                    .then(new MakeXEvent(getBot(), 1))
                    .execute();
                sleepUntil(1000, () -> getBot().getGameObjects().closest("Bar dispenser").hasAction("Take"));
            }else if(new ObjectInteractEvent(getBot(), "Bar dispenser", "Take").isFailed()){
            new BankEvent(getBot()).addReq(1, 1, "Coal bag")
                    .execute();
        }


        //                        Stamina pots DONE!
        if (!getBot().getInventory().contains("Iron ore") && !getBot().getInventory().contains("Coal") && chest.contains(me) && !getBot().getInventory().contains("Steel bar") && getBot().getSettings().getRunEnergy() <= 20) {
            new BankEvent(getBot()).addReq(1, "Stamina potion(1~4)").setDepositCondition(() -> false)
                    .then(new CloseInterfacesEvent(getBot()))
                    .execute();
            sleep(346);
            new InventoryInteractEvent(getBot(), p -> p.getName().contains("Stamina"), "Drink")
                    .execute();
        } else if (new InventoryInteractEvent(getBot(), p -> p.getName().contains("Stamina"), "Drink").isFailed()) {
            sleep(381);
            new InventoryInteractEvent(getBot(), p -> p.getName().contains("Stamina"), "Drink")
                    .execute();
        }

        if (disp.contains(me) && !getBot().getInventory().isFull() && (!getBot().getInventory().contains("Iron bar") || !getBot().getInventory().contains("Steel bar"))){
            sleepUntil(1000, () -> getBot().getGameObjects().closest("Bar dispenser").hasAction("Take"));
            new ObjectInteractEvent(getBot(), "Bar dispenser", "Take")
                    .then(new MakeXEvent(getBot(), 1))
                    .then(new BankEvent(getBot()).setDepositCondition(() -> !getBot().getInventory().isEmpty()))
                    .execute();
        }else if(new ObjectInteractEvent(getBot(), "Bar dispenser", "Take").isFailed()){
            new BankEvent(getBot()).addReq(1, 1, "Coal bag")
                    .execute();
        }


////                    Coffin system DONE!
        if (cofferAdena.getItemQuantity() <= 0 && cofferAdena != null) {
            new BankEvent(getBot()).addReq(40000, "Coins").execute();
            new ObjectInteractEvent(getBot(), "Coffer")
                    .then(new DialogueEvent(getBot(), "Deposit coins."))
                    .then(new EnterAmountEvent(getBot(), 40000, "Deposit how much?"))
                    .then(new DialogueEvent(getBot()))
                    .execute();
        }



        if (chest.contains(me) && !me.getTile().equals(belt) && !getBot().getInventory().contains("Coal") && !getBot().getInventory().contains("Iron ore") && !getBot().getInventory().contains("Steel bar") && getBot().getSettings().getRunEnergy() >= 20) {
            new BankEvent(getBot()).addReq(1, 1, "Coal bag")
                    .addReq(27, 27, "Coal")
                    .setDepositCondition(() -> false)
                    .then(new CloseInterfacesEvent(getBot()))
                    .execute();
            sleep(156);
        }

        if (chest.contains(me) && !getBot().getInventory().contains("Iron ore") && getBot().getInventory().contains("Coal") && getBot().getInventory().contains("Coal bag")) {
            sleep(150);
            new InventoryInteractEvent(getBot(), i -> i.hasName("Coal bag"), "Fill")
                    .then(new BankEvent(getBot()).addReq(27, 27, "Iron ore").addReq(1, 1, "Coal bag").setDepositCondition(() -> false))
//                    .then(new CloseInterfacesEvent(getBot()))
                    .execute();
            sleep(143);
        }

        if (getBot().getInventory().contains("Iron ore") && getBot().getInventory().contains("Coal bag")) {
            new ObjectInteractEvent(getBot(), "Conveyor belt", "Put-ore-on").setWalk(true)
                    .then(new InventoryInteractEvent(getBot(), "Coal bag", "Empty"))
                    .then(new ObjectInteractEvent(getBot(), "Conveyor belt", "Put-ore-on"))
                    .executed();
        }else if(new ObjectInteractEvent(getBot(), "Conveyor belt", "Put-ore-on").isFailed()){
            new BankEvent(getBot()).addReq(1, 1, "Coal bag")
                    .execute();
        }

        if (!getBot().getInventory().contains("Coal") && !getBot().getInventory().contains("Iron ore") && getBot().getInventory().contains("Coal bag") && me.getTile().equals(belt)){
            new WalkEvent(getBot(), dispMain).setAccuracy(0).execute();
            sleepUntil(1000, () -> me.equals(dispMain));
            sleepUntil(1000, () -> getBot().getGameObjects().closest("Bar dispenser").hasAction("Take"));
                    new ObjectInteractEvent(getBot(), "Bar dispenser", "Take")
                    .then(new MakeXEvent(getBot(), 1))
                    .execute();
        }else if(new ObjectInteractEvent(getBot(), "Bar dispenser", "Take").isFailed()){
            new BankEvent(getBot()).addReq(1, 1, "Coal bag")
                    .execute();
        }


        if ((getBot().getInventory().contains("Steel bar") || getBot().getInventory().contains("Iron bar"))) {
            new BankEvent(getBot()).setDepositCondition(() -> !getBot().getInventory().isEmpty())
                    .execute();
        }

            if (getBot().getOwnedItems().getAmount("Iron ore") <= 27 || getBot().getOwnedItems().getAmount("Coal") <= 27 || getBot().getOwnedItems().getAmount("Coins") <= 39999 || getBot().getOwnedItems().getAmount("Stamina potion(4)") <= 1 && getBot().getBank().isCached() && chest.contains(me)) {
                new BankEvent(getBot()).addReq(1, "Varrock teleport").setDepositCondition(() -> false)
                        .then(new CloseInterfacesEvent(getBot())).execute();
                sleep(246);
                new InventoryInteractEvent(getBot(), v -> v.getName().contains("Varrock"), "Break").execute();
                sleep(8793);
            }

            if (entrance.distance(me) <= 2) {
                new BankEvent(getBot()).addReq(1, 1, "Coal bag")
                        .execute();
            }

    }


        //        GE run in here

        if(shopping == true && getBot().getBank().isCached() && getBot().getClient().isInGame()) {

//            if (ge1.distance(me) >= 10 || getBot().getOwnedItems().getAmount("Iron ore") <= 27 || getBot().getOwnedItems().getAmount("Coal") <= 27 || getBot().getOwnedItems().getAmount("Coins") <= 39999 || getBot().getOwnedItems().getAmount("Stamina potion(4)") <= 1) {
//                sleep(1739);
//                new WebWalkEvent(getBot(), ge1).execute();
//            }

            if (getBot().getOwnedItems().getAmount("Iron ore") <= 27 || getBot().getOwnedItems().getAmount("Coal") <= 27 || getBot().getOwnedItems().getAmount("Coins") <= 39999 || getBot().getOwnedItems().getAmount("Stamina potion(4)") <= 1 && getBot().getBank().isCached()) {
                new GEEvent(getBot())
                        .sell(0, 500, "Steel bar")
                        .sell(0, 220, "Iron bar")
                        .buy(13000, 170, "Iron ore")
                        .buy(13000, 270,  "Coal")
                        .buy(40, 8100, "Stamina potion(4)")
                        .buy(5, 1000, "Varrock teleport")
                        .execute();

                new WebWalkEvent(getBot(), trapdoor).setDestinationAccuracy(0)
                            .execute();

            }

            if (getBot().getOwnedItems().getAmount("Iron ore") >= 10000 && getBot().getOwnedItems().getAmount("Coal") >= 10000 && me.distance(ge1) <= 10) {
                new WebWalkEvent(getBot(), trapdoor)
                        .execute();
            }

            if (trapdoor.distance(me) <= 2) {
                new ObjectInteractEvent(getBot(), "Trapdoor", "Travel")
                        .execute();
                sleep(27252);
            }

            if(travel.distance(me) <= 2){
                new WebWalkEvent(getBot(), stairs)
                        .execute();
            }

            if (stairs.distance(me) <= 2) {
                new ObjectInteractEvent(getBot(), "Stairs", "Climb-down")
                        .execute();
                sleep(7625);
            }
        }

        }
    }
