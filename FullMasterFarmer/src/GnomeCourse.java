import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.entities.Entity;
import org.quantumbot.api.entities.GroundItem;
import org.quantumbot.api.entities.Player;
import org.quantumbot.api.map.Area;
import org.quantumbot.api.map.Tile;
import org.quantumbot.enums.Skill;
import org.quantumbot.events.*;
import org.quantumbot.events.containers.InventoryInteractEvent;
import org.quantumbot.events.interactions.GroundItemInteractEvent;
import org.quantumbot.events.interactions.InteractEvent;
import org.quantumbot.interfaces.Logger;
import org.quantumbot.utils.Timer;

import java.util.Arrays;


public class GnomeCourse extends BotEvent {

    public GnomeCourse(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);

    }

    public static final Logger logger = Logger.get(GnomeCourse.class);

    Player me = getBot().getPlayers().getLocal();

    Area logArea = new Area(2470,3436,2488, 3438,0);
    Entity log = getBot().getGameObjects().closest(o-> o.hasName("Log balance"));

    Area netAreaOne = new Area(2477,3429,2470, 3426,0);
    Entity netOne = getBot().getGameObjects().closest(o-> o.hasName("Obstacle net") && o.hasId(23134));

    Area branchAreaOne = new Area(2471,3424,2476, 3422,1);
    Entity branchOne = getBot().getGameObjects().closest(o-> o.hasName("Tree branch") && o.hasId(23559));

    Area ropeArea = new Area(2472,3418,2477, 3421,2);
    Entity rope = getBot().getGameObjects().closest(o-> o.hasName("Balancing rope") && o.hasId(23557));

    Area branchAreaTwo = new Area(2483,3418,2488, 3421,2);
    Entity branchTwo = getBot().getGameObjects().closest(o-> o.hasName("Tree branch") && (o.hasId(23561) || o.hasId(23560)));

    Area netAreaTwo = new Area(2488,3418,2482, 3425,0);
    Entity netTwo = getBot().getGameObjects().closest(o-> o.hasName("Obstacle net")  && o.hasId(23135));

    Area pipeArea = new Area(2482, 3427,2489, 3431,0);
    Entity pipe = getBot().getGameObjects().closest(true, o-> o.hasId(23138));


    private Integer[] ids = {23145, 23134, 23559, 23557, (23561 | 23560), 23135, 23138};
    private String[] names = {"Log balance", "Obstacle net", "Tree branch", "Balancing rope", "Tree branch", "Obstacle net", "Obstacle pipe"};
    private String[] actions = {"Walk-across", "Climb-over", "Climb", "Walk-on", "Climb-down", "Climb-over", "Squeeze-through"};
    Entity previous;


    @Override
    public void onStart() {
        super.onStart();
        getBot().getClient().getSkillExp(Skill.AGILITY);
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


    @Override
    public void step() throws InterruptedException {


//        if (getBot().getClient().getSkillReal(Skill.AGILITY) <= 9) {
//
//            int starting = getBot().getClient().getSkillExp(Skill.AGILITY);
//            Entity nextObj = getBot().getGameObjects().closest(obj -> Arrays.asList(names).contains(obj.getName()) &&
//                    Arrays.asList(actions).contains(obj.getActions()[0]) &&
//                    getBot().getLocalWeb().canReach(obj) &&
//                    !obj.equals(previous));
//
//            if (nextObj != null && !me.isMoving() && !me.isAnimating()) {
//                sleep(350);
//                if (new InteractEvent(getBot(), nextObj, nextObj.getActions()[0]).executed()) {
//                    previous = nextObj;
//                    sleepDuring(10000, () -> getBot().getClient().getSkillExp(Skill.AGILITY) > starting);
//                }
//            }
//            sleep(350);
//        }
//

//            if (nextObj != null && !me.isMoving() && !me.isAnimating()) {
//                sleep(350);
//                if (new InteractEvent(getBot(), nextObj, nextObj.getActions()[0]).executed()) {
//                    previous = nextObj;
//                    sleepDuring(10000, () -> getBot().getClient().getSkillExp(Skill.AGILITY) > starting);
//                }
//            } else if ((!getBot().getLocalWeb().canAccess(nextObj) || !getBot().getLocalWeb().canReach(nextObj)) && !me.isMoving() && !me.isAnimating()) {
//                sleep(350);
//                setFailed();
//            }
//            sleep(350);
//        } else if (getBot().getClient().getSkillReal(Skill.AGILITY) >= 10) {
//            setComplete();
//        }

        if(!getBot().getWorlds().getCurrentWorld().isStandard() || !getBot().getWorlds().getCurrentWorld().isMembers()){
            new WorldHopEvent(getBot(), world -> world.isMembers() && world.isStandard()).execute();
        }

        logger.debug("Started GnomeCourse");
        if (getBot().getClient().getSkillReal(Skill.AGILITY) <= 9) {
            checkStamina();
            checkRun();
            if (getBot().getClient().getSkillBoosted(Skill.HITPOINTS) <=4) {
                new InventoryInteractEvent(getBot(), "Jug of wine", "Drink").execute();
            }

            if (logArea.contains(me)) {
                if (logArea.contains(me) && log != null) {
                    new InteractEvent(getBot(), log, "Walk-across").execute();
                    sleepUntil(5000, () -> netAreaOne.contains(me) && !me.isMoving() && !me.isAnimating() && netOne.distance(me) <= 4);
                }
            } else if ( netAreaOne.contains(me) && netOne != null) {
                new InteractEvent(getBot(), netOne, "Climb-over").execute();
                sleepUntil(2500, () -> branchAreaOne.contains(me) && !me.isMoving() && !me.isAnimating() && branchOne.distance(me) <= 3);
            } else if (branchAreaOne.contains(me) && branchOne != null) {
                new InteractEvent(getBot(), branchOne, "Climb").execute();
                sleepUntil(2500, () -> ropeArea.contains(me) && !me.isMoving() && !me.isAnimating() && rope.distance(me) <= 5);
            } else if (ropeArea.contains(me)) {
                if (ropeArea.contains(me) && rope != null) {
                    new InteractEvent(getBot(), rope, "Walk-on").execute();
                    sleepUntil(5000, () -> branchAreaTwo.contains(me) && !me.isMoving() && !me.isAnimating() && branchTwo.distance(me) <= 3);
                }
            }else if (branchAreaTwo.contains(me)) {
                if ( branchAreaTwo.contains(me) && branchTwo != null) {
                    new InteractEvent(getBot(), branchTwo, "Climb-down").execute();
                    sleepUntil(2500, () -> netAreaTwo.contains(me) && !me.isMoving() && !me.isAnimating() && netTwo.distance(me) <= 6);
                }
            } else if (netAreaTwo.contains(me)) {
                if ( netAreaTwo.contains(me) && netTwo != null) {
                    new InteractEvent(getBot(), netTwo, "Climb-over").execute();
                    sleepUntil(2500, () -> pipeArea.contains(me) && !me.isMoving() && !me.isAnimating() && pipe.distance(me) <= 4);
                }
            } else if (pipeArea.contains(me)) {
                if ( pipeArea.contains(me) && pipe != null) {
                    new InteractEvent(getBot(), pipe).execute();
                    sleepUntil(8500, () -> logArea.contains(me) && !me.isMoving() && !me.isAnimating() && log.distance(me) < 10);
                }

            }
        }else if (getBot().getClient().getSkillReal(Skill.AGILITY) >= 10) {
            setComplete();
        }


    }
}




