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

import com.jme3.ai.agents.AIControl;
import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviors.npc.SimpleMainBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.CompoundSteeringBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.ObstacleAvoidanceBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.QueuingBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.SeekBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.SeparationBehavior;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Cylinder;
import com.jme3.system.AppSettings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Queuing demo
 *
 * @author Jesús Martín Berlanga
 * @version 2.0.0
 */
public class QueuingDemo extends BasicDemo {

  private SeekBehavior[] neighSeek;
  private Agent[] neighbours;

  public static void main(String[] args) {
    AppSettings settings = new AppSettings(true);
    settings.setResolution(1600, 800);

    QueuingDemo app = new QueuingDemo();
    app.setSettings(settings);
    app.setShowSettings(false);
    app.start();
  }

  @Override
  public void simpleInitApp() {
    this.steerControl = new CustomSteerControl(5, 5);
    this.steerControl.setCameraSettings(getCamera());
    this.steerControl.setFlyCameraSettings(getFlyByCamera());

    //defining rootNode for brainsAppState processing
    brainsAppState.setApp(this);
    brainsAppState.setGameControl(this.steerControl);

    this.numberNeighbours = 50;
    Vector3f[] spawnArea = null;

    ColorRGBA targetNewColor = new ColorRGBA(ColorRGBA.Cyan.r, ColorRGBA.Cyan.g, ColorRGBA.Cyan.b, 0.25f);

    Agent target = this.createDoor("Door", targetNewColor, 0.28f);
    brainsAppState.addAgent(target); //Add the target to the brainsAppState
    rootNode.attachChild(target.getSpatial());
    this.setStats(
        target,
        this.targetMoveSpeed,
        this.targetRotationSpeed,
        this.targetMass,
        this.targetMaxForce);
    brainsAppState.getGameControl().spawn(target, new Vector3f(0, 0, 15));
    SimpleMainBehavior targetMainB = new SimpleMainBehavior();
    target.setMainBehavior(targetMainB);

    this.neighbours = new Agent[this.numberNeighbours];
    this.neighboursMoveSpeed *= 1.5f;

    for (int i = 0; i < this.numberNeighbours; i++) {
      this.neighbours[i] = this.createBoid("Neighbour " + i, this.neighboursColor, 0.11f);
      brainsAppState.addAgent(this.neighbours[i]); //Add the neighbours to the brainsAppState
      rootNode.attachChild(neighbours[i].getSpatial());

      this.setStats(
          neighbours[i],
          this.neighboursMoveSpeed,
          this.neighboursRotationSpeed,
          this.neighboursMass,
          this.neighboursMaxForce);
      brainsAppState.getGameControl().spawn(this.neighbours[i], spawnArea);
    }

    List<AIControl> obstacles = new ArrayList<>(Arrays.asList(this.neighbours));

    List<AIControl> wallObstacle = new ArrayList<>();

    Vector3f[] spheresSpawnPositions = new Vector3f[]{
        new Vector3f(1, 0, 0).mult(0.28f).add(new Vector3f(0, 0, 15)),
        new Vector3f(0, 1, 0).mult(0.28f).add(new Vector3f(0, 0, 15)),
        new Vector3f(-1, 0, 0).mult(0.28f).add(new Vector3f(0, 0, 15)),
        new Vector3f(0, -1, 0).mult(0.28f).add(new Vector3f(0, 0, 15)),
        new Vector3f(0.70710678118654752440084436210485f, 0.70710678118654752440084436210485f, 0).mult(0.28f).add(new Vector3f(0, 0, 15)),
        new Vector3f(-0.70710678118654752440084436210485f, 0.70710678118654752440084436210485f, 0).mult(0.28f).add(new Vector3f(0, 0, 15)),
        new Vector3f(0.70710678118654752440084436210485f, -0.70710678118654752440084436210485f, 0).mult(0.28f).add(new Vector3f(0, 0, 15)),
        new Vector3f(-0.70710678118654752440084436210485f, -0.70710678118654752440084436210485f, 0).mult(0.28f).add(new Vector3f(0, 0, 15)),
        new Vector3f(0.92387953251128675612818318939679f, 0.3826834323650897717284599840304f, 0).mult(0.28f).add(new Vector3f(0, 0, 15)),
        new Vector3f(-0.92387953251128675612818318939679f, 0.3826834323650897717284599840304f, 0).mult(0.28f).add(new Vector3f(0, 0, 15)),
        new Vector3f(0.92387953251128675612818318939679f, -0.3826834323650897717284599840304f, 0).mult(0.28f).add(new Vector3f(0, 0, 15)),
        new Vector3f(-0.92387953251128675612818318939679f, -0.3826834323650897717284599840304f, 0).mult(0.28f).add(new Vector3f(0, 0, 15)),
        new Vector3f(0.3826834323650897717284599840304f, 0.92387953251128675612818318939679f, 0).mult(0.28f).add(new Vector3f(0, 0, 15)),
        new Vector3f(-0.3826834323650897717284599840304f, 0.92387953251128675612818318939679f, 0).mult(0.28f).add(new Vector3f(0, 0, 15)),
        new Vector3f(0.3826834323650897717284599840304f, -0.92387953251128675612818318939679f, 0).mult(0.28f).add(new Vector3f(0, 0, 15)),
        new Vector3f(-0.3826834323650897717284599840304f, -0.92387953251128675612818318939679f, 0).mult(0.28f).add(
            new Vector3f(0, 0, 15)),};

    for (int i = 0; i < 16; i++) {
      Agent sphere = this.createSphere("Door obstacle " + i, ColorRGBA.Orange, 0.075f);

      wallObstacle.add(sphere);

      this.setStats(
          sphere,
          this.neighboursMoveSpeed,
          this.neighboursRotationSpeed,
          this.neighboursMass,
          this.neighboursMaxForce);
      brainsAppState.getGameControl().spawn(sphere, spheresSpawnPositions[i]);
      rootNode.attachChild(sphere.getSpatial());

      SimpleMainBehavior mainB = new SimpleMainBehavior();
      sphere.setMainBehavior(mainB);
    }

    SimpleMainBehavior[] neighboursMainBehavior = new SimpleMainBehavior[this.neighbours.length];
    neighSeek = new SeekBehavior[this.neighbours.length];

    for (int i = 0; i < this.neighbours.length; i++) {
      neighboursMainBehavior[i] = new SimpleMainBehavior();

      SeekBehavior extraSeek = new SeekBehavior(new Vector3f(0, 0, 14.9f));
      extraSeek.setAgent(neighbours[i]);
      extraSeek.setupStrengthControl(0.35f);

      this.neighSeek[i] = new SeekBehavior(new Vector3f(0, 0, 15.17f));
      this.neighSeek[i].setAgent(neighbours[i]);

      SeparationBehavior separation = new SeparationBehavior(obstacles, 0.13f);
      separation.setAgent(neighbours[i]);
      separation.setupStrengthControl(0.25f);

      ObstacleAvoidanceBehavior obstacleAvoid = new ObstacleAvoidanceBehavior(wallObstacle, 1, 5);
      obstacleAvoid.setAgent(neighbours[i]);

      // BalancedCompoundSteeringBehaviour steer = new BalancedCompoundSteeringBehaviour(this.neighbours[i]);
      CompoundSteeringBehavior steer = new CompoundSteeringBehavior();
      steer.setAgent(neighbours[i]);

      QueuingBehavior queue = new QueuingBehavior(convertToAgents(obstacles), 1.5f);
      queue.setAgent(neighbours[i]);

      SeparationBehavior queueSeparation = new SeparationBehavior(obstacles, 0.25f);
      queueSeparation.setAgent(neighbours[i]);
      queueSeparation.setupStrengthControl(0.01f); //1%

      steer.addSteerBehavior(this.neighSeek[i]);
      steer.addSteerBehavior(extraSeek);
      steer.addSteerBehavior(obstacleAvoid);
      steer.addSteerBehavior(queue);
      steer.addSteerBehavior(queueSeparation);
      steer.addSteerBehavior(separation, 1, 0.01f); //Highest layer => Highest priority

      //Remove behaviour for testing purposes
      steer.removeSteerBehavior(separation);
      steer.removeSteerBehavior(this.neighSeek[i]);
      steer.removeSteerBehavior(obstacleAvoid);
      steer.removeSteerBehavior(extraSeek);
      steer.removeSteerBehavior(queue);
      steer.removeSteerBehavior(queueSeparation);

      //Add then again
      steer.addSteerBehavior(this.neighSeek[i]);
      steer.addSteerBehavior(extraSeek);
      steer.addSteerBehavior(obstacleAvoid);
      steer.addSteerBehavior(queue);
      steer.addSteerBehavior(queueSeparation);
      steer.addSteerBehavior(separation, 1, 0.01f); //Highest layer => Highest priority

      neighboursMainBehavior[i].addBehavior(steer);

      this.neighbours[i].setMainBehavior(neighboursMainBehavior[i]);
    }

    brainsAppState.start();
  }

  private Agent createDoor(String name, ColorRGBA color, float radius) {
    Node origin = new Node();
    Cylinder mesh = new Cylinder(4, 20, radius, 0.01f, true);
    Geometry geom = new Geometry("A shape", mesh);
    origin.attachChild(geom);

    Spatial doorSpatial = origin;

    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
    mat.setColor("Color", color);
    doorSpatial.setMaterial(mat);

    // FIXME 
    Agent agent = new Agent(radius);

    doorSpatial.addControl(agent);

    return agent;
  }

  @Override
  public void simpleUpdate(float tpf) {
    brainsAppState.update(tpf);

    for (int i = 0; i < this.neighbours.length; i++) {
      if (new Vector3f(0, 0, 15.17f).subtract(this.neighbours[i].getWorldTranslation()).length() < 0.15f) {
        this.neighbours[i].setWorldTranslation(
            new Vector3f(
                ((FastMath.nextRandomFloat() * 2) - 1) * 5,
                ((FastMath.nextRandomFloat() * 2) - 1) * 5,
                ((FastMath.nextRandomFloat() * 2) - 1) * 5)
        );
      }
    }
  }

  private List<Agent> convertToAgents(List<AIControl> aiControls) {
    List<Agent> agents = new ArrayList<>();

    for (AIControl aiControl : aiControls) {
      if (aiControl instanceof Agent a) {
        agents.add(a);
      }
    }

    return agents;
  }
}
