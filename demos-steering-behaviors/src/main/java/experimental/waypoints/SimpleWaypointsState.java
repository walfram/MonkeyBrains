package experimental.waypoints;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;
import jme3utilities.debug.PointVisualizer;
import jme3utilities.math.noise.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleWaypointsState extends BaseAppState {

  private static final Logger logger = LoggerFactory.getLogger(SimpleWaypointsState.class);

  private final Node rootNode;

  private final List<Vector3f> waypoints = new ArrayList<>(64);

  public SimpleWaypointsState(Node rootNode) {
    this.rootNode = rootNode;
  }

  @Override
  protected void initialize(Application app) {
    Generator random = new Generator(42);

    List<Vector3f> seed = new ArrayList<>(1024);

    while (seed.size() < 1024) {
      Vector3f v = random.nextVector3f().mult(32f).setY(0);
      seed.add(v);
    }

    while (!seed.isEmpty()) {
      Vector3f picked = random.pick(seed);
      seed.removeIf(e -> e.distance(picked) <= 8f);
      waypoints.add(picked);
    }

    logger.debug("waypoints = {}", waypoints.size());

    for (Vector3f wp : waypoints) {
      PointVisualizer visualizer = new PointVisualizer(app.getAssetManager(), 25, ColorRGBA.Orange, "pin");
      visualizer.setLocalTranslation(wp);
      rootNode.attachChild(visualizer);
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

  public Vector3f initialWaypoint() {
    return new Vector3f(waypoints.get(0));
  }

  public List<Vector3f> waypoints() {
    return List.copyOf(waypoints);
  }
}
