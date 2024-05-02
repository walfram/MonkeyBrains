package demo.game;

import com.jme3.ai.agents.Agent;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import demo.model.Model;
import jme3utilities.debug.BoundsVisualizer;
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
    Spatial spatial = app.getAssetManager().loadModel("Models/Demo_01/Scene_01/scene_01.blend");
    scene.attachChild(spatial);

//    Material debugMaterial = new Material(app.getAssetManager(), "Common/MatDefs/Misc/ShowNormals.j3md");
//    spatial.setMaterial(debugMaterial);

    Spatial baseScene = ((Node) spatial).getChild("Scene");
    
    Spatial wall = ((Node) spatial).getChild("wall");
    Material wallMaterial = new Material(app.getAssetManager(), Materials.LIGHTING);
    wallMaterial.setBoolean("UseMaterialColors", true);
    wallMaterial.setColor("Diffuse", ColorRGBA.Gray);
    wallMaterial.setColor("Ambient", ColorRGBA.DarkGray);
    wall.setMaterial(wallMaterial);
    
    Spatial floor = ((Node) spatial).getChild("floor");
    Material floorMaterial = new Material(app.getAssetManager(), Materials.LIGHTING);
    floorMaterial.setBoolean("UseMaterialColors", true);
    floorMaterial.setColor("Diffuse", ColorRGBA.Brown);
    floorMaterial.setColor("Ambient", ColorRGBA.Black);
    floor.setMaterial(floorMaterial);

    for (Spatial child : ((Node) baseScene).getChildren()) {
      if (child.getName().contains("characterMan")) {
        getState(GameState.class).createNpcAgent(child);

        Spatial characterSpatial = getState(ResourcesState.class).characterSpatial();
        ((Node) child).detachAllChildren();
        ((Node) child).attachChild(characterSpatial);

        // debug bounds
        BoundsVisualizer boundsVisualizer = new BoundsVisualizer(app.getAssetManager());
        boundsVisualizer.setSubject(child);
        scene.getParent().addControl(boundsVisualizer);
        boundsVisualizer.setEnabled(true);
      } else {
        logger.debug("child = {}", child);
        // TODO make wall and floor physics objects
      }
    }

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
