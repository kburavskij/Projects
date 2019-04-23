import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.entities.*;
import org.quantumbot.api.enums.Bank;
import org.quantumbot.enums.Skill;
import org.quantumbot.events.*;
import org.quantumbot.events.containers.BankEvent;
import org.quantumbot.events.containers.InventoryInteractEvent;
import org.quantumbot.events.interactions.GroundItemInteractEvent;
import org.quantumbot.events.interactions.InteractEvent;
import org.quantumbot.events.interactions.NPCInteractEvent;
import org.quantumbot.events.interactions.WidgetInteractEvent;
import org.quantumbot.interfaces.Logger;

import java.util.Arrays;

public class DraynorCourse extends BotEvent {

    public DraynorCourse(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);
    }

    private String[] names = {"Rough wall", "Tightrope", "Narrow wall", "Wall", "Gap", "Crate"};
    private String[] actions = {"Climb", "Cross", "Balance", "Jump-up", "Jump", "Climb-down"};
    Entity previous;

    Player me = getBot().getPlayers().getLocal();
    GroundItem markOfGrace = getBot().getGroundItems().nearby(g -> g.hasName("Mark of grace") && g.hasAction("Take") && g.hasId(11849) && g.distance(me) <= 5 && g.exists() && getBot().getLocalWeb().canReach(g) && getBot().getLocalWeb().canAccess(g) );

    Logger logger = Logger.get(DraynorCourse.class);

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

    private void agility() throws InterruptedException{
        int starting = getBot().getClient().getSkillExp(Skill.AGILITY);
        GameObject nextObj = getBot().getGameObjects().closest(obj -> Arrays.asList(names).contains(obj.getName()) &&
                Arrays.asList(actions).contains(obj.getActions()[0]) &&
                getBot().getLocalWeb().canReach(obj) &&
                !obj.equals(previous));

        if (getBot().getClient().getSkillBoosted(Skill.HITPOINTS) <=4) {
            new InventoryInteractEvent(getBot(), "Jug of wine", "Drink").execute();
        }

        if(!getBot().getWorlds().getCurrentWorld().isStandard() || !getBot().getWorlds().getCurrentWorld().isMembers()){
            new WorldHopEvent(getBot(), world -> world.isMembers() && world.isStandard()).execute();
        }

        if (nextObj != null && !me.isMoving() && !me.isAnimating()) {
            sleep(350);
//                checkForMarks();
            if (new InteractEvent(getBot(), nextObj, nextObj.getActions()[0]).executed()) {
                previous = nextObj;
                sleepUntil(10000, () -> getBot().getClient().getSkillExp(Skill.AGILITY) > starting);
            }
        }
        sleep(350);
    }

//    private void checkForMarks()  throws InterruptedException {
//        if(!me.isMoving() && !me.isAnimating() && me.distance(markOfGrace)<=5 && markOfGrace!=null && getBot().getLocalWeb().canAccess(markOfGrace) && getBot().getLocalWeb().canReach(markOfGrace)){
//            if(new InteractEvent(getBot(), markOfGrace).executed()){
//                sleepUntil(10000, () -> !me.isAnimating() && markOfGrace==null);
//            }
//        }
//    }

    @Override
    public void step() throws InterruptedException {

        if (getBot().getClient().getSkillReal(Skill.AGILITY) <= 29) {
            checkStamina();
            checkRun();
            agility();
        } else if (getBot().getClient().getSkillReal(Skill.AGILITY) >= 30) {
            setComplete();
        }
    }

}
