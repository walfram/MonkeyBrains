package experimental.waypoints;

import com.jme3.anim.AnimComposer;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class MoveAction implements Action<NpcMoveContext> {
  
  private final Spatial subject;
  private final Vector3f moveTo;

  private boolean done = false;

  public MoveAction(Spatial subject, Vector3f moveTo) {
    this.subject = subject;
    this.moveTo = moveTo;
  }

  @Override
  public void update(float tpf) {
    Vector3f v = moveTo.subtract(subject.getWorldTranslation());

    Quaternion q = new Quaternion();
    Vector3f direction = v.normalize();
    q.lookAt(direction, Vector3f.UNIT_Y);
    
    subject.getLocalRotation().slerp(q, FastMath.HALF_PI * tpf);

    // TODO sometimes subject is circling around waypoint - needs fix
    
    final float speed = 5f;
    if (v.length() >= 0.1f) {
      Vector3f forward = subject.getLocalRotation().mult(Vector3f.UNIT_Z);

      Vector3f delta = forward.multLocal(tpf * speed);
      subject.move(delta);
    } else {
      done = true;
    }
  }

  @Override
  public boolean isDone() {
    return done;
  }

  @Override
  public Action<NpcMoveContext> nextAction(NpcMoveContext context, Spatial subject) {
    subject.getControl(AnimComposer.class).setCurrentAction("Idle");
    return new IdleAction(context);
  }

}
