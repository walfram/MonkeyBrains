package demos;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviors.npc.steering.WanderAreaBehavior;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.debug.WireBox;
import com.jme3.system.AppSettings;
import jme3utilities.debug.AxesVisualizer;
import jme3utilities.mesh.Octasphere;

public class WanderingDemo extends SimpleApplication {

  public static void main(String[] args) {
    AppSettings settings = new AppSettings(true);
    settings.setResolution(1600, 800);
    
    WanderingDemo app = new WanderingDemo();
    app.setSettings(settings);
    app.setShowSettings(false);
    
    app.start();
  }
  
  @Override
  public void simpleInitApp() {
    flyCam.setDragToRotate(true);
    flyCam.setMoveSpeed(50);
    flyCam.setZoomSpeed(0);

    AxesVisualizer axesVisualizer = new AxesVisualizer(assetManager, 128, 1);
    rootNode.addControl(axesVisualizer);
    axesVisualizer.setEnabled(true);

    cam.setLocation(new Vector3f(41.23289f, 35.46229f, 99.323906f));
    cam.setRotation(new Quaternion(-0.027147464f, 0.97173107f, -0.14046256f, -0.18780848f));
    
    Agent<Void> agent = new Agent<>(1f);
    WanderAreaBehavior mainBehavior = new WanderAreaBehavior();
    Vector3f area = new Vector3f(32, 16, 32);
    mainBehavior.setArea(new Vector3f(), area);
    agent.setMainBehavior(mainBehavior);

    Geometry bounds = new Geometry("area", new WireBox(area.x, area.y, area.z));
    Material boundsMaterial = new Material(assetManager, Materials.UNSHADED);
    boundsMaterial.setColor("Color", ColorRGBA.Orange);
    bounds.setMaterial(boundsMaterial);
    rootNode.attachChild(bounds);

    Geometry geometry = new Geometry("boid", new Octasphere(1, 1));
    Material material = new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
    geometry.setMaterial(material);
    geometry.addControl(agent);
    
    rootNode.attachChild(geometry);
  }
}
