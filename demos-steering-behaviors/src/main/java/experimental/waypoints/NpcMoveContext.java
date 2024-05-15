package experimental.waypoints;

import com.jme3.math.Vector3f;
import java.util.List;
import jme3utilities.math.noise.Generator;

public class NpcMoveContext implements ActionContext {

  private final List<Vector3f> waypoints;

  private Vector3f currentWaypoint;
  private final Generator planningRandom = new Generator(42);
  private final Generator idleGenerator = new Generator(43);

  public NpcMoveContext(List<Vector3f> waypoints, Vector3f currentWaypoint) {
    this.waypoints = waypoints;
    this.currentWaypoint = currentWaypoint;
  }
  
  public List<Vector3f> waypoints() {
    return List.copyOf(waypoints);
  }
  
  public void updateCurrentWaypoint(Vector3f currentWaypoint) {
    this.currentWaypoint = currentWaypoint;
  }

  public Vector3f currentWaypoint() {
    return currentWaypoint;
  }

  public Generator planingRandom() {
    return planningRandom;
  }

  public Generator idleRandom() {
    return idleGenerator;
  }
}
