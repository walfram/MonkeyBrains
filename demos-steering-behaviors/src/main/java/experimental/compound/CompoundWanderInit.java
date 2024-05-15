package experimental.compound;

import com.jme3.app.Application;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.FlyByCamera;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.simsilica.lemur.geom.MBox;
import jme3utilities.debug.AxesVisualizer;

public class CompoundWanderInit extends BaseAppState {

  private final Node rootNode;

  public CompoundWanderInit(Node rootNode) {
    this.rootNode = rootNode;
  }

  @Override
  protected void initialize(Application app) {
    FlyByCamera flyCam = getState(FlyCamAppState.class).getCamera();
    flyCam.setDragToRotate(true);
    flyCam.setMoveSpeed(20);
    flyCam.setZoomSpeed(0);

    AxesVisualizer axesVisualizer = new AxesVisualizer(app.getAssetManager(), 32, 1);
    rootNode.addControl(axesVisualizer);
    axesVisualizer.setEnabled(true);

    Geometry debugGrid = new Geometry("debug-grid", new MBox(32, 0, 32, 8, 0, 8));
    debugGrid.setMaterial(new Material(app.getAssetManager(), Materials.UNSHADED));
    debugGrid.getMaterial().setColor("Color", ColorRGBA.DarkGray);
//    debugGrid.getMaterial().getAdditionalRenderState().setWireframe(true);
    rootNode.attachChild(debugGrid);

    app.getCamera().setLocation(new Vector3f(16.24495f, 24.997404f, 44.096252f));
    app.getCamera().setRotation(new Quaternion(-0.054950416f, 0.9334678f, -0.31473228f, -0.16297787f));
    
//    app.getViewPort().setBackgroundColor(ColorRGBA.DarkGray);
    
    rootNode.addLight(new AmbientLight(ColorRGBA.White));
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
