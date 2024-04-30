package demo.game;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviors.npc.steering.WanderAreaBehavior;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.util.AnimMigrationUtils;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
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
//        Vector3f direction = child.getLocalRotation().mult(Vector3f.UNIT_Z);
        Spatial npc = app.getAssetManager().loadModel("Models/Demo_01/characters/character_01/character_01.j3o");
        logger.debug("npc at {}", npc.getWorldTranslation());
        logger.debug("npc bound = {}", npc.getWorldBound());
        // TODO get npc radius
        Node armature = (Node) ((Node) npc).getChild("Armature");
        Node characterMan = (Node) armature.getChild("characterMan");
        Node characterManEntity = (Node) characterMan.getChild("characterMan-entity");
        Node entity = (Node) characterManEntity.getChild("characterMan-ogremesh");
        AnimMigrationUtils.migrate(entity);
        entity.getControl(AnimComposer.class).setCurrentAction("base_stand");
        ((Node) child).detachAllChildren();
        ((Node) child).attachChild(npc);
        
        // FIXME use npc radius
        Agent<Model> agent = new Agent<>(1f);
        agent.setModel(new Model());
        agent.setMaxMoveSpeed(1f);
        agent.setRotationSpeed(FastMath.DEG_TO_RAD * 10f);
        
        WanderAreaBehavior wander = new WanderAreaBehavior();
        wander.setArea(npc.getWorldTranslation(), new Vector3f(2, 0, 2));
        agent.setMainBehavior(wander);

        child.addControl(agent);

        logger.debug("agent = {}", agent);

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
