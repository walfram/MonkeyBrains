package experimental.waypoints;

import com.jme3.scene.Spatial;

public interface Action<T extends ActionContext> {

  void update(float tpf);
  
  boolean isDone();

  Action<T> nextAction(T context, Spatial subject);
}
