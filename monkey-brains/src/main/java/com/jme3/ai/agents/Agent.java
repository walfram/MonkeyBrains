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
package com.jme3.ai.agents;

import com.jme3.ai.agents.behaviors.Behavior;
import com.jme3.ai.agents.behaviors.npc.SimpleMainBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.SteeringExceptions;
import com.jme3.scene.Spatial;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

/**
 * Class that represents Agent.
 *
 * @author Jesús Martín Berlanga
 * @author Tihomir Radosavljević
 * @version 1.7.4
 */
public class Agent<T> extends AIControl {

  /**
   * Class that enables you to add all variable you need for your agent.
   */
  private T model;

  /**
   * Main behaviour of Agent. Behavior that will be active while his alive.
   */
  private Behavior mainBehavior;

  /**
   * The speed with which the Agent will look into the new direction.
   */
  float rotationSpeed = FastMath.TWO_PI;

  ApplyType applyType = ApplyType.Spatial;

  /**
   * Instantiate an Agent without any parameters.
   * @deprecated Use the constructors specifing a radius or some behaviors
   * might not work
   */
  @Deprecated
  public Agent() {
    super(0.0f);
  }

  /**
   * @param spatial spatial that will agent have during game
   * @deprecated Use the constructors specifing a radius or some behaviors
   * might not work.<br>
   * <b>Deprecated.</b> Use the constructors not specifing a spatial attribute but 
   * add the Agent manually to a spatial (addControl)
   */
  @Deprecated
  public Agent(Spatial spatial) {
    super(0.0f);
    this.spatial = spatial;
  }

  public Agent(float radius) {
    super(radius);
  }

  /**
   * @return main behavior of agent
   */
  public Behavior getMainBehavior() {
    return mainBehavior;
  }

  /**
   * Setting main behavior to agent. For more how should main behavior look
   * like:
   *
   * @see SimpleMainBehavior
   * @param mainBehavior
   */
  public void setMainBehavior(Behavior mainBehavior) {
    this.mainBehavior = mainBehavior;
    mainBehavior.setAgent(this);
  }

  /**
   * @return The spatial's name this agent is assigned to
   */
  public String getName() {
    return spatial.getName();
  }

  /**
   * Returns the RotationSpeed (how fast the Agent will turn itself == looking
   * into the movement direction). This is essentially the "yaw" velocity.<br>
   * The Unit is "Radians per Second", so 2Pi -> Full turnaround in 1 sec<br>
   * <br>
   * @see #setRotationSpeed(float)
   * @return The Rotational Yaw Speed
   */
  public float getRotationSpeed() {
    return rotationSpeed;
  }

  /**
   * Sets the RotationSpeed (how fast the agent will turn around).<br>
   * This is essentially the "yaw" velocity<br>
   * <br>
   * @param rotationSpeed The Rotational Speed in "radians per second"
   * @see #getRotationSpeed()
   */
  public void setRotationSpeed(float rotationSpeed) {
    this.rotationSpeed = rotationSpeed;
  }

  /**
   * Return how this Agent's AI calculations are applied.
   *
   * @see ApplyType
   * @return The way calculations are applied
   */
  public ApplyType getApplyType() {
    return applyType;
  }

  /**
   * Define how this Agent's AI calculations are applied.
   *
   * @param applyType The way calculations are applied
   * @see ApplyType
   */
  public void setApplyType(ApplyType applyType) {
    this.applyType = applyType;
  }

  /**
   * @return model of agent
   */
  public T getModel() {
    return model;
  }

  /**
   * @param model of agent
   */
  public void setModel(T model) {
    this.model = model;
  }

  @Override
  public void updateAI(float tpf) {
    if (!enabled) {
        return;
    }
    
    
    if (model != null && model instanceof Updatable u) {
      u.update(tpf);
    }

    if (mainBehavior != null) {
      mainBehavior.updateAI(tpf);
    }
  }

  /**
   * Check if this agent is considered in the same "neighborhood" in relation
   * with another agent. <br> <br>
   *
   * If the distance is lower than minDistance It is definitely considered in
   * the same neighborhood. <br> <br>
   *
   * If the distance is higher than maxDistance It is definitely not
   * considered in the same neighborhood. <br> <br>
   *
   * If the distance is inside [minDistance. maxDistance] It is considered in
   * the same neighborhood if the forwardness is higher than "1 -
   * sinMaxAngle".
   *
   * @param neighbour The other agent
   * @param minDistance Min. distance to be in the same "neighborhood"
   * @param maxDistance Max. distance to be in the same "neighborhood"
   * @param maxAngle Max angle in radians
   *
   * @throws SteeringExceptions.NegativeValueException If minDistance or
   * maxDistance is lower than 0
   *
   * @return If this agent is in the same "neighborhood" in relation with
   * another agent.
   */
  public boolean inBoidNeighborhood(AIControl neighbour, float minDistance, float maxDistance, float maxAngle) {
    if (minDistance < 0) {
      throw new SteeringExceptions.NegativeValueException("The min distance can not be negative.", minDistance);
    } else if (maxDistance < 0) {
      throw new SteeringExceptions.NegativeValueException("The max distance can not be negative.", maxDistance);
    }
    boolean isInBoidNeighborhood;
    if (this == neighbour) {
      isInBoidNeighborhood = false;
    } else {
      Vector3f vecToNeighbour = vectorTo(neighbour);

      float distanceSquared = vecToNeighbour.lengthSquared();
      // definitely in neighborhood if inside minDistance sphere
      if (distanceSquared < (minDistance * minDistance)) {
        isInBoidNeighborhood = true;
      } // definitely not in neighborhood if outside maxDistance sphere
      else if (distanceSquared > maxDistance * maxDistance) {
        isInBoidNeighborhood = false;
      } // otherwise, test angular vectorTo from forward axis.
      else {
        Vector3f unitOffset = vecToNeighbour.divide(distanceSquared);
        float forwardness = this.forwardness(unitOffset);
        isInBoidNeighborhood = forwardness > FastMath.cos(maxAngle);
      }
    }

    return isInBoidNeighborhood;
  }

  /**
   * Given two vehicles, based on their current positions and velocities,
   * determine the time until nearest approach.
   *
   * @param aiControl Other aiControl
   * @return The time until nearest approach
   */
  public float predictNearestApproachTime(AIControl aiControl) {
    Vector3f agentVelocity = velocity;
    Vector3f otherVelocity = aiControl.getVelocity();

    if (agentVelocity == null) {
      agentVelocity = new Vector3f();
    }

    if (otherVelocity == null) {
      otherVelocity = new Vector3f();
    }

        /* "imagine we are at the origin with no velocity,
         compute the relative velocity of the other vehicle" */
    Vector3f relVel = otherVelocity.subtract(agentVelocity);
    float relSpeed = relVel.length();

        /* "Now consider the path of the other vehicle in this relative
         space, a line defined by the relative position and velocity.
         The distance from the origin (our vehicle) to that line is
         the nearest approach." */

    // "Take the unit tangent along the other vehicle's path"
    Vector3f relTangent = relVel.divide(relSpeed);

        /* "find distance from its path to origin (compute vectorTo from
         other to us, find length of projection onto path)" */
    Vector3f offset = aiControl.vectorTo(this);
    float projection = relTangent.dot(offset);

    return projection / relSpeed;
  }

  /**
   * Given the time until nearest approach (predictNearestApproachTime)
   * determine position of each vehicle at that time, and the distance between
   * them.
   *
   * @param agent Other agent
   * @param time The time until nearest approach
   * @return The time until nearest approach
   *
   * @see Agent#predictNearestApproachTime(com.jme3.ai.agents.Agent)
   */
  public float computeNearestApproachPositions(Agent agent, float time) {
    Vector3f agentVelocity = velocity;
    Vector3f otherVelocity = agent.getVelocity();

    if (agentVelocity == null) {
      agentVelocity = new Vector3f();
    }

    if (otherVelocity == null) {
      otherVelocity = new Vector3f();
    }

    Vector3f myTravel = agentVelocity.mult(time);
    Vector3f otherTravel = otherVelocity.mult(time);

    return myTravel.distance(otherTravel);
  }

  /**
   * Given the time until nearest approach (predictNearestApproachTime)
   * determine position of each vehicle at that time, and the distance between
   * them. <br> <br>
   *
   * Anotates the positions at nearest approach in the given vectors.
   *
   * @param aiControl Other aiControl
   * @param time The time until nearest approach
   * @param ourPositionAtNearestApproach Pointer to a vector, This bector will
   * be changed to our position at nearest approach
   * @param hisPositionAtNearestApproach Pointer to a vector, This bector will
   * be changed to other position at nearest approach
   *
   * @return The time until nearest approach
   *
   * @see Agent#predictNearestApproachTime(com.jme3.ai.agents.Agent)
   */
  public float computeNearestApproachPositions(AIControl aiControl, float time, Vector3f ourPositionAtNearestApproach,
      Vector3f hisPositionAtNearestApproach) {
    Vector3f agentVelocity = this.getVelocity();
    Vector3f otherVelocity = aiControl.getVelocity();

    if (agentVelocity == null) {
      agentVelocity = new Vector3f();
    }

    if (otherVelocity == null) {
      otherVelocity = new Vector3f();
    }

    Vector3f myTravel = agentVelocity.mult(time);
    Vector3f otherTravel = otherVelocity.mult(time);

    //annotation
    ourPositionAtNearestApproach.set(myTravel);
    hisPositionAtNearestApproach.set(otherTravel);

    return myTravel.distance(otherTravel);
  }

  @Override
  public String toString() {
    return "Agent{" + "name=" + getName() + '}';
  }
}
