package demo.game;

import com.jme3.ai.agents.AIControl;
import com.jme3.ai.agents.Agent;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import demo.model.Model;
import jme3utilities.SimpleControl;
import jme3utilities.debug.PointVisualizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectilesState extends BaseAppState {

  private static final Logger logger = LoggerFactory.getLogger(ProjectilesState.class);

  private final Node scene = new Node("projectiles-scene");
  
  private final float bulletSpeed = 50f;
  
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
    
    scene.attachChild(bullet);
    
    bullet.setLocalTranslation(agent.getWorldTranslation());

    Vector3f direction = target.getWorldTranslation().subtract(agent.getWorldTranslation()).normalize();
    
    bullet.addControl(new SimpleControl() {

      private float distance = 0f;
      private final Vector3f dir = direction.clone();
      
      @Override
      protected void controlUpdate(float updateInterval) {
        Vector3f delta = direction.mult(bulletSpeed * updateInterval);
        getSpatial().move(delta);
        distance += delta.length();
        
        if (distance > 300)
          bullet.removeFromParent();
      }
    });
    
    model.weapons().startCoolDown();
  }
}
