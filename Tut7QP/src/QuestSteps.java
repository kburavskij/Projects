import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.entities.NPC;
import org.quantumbot.api.entities.Player;
import org.quantumbot.api.map.Area;
import org.quantumbot.enums.Quest;
import org.quantumbot.events.BotEvent;
import org.quantumbot.events.DialogueEvent;
import org.quantumbot.events.WalkEvent;
import org.quantumbot.events.WebWalkEvent;
import org.quantumbot.events.interactions.EntityInteractEvent;

public class QuestSteps extends BotEvent {

    public QuestSteps(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);
    }

    @Override
    public void step() throws InterruptedException {

        int varp = getBot().getClient().getVarp(org.quantumbot.enums.Quest.ROMEO_JULIET.varp);
        boolean romeoJuliet = getBot().getQuests().isComplete(org.quantumbot.enums.Quest.ROMEO_JULIET);
        boolean romeoJulietStarted = getBot().getQuests().isStarted(org.quantumbot.enums.Quest.ROMEO_JULIET);

        Area apothecaryArea = new Area(3192, 3402, 3197, 3406);
        Area churchArea = new Area(3253, 3480, 3257, 3484);
        Area romeoArea = new Area(3206, 3432, 3218, 3422);
        Area julietArea = new Area(3160, 3425, 3156, 3426, 1);


        NPC apothecaryNPC = getBot().getNPCs().closest("Apothecary");
        NPC romeoNPC = getBot().getNPCs().closest("Romeo");
        NPC julietNPC = getBot().getNPCs().closest("Juliet");
        NPC fatherNPC = getBot().getNPCs().closest("Father Lawrence");
        Player me = getBot().getPlayers().getLocal();


        if (!romeoJulietStarted && romeoArea.contains(me) && getBot().getInventory().contains("Cadava berries")) {
            new EntityInteractEvent(getBot(), romeoNPC, "Talk-to").execute();
            if (getBot().getDialogues().inDialogue()) {
                new DialogueEvent(getBot(), "Perhaps I could help to find her for you?", "Yes, ok, I'll let her know.", "Ok, thanks.").execute();
                new DialogueEvent(getBot()).execute();
            }
        } else if(!romeoJulietStarted && !romeoArea.contains(me) && getBot().getInventory().contains("Cadava berries")){
            new WebWalkEvent(getBot(), romeoArea.getRandomTile()).execute();
            sleepMoving(14897);
        }


        if (!romeoJuliet) {
            switch (varp) {

                case 10: {
                    if (!julietArea.contains(me)) {
                        new WebWalkEvent(getBot(), julietArea.getRandomTile()).execute();
                    } else if (julietArea.contains(me) && julietNPC.distance() < 15) {
                        new EntityInteractEvent(getBot(), julietNPC, "Talk-to").execute();
                        if (getBot().getDialogues().inDialogue()) {
                            new DialogueEvent(getBot()).execute();
                        }
                    }
                    break;
                }

                case 20: {
                    if (!romeoArea.contains(me)) {
                        new WebWalkEvent(getBot(), romeoArea.getRandomTile()).execute();
                    } else if (romeoArea.contains(me)) {
                        new EntityInteractEvent(getBot(), romeoNPC, "Talk-to").execute();
                        if (getBot().getDialogues().inDialogue()) {
                            new DialogueEvent(getBot(), "Ok, thanks.").execute();
                            new DialogueEvent(getBot()).execute();
                        }
                    }
                    break;
                }

                case 30: {
                    if (!churchArea.contains(me)) {
                        new WebWalkEvent(getBot(), churchArea.getRandomTile()).execute();
                    } else if (churchArea.contains(me)) {
                        new EntityInteractEvent(getBot(), fatherNPC, "Talk-to").execute();
                        if (getBot().getDialogues().inDialogue()) {
                            new DialogueEvent(getBot()).execute();
                        }
                    }
                    break;
                }

                case 40: {
                    if (!apothecaryArea.contains(me) && !getBot().getInventory().contains("Cadava potion")) {
                        new WebWalkEvent(getBot(), apothecaryArea.getRandomTile()).execute();
                    } else if (apothecaryArea.contains(me) && !getBot().getInventory().contains("Cadava potion")) {
                        new EntityInteractEvent(getBot(), apothecaryNPC, "Talk-to").execute();
                        if (getBot().getDialogues().inDialogue()) {
                            new DialogueEvent(getBot(), "Talk about something else.", "Talk about Romeo & Juliet.").execute();
                            new DialogueEvent(getBot()).execute();
                        }
                    } else if (!julietArea.contains(me) && getBot().getInventory().contains("Cadava potion")) {
                            new WebWalkEvent(getBot(), julietArea.getRandomTile()).execute();
                        } else if (julietArea.contains(me)) {
                            new EntityInteractEvent(getBot(), julietNPC, "Talk-to").execute();
                            if (getBot().getDialogues().inDialogue()) {
                                new DialogueEvent(getBot()).execute();
                            }
                        }
                    break;
                }

                case 50: {
                    if (!apothecaryArea.contains(me) && !getBot().getInventory().contains("Cadava potion")) {
                        new WebWalkEvent(getBot(), apothecaryArea.getRandomTile()).execute();
                    } else if (apothecaryArea.contains(me) && !getBot().getInventory().contains("Cadava potion")) {
                        new EntityInteractEvent(getBot(), apothecaryNPC, "Talk-to").execute();
                        if (getBot().getDialogues().inDialogue()) {
                            new DialogueEvent(getBot(), "Talk about something else.", "Talk about Romeo & Juliet.").execute();
                            new DialogueEvent(getBot()).execute();
                        }
                    } else if (!julietArea.contains(me) && getBot().getInventory().contains("Cadava potion")) {
                        new WebWalkEvent(getBot(), julietArea.getRandomTile()).execute();
                    } else if (julietArea.contains(me)) {
                        new EntityInteractEvent(getBot(), julietNPC, "Talk-to").execute();
                        if (getBot().getDialogues().inDialogue()) {
                            new DialogueEvent(getBot()).execute();
                        }
                    }
                    break;
                }

                case 60: {
                    if (!romeoArea.contains(me) && !getBot().getDialogues().inDialogue()) {
                        new WebWalkEvent(getBot(), romeoArea.getRandomTile()).execute();
                        sleepAnimating(7816, 19787);
                    }else if(romeoArea.contains(me) && !getBot().getDialogues().inDialogue()){
                       new EntityInteractEvent(getBot(), romeoNPC, "Talk-to").execute();
                        if (getBot().getDialogues().inDialogue()) {
                            new DialogueEvent(getBot()).execute();
                        }
                    }
                }
                    break;
                }

        } else if(romeoJuliet){
            setComplete();
        }
    }

}
