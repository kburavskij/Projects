import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.entities.NPC;
import org.quantumbot.api.entities.Player;
import org.quantumbot.api.enums.Bank;
import org.quantumbot.api.map.Area;
import org.quantumbot.enums.Food;
import org.quantumbot.enums.Skill;
import org.quantumbot.events.BotEvent;
import org.quantumbot.events.HealEvent;
import org.quantumbot.events.WebWalkEvent;
import org.quantumbot.events.WorldHopEvent;
import org.quantumbot.events.containers.BankEvent;
import org.quantumbot.events.containers.InventoryInteractEvent;
import org.quantumbot.events.interactions.NPCInteractEvent;

public class ManThieverEvent extends BotEvent {

    public ManThieverEvent(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);
    }

    Player me = getBot().getPlayers().getLocal();
    NPC man = getBot().getNPCs().closest(m -> m.hasName("Man"));

    Area manLumbridge = new Area(3213, 3212, 3226, 3225, 0);

    @Override
    public void step() throws InterruptedException {

        if(!getBot().getWorlds().getCurrentWorld().isStandard() || !getBot().getWorlds().getCurrentWorld().isMembers()){
            new WorldHopEvent(getBot(), world -> world.isMembers() && world.isStandard()).execute();
        }

        if(getBot().getClient().getSkillReal(Skill.THIEVING)<=4) {

            if (me.distance(manLumbridge.getAvgCenterTile()) <=19) {
                if (getBot().getInventory().getAmount("Jug of wine") <= 0 || getBot().getInventory().isFull()) {
                    new BankEvent(getBot()).addReq(20, "Jug of wine").execute();
                }
                if(getBot().getInventory().getAmount("Jug of wine") > 0 && !getBot().getInventory().isFull()) {
                    if (getBot().getClient().getSkillBoosted(Skill.HITPOINTS) <=4) {
                        new InventoryInteractEvent(getBot(), "Jug of wine", "Drink").execute();
                        sleep(573);
                    }
                    if (getBot().getClient().getSkillBoosted(Skill.HITPOINTS) >=5) {

                        if(getBot().getLocalWeb().getPlane()==3 || getBot().getLocalWeb().getPlane()==2 || getBot().getLocalWeb().getPlane()==1){
                            new WebWalkEvent(getBot(), manLumbridge.getRandomTile()).execute();
                        }
                        if (getBot().getLocalWeb().getPlane()==0 && manLumbridge.contains(me)) {
                            new NPCInteractEvent(getBot(), g -> g.hasName("Man") && g.getHealthPercentage() > 0, "Pickpocket").execute();
                            sleep(600);
                        }

                        if (getBot().getInventory().getAmount(22521) >= 28) {
                            new InventoryInteractEvent(getBot(), d -> d.hasId(22521), "Open-all").execute();
                        }
                    }
                }
            }
            if(me.distance(manLumbridge.getAvgCenterTile()) >=20){
                new WebWalkEvent(getBot(), manLumbridge.getRandomTile()).execute();
            }

        }else if (getBot().getClient().getSkillReal(Skill.THIEVING)>=5){
            new BankEvent(getBot(), Bank.LUMBRIDGE_UPPER_BANK).addReq("Ring of wealth (1~5)").execute();
            if (getBot().getInventory().contains("Ring of wealth (1~5)")) {
                setComplete();
            }
        }

    }
}
