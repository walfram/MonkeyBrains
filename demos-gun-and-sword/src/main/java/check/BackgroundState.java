package check;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.simsilica.lemur.geom.MBox;

public class BackgroundState extends BaseAppState {

  private final Node scene = new Node("background-scene");
  
  public BackgroundState(Node rootNode) {
    rootNode.attachChild(scene);
  }

  @Override
  protected void initialize(Application app) {
    Geometry geometry = new Geometry("background", new MBox(8, 0, 8, 2, 0, 2));
    Material material = new Material(app.getAssetManager(), "Common/MatDefs/Misc/ShowNormals.j3md");
    geometry.setMaterial(material);
    scene.attachChild(geometry);

    app.getCamera().setLocation(new Vector3f(3.2526035f, 3.864782f, 5.550612f));
    app.getCamera().setRotation(new Quaternion(-0.057457604f, 0.9401395f, -0.20034528f, -0.2696259f));
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
