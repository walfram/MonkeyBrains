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

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviors.npc.steering.SeekBehavior;
import com.jme3.ai.agents.events.AIControlSeenEvent;
import com.jme3.ai.agents.events.AIControlSeenListener;
import demo.model.Model;

/**
 * Behavior for coming closer to enemy.
 *
 * @author Tihomir Radosavljević
 * @version 1.0.0
 */
public class AISeekBehavior extends SeekBehavior implements AIControlSeenListener {

  @Override
  public void handleAIControlSeenEvent(AIControlSeenEvent event) {
    if (event.getAIControlSeen() instanceof Agent<?> targetAgent) {
      Model model = (Model) agent.getModel();
      
      if (model.isSameTeam(targetAgent)) {
        return;
      }
      
      setTarget(targetAgent);
    }
  }

//  @Override
//  public void updateAI(float tpf) {
//    super.updateAI(tpf);
//    Vector3f vel = calculateNewVelocity(tpf);
//    model.setWalkDirection(vel);
//    rotateAgent(tpf);
//  }

//  @Override
//  protected void rotateAgent(float tpf) {
//    Quaternion q = new Quaternion();
//    q.lookAt(velocity, new Vector3f(0, 1, 0));
//    agent.getWorldRotation().slerp(q, agent.getRotationSpeed());
//    model.setViewDirection(agent.getLocalRotation().mult(Vector3f.UNIT_Z).normalize());
//  }

}