package no.kh498.boxy3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * @author karl henrik
 * @since 0.1.0
 */
class Player {

    final Body body;

    // A variable for tracking elapsed time for the animation
    private float stateTime;
    private final float sizeModifer;


    private final Animation<TextureRegion> walkAnimation;

    Player() {
        this.sizeModifer = 0.9f;
        // First we create a body definition
        final BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set our body's starting position in the world
        bodyDef.position.set(BoxyMain.WIDTH_RESOLUTION / 2, BoxyMain.TILE_RESOLUTION * 3 / 2);
        bodyDef.fixedRotation = true;

        // Create our body in the world using our body definition
        this.body = BoxyMain.world.createBody(bodyDef);

        final PolygonShape playerBox = new PolygonShape();
        final float size = (BoxyMain.TILE_RESOLUTION / 2f) * this.sizeModifer;
        playerBox.setAsBox(size, size);

        // Create a fixture definition to apply our shape to
        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = playerBox;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.2f;
        fixtureDef.restitution = 0.15f; // Make it bounce a little bit

        // Create our fixture and attach it to the body
        this.body.createFixture(fixtureDef);
        playerBox.dispose();

        final TextureRegion[] walkFrames = BoxyMain.getAllRegions(new Texture(Gdx.files.internal("player.png")), 4, 2);
        // Initialize the Animation with the frame interval and array of frames
        this.walkAnimation = new Animation<TextureRegion>(0.33f, walkFrames);

    }

    private static final float MAX_VELOCITY_VERTICAL = 10f;
    private static final float MAX_VELOCITY_HORIZONTAL = 7.5f;

    void updatePlayerLocation() {
        if (this.body.getLinearVelocity().x < MAX_VELOCITY_HORIZONTAL && Gdx.input.isKeyPressed(Input.Keys.LEFT) ||
            Gdx.input.isKeyPressed(Input.Keys.A)) {
//            this.deltaX -= this.movement * Gdx.graphics.getDeltaTime();
            this.body.applyLinearImpulse(-2f, 0, this.body.getPosition().x, this.body.getPosition().y, true);
        }
        if (this.body.getLinearVelocity().x < MAX_VELOCITY_HORIZONTAL && Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
            Gdx.input.isKeyPressed(Input.Keys.D)) {
//            this.deltaX += this.movement * Gdx.graphics.getDeltaTime();
            this.body.applyLinearImpulse(2f, 0, this.body.getPosition().x, this.body.getPosition().y, true);
        }
        if (this.body.getLinearVelocity().x < MAX_VELOCITY_VERTICAL && Gdx.input.isKeyPressed(Input.Keys.UP) ||
            Gdx.input.isKeyPressed(Input.Keys.W)) {
//            this.deltaX += this.movement * Gdx.graphics.getDeltaTime();
            this.body.applyLinearImpulse(0, 10f, this.body.getPosition().x, this.body.getPosition().y, true);
        }
        if (this.body.getPosition().y > BoxyMain.HEIGHT_RESOLUTION) {
            this.body.getPosition().y = BoxyMain.HEIGHT_RESOLUTION;
            this.body.setLinearVelocity(this.body.getLinearVelocity().x, 0);
        }
    }

    void render(final Batch batch) {

        this.stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
        final TextureRegion currentFrame = this.walkAnimation.getKeyFrame(this.stateTime, true);

        final float size = BoxyMain.TILE_RESOLUTION * this.sizeModifer;
        final float locationModifier = (BoxyMain.TILE_RESOLUTION / 2) * this.sizeModifer;
        batch.draw(currentFrame, this.body.getPosition().x - locationModifier,
                   this.body.getPosition().y - locationModifier, size, size);
    }
}
