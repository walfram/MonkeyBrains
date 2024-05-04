package check;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.style.ElementId;

public class GuiState extends BaseAppState {

  private final Node scene = new Node("scene");

  public GuiState(Node guiNode) {
    guiNode.attachChild(scene);
  }

  @Override
  protected void initialize(Application app) {
    Container container = new Container();

    Label header = container.addChild(new Label("animations", new ElementId("header")));
    header.setMaxWidth(128f);

    // "base_stand", "run_01", "shoot", "strike_sword"
    container.addChild(new Button("idle")).addClickCommands(b -> getState(CharState.class).stand());
    container.addChild(new Button("run")).addClickCommands(b -> getState(CharState.class).run());
    container.addChild(new Button("shoot")).addClickCommands(b -> getState(CharState.class).shoot());
    container.addChild(new Button("strike")).addClickCommands(b -> getState(CharState.class).strike());

    scene.attachChild(container);
    container.setLocalTranslation(10, app.getCamera().getHeight() - 10, 0);
  }

  @Override
  protected void cleanup(Application app) {
  }

  @Override
  protected void onEnable() {
  }

  @Override
  protected void onDisable() {
  }
}
