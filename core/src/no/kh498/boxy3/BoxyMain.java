package no.kh498.boxy3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import no.kh498.boxy3.tile.WorldListener;

public class BoxyMain extends ApplicationAdapter {
    private SpriteBatch batch;
    private static OrthographicCamera camera;
    private Player player;
    static World world;
    private static Box2DDebugRenderer debugRenderer;

    static final int TILE_RESOLUTION = 2;
    static final int WIDTH_RESOLUTION = 30;
    static final int HEIGHT_RESOLUTION = 18;

    private final boolean debugInfo = true;

    private float accumulator = 0;

    private BitmapFont yourBitmapFontName;
    //    private TileEntity[] defaultGround;
    private Map map;

    @Override
    public void create() {
        this.yourBitmapFontName = new BitmapFont();
        this.yourBitmapFontName.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        // load the images for the droplet and the player, 64x64 pixels each
//        this.walkSheet = new Texture(Gdx.files.internal("player.png"));

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH_RESOLUTION, HEIGHT_RESOLUTION);
        camera.zoom = 2f;
        this.batch = new SpriteBatch(64);


        debugRenderer = new Box2DDebugRenderer();
        world = new World(new Vector2(0, -100), true);
        world.setContactListener(new WorldListener());

        // create a Rectangle to logically represent the player
        this.player = new Player();
        this.map = new Map("1.lvl");
    }


    @Override
    public void render() {
        // clear the screen with a dark blue color.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);

        /* Update positions */

        // tell the camera to update its matrices.
//        camera.position.set(this.player.body.getPosition().x + (HEIGHT_RESOLUTION * .25f), HEIGHT_RESOLUTION / 2, 0);
        camera.update();
        final float lerp = 1f;
        final Vector3 position = camera.position;
        position.x += ((this.player.body.getPosition().x + (HEIGHT_RESOLUTION * .25f)) - position.x) * lerp *
                      Gdx.graphics.getDeltaTime();
//        position.y += ((HEIGHT_RESOLUTION / 2) - position.y) * lerp * Gdx.graphics.getDeltaTime();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        this.batch.setProjectionMatrix(camera.combined);


        // Get current frame of animation for the current stateTime

        //Handle player movements
        this.player.updatePlayerLocation();

        /* Draw the screen */
        this.batch.begin();
        this.player.render(this.batch);
        this.map.draw(this.batch);
        this.batch.end();

        debugRenderer.render(world, camera.combined);
        doPhysicsStep(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        this.batch.dispose();
        this.yourBitmapFontName.dispose();
    }

    private void doPhysicsStep(final float deltaTime) {
        final float timeStep = 1 / 45f;
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        final float frameTime = Math.min(deltaTime, 0.25f);
        this.accumulator += frameTime;
        while (this.accumulator >= timeStep) {
            world.step(timeStep, 6, 2);
            this.accumulator -= timeStep;
        }
    }

    @SuppressWarnings("SameParameterValue")
    static TextureRegion[] getAllRegions(final Texture texture, final int colons, final int rows) {
        final TextureRegion[][] tmp =
            TextureRegion.split(texture, texture.getWidth() / colons, texture.getHeight() / rows);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first.
        final TextureRegion[] walkFrames = new TextureRegion[colons * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colons; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        return walkFrames;
    }
}
