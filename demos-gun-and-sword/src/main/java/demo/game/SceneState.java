package demo.game;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SceneState extends BaseAppState {

  private static final Logger logger = LoggerFactory.getLogger(SceneState.class);
  
  private final Node scene = new Node("scene");
  
  public SceneState(Node rootNode) {
    rootNode.attachChild(scene);
  }

  @Override
  protected void initialize(Application app) {
    Spatial sceneSpatial = app.getAssetManager().loadModel("Models/Demo_01/Scene_01/scene_01.blend");
    scene.attachChild(sceneSpatial);

    Material debugMaterial = new Material(app.getAssetManager(), "Common/MatDefs/Misc/ShowNormals.j3md");
    sceneSpatial.setMaterial(debugMaterial);
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
