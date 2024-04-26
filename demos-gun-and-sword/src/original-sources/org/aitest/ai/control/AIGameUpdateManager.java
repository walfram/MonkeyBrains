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
package org.aitest.ai.control;

import com.jme3.app.state.AbstractAppState;

/**
 * Class for controling that all updates are in 60fps.
 *
 * @author mifth
 * @version 1.0.0
 */
public class AIGameUpdateManager extends AbstractAppState {

  private long lastFrame = System.nanoTime();
  private double lastPreviousFrame = 0.0;
  private float currentTpf = 0f;
  private boolean update = false;
  // It's 60fps.
  private final static double framerate = 1.0 / 60.0;

  public boolean IsUpdate() {
    return update;
  }

  public float getCurrentTpf() {
    return currentTpf;
  }

  public void setCurrentTpf(float currentTpf) {
    this.currentTpf = currentTpf;
  }

  @Override
  public void update(float tpf) {
    // Use our own tpf calculation in case frame rate is
    // running away making this tpf unstable
    long time = System.nanoTime();

    long delta = time - lastFrame;

    double seconds = (delta / 1000000000.0);

    // Clamp frame time to no bigger than a certain amount 60fps
    if (seconds + lastPreviousFrame >= framerate) {
      lastFrame = time;
//            System.out.println(seconds);
      update = true;

      currentTpf = (float) seconds;
      lastPreviousFrame = (seconds + lastPreviousFrame) % framerate;

//            System.out.println(currentTpf + "" + tpf);

    } else {
      update = false;
//            System.out.println("Shit  " + tpf);
    }
  }
}
