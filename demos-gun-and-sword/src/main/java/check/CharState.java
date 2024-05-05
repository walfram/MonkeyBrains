package check;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import demo.game.AnimationControl;
import demo.game.ResourcesState;
import java.util.List;

public class CharState extends BaseAppState {

  private final Node scene = new Node("char-scene");

  private Spatial character;
  private ParticleEmitter shotTraceEmitter;

  public CharState(Node rootNode) {
    rootNode.attachChild(scene);
  }

  @Override
  protected void initialize(Application app) {
    character = getState(ResourcesState.class).characterSpatial();
    character.rotate(0, FastMath.QUARTER_PI, 0);
    character.move(-2, 0, -4);
    scene.attachChild(character);

    ShotTrace shotTrace = new ShotTrace(app.getAssetManager());
    shotTraceEmitter = shotTrace.emitter();
    scene.attachChild(shotTraceEmitter);
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

  public void stand() {
    character.getControl(AnimationControl.class).stand();
  }

  public void run() {
    character.getControl(AnimationControl.class).run();
  }

  public void strike() {
    character.getControl(AnimationControl.class).strike();
  }

  public void shoot() {
    character.getControl(AnimationControl.class).shoot();

    shotTraceEmitter.setLocalRotation(character.getLocalRotation().clone());
    shotTraceEmitter.setLocalTranslation(character.getLocalTranslation().clone());
    shotTraceEmitter.move(0.5f, 1.5f, 1);

    shotTraceEmitter.setEnabled(true);
    shotTraceEmitter.emitAllParticles();
  }
}
