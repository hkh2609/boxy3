package no.kh498.boxy3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class BoxyMain extends ApplicationAdapter {
    private Texture groundTexture;
    private Texture bucketImage;
    //    private Sound dropSound;
//    private Music rainMusic;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Player player;
    private Array<Rectangle> raindrops;
    private long lastDropTime;
    public static final int TILE_RESOLUTION = 64;
    public static final int GROUND_LEVEL = 64;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;

    // Constant rows and columns of the sprite sheet
    private static final int FRAME_COLS = 4, FRAME_ROWS = 2;
    com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
    Texture walkSheet;

    // A variable for tracking elapsed time for the animation
    float stateTime;

    @Override
    public void create() {
        // load the images for the droplet and the player, 64x64 pixels each
        this.groundTexture = new Texture(Gdx.files.internal("ground.png"));
        this.walkSheet = new Texture(Gdx.files.internal("textureAtlas.png"));

        // create the camera and the SpriteBatch
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, WIDTH, HEIGHT);
        this.batch = new SpriteBatch();

        // create a Rectangle to logically represent the player
        this.player = new Player();
        this.player.getRectangle().x = WIDTH / 2 - TILE_RESOLUTION / 2; // center the player horizontally
        this.player.getRectangle().y = 20; // bottom left corner of the player is 20 pixels above the bottom screen edge
        this.player.getRectangle().width = TILE_RESOLUTION;
        this.player.getRectangle().height = TILE_RESOLUTION;

        // create the raindrops array and spawn the first raindrop
        this.raindrops = new Array<Rectangle>();
//        spawnRaindrop();

        // --- Animation

        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        final TextureRegion[][] tmp = TextureRegion
            .split(this.walkSheet, this.walkSheet.getWidth() / FRAME_COLS, this.walkSheet.getHeight() / FRAME_ROWS);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        final TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        // Initialize the Animation with the frame interval and array of frames
        this.walkAnimation = new Animation<TextureRegion>(0.33f, walkFrames);

        // Instantiate a SpriteBatch for drawing and reset the elapsed animation
        // time to 0
        this.stateTime = 0f;

    }

    private void spawnRaindrop() {
        final Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, WIDTH - 64);

        raindrop.y = 480;
        raindrop.width = TILE_RESOLUTION;
        raindrop.height = TILE_RESOLUTION;
        this.raindrops.add(raindrop);
        this.lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void render() {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);


        // tell the camera to update its matrices.
        this.camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        this.batch.setProjectionMatrix(this.camera.combined);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen
        this.stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        // Get current frame of animation for the current stateTime
        final TextureRegion currentFrame = this.walkAnimation.getKeyFrame(this.stateTime, true);

        //Move the player with the acceleration
        this.player.getRectangle().x += this.player.deltaX;
        this.player.getRectangle().y += this.player.deltaY;
        //reduce the acceleration by the drag
        this.player.deltaX *= this.player.getDrag();
        this.player.deltaY *= this.player.getDrag();

        // begin a new batch
        this.batch.begin();
        //player
        this.batch.draw(currentFrame, this.player.getRectangle().x, this.player.getRectangle().y);
        //ground
        for (int i = 0; i <= WIDTH / TILE_RESOLUTION; i++) {
            this.batch.draw(this.groundTexture, (i * TILE_RESOLUTION), 0);
        }
        this.batch.end();

        updatePlayerLocation();
    }

    private void updatePlayerLocation() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.player.deltaX -= this.player.getMovement() * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.player.deltaX += this.player.getMovement() * Gdx.graphics.getDeltaTime();
        }
        if (isJumpKeyPressed()) {
            boolean canJump = true;
            //Disabled automatic rejumping when hitting the ground
            if (this.player.isOnGround() && !isJumpKeyJustPressed()) {
                canJump = false;
                this.player.setOnGround(false);
            }
            //Make sure you cannot spam jump key to stay in the air
            else if (!this.player.isOnGround() && isJumpKeyJustPressed()) {
                this.player.setCanJump(false);
            }

            if (this.player.canJump() && canJump) {
                //JUMP!
                this.player.deltaY += this.player.getMovement() * Gdx.graphics.getDeltaTime();
                //do not add forward momentum when above a certain y value
                if (this.player.getRectangle().y > GROUND_LEVEL * this.player.getMaxJumpHeightModifier()) {
                    this.player.setCanJump(false);
                }
            }
        }
        //gravity
        if (this.player.getRectangle().y > GROUND_LEVEL) {
            this.player.deltaY -= this.player.getRectangle().y * Gdx.graphics.getDeltaTime();
        }
        //lowest y value is ground level
        else {
            this.player.getRectangle().y = GROUND_LEVEL;
            this.player.setCanJump(true);
            this.player.setOnGround(true);
        }

        // make sure the player stays within the screen bounds
        if (this.player.getRectangle().x < 0) {
            this.player.getRectangle().x = 0;
        }
        if (this.player.getRectangle().x > WIDTH - TILE_RESOLUTION) {
            this.player.getRectangle().x = WIDTH - TILE_RESOLUTION;
        }
        if (this.player.getRectangle().y > HEIGHT - TILE_RESOLUTION) {
            this.player.getRectangle().y = HEIGHT - TILE_RESOLUTION;
        }
    }

    private boolean isJumpKeyPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W);
    }

    private boolean isJumpKeyJustPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W);
    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        this.groundTexture.dispose();
        this.bucketImage.dispose();
        this.batch.dispose();
        this.walkSheet.dispose();
    }
}
