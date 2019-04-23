import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.entities.Entity;
import org.quantumbot.api.entities.GameObject;
import org.quantumbot.api.entities.GroundItem;
import org.quantumbot.api.entities.Player;
import org.quantumbot.api.map.Area;
import org.quantumbot.api.map.Tile;
import org.quantumbot.enums.Skill;
import org.quantumbot.events.*;
import org.quantumbot.events.containers.InventoryInteractEvent;
import org.quantumbot.events.interactions.*;
import org.quantumbot.interfaces.Logger;
import org.quantumbot.utils.Timer;

import java.util.Arrays;


public class VarrockCourse extends BotEvent {

    public VarrockCourse(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);
    }


    private Integer[] ids = {10586, 10587, 10642, 10777, 10778, 10779, 10780, 10781, 10819};
    private String[] names = {"Rough wall", "Clothes line", "Gap", "Wall", "Gap", "Gap", "Gap", "Ledge", "Edge"};
    private String[] actions = {"Climb", "Cross", "Leap", "Balance", "Leap", "Leap", "Leap", "Hurdle", "Jump-off"};
    Entity previous;

    Player me = getBot().getPlayers().getLocal();
    GroundItem markOfGrace = getBot().getGroundItems().nearby(g -> g.hasName("Mark of grace") && g.hasAction("Take") && g.hasId(11849) && g.distance(me) <= 6);

    Area bugArea = new Area(3194, 3416, 3197, 3416, 1);

    Area bug = new Area(3192, 3415, 3191, 3416, 1);
    Entity bugWallTEST = getBot().getGameObjects().closest(o->o.hasName("Wall") && o.hasAction("Balance") && o.getTile().equals(bug.getRandomTile()));

    Entity bugWall = getBot().getGameObjects().closest(o->o.hasName("Wall") && o.hasAction("Balance"));


    Logger logger = Logger.get(VarrockCourse.class);



    @Override
    public void onStart() {
        super.onStart();
        getBot().getClient().getSkillExp(Skill.AGILITY);
    }

    private void checkForMarks()  throws InterruptedException {
        if(!me.isMoving() && !me.isAnimating() && markOfGrace!=null && me.distance(markOfGrace)<=6 && getBot().getLocalWeb().canAccess(markOfGrace) && getBot().getLocalWeb().canReach(markOfGrace)){
            if(new InteractEvent(getBot(), markOfGrace).executed()){
                sleepUntil(10000, () -> !me.isAnimating() && markOfGrace==null);
            }
        }
    }

    private void checkStamina() throws InterruptedException{
        if(getBot().getSettings().getRunEnergy()<= 20 && getBot().getInventory().contains("Stamin potion(1~4)")){
            new InventoryInteractEvent(getBot(), "Stamina potion(1~4)", "Drink").executed();
            if(getBot().getInventory().contains("Vial")){
                new DropItemsEvent(getBot(), "Vial").execute();
            }
        }
    }



    private void checkRun() throws InterruptedException{
        if(!getBot().getSettings().getRunEnabled()){
            new SettingsEvent(getBot()).setRunEnabled(true).execute();
        }
    }

    private void agility() throws InterruptedException{
        int starting = getBot().getClient().getSkillExp(Skill.AGILITY);
        Entity nextObj = getBot().getGameObjects().closest(obj -> Arrays.asList(names).contains(obj.getName()) &&
                Arrays.asList(actions).contains(obj.getActions()[0]) &&
                getBot().getLocalWeb().canAccess(obj) &&
                !obj.equals(previous));


        if (getBot().getClient().getSkillBoosted(Skill.HITPOINTS) <4) {
            new InventoryInteractEvent(getBot(), "Jug of wine", "Drink").execute();
        }

        if(!getBot().getWorlds().getCurrentWorld().isStandard() || !getBot().getWorlds().getCurrentWorld().isMembers()){
            new WorldHopEvent(getBot(), world -> world.isMembers() && world.isStandard()).execute();
        }


        if (nextObj != null && !me.isMoving() && !me.isAnimating()) {
//                checkForMarks();
            checkStamina();
            checkRun();
            new InteractEvent(getBot(), nextObj, nextObj.getActions()[0]).execute();
            previous = nextObj;
        } else if (!me.isMoving() && !me.isAnimating() && bugArea.contains(me)) {
//                checkForMarks();
            new ObjectInteractEvent(getBot(), o->o.hasName("Wall") && o.hasAction("Balance")).setWalk(false).execute();
        }else if(nextObj==null && !me.isMoving() && !me.isAnimating()){
            new InteractEvent(getBot(), previous, nextObj.getActions()[0]).execute();
        }
        sleepDuring(5000, () -> me.isMoving() && me.isAnimating());
        sleep(600);
    }

    @Override
    public void step() throws InterruptedException {

        if (getBot().getClient().getSkillReal(Skill.AGILITY) <= 49) {
            agility();
        } else if (getBot().getClient().getSkillReal(Skill.AGILITY) >= 50) {
            setComplete();
        }
    }
}
