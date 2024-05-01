package demo.game;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.ChaseCamera;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class CameraState extends BaseAppState {

  private ChaseCamera chaseCamera;
  
  @Override
  protected void initialize(Application app) {
    chaseCamera = new ChaseCamera(app.getCamera(), app.getInputManager());
    chaseCamera.setInvertVerticalAxis(true);
    
    chaseCamera.setMinDistance(2.5f);
    chaseCamera.setLookAtOffset(new Vector3f(0, 1, 0));
    
    chaseCamera.setDefaultHorizontalRotation(-FastMath.HALF_PI);
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

  public void chase(Spatial spatial) {
    spatial.addControl(chaseCamera);
  }
}
