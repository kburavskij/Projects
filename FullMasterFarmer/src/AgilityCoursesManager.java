import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.entities.Player;
import org.quantumbot.api.map.Area;
import org.quantumbot.enums.Skill;
import org.quantumbot.events.BotEvent;
import org.quantumbot.events.WebWalkEvent;
import org.quantumbot.interfaces.Logger;



public class AgilityCoursesManager extends BotEvent {

    public AgilityCoursesManager(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);

    }

    public static final Logger logger = Logger.get(AgilityCoursesManager.class);
    Player me = getBot().getPlayers().getLocal();

    Area gnomeCourse = new Area(2470, 3437, 2488, 3419);
    Area gnomeCourseBeggining = new Area(2471, 3435, 2477, 3437);

    Area draynorCourse = new Area(3103, 3282, 3082, 3250);
    Area draynorCourseBeggining = new Area(3103, 3281, 3106, 3276);

    Area alKharidCourse = new Area(3319, 3156, 3264, 3198);
    Area alKharidCourseBeggining = new Area(3278, 3195, 3269, 3197);

    Area varrockCourse = new Area(3248, 3389, 3178, 3429);
    Area varrockCourseBeggining = new Area(3221, 3411, 3223, 3417);


    @Override
    public void step() throws InterruptedException {
        logger.debug("Started SkillCheck");

        if(getBot().getClient().getSkillReal(Skill.AGILITY)<9 && (!getBot().getInventory().contains("Jug of wine") || !getBot().getInventory().contains("Stamina potion(4)"))){
            new StartingItemsPerequisites(getBot(), true).execute();
        } else if (getBot().getClient().getSkillReal(Skill.AGILITY) <= 9) {
            if (me.distance(gnomeCourse.getAvgCenterTile()) >= 15) {
                logger.debug("Started WalkerToGnome");
                new WebWalkEvent(getBot(), gnomeCourseBeggining).execute();
            } else if (me.distance(gnomeCourse.getAvgCenterTile()) <= 14) {
                logger.debug("Started GnomeEvent");
                new GnomeCourse(getBot(), true).execute();
            }

        }else if (getBot().getClient().getSkillReal(Skill.AGILITY) <= 29) {
            if (me.distance(draynorCourse.getAvgCenterTile()) >= 21) {
                new WebWalkEvent(getBot(), draynorCourseBeggining).execute();
            } else if (me.distance(draynorCourse.getAvgCenterTile()) <= 20) {
                new DraynorCourse(getBot(), true).execute();
            }

//        } else if (getBot().getClient().getSkillReal(Skill.AGILITY) <= 29) {
//                if (me.distance(alKharidCourse.getAvgCenterTile()) >= 33) {
//                    logger.debug("Started WalkerToAlKharid");
//                    new WebWalkEvent(getBot(), alKharidCourseBeggining).execute();
//                } else if (me.distance(alKharidCourse.getAvgCenterTile()) <= 32) {
//                    logger.debug("Started AlKharidEvent");
//                    new AlKharidCourse(getBot(), true).execute();
//                }
        } else if (getBot().getClient().getSkillReal(Skill.AGILITY) <= 49) {
            if (me.distance(varrockCourse.getAvgCenterTile()) >= 28) {
                new WebWalkEvent(getBot(), varrockCourseBeggining).execute();
            } else if (me.distance(varrockCourse.getAvgCenterTile()) <= 27) {
                new VarrockCourse(getBot(), true).execute();
            }

        } else if (getBot().getClient().getSkillReal(Skill.AGILITY) >= 50) {
            setComplete();
        }

    }
}



