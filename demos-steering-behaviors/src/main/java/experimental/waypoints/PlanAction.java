package experimental.waypoints;

import com.jme3.anim.AnimComposer;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlanAction implements Action<NpcMoveContext> {

  private static final Logger logger = LoggerFactory.getLogger(PlanAction.class);
  
  private final NpcMoveContext context;
  
  private Vector3f nextWaypoint;

  public PlanAction(NpcMoveContext context) {
    this.context = context;
  }

  @Override
  public void update(float tpf) {
    Vector3f currentWaypoint = context.currentWaypoint();
    
    Vector3f picked = context.planingRandom().pick(context.waypoints());
    while (picked.equals(currentWaypoint)) {
      picked = context.planingRandom().pick(context.waypoints());
    }
    
    logger.debug("planning to move from {} to {}", currentWaypoint, picked);
    nextWaypoint = picked;
    context.updateCurrentWaypoint(picked);
  }

  @Override
  public boolean isDone() {
    return nextWaypoint != null;
  }

  @Override
  public Action<NpcMoveContext> nextAction(NpcMoveContext context, Spatial subject) {
    subject.getControl(AnimComposer.class).setCurrentAction("Run");
    return new MoveAction(subject, nextWaypoint);
  }

}
