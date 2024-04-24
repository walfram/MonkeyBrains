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
import com.jme3.ai.agents.behaviors.npc.steering.HideBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.PathFollowBehavior;
import com.jme3.ai.agents.util.GameEntity;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import java.util.ArrayList;
import java.util.List;

/**
 * Hide demo
 *
 * @author Jesús Martín Berlanga
 * @version 1.0.0
 */
public class HideDemo extends BasicDemo {

  private PathFollowBehavior targetPathFollow;

  public static void main(String[] args) {
    AppSettings settings = new AppSettings(true);
    settings.setResolution(1600, 800);

    HideDemo app = new HideDemo();
    app.setSettings(settings);
    app.setShowSettings(false);
    app.start();
  }

  @Override
  public void simpleInitApp() {
    this.steerControl = new CustomSteerControl(8.5f, 5);
    this.steerControl.setCameraSettings(getCamera());
    this.steerControl.setFlyCameraSettings(getFlyByCamera());

    //defining rootNode for brainsAppState processing
    brainsAppState.setApp(this);
    brainsAppState.setGameControl(this.steerControl);

    Vector3f[] spawnArea = null;

    Agent target = this.createBoid("Target", this.targetColor, 0.11f);
    brainsAppState.addAgent(target); //Add the target to the brainsAppState
    rootNode.attachChild(target.getSpatial());
    brainsAppState.getGameControl().spawn(target, new Vector3f(0, 0, -1));
    this.setStats(
        target,
        this.targetMoveSpeed,
        this.targetRotationSpeed,
        this.targetMass,
        this.targetMaxForce);

    Agent hider = this.createBoid("Seeker", this.neighboursColor, 0.11f);
    brainsAppState.addAgent(hider);
    rootNode.attachChild(hider.getSpatial());
    this.setStats(
        hider,
        this.neighboursMoveSpeed,
        this.neighboursMoveSpeed,
        this.neighboursMass,
        this.neighboursMaxForce);
    brainsAppState.getGameControl().spawn(hider, spawnArea);

    Agent obstacle = this.createSphere("Obstacle", ColorRGBA.Yellow, 0.35f);
    brainsAppState.addAgent(obstacle);
    rootNode.attachChild(obstacle.getSpatial());
    this.setStats(
        obstacle,
        this.neighboursMoveSpeed,
        this.neighboursRotationSpeed,
        this.neighboursMass,
        this.neighboursMaxForce);
    brainsAppState.getGameControl().spawn(obstacle, new Vector3f(2.5f, 2.5f, 2.5f));

    Agent farAwayObstacle = this.createSphere("Obstacle", ColorRGBA.Yellow, 0.35f);
    brainsAppState.addAgent(farAwayObstacle);
    rootNode.attachChild(farAwayObstacle.getSpatial());
    this.setStats(
        farAwayObstacle,
        this.neighboursMoveSpeed,
        this.neighboursRotationSpeed,
        this.neighboursMass,
        this.neighboursMaxForce);
    brainsAppState.getGameControl().spawn(farAwayObstacle, new Vector3f(7.5f, 7.5f, 5f));

    List<AIControl> obstacles = new ArrayList<>();
    obstacles.add(obstacle);
    obstacles.add(farAwayObstacle);

    ArrayList<Vector3f> orderedPointsList = new ArrayList<>();
    orderedPointsList.add(new Vector3f(0, 0, 0));
    orderedPointsList.add(new Vector3f(0, 0, 5));
    orderedPointsList.add(new Vector3f(5, 0, 5));
    orderedPointsList.add(new Vector3f(5, 5, 5));
    orderedPointsList.add(new Vector3f(5, 5, 0));
    orderedPointsList.add(new Vector3f(0, 5, 0));
    orderedPointsList.add(new Vector3f(0, 0, 0));

    SimpleMainBehavior targetMainBehavior = new SimpleMainBehavior();
    this.targetPathFollow = new PathFollowBehavior(orderedPointsList, 1, 1);
    targetPathFollow.setAgent(target);
    targetMainBehavior.addBehavior(this.targetPathFollow);
    target.setMainBehavior(targetMainBehavior);

    obstacle.setMainBehavior(new SimpleMainBehavior());
    farAwayObstacle.setMainBehavior(new SimpleMainBehavior());

    SimpleMainBehavior hiderMainBehavior = new SimpleMainBehavior();
    hiderMainBehavior.setAgent(hider);
    HideBehavior hide = new HideBehavior(target, obstacles, 1f);
    hiderMainBehavior.addBehavior(hide);
    hider.setMainBehavior(hiderMainBehavior);

    brainsAppState.start();
  }

  @Override
  public void simpleUpdate(float tpf) {
    brainsAppState.update(tpf);

    if (!this.targetPathFollow.isActive()) {
      this.targetPathFollow.reset();
    }
  }
}
