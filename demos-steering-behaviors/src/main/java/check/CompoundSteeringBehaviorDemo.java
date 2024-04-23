package check;

import com.jme3.ai.agents.AIControl;
import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviors.npc.SimpleMainBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.AlignmentBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.CompoundSteeringBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.SeparationBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.WanderAreaBehavior;
import com.jme3.ai.agents.util.control.MonkeyBrainsAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.system.AppSettings;
import java.util.ArrayList;
import java.util.List;
import jme3utilities.debug.AxesVisualizer;
import jme3utilities.math.noise.Generator;
import jme3utilities.mesh.Octasphere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompoundSteeringBehaviorDemo extends SimpleApplication {

  public static final int MAX_AGENTS = 32;

  public static void main(String[] args) {
    AppSettings settings = new AppSettings(true);
    settings.setResolution(1600, 800);
    
    CompoundSteeringBehaviorDemo app = new CompoundSteeringBehaviorDemo();
    
    app.setSettings(settings);
    app.setShowSettings(false);
    
    app.start();
  }

  private static final Logger logger = LoggerFactory.getLogger(CompoundSteeringBehaviorDemo.class);
  
  @Override
  public void simpleInitApp() {
    logger.debug("initializing");

    stateManager.attach(MonkeyBrainsAppState.getInstance());
    
    flyCam.setDragToRotate(true);
    flyCam.setMoveSpeed(25);
    flyCam.setZoomSpeed(0);
    
    rootNode.addControl(new AxesVisualizer(assetManager, MAX_AGENTS, 1));
    rootNode.getControl(AxesVisualizer.class).setEnabled(true);

    List<AIControl> obstacles = new ArrayList<>(MAX_AGENTS);
    List<AIControl> neighbours = new ArrayList<>(MAX_AGENTS);
    
    Mesh mesh = new Octasphere(1, 0.1f);
    Material material = new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
    
    Vector3f area = new Vector3f(16f, 16f, 16f);
    Generator random = new Generator(42);
    
    for (int i = 0; i < MAX_AGENTS; i++) {
      Agent<Void> agent = new Agent<>(0.1f);
      
      obstacles.add(agent);
      neighbours.add(agent);
      
      CompoundSteeringBehavior steer = new CompoundSteeringBehavior();

      WanderAreaBehavior wandering = new WanderAreaBehavior();
      wandering.setArea(new Vector3f(), area);
      wandering.setupStrengthControl(0.35f);
      steer.addSteerBehavior(wandering);
      
      SeparationBehavior separation = new SeparationBehavior(obstacles);
      separation.setupStrengthControl(0.85f);
      steer.addSteerBehavior(separation);
      
      AlignmentBehavior alignment = new AlignmentBehavior(neighbours);
      alignment.setupStrengthControl(2.15f);
      steer.addSteerBehavior(alignment);

      // SimpleMainBehavior
      SimpleMainBehavior simple = new SimpleMainBehavior();
      simple.addBehavior(steer);
      agent.setMainBehavior(simple);

      Geometry geometry = new Geometry("boid-%s".formatted(i), mesh);
      geometry.setMaterial(material);
      geometry.setLocalTranslation(random.nextVector3f().mult(16f));
      geometry.addControl(agent);
      rootNode.attachChild(geometry);

      stateManager.getState(MonkeyBrainsAppState.class).addAgent(agent);
    }

    logger.debug("initialized");
  }
}
