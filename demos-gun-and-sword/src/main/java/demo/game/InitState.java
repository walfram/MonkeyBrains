package demo.game;

import com.jme3.app.Application;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.state.BaseAppState;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import jme3utilities.debug.AxesVisualizer;

public class InitState extends BaseAppState {

  private final Node rootNode;

  public InitState(Node rootNode) {
    this.rootNode = rootNode;
  }

  @Override
  protected void initialize(Application app) {
    getState(FlyCamAppState.class).getCamera().setDragToRotate(true);
    getState(FlyCamAppState.class).getCamera().setMoveSpeed(25f);
    getState(FlyCamAppState.class).getCamera().setZoomSpeed(0);

    rootNode.addLight(new AmbientLight(new ColorRGBA(0.7f, 0.8f, 1.0f, 1f)));
    rootNode.addLight(new DirectionalLight(
        new Vector3f(-0.5501984f, -0.6679371f, 0.5011405f),
        new ColorRGBA(1.0f, 1.0f, 0.7f, 1f)
    ));

    AxesVisualizer axesVisualizer = new AxesVisualizer(app.getAssetManager(), 128, 1);
    rootNode.addControl(axesVisualizer);
    axesVisualizer.setEnabled(true);
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
