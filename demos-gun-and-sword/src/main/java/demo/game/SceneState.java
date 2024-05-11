package demo.game;

import com.jme3.ai.agents.AIControl;
import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviors.Behavior;
import com.jme3.ai.agents.behaviors.npc.SimpleMainBehavior;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import demo.ai.AILookBehavior;
import demo.ai.AIStaticObjectControl;
import demo.ai.AIStaticObjectType;
import jme3utilities.debug.BoundsVisualizer;
import jme3utilities.mesh.LoopMesh;
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
      logger.debug("adding to scene = {}", child);
      if (child.getName().contains("characterMan")) {
        getState(AgentContextState.class).createNpcAgent(child);

        Spatial characterSpatial = getState(ResourcesState.class).characterSpatial();
        ((Node) child).detachAllChildren();
        ((Node) child).attachChild(characterSpatial);

        // debug bounds
//        BoundsVisualizer boundsVisualizer = new BoundsVisualizer(app.getAssetManager());
//        boundsVisualizer.setSubject(child);
//        scene.getParent().addControl(boundsVisualizer);
//        boundsVisualizer.setEnabled(true);
        
        // debug visibilisty
        Agent a = child.getControl(Agent.class);
        SimpleMainBehavior mb = (SimpleMainBehavior) a.getMainBehavior();
        AILookBehavior lookBehavior = (AILookBehavior) mb.getBehaviors()
            .stream().filter(b -> b instanceof AILookBehavior).findFirst().orElseThrow();
        float visibilityRange = lookBehavior.getVisibilityRange();
        Geometry visibility = new Geometry("visibility-%s".formatted(a.getName()), new LoopMesh(24, visibilityRange));
        Material material = new Material(app.getAssetManager(), Materials.UNSHADED);
        material.setColor("Color", ColorRGBA.Blue);
        material.getAdditionalRenderState().setDepthTest(false);
        visibility.setMaterial(material);
        ((Node) child).attachChild(visibility);
      } else {
        CollisionShape cShape = CollisionShapeFactory.createMeshShape(child);
        AIStaticObjectType objType;
        if (child.getName().equals("floor")) {
          objType = AIStaticObjectType.Floor;
        } else {
          objType = AIStaticObjectType.Obstacle;
        }
        AIStaticObjectControl rg = new AIStaticObjectControl(objType, cShape, 0f);
        child.addControl(rg);
        app.getStateManager().getState(BulletAppState.class).getPhysicsSpace().add(rg);
//        scene.attachChild(child);
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
