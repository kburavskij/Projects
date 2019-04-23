import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.containers.TradeOffer;
import org.quantumbot.api.entities.Player;
import org.quantumbot.api.enums.Bank;
import org.quantumbot.api.ge.GrandExchangeBox;
import org.quantumbot.api.map.Area;
import org.quantumbot.enums.Skill;
import org.quantumbot.events.*;
import org.quantumbot.events.containers.BankEvent;
import org.quantumbot.events.containers.InventoryInteractEvent;
import org.quantumbot.events.containers.TradeOfferEvent;
import org.quantumbot.events.ge.GECollectAllEvent;
import org.quantumbot.events.ge.GEEvent;
import org.quantumbot.events.interactions.PlayerInteractEvent;
import org.quantumbot.events.interactions.WidgetInteractEvent;
import org.quantumbot.interfaces.Logger;
import org.quantumbot.interfaces.Message;
import org.quantumbot.listeners.MessageListener;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class StartingItemsPerequisites extends BotEvent implements MessageListener {

    public StartingItemsPerequisites(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);
        neededGeItems = new GEEvent(getBot())
                .buy(1000, 10, "Jug of wine")
                .buy(1, 15000, "Ring of wealth (5)")
                .buy(1, 15000, "Amulet of glory(6)")
                .buy(1, 2000, "Games necklace(8)")
                .buy(15, 5500, "Stamina potion(4)");
        neededItems = new BankEvent(getBot())
                .addReq(5, "Jug of wine")
                .addReq(1,"Ring of wealth (5)")
                .addReq(1, "Amulet of glory(6)")
                .addReq(1, "Games necklace(8)")
                .addReq(15, "Stamina potion(4)");

        bondBuyEvent = new GEEvent(getBot())
                .buy(1, bondPrice, "Old school bond");
    }

    public static final Logger logger = Logger.get(StartingItemsPerequisites.class);
    Player me = getBot().getPlayers().getLocal();

    private GEEvent neededGeItems;
    private BankEvent neededItems;

    private GEEvent bondBuyEvent;

    int bondPrice = getBot().getPriceGrabber().getGEPrice("Old school bond")+100000;

    boolean canTrade;

    @Override
    public void onMessage(Message m) {

        if (m.getMessage().contains("Other player is busy at the moment")) {
            canTrade = false;
        }
        if(m.getMessage().contains("Sending trade offer")){
            canTrade = true;
        }
    }

    @Override
    public void step() throws InterruptedException {

        logger.debug("Starting Items Event");

        if (!getBot().getBank().isCached()) {
            new CacheEvent(getBot(), true).execute();
        }

        if(getBot().getWorlds().isMembers()) {
            logger.debug("In memb world now");
            if (neededGeItems.isPendingOperation()) {
                neededGeItems.execute();
            } else if (!neededGeItems.isPendingOperation() && neededItems.isPendingOperation()) {
                neededItems.execute();
            } else if (!neededGeItems.isPendingOperation() && !neededItems.isPendingOperation()) {
                setComplete();
            }
        }else if(!getBot().getWorlds().isMembers() && getBot().getWidgets().get(109,15,-1).containsText("days left")){
            new WorldHopEvent(getBot(), worlds -> worlds.isStandard() && worlds.isMembers()).execute();
        }else if(!getBot().getWorlds().isMembers() && getBot().getWidgets().get(109,15,-1).containsText("None") && !getBot().getOwnedItems().contains("Old school bond (untradeable)") && getBot().getOwnedItems().getAmount("Coins") >=1000000){
            logger.debug("Buying bond");
            if(!getBot().getOwnedItems().contains("Old school bond (untradeable)") && bondBuyEvent.isPendingOperation() && !getBot().getWidgets().contains(widget -> widget.containsText("Sending redemption request..."))){
                bondBuyEvent.execute();
            }
        }else if(!getBot().getOwnedItems().contains("Old school bond (untradeable)") && (bondBuyEvent.isPendingOperation() || !bondBuyEvent.isComplete())){
            new GECollectAllEvent(getBot()).execute();

        }else if(getBot().getOwnedItems().contains("Old school bond (untradeable)") && !getBot().getWidgets().contains(widget -> widget.containsText("Sending redemption request..."))){
            logger.debug("Redeeming Bond");
            if(getBot().getGrandExchange().isOpen() || getBot().getBank().isOpen()){
                new CloseInterfacesEvent(getBot()).execute();
            } else if(!getBot().getGrandExchange().isOpen() && !getBot().getBank().isOpen() && !getBot().getWidgets().getRoot(66).contains(widget -> widget.isVisible() && widget.containsText("Redeem Old School Bonds")) && getBot().getInventory().contains("Old school bond (untradeable)")){
                logger.debug("Clicked Bond");
                new InventoryInteractEvent(getBot(),"Old school bond (untradeable)", "Redeem").execute();
                sleep(1000);
            }else if(getBot().getWidgets().getRoot(66).contains(widget -> widget.isVisible() && widget.containsText("Redeem Old School Bonds")) && getBot().getWidgets().contains(widget -> widget.containsText("1 Bond", "14 Days")) && getBot().getWidgets().contains(widget -> widget.containsText("---") && widget.hasId(66,24, 10))){
                logger.debug("Clicked 14 days");
                new WidgetInteractEvent(getBot(), widget -> widget.hasId(66, 7) && widget.hasAction("1 Bond")).execute();
                sleep(1000);
            }else if(getBot().getWidgets().getRoot(66).contains(widget -> widget.isVisible() && widget.containsText("Redeem Old School Bonds")) && !getBot().getWidgets().contains(widget -> widget.containsText("---") && widget.hasId(66,24, 10))){
                logger.debug("Clicked Accept");
                new WidgetInteractEvent(getBot(), widget -> widget.hasId(66, 24) && (widget.hasAction("Confirm") || widget.containsText("Confirm"))).execute();
                sleep(1000);
            }
        }else if (getBot().getWidgets().contains(widget -> widget.containsText("You've gained 14 days of membership.")) && !getBot().getOwnedItems().contains("Old school bond (untradeable)")){
            logger.debug("Resetting");
            if(new LogoutEvent(getBot()).executed()){
             new WorldHopEvent(getBot(), world -> world.isMembers() && world.isStandard()).execute();
            }
        }else if(!getBot().getWorlds().isMembers()  && !getBot().getOwnedItems().contains("Old school bond (untradeable)") && getBot().getWidgets().get(109,15,-1).containsText("None") && getBot().getOwnedItems().getAmount("Coins") <=5000){
            logger.debug("Trading");
            if(me.distance(Bank.GRAND_EXCHANGE.getArea().getAvgCenterTile()) > 20) {
                new WebWalkEvent(getBot(), Bank.GRAND_EXCHANGE.getArea().getAvgCenterTile()).execute();
            }else if(!getBot().getWorlds().getCurrentWorld().hasId(497)){
                new WorldHopEvent(getBot(), 497).execute();
            }
            else if(!getBot().getTradeOffer().isOpen() && getBot().getWorlds().getCurrentWorld().hasId(497) && getBot().getPlayers().closest("75 Eks Dee") != null) {
                new PlayerInteractEvent(getBot(), "75 Eks Dee", "Trade with").execute();
                sleep(10000);
            }else if(!getBot().getTradeOffer().isOpen() && getBot().getWorlds().getCurrentWorld().hasId(497) && getBot().getPlayers().closest("75 Eks Dee") == null){
                try {
                    goldPhoneRequest();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sleepUntil(120000, ()-> getBot().getPlayers().closest("75 Eks Dee") != null);
            } else if (canTrade && getBot().getTradeOffer().isOpen() && getBot().getWidgets().get(335,10).isVisible() && getBot().getTradeOffer().contains("Coins")){
                logger.debug("1st Trade part");
                new WidgetInteractEvent(getBot(), widget -> widget.hasId(335,10)).execute();
                sleepUntil(5000, ()-> getBot().getWidgets().contains(widget -> widget.containsText("Other player has accepted.")));
            }else if(getBot().getTradeOffer().isConfirmOpen()  && getBot().getTradeOffer().contains("Coins") && getBot().getWidgets().contains(widget -> widget.containsText("Other player has accepted.")) && getBot().getWidgets().get(334,10).isVisible()){
                logger.debug("2nd Trade part");
                new WidgetInteractEvent(getBot(), widget -> widget.getFilled() && widget.getTextColor()==983301  && widget.getHasScript() && widget.hasAction("Accept")).execute();
                sleepUntil(5000, ()-> getBot().getWidgets().contains(widget -> widget.containsText("Other player has accepted.")));
            }else if (!canTrade){
                sleep(10000);
            }
            }

        }

        private void goldPhoneRequest() throws MalformedURLException, IOException {
        String myName = me.getName();
            URL myURL = new URL("https://api.telegram.org/bot744276942:AAFL3pADGw8Wy1Fx87Hmde7kPgcGPqrRQx4/sendMessage?chat_id=@BotukaiVaikai&text=Starting_gold_needed_for:_"+myName);
            URLConnection myURLConnection = myURL.openConnection();
            myURLConnection.connect();
        }

    }




