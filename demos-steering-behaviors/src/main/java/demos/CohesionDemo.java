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
import com.jme3.ai.agents.behaviors.npc.steering.CohesionBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.CompoundSteeringBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.SeparationBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.WanderAreaBehavior;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Cohesion behavior demo
 *
 * @author Jesús Martín Berlanga
 * @version 2.0.1
 */
public class CohesionDemo extends BasicDemo {

  public static void main(String[] args) {
    AppSettings settings = new AppSettings(true);
    settings.setResolution(1600, 800);

    CohesionDemo app = new CohesionDemo();
    app.setSettings(settings);
    app.setShowSettings(false);
    app.start();
  }

  @Override
  public void simpleInitApp() {
    this.steerControl = new CustomSteerControl(25, 40);
    this.steerControl.setCameraSettings(getCamera());
    this.steerControl.setFlyCameraSettings(getFlyByCamera());

    //defining rootNode for brainsAppState processing
    brainsAppState.setApp(this);
    brainsAppState.setGameControl(this.steerControl);

    this.numberNeighbours = 150;
    Vector3f[] spawnArea = null;
    Agent[] boids = new Agent[this.numberNeighbours];

    for (int i = 0; i < this.numberNeighbours; i++) {
      boids[i] = this.createBoid("boid " + i, this.neighboursColor, 0.1f);
      brainsAppState.addAgent(boids[i]); //Add the neighbours to the brainsAppState
      rootNode.attachChild(boids[i].getSpatial());
      this.setStats(boids[i], this.neighboursMoveSpeed,
          this.neighboursRotationSpeed, this.neighboursMass,
          this.neighboursMaxForce);
      brainsAppState.getGameControl().spawn(boids[i], spawnArea);
    }

    List<AIControl> obstacles = new ArrayList<>(Arrays.asList(boids));

    SimpleMainBehavior[] neighboursMainBehavior = new SimpleMainBehavior[boids.length];

    SeparationBehavior[] separation = new SeparationBehavior[boids.length];
    CohesionBehavior[] cohesion = new CohesionBehavior[boids.length];
    WanderAreaBehavior[] wander = new WanderAreaBehavior[boids.length];

    for (int i = 0; i < boids.length; i++) {
      neighboursMainBehavior[i] = new SimpleMainBehavior();

      separation[i] = new SeparationBehavior(obstacles);
      cohesion[i] = new CohesionBehavior(obstacles, 5f, FastMath.PI / 4);
      wander[i] = new WanderAreaBehavior();
      wander[i].setArea(Vector3f.ZERO, 37.5f, 37.5f, 37.5f);

      separation[i].setupStrengthControl(0.85f);
      cohesion[i].setupStrengthControl(2.15f);
      wander[i].setupStrengthControl(0.35f);

      CompoundSteeringBehavior steer = new CompoundSteeringBehavior();

      steer.addSteerBehavior(cohesion[i]);
      steer.addSteerBehavior(separation[i]);
      steer.addSteerBehavior(wander[i]);
      neighboursMainBehavior[i].addBehavior(steer);

      boids[i].setMainBehavior(neighboursMainBehavior[i]);
    }
    brainsAppState.start();
  }

  @Override
  public void simpleUpdate(float tpf) {
    brainsAppState.update(tpf);
  }
}
