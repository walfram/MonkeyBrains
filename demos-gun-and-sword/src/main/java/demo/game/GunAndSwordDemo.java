package demo.game;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.plugins.blender.BlenderLoader;
import com.jme3.system.AppSettings;

public class GunAndSwordDemo extends SimpleApplication {

  public static void main(String[] args) {
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
    stateManager.attach(new SceneState(rootNode));
    stateManager.attach(new PlayerState(rootNode));
  }
}
