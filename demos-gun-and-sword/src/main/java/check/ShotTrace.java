package check;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterBoxShape;
import com.jme3.effect.shapes.EmitterShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import jme3utilities.SimpleControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShotTrace {

  private static final Logger logger = LoggerFactory.getLogger(ShotTrace.class);
  
  private final AssetManager assetManager;

  public ShotTrace(AssetManager assetManager) {
    this.assetManager = assetManager;
  }

  public ParticleEmitter emitter() {
    ParticleEmitter emitter = new ParticleEmitter("sparks", Type.Point, 256);

    Material material = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
    material.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flash.png"));
    material.setBoolean("PointSprite", true);
    emitter.setMaterial(material);

    EmitterShape emitterShape = new EmitterBoxShape(
        new Vector3f(-0.0125f, -0.0125f, 0f),
        new Vector3f(0.0125f, 0.0125f, 32f)
    );
    emitter.setShape(emitterShape);

    emitter.setParticlesPerSec(0);

    emitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0, 0));
    emitter.getParticleInfluencer().setVelocityVariation(0.1f);

    emitter.setLowLife(1.3f);
    emitter.setHighLife(1.5f);

    emitter.setStartSize(0.15f);
    emitter.setEndSize(0.125f);
    
    emitter.setStartColor(ColorRGBA.Red);
    emitter.setEndColor(ColorRGBA.Orange);

    emitter.setImagesX(2);
    emitter.setImagesY(2);

    emitter.setFacingVelocity(true);

    emitter.setEnabled(false);
    
    emitter.addControl(new SimpleControl() {
      @Override
      protected void controlUpdate(float updateInterval) {
        if (!emitter.isEnabled())
          return;

        int numVisibleParticles = emitter.getNumVisibleParticles();
        if (numVisibleParticles <= 1) {
          logger.debug("### shot trace: disabling particles");
          emitter.killAllParticles();
          emitter.setEnabled(false);
        }
      }
    });
    
    return emitter;
  }
}
