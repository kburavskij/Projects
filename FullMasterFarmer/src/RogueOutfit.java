import org.quantumbot.api.QuantumBot;
import org.quantumbot.api.entities.Player;
import org.quantumbot.api.map.Area;
import org.quantumbot.api.map.Tile;
import org.quantumbot.events.BotEvent;
import org.quantumbot.events.CloseInterfacesEvent;
import org.quantumbot.events.DialogueEvent;
import org.quantumbot.events.containers.BankEvent;
import org.quantumbot.events.containers.BankOpenEvent;
import org.quantumbot.events.containers.InventoryInteractEvent;

public class RogueOutfit extends BotEvent {

    public RogueOutfit(QuantumBot bot, boolean onlyInGame) {
        super(bot, onlyInGame);
    }



    @Override
    public void step() throws InterruptedException {

        if (!getBot().getBank().isCached()) {
            new CacheEvent(getBot(),true).execute();
        } else if (getBot().getBank().isCached()) {
            if(!getBot().getOwnedItems().contains("Rogue mask") || !getBot().getOwnedItems().contains("Rogue top") || !getBot().getOwnedItems().contains("Rogue trousers") || !getBot().getOwnedItems().contains("Rogue boots") || !getBot().getOwnedItems().contains("Rogue gloves")) {
                if (!getBot().getInventory().contains("Rogue's equipment crate")) {
                    new BankEvent(getBot())
                            .addReq(5, "Rogue's equipment crate")
                            .execute();
                        new CloseInterfacesEvent(getBot()).execute();
                }else if (getBot().getInventory().contains("Rogue's equipment crate")) {
                    if(getBot().getBank().isOpen()){
                        new CloseInterfacesEvent(getBot()).execute();
                    }
                      if (!getBot().getOwnedItems().contains("Rogue mask")) {
                          new InventoryInteractEvent(getBot(), "Rogue's equipment crate", "Search").execute();
                          sleep(1500);
                          if (getBot().getDialogues().inDialogue()) {
                              new DialogueEvent(getBot(), "A piece of Rogue equipment", "Mask.").execute();
                              new DialogueEvent(getBot()).execute();
                          }
                      } else if (!getBot().getOwnedItems().contains("Rogue top")) {
                          new InventoryInteractEvent(getBot(), "Rogue's equipment crate", "Search").execute();
                          sleep(1500);
                          if (getBot().getDialogues().inDialogue()) {
                              new DialogueEvent(getBot(), "A piece of Rogue equipment", "Top.").execute();
                              new DialogueEvent(getBot()).execute();
                          }
                      } else if (!getBot().getOwnedItems().contains("Rogue trousers")) {
                          new InventoryInteractEvent(getBot(), "Rogue's equipment crate", "Search").execute();
                          sleep(1500);
                          if (getBot().getDialogues().inDialogue()) {
                              new DialogueEvent(getBot(), "A piece of Rogue equipment", "Trousers.").execute();
                              new DialogueEvent(getBot()).execute();
                          }
                      } else if (!getBot().getOwnedItems().contains("Rogue boots")) {
                          new InventoryInteractEvent(getBot(), "Rogue's equipment crate", "Search").execute();
                          sleep(1500);
                          if (getBot().getDialogues().inDialogue()) {
                              new DialogueEvent(getBot(), "A piece of Rogue equipment", "Boots.").execute();
                              new DialogueEvent(getBot()).execute();
                          }
                      } else if (!getBot().getOwnedItems().contains("Rogue gloves")) {
                          new InventoryInteractEvent(getBot(), "Rogue's equipment crate", "Search").execute();
                          sleep(1500);
                          if (getBot().getDialogues().inDialogue()) {
                              new DialogueEvent(getBot(), "A piece of Rogue equipment", "Gloves.").execute();
                              new DialogueEvent(getBot()).execute();
                          }
                      }
                  }
            }else if(getBot().getOwnedItems().contains("Rogue mask") && getBot().getOwnedItems().contains("Rogue top") && getBot().getOwnedItems().contains("Rogue trousers") && getBot().getOwnedItems().contains("Rogue boots") && getBot().getOwnedItems().contains("Rogue gloves")){
                setComplete();
            }
        }
    }
}
