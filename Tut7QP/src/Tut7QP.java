import org.quantumbot.api.Script;
import org.quantumbot.client.script.ScriptManifest;
import org.quantumbot.enums.Quest;
import org.quantumbot.interfaces.Logger;

@ScriptManifest(author = "Gwanz", name = "Tut7QP", version = 1, description = "", image = "")
public class Tut7QP extends Script implements Logger{

    boolean tutIsle = getBot().getQuests().isComplete(Quest.TUTORIAL_ISLAND);
    boolean cooksAssistant = getBot().getQuests().isComplete(Quest.COOKS_ASSISTANT);
    boolean sheepShear = getBot().getQuests().isComplete(Quest.SHEEP_SHEARER);
    boolean romeoJuliet = getBot().getQuests().isComplete(Quest.ROMEO_JULIET);



    public static final Logger logger = Logger.get(Tut7QP.class);

    @Override
    public void onLoop() throws InterruptedException {

        if(!tutIsle && !cooksAssistant && !sheepShear && !romeoJuliet){
            new Tutorial(getBot(), true).execute();
        }else if(tutIsle && !cooksAssistant && !sheepShear && !romeoJuliet){
            new Items(getBot(), true).execute();
            new CooksSteps(getBot(), true).execute();
        }else if(tutIsle && cooksAssistant && !sheepShear && !romeoJuliet){
            new SheepSteps(getBot(), true).execute();
        }else if(tutIsle && cooksAssistant && sheepShear && !romeoJuliet){
            new Cadava(getBot(), true).execute();
            new QuestSteps(getBot(), true).execute();
        }else if(tutIsle && cooksAssistant && sheepShear && romeoJuliet){
            logger.debug("DONZOOOO!!!!!!!!");
        }

    }


}
