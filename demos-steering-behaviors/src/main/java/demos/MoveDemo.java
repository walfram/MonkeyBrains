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
import com.jme3.ai.agents.behaviors.npc.SimpleMainBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.MoveBehavior;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;

/**
 * Move Demo
 *
 * @author Jesús Martín Berlanga
 * @version 1.0.0
 */
public class MoveDemo extends BasicDemo {

  public static void main(String[] args) {
    AppSettings settings = new AppSettings(true);
    settings.setResolution(1600, 800);
    
    MoveDemo app = new MoveDemo();
    app.setSettings(settings);
    app.setShowSettings(false);
    app.start();
  }

  @Override
  public void simpleInitApp() {
    this.steerControl = new CustomSteerControl(9, 1);
    this.steerControl.setCameraSettings(getCamera());
    this.steerControl.setFlyCameraSettings(getFlyByCamera());

    //defining rootNode for brainsAppState processing
    brainsAppState.setApp(this);
    brainsAppState.setGameControl(new CustomSteerControl(5f));

    Agent target = this.createBoid("Target", this.targetColor, 0.11f);

    brainsAppState.addAgent(target); //Add the target to the brainsAppState
    rootNode.attachChild(target.getSpatial());
    brainsAppState.getGameControl().spawn(target, Vector3f.ZERO);
    this.setStats(
        target,
        this.targetMoveSpeed,
        this.targetRotationSpeed,
        this.targetMass,
        this.targetMaxForce);

    SimpleMainBehavior targetMainBehavior = new SimpleMainBehavior();

    MoveBehavior targetMoveBehavior = new MoveBehavior();
    targetMoveBehavior.setAgent(target);
    targetMoveBehavior.setMoveDirection(new Vector3f(
        FastMath.nextRandomFloat() - 0.5f,
        FastMath.nextRandomFloat() - 0.5f,
        FastMath.nextRandomFloat() - 0.5f));

    targetMainBehavior.addBehavior(targetMoveBehavior);
    target.setMainBehavior(targetMainBehavior);

    brainsAppState.start();
  }

  @Override
  public void simpleUpdate(float tpf) {
    brainsAppState.update(tpf);
  }
}
