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
import com.jme3.math.Vector3f;
import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;
import java.util.List;

/**
 * Separation steering behavior gives a character the ability to maintain a
 * certain separation distance from others nearby. This can be used to prevent
 * characters from crowding together. <br><br>

 For each nearby character, a repulsive force is computed by subtracting the
 positions of our character and the nearby character, normalizing, and then
 applying a 1/r weighting. (That is, the position vectorTo vector is scaled by
 1/r^2.)". "These repulsive forces for each nearby character are summed
 together to produce the overall steering force. <br><br>
 *
 * The supplied neighbours should only be the nearby neighbours in the field of
 * view of the character that is steering. It is good to ignore anything behind
 * the character."
 *
 * @author Jesús Martín Berlanga
 * @author MeFisto94
 * @version 1.3.3
 */
public class SeparationBehavior extends AbstractStrengthSteeringBehavior {

    private float minDistance;
    /*
     * List of the obstacles that we want to be separated
     */
    private List<AIControl> obstacles;

    /**
     * @param initialObstacles Initializes a list with the obstacles from the
     * agent want to be separated
     */
    public SeparationBehavior(List<AIControl> initialObstacles) {
        this(initialObstacles, Float.POSITIVE_INFINITY);
    }

    /**
     * @param minDistance Min. distance from center to center to consider an
     * obstacle
     * @see SeparationBehavior#SeparationBehavior(com.jme3.ai.agents.Agent,
     * java.util.List)
     */
    public SeparationBehavior(List<AIControl> initialObstacles, float minDistance) {
        super();
        this.validateMinDistance(minDistance);
        this.obstacles = initialObstacles;
        this.minDistance = minDistance;
    }

    private void validateMinDistance(float minDistance) {
        if (minDistance < 0) {
            throw new SteeringExceptions.NegativeValueException("The min distance from an obstacle can not be negative.", minDistance);
        }
    }

    /**
     * @see AbstractSteeringBehavior#calculateSteering()
     */
    @Override
    protected Vector3f calculateRawSteering() {
        //Propities whom behaviour belongs.
        Vector3f agentLocation = super.agent.getWorldTranslation();
        Vector3f steering = new Vector3f();

        for (AIControl obstacle : this.obstacles) {
            //If the obstacle is not himself
            if (obstacle != this.agent && obstacle.distanceTo(this.agent) < this.minDistance) {
                Vector3f location = obstacle.getWorldTranslation().subtract(agentLocation);
                float lengthSquared = location.lengthSquared();
                location.normalizeLocal();
                steering.addLocal(location.negate().mult(1f / ((float) FastMath.pow(lengthSquared, 2))));
            }
        }

        return steering;
    }

    public void setMinDistance(float minDistance) {
        this.minDistance = minDistance;
    }

    /**
     * Sets the list containing all obstacles for this behavior.<br>To remove an 
     * obstacle, better use <code>getObstacles().remove(Obstacle)</code>.
     *
     * @see #getObstacles()
     * @param obstacles The list of agents
     */
    public void setObstacles(List<AIControl> obstacles) {
        this.obstacles = obstacles;
    }

    /**
     * Returns the list of obstacles influencing this behavior, so you can
     * modify it.<br>
     *
     * @see #setObstacles(java.util.List)
     * @return The obstacles
     */
    public List<AIControl> getObstacles() {
        return obstacles;
    }
}
