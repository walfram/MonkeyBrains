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
package com.jme3.ai.agents.util;

import com.jme3.ai.agents.AIControl;

/**
 * Class container for exceptions related to game entities.
 *
 * @author Tihomir Radosavljević
 * @author Jesús Martín Berlanga
 * @version 1.1.0
 */
public class AIControlExceptions {

    /**
     * Main exception for all AIControl related exceptions.
     */
    public static class AIControlException extends RuntimeException {

        public AIControlException(String message) {
            super(message);
        }
    }

    /**
     * Attribute is not initialized in game entity.
     */
    public static class AIControlAttributeNotFound extends AIControlException {

        public AIControlAttributeNotFound(AIControl aiControl, String message) {
            super(aiControl + " doesn't have " + message + '.');
        }
    }

    /**
     * Game entity has negative radius.
     */
    public static class NegativeRadiusException extends AIControlException {

        public NegativeRadiusException(String message) {
            super(message);
        }
    }
    
    public static class MaxAIControlsException extends RuntimeException {

        public MaxAIControlsException() {
            super("There are more AIControls than the framework supports.");
        } 
    }
}
