/**
 * Copyright (c) 2014, 2016 jMonkeyEngine. All rights reserved.
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

import com.jme3.ai.agents.AIControl;
import com.jme3.ai.agents.Agent;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.List;

/**
 * Move toward center of neighbors.
 *
 * @author Jesús Martín Berlanga
 * @author MeFisto94
 * @version 1.1.4
 */
public class CohesionBehavior extends AbstractStrengthSteeringBehavior {

    private List<AIControl> neighbours;
    private float maxDistance = Float.POSITIVE_INFINITY;
    private float maxAngle = FastMath.PI / 2;

    /**
     * maxAngle is setted to PI / 2 by default and maxDistance to infinite.
     *
     * @param agent To whom behavior belongs.
     * @param neighbours Neighbours, this agent is moving toward the center of
     * this neighbours.
     */
    public CohesionBehavior(Agent agent, List<AIControl> neighbours) {
        super(agent);
        this.neighbours = neighbours;
    }

    /**
     * @param maxDistance In order to consider a neighbour inside the
     * neighbourhood
     * @param maxAngle In order to consider a neighbour inside the neighbourhood
     *
     * @throws SteeringExceptions.NegativeValueException If maxDistance is lower than 0
     *
     * @see Agent#inBoidNeighborhoodMaxAngle(com.jme3.ai.agents.Agent, float,
     * float, float)
     * @see CohesionBehavior#CohesionBehavior(com.jme3.ai.agents.Agent,
     * java.util.List)
     */
    public CohesionBehavior(Agent agent, List<AIControl> neighbours, float maxDistance, float maxAngle) {
        super(agent);
        this.validateMaxDistance(maxDistance);
        this.neighbours = neighbours;
        this.maxDistance = maxDistance;
        this.maxAngle = maxAngle;
    }

    /**
     * @param spatial active spatial during excecution of behavior
     * @see CohesionBehavior#CohesionBehavior(com.jme3.ai.agents.Agent,
     * java.util.List)
     */
    public CohesionBehavior(Agent agent, List<AIControl> neighbours, Spatial spatial) {
        super(agent, spatial);
        this.neighbours = neighbours;
    }

    /**
     * @see CohesionBehavior#CohesionBehavior(com.jme3.ai.agents.Agent,
     * java.util.List)
     * @see CohesionBehavior#CohesionBehavior(com.jme3.ai.agents.Agent,
     * java.util.List, float, float)
     */
    public CohesionBehavior(Agent agent, List<AIControl> neighbours, float maxDistance, float maxAngle, Spatial spatial) {
        super(agent, spatial);
        this.validateMaxDistance(maxDistance);
        this.neighbours = neighbours;
        this.maxDistance = maxDistance;
        this.maxAngle = maxAngle;
    }

    private void validateMaxDistance(float maxDistance) {
        if (maxDistance < 0) {
            throw new SteeringExceptions.NegativeValueException("The max distance value can not be negative.", maxDistance);
        }
    }

    /**
     * @see AbstractSteeringBehavior#calculateSteering()
     */
    @Override
    protected Vector3f calculateRawSteering() {
        // steering accumulator and count of neighbors, both initially zero
        Vector3f steering = new Vector3f();
        int realNeighbors = 0;

        // for each of the other vehicles...
        for (AIControl neighbour : this.neighbours) {
            if (agent.inBoidNeighborhood(neighbour, agent.getRadius() * 3, this.maxDistance, this.maxAngle)) {
                // accumulate sum of neighbor's positions
                steering = steering.add(neighbour.getWorldTranslation());
                realNeighbors++;
            }
        }

        // divide by neighbors, subtract off current position to get error-correcting direction
        if (realNeighbors > 0) {
            steering = steering.divide(realNeighbors);
            steering.subtractLocal(agent.getWorldTranslation());
        }
        return steering;
    }

    /**
     * Sets the neighbourhood for this behavior, which defines which agents
     * influence it.
     *
     * @see #getNeighbours()
     * @param neighbours The list of agents
     */
    public void setNeighbours(List<AIControl> neighbours) {
        this.neighbours = neighbours;
    }

    /**
     * Returns the list of neighbours influencing this behavior, so you can
     * modify it.<br>
     * When an Agent dies, for example, you might want to remove it from the
     * neighbourhood of all other agent's behaviors.
     *
     * @see #setNeighbours(java.util.List)
     * @return The neighbourhood
     */
    public List<AIControl> getNeighbours() {
        return neighbours;
    }
}