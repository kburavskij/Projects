import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.entities.Entity;
import org.quantumbot.api.entities.NPC;
import org.quantumbot.api.entities.Player;
import org.quantumbot.api.map.Area;
import org.quantumbot.api.map.Tile;
import org.quantumbot.enums.Quest;
import org.quantumbot.enums.Skill;
import org.quantumbot.events.*;
import org.quantumbot.events.containers.InventoryInteractEvent;
import org.quantumbot.events.interactions.EntityInteractEvent;
import org.quantumbot.events.interactions.NPCInteractEvent;
import org.quantumbot.events.interactions.ObjectInteractEvent;
import org.quantumbot.events.interactions.WidgetInteractEvent;
import org.quantumbot.interfaces.Logger;

import static org.quantumbot.enums.spells.StandardSpellbook.WIND_STRIKE;


public class Tutorial extends BotEvent {

    private static final Logger logger = Logger.get(Tutorial.class);

    Player me = getBot().getPlayers().getLocal();
    NPC rat = getBot().getNPCs().closest("Giant rat");
    NPC chicken = getBot().getNPCs().closest("Chicken");


    Tile fishGuide = new Tile(3104, 3097, 0);
    Tile cookGuide = new Tile(3079, 3084, 0);

    Area tinArea = new Area(3075, 9506, 3079, 9503);
    Area copperArea = new Area(3086, 9501, 3083, 9503);


    public Tutorial(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);
    }


    @Override
    public void step() throws InterruptedException {

        int varp = getBot().getClient().getVarp(Quest.TUTORIAL_ISLAND.varp);
        boolean tutIsle = getBot().getQuests().isComplete(Quest.TUTORIAL_ISLAND);

        if(tutIsle){
            setComplete();
            System.exit(0);
        }

        if (getBot().getWidgets().contains(h -> h.containsText("Someone else is fighting that.")) ) {
            new DialogueEvent(getBot(),"Click to continue").execute();
            new DialogueEvent(getBot()).execute();
        }

        if (getBot().getDialogues().isPendingContinuation()) {
            new DialogueEvent(getBot()).execute();
        }



        switch (varp) {


            //                character creation
            case 1:
                new Character(getBot(), true).execute();
                break;

            //                Guide
            case 2:
                if (getBot().getDialogues().inDialogue()) {
                    new DialogueEvent(getBot(), "I've played in the past, but not recently.").execute();
                    new DialogueEvent(getBot()).execute();
                }
                if (getBot().getWidgets().contains(x -> x.containsText("Getting started") && x.isVisible())) {
                    new NPCInteractEvent(getBot(), "Gielinor Guide", "Talk-to").execute();
                    sleep(1734);
                }
                break;

            //                Options
            case 3:
                if (getBot().getDialogues().isPendingContinuation()) {
                    new DialogueEvent(getBot()).execute();
                }
                new WidgetInteractEvent(getBot(), x -> x.hasId(164, 38) && x.isVisible()).execute();
                sleep(2338);
                break;

            //                guideFinish
            case 7:
                new NPCInteractEvent(getBot(), "Gielinor Guide", "Talk-to").execute();
                if (getBot().getDialogues().isPendingContinuation()) {
                    new DialogueEvent(getBot()).execute();
                }
                break;

            //                Door
            case 10:
                new ObjectInteractEvent(getBot(), "Door", "Open").execute();
                break;

            //                Fish Guide Walk
            case 20:
                new WalkEvent(getBot(), fishGuide).execute();
                sleepMoving(3973);
                new NPCInteractEvent(getBot(), "Survival Expert", "Talk-to").execute();
                break;

            //                Fish Guide Talk
            case 30:
                if (getBot().getDialogues().isPendingContinuation()) {
                    new DialogueEvent(getBot()).execute();
                }
                new WidgetInteractEvent(getBot(), x -> x.hasId(164, 53) && x.isVisible()).execute();
                sleep(3740);
                break;

            //                Fishing
            case 40:
                if (!getBot().getInventory().contains("Raw shrimps")) {
                    new NPCInteractEvent(getBot(), "Fishing spot").execute();
                }
                break;

            //                //                Stats
            case 50:
                new WidgetInteractEvent(getBot(), x -> x.hasId(164, 51) && x.isVisible()).execute();
                sleep(2338);
                break;

            //                Fish Guide Talk
            case 60:
                new NPCInteractEvent(getBot(), "Survival Expert", "Talk-to").execute();
                if (getBot().getDialogues().isPendingContinuation()) {
                    new DialogueEvent(getBot()).execute();
                }
                break;

            //                //                Talk n Chop
            case 70:
                new ObjectInteractEvent(getBot(), "Tree", "Chop down").execute();
                break;

            //                //                Fire
            case 80:
                Tile myPosition = new Tile(me.getTile());
                Tile aroundMeTile1 = myPosition.translate(-2, -2);
                Tile aroundMeTile2 = myPosition.translate(2, 2);
                for (Tile tile : new Area(aroundMeTile1, aroundMeTile2).getTiles()) {
                    if (!getBot().getGameObjects().contains(obj -> getBot().getLocalWeb().canAccess(obj) && obj.hasAction() && tile.equals(obj))) {
                        new WebWalkEvent(getBot(), tile)
                                .then(new ItemCombineEvent(getBot(), "Logs", "Tinderbox"))
                                .execute();
                        break;
                    } else {
                        new ItemCombineEvent(getBot(), "Logs", "Tinderbox").execute();
                        sleep(2168);
                    }
                }

                //                Cooking
            case 90:
                sleep(1890);
                if(getBot().getGameObjects().closest("Fire").exists()) {
                    if (getBot().getInventory().contains("Raw shrimps")) {
                        new InventoryInteractEvent(getBot(), "Raw shrimps", "Use").execute();
                        sleep(1678);
                        new ObjectInteractEvent(getBot(), "Fire").execute();
                        sleepAnimating(1769, 3705);
                    } else if (!getBot().getInventory().contains("Raw shrimps")) {
                        new NPCInteractEvent(getBot(), "Fishing spot").execute();
                    }
                    break;
                } else{
                    if(!getBot().getInventory().contains("Logs")){
                        new ObjectInteractEvent(getBot(), "Tree", "Chop down").execute();
                        sleepAnimating(1798, 4789);
                    }else{
                        new ItemCombineEvent(getBot(), "Logs", "Tinderbox").execute();
                        sleep(2168);
                    }
                    break;
                }

            //                Move on
            case 120:
                new WalkEvent(getBot(), new Tile(3090, 3091, 0)).execute();
                sleepMoving(3973);
                new ObjectInteractEvent(getBot(), "Gate", "Open").execute();
                break;

            //                Move on
            case 130:
                new WalkEvent(getBot(), cookGuide).execute();
                sleepMoving(3973);
                new ObjectInteractEvent(getBot(), "Door", "Open").execute();
                break;

            //                Talk to Chef
            case 140:
                new NPCInteractEvent(getBot(), "Master Chef", "Talk-to").execute();
                if (getBot().getDialogues().isPendingContinuation()) {
                    new DialogueEvent(getBot()).execute();
                }
                break;

            //                Mix dough
            case 150:
                new ItemCombineEvent(getBot(), "Pot of flour", "Bucket of water").execute();
                break;

            //                Cook dough
            case 160:
                new InventoryInteractEvent(getBot(), "Bread dough", "Use")
                        .then(new ObjectInteractEvent(getBot(), "Range"))
                        .execute();
                sleepAnimating(1869, 3705);
                break;

            case 170:
                new WalkEvent(getBot(), new Tile(3073, 3090, 0)).execute();
                sleepMoving(3973);
                new ObjectInteractEvent(getBot(), "Door", "Open").execute();
                break;

            case 200:
                new WalkEvent(getBot(), new Tile(3086, 3126, 0)).execute();
                sleepMoving(3973);
                new NPCInteractEvent(getBot(), "Quest Guide", "Talk-to").execute();
                break;

            //                Talk to Q guide
            case 220:
                new NPCInteractEvent(getBot(), "Quest Guide", "Talk-to").execute();
                break;

            case 230:
                if (getBot().getDialogues().isPendingContinuation()) {
                    new DialogueEvent(getBot()).execute();
                }
                new WidgetInteractEvent(getBot(), x -> x.hasId(164, 52) && x.isVisible()).execute();
                sleep(2015);
                break;

            //                Talk to Q guide
            case 240:
                new NPCInteractEvent(getBot(), "Quest Guide", "Talk-to").execute();
                if (getBot().getDialogues().isPendingContinuation()) {
                    new DialogueEvent(getBot()).execute();
                }
                break;

            //                              Climb down
            case 250:
                new ObjectInteractEvent(getBot(), "Ladder", "Climb-down").execute();
                break;

            //                Talk to Mining guide
            case 260:
                new WalkEvent(getBot(), new Tile(3081, 9506, 0)).execute();
                sleepMoving(3973);
                new NPCInteractEvent(getBot(), "Mining Instructor", "Talk-to").execute();
                if (getBot().getDialogues().isPendingContinuation()) {
                    new DialogueEvent(getBot()).execute();
                }
                break;

            //                              Mining
            case 300: {
                new WebWalkEvent(getBot(), tinArea)
                        .then(new ObjectInteractEvent(getBot(), x -> x.hasId(10080)))
                        .execute();
                sleepAnimating(1769, 3705);
            }
            break;

            //                              Mining
            case 310: {
                new WebWalkEvent(getBot(), copperArea)
                        .then(new ObjectInteractEvent(getBot(), x -> x.hasId(10079)))
                        .execute();
                sleepAnimating(1769, 3705);
            }
            break;

            //                              Smelting
            case 320: {
                new ObjectInteractEvent(getBot(), "Furnace").execute();
                sleepAnimating(1769, 3705);
            }
            break;

            //                Talk to Mining guide
            case 330:
                new NPCInteractEvent(getBot(), "Mining Instructor", "Talk-to").execute();
                if (getBot().getDialogues().isPendingContinuation()) {
                    new DialogueEvent(getBot()).execute();
                }
                break;

            //                              Smithing
            case 340: {
                new ObjectInteractEvent(getBot(), "Anvil", "Smith").execute();
                sleepAnimating(1769, 3705);
                new WidgetInteractEvent(getBot(), x -> x.hasId(312, 2) && x.isVisible()).execute();
                sleep(3569);
            }
            break;

            //                              Smithing
            case 350: {
                new ObjectInteractEvent(getBot(), "Anvil", "Smith").execute();
                sleepAnimating(1769, 3705);
                new WidgetInteractEvent(getBot(), x -> x.hasId(312, 2) && x.isVisible()).execute();
                sleep(3569);
            }
            break;

            //                              Combat
            case 360: {
                new WebWalkEvent(getBot(), new Tile(3096, 9502, 0)).execute();
            }
            break;

            //                              Combat Instructor
            case 370: {
                new NPCInteractEvent(getBot(), "Combat Instructor", "Talk-to").execute();
                if (getBot().getDialogues().isPendingContinuation()) {
                    new DialogueEvent(getBot()).execute();
                }
            }
            break;

            //                              Gear
            case 390: {
                new WidgetInteractEvent(getBot(), x -> x.hasId(164, 54) && x.isVisible()).execute();
                sleep(1740);
            }
            break;

            //                              Gear
            case 400: {
                if (getBot().getWidgets().contains(x -> x.hasId(164, 64) && x.isVisible())) {
                    new WidgetInteractEvent(getBot(), x -> x.hasId(387, 17)).execute();
                    sleep(1740);
                } else {
                    new WidgetInteractEvent(getBot(), x -> x.hasId(164, 54) && x.isVisible()).execute();
                    sleep(1740);
                }
            }
                break;


                //                              Gear
                case 405: {
                    new InventoryInteractEvent(getBot(), "Bronze dagger", "Equip").execute();
                    sleep(1740);
                }
                break;

            //                              Combat Instructor
            case 410: {
                new NPCInteractEvent(getBot(), "Combat Instructor", "Talk-to").execute();
                if (getBot().getDialogues().isPendingContinuation()) {
                    new DialogueEvent(getBot()).execute();
                }
            }
            break;


            //                              Combat Instructor
            case 420: {
                new InventoryInteractEvent(getBot(), "Wooden shield", "Wield").execute();
                sleep(1740);
                new InventoryInteractEvent(getBot(), "Bronze sword", "Wield").execute();
                sleep(1740);
            }
            break;

            //                              Gear
            case 430: {
                new WidgetInteractEvent(getBot(), x -> x.hasId(164, 50) && x.isVisible()).execute();
                sleep(1740);
            }
            break;

            //                              Gear
            case 440: {
                if(!rat.isUnderAttack() && rat!=null && !me.isAnimating()) {
                    new NPCInteractEvent(getBot(), a->a.distance() < 6 && a.hasName("Giant rat")).setWalk(true).execute();
                        sleepAnimating(3987, 9741);
                }
                break;
            }

            //                              Gear
            case 450: {
                if(!rat.isUnderAttack() && rat!=null && !me.isAnimating()) {
                    new NPCInteractEvent(getBot(), a->a.distance() < 6 && a.hasName("Giant rat")).setWalk(true).execute();
                    sleepAnimating(3987, 9741);
                }
                break;
            }

            //                              Gear
            case 460: {
                if(!rat.isUnderAttack() && rat!=null && !me.isAnimating()) {
                    new NPCInteractEvent(getBot(), a->a.distance() < 6 && a.hasName("Giant rat")).setWalk(true).execute();
                    sleepAnimating(3987, 9741);
                }
                break;
            }

            //                              Combat Instructor
            case 470: {
                new WebWalkEvent(getBot(), new Tile(3110, 9511,0))
                        .then(new NPCInteractEvent(getBot(), "Combat Instructor", "Talk-to"))
                        .execute();
                if (getBot().getDialogues().isPendingContinuation()) {
                    new DialogueEvent(getBot()).execute();
                }
            }
            break;

            //                              Combat Instructor
            case 480: {
                new InventoryInteractEvent(getBot(), "Shortbow", "Wield")
                        .then(new InventoryInteractEvent(getBot(), 882, "Wield"))
                        .execute();
                sleep(1740);
                if(getBot().getEquipment().contains("Shortbow") && getBot().getEquipment().contains(882)){
                    new WebWalkEvent(getBot(), new Tile(3107,9512,0)).execute();
                    if(!rat.isUnderAttack() && rat!=null && !me.isAnimating()){
                    new NPCInteractEvent(getBot(), a->a.distance() < 6 && a.hasName("Giant rat")).setWalk(false).execute();
                    sleepAnimating(3698, 8791);
                }
                }
            }
            break;


            //                              Combat Instructor
            case 490: {
                new InventoryInteractEvent(getBot(), "Shortbow", "Wield")
                        .then(new InventoryInteractEvent(getBot(), 882, "Wield"))
                        .execute();
                sleep(1740);
                if(getBot().getEquipment().contains("Shortbow") && getBot().getEquipment().contains(882)){
                    new WebWalkEvent(getBot(), new Tile(3107,9512,0)).execute();
                    if(!rat.isUnderAttack() && rat!=null && !me.isAnimating()){
                        new NPCInteractEvent(getBot(), a->a.distance() < 6 && a.hasName("Giant rat")).setWalk(false).execute();
                        sleepAnimating(3698, 8791);
                    }
                }
            }
            break;

            //                              Climb up
            case 500: {
                new ObjectInteractEvent(getBot(), "Ladder", "Climb-up").execute();
                sleepMoving(2430);
                break;
            }

            //                              Banking
            case 510:{
                new ObjectInteractEvent(getBot(), "Bank booth").execute();
                sleepMoving(2430);
                break;
            }

            //                              Banking
            case 520:{
                new CloseInterfacesEvent(getBot())
                        .then(new ObjectInteractEvent(getBot(), "Poll booth"))
                        .execute();
                if (getBot().getDialogues().isPendingContinuation()) {
                    new DialogueEvent(getBot()).execute();
                }
                sleepMoving(2430);
                break;
            }

            //                              Banking
            case 525:{
                new CloseInterfacesEvent(getBot()).execute();
                new WebWalkEvent(getBot(), new Tile(3125, 3124, 0)).execute();
                sleepMoving(2430);
                break;
            }

            //                              Account Instructor
            case 530: {
                new NPCInteractEvent(getBot(), "Account Guide", "Talk-to").execute();
                if (getBot().getDialogues().isPendingContinuation()) {
                    new DialogueEvent(getBot()).execute();
                }
            }
            break;

            //                              Gear
            case 531: {
                new WidgetInteractEvent(getBot(), x -> x.hasId(164, 36) && x.isVisible()).execute();
                sleep(1740);
            }
            break;

            //                              Account Instructor
            case 532: {
                new NPCInteractEvent(getBot(), "Account Guide", "Talk-to").execute();
                if (getBot().getDialogues().isPendingContinuation()) {
                    new DialogueEvent(getBot()).execute();
                }
            }
            break;

            //                              Banking
            case 540:{
                if (getBot().getDialogues().inDialogue()) {
                    new DialogueEvent(getBot(), "No.").execute();
                    new DialogueEvent(getBot()).execute();
                }
                new WebWalkEvent(getBot(), new Tile(3133, 3122, 0)).execute();
                sleepMoving(2430);
                break;
            }

            //                              Prayer Instructor
            case 550: {
                if(getBot().getNPCs().closest("Brother Brace") == null){
                    new WebWalkEvent(getBot(), new Tile(3124, 3106, 0)).setMiniMapAccuracy(4).execute();
                }
                new NPCInteractEvent(getBot(), "Brother Brace", "Talk-to").execute();
                if (getBot().getDialogues().isPendingContinuation()) {
                    new DialogueEvent(getBot()).execute();
                }
            }
            break;

            //                              Prayer Instructor
            case 560: {
                if(!getBot().getWidgets().get(164,55).isValid() && !getBot().getWidgets().get(164,55).isVisible()) {
                    new NPCInteractEvent(getBot(), "Brother Brace", "Talk-to").execute();
                    sleep(1534);
                    if (getBot().getDialogues().isPendingContinuation()) {
                        new DialogueEvent(getBot()).execute();
                    }
                }else if(getBot().getWidgets().get(164,55).isValid() && getBot().getWidgets().get(164,55).isVisible()){
                    new WidgetInteractEvent(getBot(), x -> x.hasId(164, 55) && x.isVisible()).execute();
                    sleep(876);
                }
            }
            break;

            //                              Prayer Instructor
            case 570: {
                new NPCInteractEvent(getBot(), "Brother Brace", "Talk-to").execute();
                if (getBot().getDialogues().isPendingContinuation()) {
                    new DialogueEvent(getBot()).execute();
                }
            }
            break;

            //                              FL widget
            case 580: {
                new WidgetInteractEvent(getBot(), x -> x.hasId(164, 37) && x.isVisible()).execute();
                sleep(1740);
            }
            break;

            //                              Prayer Instructor
            case 600: {
                new NPCInteractEvent(getBot(), "Brother Brace", "Talk-to").execute();
                if (getBot().getDialogues().isPendingContinuation()) {
                    new DialogueEvent(getBot()).execute();
                }
            }
            break;

            //                              Walking
            case 610:{
                new WebWalkEvent(getBot(), new Tile(3120, 3099, 0)).execute();
                sleepMoving(2430);
                break;
            }

            //                              Magic Instructor
            case 620: {
                if(getBot().getNPCs().closest("Magic Instructor") == null){
                    new WebWalkEvent(getBot(), new Tile(3141, 3089, 0)).execute();
                    sleepMoving(1987);
                }
                new NPCInteractEvent(getBot(), "Magic Instructor", "Talk-to").execute();
                if (getBot().getDialogues().isPendingContinuation()) {
                    new DialogueEvent(getBot()).execute();
                }
            }
            break;

            //                              FL widget
            case 630: {
                new WidgetInteractEvent(getBot(), x -> x.hasId(164, 56) && x.isVisible()).execute();
                sleep(1740);
            }
            break;

            //                              Magic Instructor
            case 640: {
                new NPCInteractEvent(getBot(), "Magic Instructor", "Talk-to").execute();
                if (getBot().getDialogues().isPendingContinuation()) {
                    new DialogueEvent(getBot()).execute();
                }
            }
            break;

            //                              Spells widget
            case 650: {
                if (!chicken.isUnderAttack() && chicken!=null && !me.isAnimating()) {
                    new EntityInteractEvent(getBot(), getBot().getNPCs().closest("Chicken")).setWalk(false).setCast(WIND_STRIKE).setIgnoreObstacles(true).execute();
                    sleep(2440);
                }
            }
            break;

            //                              Magic Instructor
            case 670: {
                new NPCInteractEvent(getBot(), "Magic Instructor", "Talk-to").execute();
                if (getBot().getDialogues().isPendingContinuation()) {
                    new DialogueEvent(getBot(), "Yes.", "No, I'm not planning to do that.").execute();
                    new DialogueEvent(getBot()).execute();
                }
            }
            break;


        }
        }
    }
