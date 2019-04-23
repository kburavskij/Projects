import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.Script;
import org.quantumbot.client.script.ScriptManifest;
import org.quantumbot.enums.EquipmentSlot;
import org.quantumbot.enums.Skill;
import org.quantumbot.events.CloseInterfacesEvent;
import org.quantumbot.events.DialogueEvent;
import org.quantumbot.events.WorldHopEvent;
import org.quantumbot.events.containers.*;
import org.quantumbot.events.ge.GEEvent;
import org.quantumbot.interfaces.Logger;


@ScriptManifest(author = "Gwanz", name = "FullMasterFarmer", version = 0, description = "My 2nd script", image = "")
public class MainModule extends Script {



    public static final Logger logger = Logger.get(MainModule.class);



    @Override
    public void onStart() {
        super.onStart();
        reqSet = new EquipmentLoadout()
                .set(EquipmentSlot.NECK, "Dodgy necklace")
                .set(EquipmentSlot.HEAD, "Rogue mask")
                .set(EquipmentSlot.TORSO, "Rogue top")
                .set(EquipmentSlot.LEGS, "Rogue trousers")
                .set(EquipmentSlot.FEET, "Rogue boots")
                .set(EquipmentSlot.HANDS, "Rogue gloves");
        reqWithdrawEvent = new BankEvent(getBot()).addReq(reqSet);
    }

    private EquipmentLoadout reqSet;
    private BankEvent reqWithdrawEvent;

    public void onLoop() throws InterruptedException {

        logger.debug("MainModule");

        if(getBot().getWidgets().get(109,15,-1).containsText("None")){
            logger.debug("Starting");
            new StartingItemsPerequisites(getBot(),true).execute();
        }else if(!getBot().getBank().isCached() && !getBot().getWorlds().isMembers() && getBot().getWidgets().get(109,15,-1).containsText("days left")){
            logger.debug("Hopping");
            new WorldHopEvent(getBot(), world -> world.isStandard() && world.isMembers()).execute();
        } else if(!getBot().getBank().isCached() && getBot().getWorlds().isMembers()){
                                 logger.debug("Not cached");
             new CacheEvent(getBot(),true).execute();
         }else if(getBot().getBank().isCached()){
             logger.debug("Cached");
             if (getBot().getClient().getSkillReal(Skill.AGILITY) <= 49) {
                logger.debug("Agility ON!");
                new AgilityCoursesManager(getBot(), true).execute();
            } else if (getBot().getClient().getSkillReal(Skill.AGILITY) >= 50) {
                if (getBot().getClient().getSkillReal(Skill.THIEVING) <= 4) {
                    logger.debug("Man Thief ON!");
                    new ManThieverEvent(getBot(), true).execute();
                } else if (getBot().getClient().getSkillReal(Skill.THIEVING) <= 37) {
                    logger.debug("Tea Thief ON!");
                    new TeaThieverEvent(getBot(), true).execute();
                } else if (getBot().getClient().getSkillReal(Skill.THIEVING) <= 49) {
                    logger.debug("MF until 49 Thief ON!");
                    new MasterFarmerEvent(getBot(), true).execute();
                } else if (getBot().getClient().getSkillReal(Skill.AGILITY) >= 50 && getBot().getClient().getSkillReal(Skill.THIEVING) >= 50 && getBot().getOwnedItems().getAmount("Rogue's equipment crate") <= 4 && (!getBot().getOwnedItems().contains("Rogue mask") || !getBot().getOwnedItems().contains("Rogue top") || !getBot().getOwnedItems().contains("Rogue trousers") || !getBot().getOwnedItems().contains("Rogue boots") || !getBot().getOwnedItems().contains("Rogue gloves"))) {
                    logger.debug("Rogues boxes farm ON!");
                    new RoguesDenEvent(getBot(), true).execute();
                } else if (getBot().getClient().getSkillReal(Skill.AGILITY) >= 50 && getBot().getClient().getSkillReal(Skill.THIEVING) >= 50 && (getBot().getOwnedItems().getAmount("Rogue's equipment crate") >= 5 || !getBot().getOwnedItems().contains("Rogue mask") || !getBot().getOwnedItems().contains("Rogue top") || !getBot().getOwnedItems().contains("Rogue trousers") || !getBot().getOwnedItems().contains("Rogue boots") || !getBot().getOwnedItems().contains("Rogue gloves"))) {
                    logger.debug("Rogues boxes opening ON!");
                    new RogueOutfit(getBot(), true).execute();
                } else if (getBot().getClient().getSkillReal(Skill.AGILITY) >= 50 && getBot().getClient().getSkillReal(Skill.THIEVING) >= 50 && getBot().getOwnedItems().contains("Rogue mask") && getBot().getOwnedItems().contains("Rogue top") && getBot().getOwnedItems().contains("Rogue trousers") && getBot().getOwnedItems().contains("Rogue boots") && getBot().getOwnedItems().contains("Rogue gloves") && !getBot().getEquipment().isOnlyWearing(reqSet) ) {
                    logger.debug("Rogue equip ON!");
                    new RogueReq(getBot(), true).execute();
                } else if (getBot().getClient().getSkillReal(Skill.AGILITY) >= 50 && getBot().getClient().getSkillReal(Skill.THIEVING) >= 50 && getBot().getEquipment().isOnlyWearing(reqSet) ) {
                    logger.debug("End Game MF Thief ON!!!!!!!!");
                    new MasterFarmerEvent(getBot(), true).execute();
                }
            }
            }

        if (getBot().getInventory().contains("Mystic jewel")) {
            new CacheEvent(getBot(),true).execute();
        }

    }
}