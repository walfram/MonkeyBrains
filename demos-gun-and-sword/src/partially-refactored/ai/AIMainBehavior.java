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
package demo.ai;

import com.jme3.ai.agents.behaviors.Behavior;
import com.jme3.math.Vector3f;

/**
 *
 * @author Tihomir Radosavljević
 * @version 1.0.4
 */
public class AIMainBehavior extends Behavior {

  private final AILookBehavior lookBehavior;
  private final AIAttackBehavior attackBehavior;
  private final AIWanderBehavior wanderBehavior;
  private final AISeekBehavior seekBehavior;
  private final Vector3f area = new Vector3f(2, 0, 2);

  public AIMainBehavior() {
    attackBehavior = new AIAttackBehavior();
    // FIXME AISeekBehavior must accept target, not "this" agent
    seekBehavior = new AISeekBehavior();
    lookBehavior = new AILookBehavior();
    lookBehavior.addListener(attackBehavior);
    lookBehavior.addListener(seekBehavior);
    //setting viewing distance of agent
    lookBehavior.setVisibilityRange(300f);
    wanderBehavior = new AIWanderBehavior(agent);
    wanderBehavior.setArea(agent.getWorldTranslation(), area);
  }

  @Override
  public void updateAI(float tpf) {
    lookBehavior.updateAI(tpf);
    attackBehavior.updateAI(tpf);
    if (seekBehavior.getTarget() != null && seekBehavior.getTarget().isEnabled()) {
      seekBehavior.updateAI(tpf);
    } else {
      wanderBehavior.updateAI(tpf);
      //FIXME: need to include obstacles avoidance
    }
  }
}
