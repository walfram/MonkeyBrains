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
import com.jme3.ai.agents.behaviors.npc.steering.BalancedCompoundSteeringBehavior;
import com.jme3.ai.agents.behaviors.npc.SimpleMainBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.CompoundSteeringBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.ObstacleAvoidanceBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.SeekBehavior;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.math.FastMath;
import com.jme3.system.AppSettings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Obstacle avoidance demo
 *
 * @author Jesús Martín Berlanga
 * @version 2.0.0
 */
public class ObstacleAvoidanceDemo extends BasicDemo {

  private Agent agent;
  private Agent focus;
  private boolean positiveXSide = true;

  public static void main(String[] args) {
    AppSettings settings = new AppSettings(true);
    settings.setResolution(1600, 800);

    ObstacleAvoidanceDemo app = new ObstacleAvoidanceDemo();
    app.setSettings(settings);
    app.setShowSettings(false);
    app.start();
  }

  @Override
  public void simpleInitApp() {
    this.steerControl = new CustomSteerControl(7, 4.5f);
    this.steerControl.setCameraSettings(getCamera());
    this.steerControl.setFlyCameraSettings(getFlyByCamera());

    //defining rootNode for brainsAppState processing
    brainsAppState.setApp(this);
    brainsAppState.setGameControl(this.steerControl);

    Vector3f[] spawnArea = null;
    this.numberNeighbours = 150;

    agent = this.createBoid("Target", ColorRGBA.Blue, 0.27f);

    brainsAppState.addAgent(agent); //Add the target to the brainsAppState
    rootNode.attachChild(agent.getSpatial());
    this.setStats(
        agent,
        this.targetMoveSpeed,
        this.targetRotationSpeed,
        this.targetMass,
        this.targetMaxForce);
    brainsAppState.getGameControl().spawn(agent, new Vector3f());

    Agent[] neighbours = new Agent[this.numberNeighbours];

    for (int i = 0; i < this.numberNeighbours; i++) {
      neighbours[i] = this.createSphere("neighbour_" + i, ColorRGBA.Orange, 0.4f);

      brainsAppState.addAgent(neighbours[i]); //Add the neighbours to the brainsAppState
      rootNode.attachChild(neighbours[i].getSpatial());
      this.setStats(
          neighbours[i],
          this.neighboursMoveSpeed,
          this.neighboursRotationSpeed,
          this.neighboursMass,
          this.neighboursMaxForce);
      brainsAppState.getGameControl().spawn(neighbours[i], spawnArea);

      SimpleMainBehavior mainB = new SimpleMainBehavior();
      neighbours[i].setMainBehavior(mainB);
    }

    focus = this.createSphere("focus", ColorRGBA.Green, 0.4f);
    brainsAppState.addAgent(focus); //Add the neighbours to the brainsAppState
    rootNode.attachChild(focus.getSpatial());

    this.setStats(
        focus,
        this.neighboursMoveSpeed,
        this.neighboursRotationSpeed,
        this.neighboursMass,
        this.neighboursMaxForce);
    brainsAppState.getGameControl().spawn(focus, this.generateRandomPosition());

    SimpleMainBehavior mainB = new SimpleMainBehavior();
    focus.setMainBehavior(mainB);

    List<AIControl> obstacles = new ArrayList<>(Arrays.asList(neighbours));

    //ADD OBSTACLE AVOIDANCE TO THE TARGET

    CompoundSteeringBehavior steer = new BalancedCompoundSteeringBehavior();
    SimpleMainBehavior targetMainB = new SimpleMainBehavior();
    targetMainB.setAgent(agent);

    SeekBehavior seekSteer = new SeekBehavior(agent, focus);

    ObstacleAvoidanceBehavior obstacleAvoidance = new ObstacleAvoidanceBehavior(obstacles, 15f, 15);
    obstacleAvoidance.setAgent(agent);
    obstacleAvoidance.setupStrengthControl(10f);

    steer.addSteerBehavior(seekSteer);
    steer.addSteerBehavior(obstacleAvoidance);
    targetMainB.addBehavior(steer);
    agent.setMainBehavior(targetMainB);

    brainsAppState.start();
  }

  @Override
  public void simpleUpdate(float tpf) {
    brainsAppState.update(tpf);

    if (this.agent.distanceTo(this.focus) < 0.5f) {
      this.focus.setWorldTranslation(this.generateRandomPosition());
    }
  }

  private Vector3f generateRandomPosition() {
    Random rand = FastMath.rand;
    Vector3f randomPos;

    if (this.positiveXSide) {
      randomPos = new Vector3f(7.5f, (rand.nextFloat() - 0.5f) * 7.5f, (rand.nextFloat() - 0.5f) * 7.5f);
      this.positiveXSide = false;
    } else {
      randomPos = new Vector3f(-7.5f, (rand.nextFloat() - 0.5f) * 7.5f, (rand.nextFloat() - 0.5f) * 7.5f);
      this.positiveXSide = true;
    }

    return randomPos;
  }
}
