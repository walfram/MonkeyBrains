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

import com.jme3.ai.agents.AIControl;
import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.events.AIControlSeenEvent;
import demo.model.Model;
import demo.model.Weapons;
import java.util.Random;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Behavior for attacking opponent if opponent is seen.
 *
 * @author Tihomir Radosavljević
 * @version 1.0.1
 */
public class AIAttackBehavior extends SimpleAttackBehaviour {

  private static final Logger logger = LoggerFactory.getLogger(AIAttackBehavior.class);
  
  private final Weapons weapons;
  /**
   * Bigger value means easier game, if it is 1, then agent will never miss. Must be greater or equal to 1.
   */
  private final int simplicity;
  /**
   * To add some randomness to game.
   */
  private final Random random;
  private Consumer<AIControl> callback;

  public AIAttackBehavior(Weapons weapons, int simplicity) {
    this.random = new Random();
    this.weapons = weapons;
    this.simplicity = simplicity;
  }
  
  public AIAttackBehavior(Weapons weapons) {
    this(weapons, 60);
  }

  public void useAttackCallback(Consumer<AIControl> callback) {
    this.callback = callback;
  }
  
  @Override
  public <T> void setAgent(Agent<T> agent) {
    super.setAgent(agent);
//    weapons = ((Model) agent.getModel()).weapons();
  }

  @Override
  public void updateAI(float tpf) {
    if (targetPosition != null) {
      weapons.gun().attack(targetedObject, tpf);
      callback.accept(targetedObject);
      targetedObject = null;
      //is he supossed to miss next time
      missOrNot((Agent) targetedObject);
    } else {
      //if target is seen
      if (targetedObject != null && targetedObject.isEnabled()) {
        //attack with all weapon at disposal

        //if target is in range of sword strike, strike target
        if (weapons.sword().isInRange(agent, targetedObject)) {
          weapons.sword().attack(targetedObject, tpf);
          callback.accept(targetedObject);
        }
        //if target is in range of gun, fire at target
        if (weapons.gun().isInRange(agent, targetedObject)) {
          weapons.gun().attack(targetedObject, tpf);
          callback.accept(targetedObject);
        }
        //is he supposed to miss next time
        missOrNot((Agent) targetedObject);
      }
    }
  }

  @Override
  public void handleAIControlSeenEvent(AIControlSeenEvent event) {
    if (event.getAIControlSeen() instanceof Agent targetAgent) {
      Model targetModel = (Model) targetAgent.getModel();

      if (targetModel.isSameTeam(agent)) {
        return;
      }
      
      targetedObject = targetAgent;
      missOrNot(targetAgent);
    }
  }

  private void missOrNot(Agent agent) {
    if (agent == null) {
      targetPosition = null;
    } else {
      if (simplicity > 1) {
        int number = random.nextInt(simplicity);
        if (number > 1) {
          targetPosition = agent.getWorldTranslation().clone().mult(1.1f);
        }
      }
    }
  }
}
