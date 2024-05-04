package check;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import demo.game.InitState;
import demo.game.ResourcesState;

public class CharDemo extends SimpleApplication {

  public static void main(String[] args) {
    AppSettings settings = new AppSettings(true);
    settings.setResolution(1600, 800);
    
    CharDemo app = new CharDemo();
    app.setSettings(settings);
    app.setShowSettings(false);
    app.start();
  }
  
  @Override
  public void simpleInitApp() {
    stateManager.attach(new InitState(rootNode));
    stateManager.attach(new ResourcesState());
    stateManager.attach(new BackgroundState(rootNode));
    stateManager.attach(new CharState(rootNode));
    stateManager.attach(new GuiState(guiNode));
  }
  
}
