package experimental.waypoints;

import com.jme3.scene.Spatial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdleAction implements Action<NpcMoveContext> {

  private static final Logger logger = LoggerFactory.getLogger(IdleAction.class);
  
  private float eta;

  public IdleAction(NpcMoveContext context) {
    this.eta = context.idleRandom().nextFloat(1, 5);
    logger.debug("idling for {} sec", eta);
  }
  
  @Override
  public void update(float tpf) {
    eta -= tpf;
    if (eta < 0)
      eta = 0;
  }
  
  public float eta() {
    return eta;
  }

  @Override
  public boolean isDone() {
    return eta <= 0;
  }

  @Override
  public Action<NpcMoveContext> nextAction(NpcMoveContext context, Spatial subject) {
    return new PlanAction(context);
  }

}
