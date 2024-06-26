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
import com.jme3.ai.agents.behaviors.npc.steering.MoveBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.SeekBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.UnalignedCollisionAvoidanceBehavior;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.math.FastMath;
import com.jme3.system.AppSettings;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;

/**
 * Unaligned avoidance demo
 *
 * @author Jesús Martín Berlanga
 * @version 2.0.0
 */
public class UnalignedAvoidanceDemo extends BasicDemo {

  private Agent agent;
  private Agent focus;
  private boolean positiveXSide = true;
  //ObstaclesMove
  private MoveBehavior obstaclesMoves[] = new MoveBehavior[150];
  //Negate Direction Timer
  private Timer iterationTimer;
  private java.awt.event.ActionListener negateMoveDir = new java.awt.event.ActionListener() {
    public void actionPerformed(ActionEvent event) {
      for (MoveBehavior move : obstaclesMoves) {
        move.setMoveDirection(move.getMoveDirection().negate());
      }
    }
  };

  public static void main(String[] args) {
    AppSettings settings = new AppSettings(true);
    settings.setResolution(1600, 800);

    UnalignedAvoidanceDemo app = new UnalignedAvoidanceDemo();
    app.setSettings(settings);
    app.setShowSettings(false);
    app.start();
  }

  @Override
  public void simpleInitApp() {
    this.steerControl = new CustomSteerControl(11, 4);
    this.steerControl.setCameraSettings(getCamera());
    this.steerControl.setFlyCameraSettings(getFlyByCamera());

    //defining rootNode for brainsAppState processing
    brainsAppState.setApp(this);
    brainsAppState.setGameControl(this.steerControl);

    Vector3f[] spawnArea = null;
    this.numberNeighbours = 150;

    agent = this.createBoid("Target", ColorRGBA.Blue, 0.31f);

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
    Random rand = FastMath.rand;
    for (int i = 0; i < this.numberNeighbours; i++) {
      neighbours[i] = this.createSphere("neighbour_" + i, ColorRGBA.Orange, 0.25f);
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
      mainB.setAgent(neighbours[i]);

      obstaclesMoves[i] = new MoveBehavior();
      obstaclesMoves[i].setAgent(neighbours[i]);
      obstaclesMoves[i].setMoveDirection(new Vector3f(rand.nextFloat() - 1, rand.nextFloat() - 1, rand.nextFloat() - 1));

      mainB.addBehavior(obstaclesMoves[i]);
      neighbours[i].setMainBehavior(mainB);
    }

    this.iterationTimer = new Timer(4000, this.negateMoveDir); //4k ns = 4s
    this.iterationTimer.start();

    focus = this.createSphere("focus", ColorRGBA.Green, 0.35f);

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

    CompoundSteeringBehavior steer = new CompoundSteeringBehavior();//new BalancedCompoundSteeringBehaviour(agent);//new CompoundSteeringBehavior(agent);
    steer.setAgent(agent);
    SimpleMainBehavior targetMainB = new SimpleMainBehavior();
    targetMainB.setAgent(agent);

    SeekBehavior seekSteer = new SeekBehavior(agent, focus);

    UnalignedCollisionAvoidanceBehavior obstacleAvoidance = new UnalignedCollisionAvoidanceBehavior(obstacles, 2, 5, 0.75f);
    obstacleAvoidance.setAgent(agent);
    obstacleAvoidance.setupStrengthControl(5f);

    steer.addSteerBehavior(seekSteer);
    steer.addSteerBehavior(obstacleAvoidance);
    targetMainB.addBehavior(steer);
    agent.setMainBehavior(targetMainB);

    brainsAppState.start();
  }

  @Override
  public void simpleUpdate(float tpf) {
    brainsAppState.update(tpf);

    if (this.agent.vectorTo(this.focus).length() < 0.5f) {
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
