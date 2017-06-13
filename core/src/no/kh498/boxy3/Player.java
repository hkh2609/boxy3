package no.kh498.boxy3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * @author karl henrik
 * @since 0.1.0
 */
public class Player {

    private final Body body;

    // A variable for tracking elapsed time for the animation
    private float stateTime;
    private final float sizeModifier;


    private boolean onGround = true;
    private boolean canJump = true;

    private float startJumpY = Float.MAX_VALUE;


    private final Animation<TextureRegion> walkAnimation;

    Player() {
        this.sizeModifier = 0.9f;
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
        final float size = (BoxyMain.TILE_RESOLUTION / 2f) * this.sizeModifier;
        playerBox.setAsBox(size, size);

        // Create a fixture definition to apply our shape to
        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = playerBox;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.15f; // Make it bounce a little bit

        // Create our fixture and attach it to the body
        final Fixture fixture = this.body.createFixture(fixtureDef);
        fixture.setUserData(this);
        playerBox.dispose();

        final TextureRegion[] walkFrames = BoxyMain.getAllRegions(new Texture(Gdx.files.internal("player.png")), 4, 2);
        // Initialize the Animation with the frame interval and array of frames
        this.walkAnimation = new Animation<TextureRegion>(0.33f, walkFrames);

    }

    private static final float MAX_VELOCITY_VERTICAL = 10f;
    private static final float MAX_VELOCITY_HORIZONTAL = 10f;

//    void updatePlayerLocation() {

////        System.out.println(this.body.getLinearVelocity().x);
//        final boolean canMoveHorizontally = Math.abs(vel.x) < MAX_VELOCITY_HORIZONTAL;
//
//        if (canMoveHorizontally && Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
////            this.deltaX -= this.movement * Gdx.graphics.getDeltaTime();
//            this.body.applyLinearImpulse(-2f, 0, pos.x, pos.y, true);
//        }
//        if (canMoveHorizontally && Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
////            this.deltaX += this.movement * Gdx.graphics.getDeltaTime();
//            this.body.applyLinearImpulse(2f, 0, pos.x, pos.y, true);
//        }
//        if (vel.x < MAX_VELOCITY_VERTICAL && Gdx.input.isKeyPressed(Input.Keys.UP) ||
//            Gdx.input.isKeyPressed(Input.Keys.W)) {
////            this.deltaX += this.movement * Gdx.graphics.getDeltaTime();
//            this.body.applyLinearImpulse(0, 10f, pos.x, pos.y, true);
//        }
//
//        if (Math.abs(vel.x) > MAX_VELOCITY_HORIZONTAL) {
//            vel.x = Math.signum(vel.x) * MAX_VELOCITY_HORIZONTAL;
//            this.body.setLinearVelocity(vel.x, vel.y);
//        }

//        if (pos.y > BoxyMain.HEIGHT_RESOLUTION) {
//            pos.y = BoxyMain.HEIGHT_RESOLUTION;
//            this.body.setLinearVelocity(this.body.getLinearVelocity().x, 0);
//        }

    void updatePlayerLocation() {
        final Vector2 vel = this.body.getLinearVelocity();
        final Vector2 pos = this.body.getPosition();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
//                this.deltaX -= this.movement * Gdx.graphics.getDeltaTime();
            this.body.applyLinearImpulse(-2f, 0, pos.x, pos.y, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
//                this.deltaX += this.movement * Gdx.graphics.getDeltaTime();
            this.body.applyLinearImpulse(2f, 0, pos.x, pos.y, true);
        }
        if (isJumpKeyPressed()) {
            if (isJumpKeyJustPressed() && this.onGround) {
                this.startJumpY = pos.y;
            }
            boolean canJump = true;
            //Disabled automatic rejumping when hitting the ground
            if (this.onGround && !isJumpKeyJustPressed()) {
                canJump = false;
                this.onGround = false;
            }
            //Make sure you cannot spam jump key to stay in the air
            else if (!this.onGround && isJumpKeyJustPressed()) {
                this.canJump = false;
            }


            if (this.canJump && canJump) {
                //JUMP!
//                    this.deltaY += (BoxyMain.HEIGHT - this.rectangle.y) * Gdx.graphics.getDeltaTime();
                this.body.applyLinearImpulse(0, 10f, pos.x, pos.y, true);
                //do not add forward momentum when above a certain y value
                final float maxJumpHeightModifier = 2f;
                if (pos.y > this.startJumpY * maxJumpHeightModifier) {
                    this.canJump = false;
                }
            }
        }
        //gravity
//        if (this.rectangle.y > BoxyMain.GROUND_LEVEL) {
//            this.deltaY -= this.rectangle.y * Gdx.graphics.getDeltaTime();
//        }
        //lowest y value is ground level
//        else {
//            this.rectangle.y = BoxyMain.GROUND_LEVEL;
//            this.canJump = true;
//            this.onGround = true;
//        }

        // make sure the player stays within the screen bounds
//            if (this.rectangle.x < 0) {
//                this.rectangle.x = 0;
//            }
//            if (this.rectangle.x > BoxyMain.WIDTH - BoxyMain.TILE_RESOLUTION) {
//                this.rectangle.x = BoxyMain.WIDTH - BoxyMain.TILE_RESOLUTION;
//            }
//            if (this.rectangle.y > BoxyMain.HEIGHT - BoxyMain.TILE_RESOLUTION) {
//                this.rectangle.y = BoxyMain.HEIGHT - BoxyMain.TILE_RESOLUTION;
//            }
    }

    private boolean isJumpKeyPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W);
    }

    private boolean isJumpKeyJustPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W);
    }


    void render(final Batch batch) {

        this.stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
        final TextureRegion currentFrame = this.walkAnimation.getKeyFrame(this.stateTime, true);

        final float size = BoxyMain.TILE_RESOLUTION * this.sizeModifier;
        final float locationModifier = (BoxyMain.TILE_RESOLUTION / 2) * this.sizeModifier;
        batch.draw(currentFrame, this.body.getPosition().x - locationModifier,
                   this.body.getPosition().y - locationModifier, size, size);
    }

    public Body getBody() {
        return this.body;
    }
    public boolean isOnGround() {
        return this.onGround;
    }
    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
    }
    public boolean isCanJump() {
        return this.canJump;
    }
    public void setCanJump(final boolean canJump) {
        this.canJump = canJump;
    }
}
