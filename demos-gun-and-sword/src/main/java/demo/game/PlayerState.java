package demo.game;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerState extends BaseAppState {

  private static final Logger logger = LoggerFactory.getLogger(PlayerState.class);

  private final float moveSpeed = 5f;
  private final float strafeSpeed = 2.5f;

  private final Node scene = new Node("scene");

  private Spatial player;

  public PlayerState(Node rootNode) {
    rootNode.attachChild(scene);
  }

  @Override
  protected void initialize(Application app) {
    player = getState(ResourcesState.class).characterSpatial();
    scene.attachChild(player);

    getState(CameraState.class).chase(player);
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

  public void move(double value, double tpf) {
    Vector3f direction = getApplication().getCamera().getDirection();
    direction.setY(0);
    direction.normalizeLocal();
    player.getLocalRotation().lookAt(direction, Vector3f.UNIT_Y);
    
    float delta = (float) (value * tpf) * moveSpeed;
    direction.multLocal(delta);
    player.move(direction);
  }

  public void strafe(double value, double tpf) {
    Vector3f right = player.getLocalRotation().mult(Vector3f.UNIT_X);
    float delta = (float) (value * tpf) * strafeSpeed;
    right.multLocal(delta);
    player.move(right);
  }

  public void walking(boolean walking) {
    if (walking) {
      player.getControl(AnimationControl.class).run();
    } else {
      player.getControl(AnimationControl.class).stand();
    }
  }
}
