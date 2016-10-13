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
package com.jme3.ai.agents.behaviors;

import com.jme3.ai.agents.Agent;

/**
 * Base class for agent behaviors.
 *
 * @author Tihomir Radosavljević
 * @author Jesús Martín Berlanga
 * @version 1.2.1
 */
public abstract class Behavior {

    /**
     * Agent to whom behavior belongs.
     */
    protected Agent agent;

    /**
     * Instantiate a new Behavior. Agent is passed when you add this behavior to
     * an agent.
     */
    public Behavior() {
    }

    /**
     * Sets this Behavior's Agent.<br>This is called internally when you set
     * an agent's behavior field.<br>
     * That way you don't have to specify the agent for a behavior and it's 
     * agent field is never null.
     * @param agent 
     */
    public void setAgent(Agent agent) {
        this.agent = agent;
    }
    
    /**
     * Triggers a behavior iteration/calculation for this frame.<br>
     * After a call to updateAI the velocity and all have to be updated.<br>
     * <br>
     * @param tpf Time per Frame
     * (how many seconds it took after the last update call)
     */
    public abstract void updateAI(float tpf);
}
