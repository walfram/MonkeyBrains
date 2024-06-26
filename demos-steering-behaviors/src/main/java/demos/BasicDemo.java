/**
 * Copyright (c) 2014, jMonkeyEngine All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are
 * met:
 * <p>
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * <p>
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * <p>
 * Neither the name of 'jMonkeyEngine' nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package demos;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.util.control.MonkeyBrainsAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

/**
 * Provides the basic structure for all the demos.
 *
 * @author Jesús Martín Berlanga
 * @version 1.0.0
 */
public abstract class BasicDemo extends SimpleApplication {

  protected CustomSteerControl steerControl;
  //creating brainsAppState
  protected MonkeyBrainsAppState brainsAppState = MonkeyBrainsAppState.getInstance();
  private static final String BOID_MODEL_NAME = "Models/boid.j3o";
  private static final float BOID_MODEL_SIZE = 0.1f;
  private static final String BOID_MATERIAL_NAME = "Common/MatDefs/Misc/Unshaded.j3md";
  protected ColorRGBA targetColor = ColorRGBA.Red;
  protected float targetMoveSpeed = 1f;
  protected float targetRotationSpeed = 30;
  protected float targetMass = 50;
  protected float targetMaxForce = 20;
  protected ColorRGBA neighboursColor = ColorRGBA.Blue;
  protected float neighboursMoveSpeed = 0.96f;
  protected float neighboursRotationSpeed = 30;
  protected float neighboursMass = 50;
  protected float neighboursMaxForce = 20;
  protected int numberNeighbours;

  //Create an agent with a name and a color
  protected Agent createBoid(String name, ColorRGBA color, float size) {
    Spatial boidSpatial = assetManager.loadModel(BasicDemo.BOID_MODEL_NAME);
    boidSpatial.setLocalScale(BasicDemo.BOID_MODEL_SIZE); //Resize

    Material mat = new Material(assetManager, BasicDemo.BOID_MATERIAL_NAME);
    mat.setColor("Color", color);
    boidSpatial.setMaterial(mat);
    
    Agent agent = new Agent(size);
    boidSpatial.addControl(agent);
    return agent;
  }

  //Create a sphere
  protected Agent createSphere(String name, ColorRGBA color, float size) {
    Sphere sphere = new Sphere(13, 12, size);
    Spatial spatial = new Geometry("Sphere Geometry", sphere);

    Material mat = new Material(assetManager, BasicDemo.BOID_MATERIAL_NAME);
    mat.setColor("Color", color);
    spatial.setMaterial(mat);
    
    Agent agent = new Agent(size);
    spatial.addControl(agent);
    return agent;
  }

  protected void createSphereHelper(String name, ColorRGBA color, float size, Vector3f loc) {
    Sphere sphere = new Sphere(13, 12, size);
    Geometry sphereG = new Geometry("Sphere Geometry", sphere);

    Material mat = new Material(assetManager, BasicDemo.BOID_MATERIAL_NAME);
    mat.setColor("Color", color);
    ((Spatial) sphereG).setMaterial(mat);

    sphereG.setLocalTranslation(loc);
    rootNode.attachChild(sphereG);
  }

  protected void addBoxHelper(Vector3f center, float x, float y, float z) {
    Box box = new Box(center, x, y, z);

    Geometry geom = new Geometry("A shape", box); // wrap shape into geometry
    Geometry geomWire = new Geometry("A shape", box);

    Material matTranslucent = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    matTranslucent.setColor("Color", new ColorRGBA(0, 1, 0, 0.17f));
    matTranslucent.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
    geom.setQueueBucket(RenderQueue.Bucket.Translucent);
    geom.setMaterial(matTranslucent);

    Material wireMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    wireMat.setColor("Color", new ColorRGBA(0, 1, 0, 0.25f));
    geomWire.setMaterial(wireMat);
    wireMat.getAdditionalRenderState().setWireframe(true);

    rootNode.attachChild(geom);
    rootNode.attachChild(geomWire);
  }

  //Setup the stats for an agent
  protected void setStats(Agent myAgent, float moveSpeed, float rotationSpeed, float mass, float maxForce) {
    myAgent.setMaxMoveSpeed(moveSpeed);
    myAgent.setRotationSpeed(rotationSpeed);
    myAgent.setMass(mass);
//        myAgent.setMaxForce(maxForce);
  }
}
