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
package com.jme3.ai.agents.behaviors.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.math.Vector3f;

/**
 * Purpose of seek behavior is to steer agent towards a specified position or
 * object.<br>
 *
 * You can seek another agent or a specific space location.
 *
 * @author Tihomir Radosavljević
 * @author Jesús Martín Berlanga
 * @version 1.5.1
 */
public class SeekBehavior extends AbstractStrengthSteeringBehavior {

    /**
     * Agent whom we seek.
     */
    private Agent target;
    private Vector3f seekingPosition;

    /**
     * Instantiate a new Behavior. Agent is passed when you add this behavior to
     * an agent.
     */
    public SeekBehavior() {
        super();
    }

    /**
     * Constructor for seek behaviour.
     *
     * @param target agent whom we seek
     */
    public SeekBehavior(Agent target) {
        this();
        this.target = target;
    }

    /**
     * Constructor for seek behavior.
     *
     * @param seekingPosition position that we seek
     */
    public SeekBehavior(Vector3f seekingPosition) {
        this();
        this.seekingPosition = seekingPosition;
    }

    public SeekBehavior(Agent agent, Agent target) {
        this(target);
        setAgent(agent);
    }

    /**
     * Calculate steering vector.
     *
     * @return steering vector
     *
     * @see AbstractStrengthSteeringBehavior#calculateRawSteering()
     */
    @Override
    protected Vector3f calculateRawSteering() {
        Vector3f desiredVelocity; // the velocity we want to have
        float len = 0f;
        
        if (this.target != null) {
            desiredVelocity = agent.vectorTo(target).normalizeLocal().multLocal(agent.getMaxMoveSpeed());
            len = agent.vectorTo(target).lengthSquared();
        } else if (this.seekingPosition != null) {
            desiredVelocity = agent.vectorTo(seekingPosition).normalizeLocal().multLocal(agent.getMaxMoveSpeed());
            len = agent.vectorTo(seekingPosition).lengthSquared();
        } else {
            return new Vector3f(); //We do not have a target or position to seek
        }
        
        if (len < ArriveBehavior.ERROR_FACTOR * ArriveBehavior.ERROR_FACTOR) {
            return Vector3f.ZERO.clone();
        }
        
        Vector3f aVelocity = agent.getVelocity();

        if (aVelocity == null) {
            aVelocity = new Vector3f();
        }
        
        /* For this, we had to multiply desiredVelocity with MaxMoveSpeed.
         * If we would normalize anything, we wouldn't accelerate once we head 
         * into the right direction. That way the steering only becomes zero,
         * if we head full throttle into the right direction
         */

        return desiredVelocity.subtractLocal(aVelocity).normalizeLocal();
    }

    /**
     * Get agent from we seek.
     *
     * @return agent
     */
    public Agent getTarget() {
        return target;
    }

    /**
     * Setting agent from we seek.
     *
     * @param target
     */
    public void setTarget(Agent target) {
        this.target = target;
        this.seekingPosition = null;
    }

    public Vector3f getSeekingPosition() {
        return this.seekingPosition;
    }

    public void setSeekingPosition(Vector3f seekingPosition) {
        this.seekingPosition = seekingPosition;
        this.target = null;
    }
}
