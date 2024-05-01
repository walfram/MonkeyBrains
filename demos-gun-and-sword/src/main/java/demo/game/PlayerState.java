package demo.game;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.util.AnimMigrationUtils;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerState extends BaseAppState {

  private static final Logger logger = LoggerFactory.getLogger(PlayerState.class);

  private final float moveSpeed = 5f;
  private final float strafeSpeed = 1f;

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
//    Vector3f direction = player.getLocalRotation().mult(Vector3f.UNIT_Z);
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
    float delta = (float) (value * tpf) * moveSpeed;
    right.multLocal(delta);
    player.move(right);
  }
}
