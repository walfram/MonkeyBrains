package check;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import demo.game.AnimationControl;
import demo.game.ResourcesState;
import java.util.List;

public class CharState extends BaseAppState {
  
  private final Node scene = new Node("char-scene");

  private Spatial character;
  
  public CharState(Node rootNode) {
    rootNode.attachChild(scene);
  }

  @Override
  protected void initialize(Application app) {
    character = getState(ResourcesState.class).characterSpatial();
    scene.attachChild(character);
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

  public void stand() {
    character.getControl(AnimationControl.class).stand();
  }

  public void run() {
    character.getControl(AnimationControl.class).run();
  }

  public void strike() {
    character.getControl(AnimationControl.class).strike();
  }

  public void shoot() {
    character.getControl(AnimationControl.class).shoot();
    // create shoot particles
  }
}
