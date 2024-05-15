package experimental.compound;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.util.control.MonkeyBrainsAppState;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Mesh.Mode;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;
import java.util.List;
import jme3utilities.debug.PointVisualizer;
import jme3utilities.math.noise.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaypointsState extends BaseAppState {

  private static final Logger logger = LoggerFactory.getLogger(WaypointsState.class);
  
  private final Node scene = new Node("waypoints-scene");

  public WaypointsState(Node rootNode) {
    rootNode.attachChild(scene);
  }

  @Override
  protected void initialize(Application app) {
    int domainSize = 1024;
    List<Vector3f> domain = new ArrayList<>(domainSize);
    Generator random = new Generator(123);

    for (int i = 0; i < domainSize; i++) {
      Vector3f point = random.nextVector3f();
      point.multLocal(32f);
      point.setY(0);
      domain.add(point);
    }
    
    // TODO filter domain

    List<Vector3f> waypoints = new ArrayList<>();
    while (!domain.isEmpty()) {
      Vector3f waypoint = random.pick(domain);
      domain.remove(waypoint);
      waypoints.add(waypoint);
      
      logger.debug("waypoint = {}", waypoint);
      
      domain.removeIf(e -> waypoint.distance(e) <= 8f);
    }

    Mesh mesh = new Mesh();
    mesh.setMode(Mode.Points);

    Vector3f[] vertices = waypoints.toArray(Vector3f[]::new);
    mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
    
    mesh.updateCounts();
    mesh.updateBound();

    Geometry geometry = new Geometry("waypoints", mesh);
    
    Material material = new Material(app.getAssetManager(), Materials.UNSHADED);
    material.setColor("Color", ColorRGBA.Yellow);
    material.setFloat("PointSize", 4f);
    
    geometry.setMaterial(material);
    
    scene.attachChild(geometry);

    for (Vector3f waypoint : waypoints) {
      PointVisualizer visualizer = new PointVisualizer(app.getAssetManager(), 6, ColorRGBA.Orange, "cross");
      visualizer.setName(waypoint.toString());
      visualizer.setLocalTranslation(waypoint);

      Agent<Void> agent = new Agent<>(0.5f);
      visualizer.addControl(agent);
      
      getState(MonkeyBrainsAppState.class).addAgent(agent);
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
