package demo.game;

import com.jme3.ai.agents.AIControl;
import com.jme3.ai.agents.Agent;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import demo.model.Model;
import jme3utilities.SimpleControl;
import jme3utilities.debug.PointVisualizer;
import jme3utilities.mesh.Octasphere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectilesState extends BaseAppState {

  private static final Logger logger = LoggerFactory.getLogger(ProjectilesState.class);

  private final Node scene = new Node("projectiles-scene");
  
  private final float bulletSpeed = 10f;
  
  public ProjectilesState(Node rootNode) {
    rootNode.attachChild(scene);
  }

  @Override
  protected void initialize(Application app) {
    
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

  public void spawn(Agent<Model> agent, AIControl target) {
    Model model = agent.getModel();
    
    if (model.weapons().isCoolDown())
      return;

    logger.debug("spawning bullet from {} to {}", agent, target);
    
    Node bullet = new Node("bullet");
    
    PointVisualizer visualizer = new PointVisualizer(getApplication().getAssetManager(), 4, ColorRGBA.Red, "square");
    bullet.attachChild(visualizer);

    Geometry geometry = new Geometry("bullet-geometry", new Octasphere(1, 0.125f));
    geometry.setMaterial(new Material(getApplication().getAssetManager(), "Common/MatDefs/Misc/ShowNormals.j3md"));
    bullet.attachChild(geometry);
    
    scene.attachChild(bullet);
    
    bullet.setLocalTranslation(agent.getWorldTranslation());
    bullet.move(0, 1, 0);

    Vector3f direction = target.getWorldTranslation().subtract(agent.getWorldTranslation()).normalize();
    
    bullet.addControl(new SimpleControl() {

      private float distance = 0f;
      private final Vector3f dir = direction.clone();
      
      @Override
      protected void controlUpdate(float updateInterval) {
        Vector3f delta = dir.mult(bulletSpeed * updateInterval);
        getSpatial().move(delta);
        distance += delta.length();
        
        if (distance > 300)
          bullet.removeFromParent();
      }
    });
    
    model.weapons().startCoolDown();
  }
}
