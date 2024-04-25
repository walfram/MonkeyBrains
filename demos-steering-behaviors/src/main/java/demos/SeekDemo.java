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
import com.jme3.ai.agents.behaviors.npc.steering.SeekBehavior;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;

/**
 * Seek Demo
 *
 * @author Jesús Martín Berlanga
 * @version 1.0
 */
public class SeekDemo extends BasicDemo {

  private Agent target;
  private SeekBehavior targetMove;
  private Vector3f[] locations = new Vector3f[]{
      new Vector3f(7, 0, 0),
      new Vector3f(0, 7, 0),
      new Vector3f(0, 0, 7)
  };
  private int currentFocus = -1;

  public static void main(String[] args) {
    AppSettings settings = new AppSettings(true);
    settings.setResolution(1600, 800);
 
    SeekDemo app = new SeekDemo();
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

    target = this.createBoid("Target", this.targetColor, 0.11f);

    for (Vector3f loc : this.locations) {
      this.createSphereHelper("Sphere " + loc.toString(), ColorRGBA.Yellow, 0.05f, loc);
    }

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
    targetMainBehavior.setAgent(target);

    this.targetMove = new SeekBehavior(this.locations[0]);
    this.targetMove.setAgent(target);
    this.currentFocus = 0;
    targetMainBehavior.addBehavior(this.targetMove);

    target.setMainBehavior(targetMainBehavior);

    brainsAppState.start();
  }

  @Override
  public void simpleUpdate(float tpf) {
    brainsAppState.update(tpf);
    if (this.target.vectorTo(this.locations[this.currentFocus]).length() < 0.01f) {
      this.currentFocus++;
      if (this.currentFocus > this.locations.length - 1) {
        this.currentFocus = 0;
      }
      this.targetMove.setSeekingPosition(this.locations[this.currentFocus]);
    }
  }
}
