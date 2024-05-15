package experimental.waypoints;

import jme3utilities.SimpleControl;

public class NpcActionControl<T extends ActionContext> extends SimpleControl {

  private Action<T> current;
  private final T context;

  public NpcActionControl(Action<T> initialAction, T context) {
    current = initialAction;
    this.context = context;
  }

  @Override
  protected void controlUpdate(float updateInterval) {
    current.update(updateInterval);

    if (current.isDone()) {
      current = current.nextAction(context, getSpatial());
    }
  }
}
