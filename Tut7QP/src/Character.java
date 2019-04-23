import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.entities.Player;
import org.quantumbot.enums.Quest;
import org.quantumbot.events.BotEvent;
import org.quantumbot.events.interactions.WidgetInteractEvent;
import org.quantumbot.interfaces.Logger;

import java.util.Random;


public class Character extends BotEvent{

    private static Random rand = new Random();
    private static Random randSex = new Random();
    private static final Logger logger = Logger.get(Character.class);
    private static boolean complete = false;
    Player me = getBot().getPlayers().getLocal();


    public Character(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);
    }


    public void nameCreation() throws InterruptedException{
        if(!me.hasName(NameGenerator.generateName())&& getBot().getWidgets().contains(x -> x.hasRootId(558) && x.isVisible() && x.containsText("Look up name"))){
            NameGenerator.generateName();
            logger.debug(NameGenerator.generateName());
        }
        if (getBot().getWidgets().contains(x -> x.hasRootId(558) && x.isVisible() && x.containsText("Look up name"))) {
            new WidgetInteractEvent(getBot(), x -> x.hasRootId(558) && x.hasAction("Look up name") && x.isVisible()).execute();
            sleep(2034);
        }
        if (getBot().getWidgets().contains(x -> x.containsText("What name would you like to check") && x.isVisible())) {
            getBot().getKeyboard().type(NameGenerator.generateName(), true);
            sleep(5734);
        }
        if (getBot().getWidgets().contains(x -> x.hasRootId(558) && x.isVisible() && x.containsText("Set name"))) {
            new WidgetInteractEvent(getBot(), x -> x.hasRootId(558) && x.hasAction("Set name") && x.isVisible()).execute();
            sleep(2338);
        }
    }

    public void appearanceCreation() throws InterruptedException {
        if (getBot().getWidgets().contains(h -> h.hasId(269,113))) {
            for (int i = 0; i < rand.nextInt(20); i++ ) {
                new WidgetInteractEvent(getBot(), x -> x.hasId(269, 113) && x.isVisible()).execute();
            }
        }
        if (getBot().getWidgets().contains(h -> h.hasId(269,114))) {
            for (int i = 0; i < rand.nextInt(20); i++ ) {
                new WidgetInteractEvent(getBot(), x -> x.hasId(269, 114) && x.isVisible()).execute();
            }
        }
        if (getBot().getWidgets().contains(h -> h.hasId(269,115))) {
            for (int i = 0; i < rand.nextInt(20); i++ ) {
                new WidgetInteractEvent(getBot(), x -> x.hasId(269, 115) && x.isVisible()).execute();
            }
        }
        if (getBot().getWidgets().contains(h -> h.hasId(269,116))) {
            for (int i = 0; i < rand.nextInt(20); i++ ) {
                new WidgetInteractEvent(getBot(), x -> x.hasId(269, 116) && x.isVisible()).execute();
            }
        }
        if (getBot().getWidgets().contains(h -> h.hasId(269,117))) {
            for (int i = 0; i < rand.nextInt(20); i++ ) {
                new WidgetInteractEvent(getBot(), x -> x.hasId(269, 117) && x.isVisible()).execute();
            }
        }
        if (getBot().getWidgets().contains(h -> h.hasId(269,118))) {
            for (int i = 0; i < rand.nextInt(20); i++ ) {
                new WidgetInteractEvent(getBot(), x -> x.hasId(269, 118) && x.isVisible()).execute();
            }
        }
        if (getBot().getWidgets().contains(h -> h.hasId(269,119))) {
            for (int i = 0; i < rand.nextInt(20); i++ ) {
                new WidgetInteractEvent(getBot(), x -> x.hasId(269, 119) && x.isVisible()).execute();
            }
        }
        if (getBot().getWidgets().contains(h -> h.hasId(269,121))) {
            for (int i = 0; i < rand.nextInt(20); i++ ) {
                new WidgetInteractEvent(getBot(), x -> x.hasId(269, 121) && x.isVisible()).execute();
            }
        }
        if (getBot().getWidgets().contains(h -> h.hasId(269,127))) {
            for (int i = 0; i < rand.nextInt(20); i++ ) {
                new WidgetInteractEvent(getBot(), x -> x.hasId(269, 127) && x.isVisible()).execute();
            }
        }
        if (getBot().getWidgets().contains(h -> h.hasId(269,129))) {
            for (int i = 0; i < rand.nextInt(20); i++ ) {
                new WidgetInteractEvent(getBot(), x -> x.hasId(269, 129) && x.isVisible()).execute();
            }
        }
        if (getBot().getWidgets().contains(h -> h.hasId(269,130))) {
            for (int i = 0; i < rand.nextInt(20); i++ ) {
                new WidgetInteractEvent(getBot(), x -> x.hasId(269, 130) && x.isVisible()).execute();
            }
        }
        if (getBot().getWidgets().contains(h -> h.hasId(269,131))) {
            for (int i = 0; i < rand.nextInt(20); i++ ) {
                new WidgetInteractEvent(getBot(), x -> x.hasId(269, 131) && x.isVisible()).execute();
            }
        }
        if (getBot().getWidgets().contains(h -> h.hasId(269,136)) && getBot().getWidgets().contains(h -> h.hasId(269,137))) {
            switch (randSex.nextInt(2)){
                case 1:
                {
                    new WidgetInteractEvent(getBot(), x -> x.hasId(269, 136) && x.isVisible()).execute();
                }
                case 2:
                {
                    new WidgetInteractEvent(getBot(), x -> x.hasId(269, 137) && x.isVisible()).execute();
                }
            }
        }
        if (getBot().getWidgets().contains(h -> h.hasRootId(269))) {
            new WidgetInteractEvent(getBot(), x -> x.hasRootId(269) && x.hasAction("Accept") && x.isVisible()).execute();
            complete=true;
        }
        if (complete){
            setComplete();
        }
    }


    @Override
    public void step() throws InterruptedException {
        nameCreation();
        appearanceCreation();
    }
}
