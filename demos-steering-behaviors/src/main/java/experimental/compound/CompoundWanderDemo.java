package experimental.compound;

import com.jme3.ai.agents.util.control.MonkeyBrainsAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;

public class CompoundWanderDemo extends SimpleApplication {

  public static void main(String[] args) {
    AppSettings settings = new AppSettings(true);
    settings.setResolution(1600, 800);

    CompoundWanderDemo app = new CompoundWanderDemo();
    app.setSettings(settings);
    app.setShowSettings(false);
    app.start();
  }
  
  @Override
  public void simpleInitApp() {
    stateManager.attach(new CompoundWanderInit(rootNode));

    stateManager.attach(MonkeyBrainsAppState.getInstance());
    MonkeyBrainsAppState.getInstance().setApp(this);
    
    stateManager.attach(new WaypointsState(rootNode));
    
    stateManager.attach(new AgentsState(rootNode));
  }
}
