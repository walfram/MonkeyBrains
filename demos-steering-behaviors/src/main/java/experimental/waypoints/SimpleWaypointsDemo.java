package experimental.waypoints;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;

public class SimpleWaypointsDemo extends SimpleApplication {

  public static void main(String[] args) {
    AppSettings settings = new AppSettings(true);
    settings.setResolution(1600, 800);

    SimpleWaypointsDemo app = new SimpleWaypointsDemo();
    app.setSettings(settings);
    app.setShowSettings(false);
    app.setPauseOnLostFocus(false);
    app.start();
  }

  @Override
  public void simpleInitApp() {
    stateManager.attach(new SimpleWaypointsInit(rootNode));
    
    stateManager.attach(new SimpleWaypointsState(rootNode));
    stateManager.attach(new NpcState(rootNode));
  }
}
