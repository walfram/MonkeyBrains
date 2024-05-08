package demo.game;

import com.jme3.ai.agents.util.control.MonkeyBrainsAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.scene.plugins.blender.BlenderLoader;
import com.jme3.system.AppSettings;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class GunAndSwordDemo extends SimpleApplication {

  public static void main(String[] args) {
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();
    
    AppSettings settings = new AppSettings(true);
    settings.setResolution(1600, 800);
    
    GunAndSwordDemo app = new GunAndSwordDemo();
    app.setSettings(settings);
    app.setShowSettings(false);
    app.start();
  }
  
  @Override
  public void simpleInitApp() {
    assetManager.registerLoader(BlenderLoader.class, "blend");
    
    stateManager.attach(new InitState(rootNode));
    
    stateManager.attach(MonkeyBrainsAppState.getInstance());
    MonkeyBrainsAppState.getInstance().setApp(this);
    stateManager.attach(new BulletAppState());
    
    stateManager.attach(new ResourcesState());
    stateManager.attach(new CameraState());

    stateManager.attach(new ProjectilesState(rootNode));
    
    stateManager.attach(new AgentContextState());
    
    stateManager.attach(new SceneState(rootNode));
    stateManager.attach(new PlayerState(rootNode));
    
    stateManager.attach(new ControlState());
  }
}
