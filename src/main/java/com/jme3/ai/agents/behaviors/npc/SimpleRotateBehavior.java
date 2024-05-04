/**
 * Copyright (c) 2014, jMonkeyEngine All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of 'jMonkeyEngine' nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.ai.agents.behaviors.npc;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviors.Behavior;
import com.jme3.math.Quaternion;
import com.jme3.scene.Spatial;

/**
 * Simple rotate behavior. If spatial isn't added, then it will rotate all
 * agent spatials. If spatial is added then it will rotate only that spatial.
 *
 * @author Tihomir Radosavljević
 * @version 1.0.1
 */
public class SimpleRotateBehavior extends Behavior {

    /**
     * Direction of rotation in whic agent should rotate.
     */
    private Quaternion rotationDirection;
    /**
     * Rotation to which agent should turn.
     */
    private Quaternion rotationTarget;

    /**
     * Instantiate a new Behavior. Agent is passed when you add this behavior to
     * an agent.
     */
    public SimpleRotateBehavior() {
        super();
    }

    @Override
    public void updateAI(float tpf) {
        //if there isn't spatial
        if (agent.getSpatial() == null) {
            //if there is rotation target
            if (rotationTarget != null) {
                rotationDirection = agent.getSpatial().getLocalRotation().clone();
                rotationDirection.slerp(rotationTarget, agent.getRotationSpeed() * tpf);
                agent.getSpatial().rotate(rotationDirection);
            } else {
                //if there is movement direction in which agent should move
                if (rotationDirection != null) {
                    agent.getSpatial().rotate(rotationDirection.mult(agent.getRotationSpeed() * tpf));
                }
            }
        } else {
            //if there is spatial
            //if there is rotation target
            if (rotationTarget != null) {
                rotationDirection = agent.getSpatial().getLocalRotation().clone();
                rotationDirection.slerp(rotationTarget, agent.getRotationSpeed() * tpf);
                agent.getSpatial().rotate(rotationDirection);
            } else {
                //if there is movement direction in which agent should move
                if (rotationDirection != null) {
                    agent.getSpatial().rotate(rotationDirection.mult(agent.getRotationSpeed() * tpf));
                }
            }
        }
    }

    /**
     *
     * @return direction of rotation
     */
    public Quaternion getRotationDirection() {
        return rotationDirection;
    }

    /**
     *
     * @param rotationDirection direction of rotation
     */
    public void setRotationDirection(Quaternion rotationDirection) {
        this.rotationDirection = rotationDirection;
    }

    /**
     *
     * @return rotation of target
     */
    public Quaternion getRotationTarget() {
        return rotationTarget;
    }

    /**
     *
     * @param rotationTarget rotation of target
     */
    public void setRotationTarget(Quaternion rotationTarget) {
        this.rotationTarget = rotationTarget;
    }
}