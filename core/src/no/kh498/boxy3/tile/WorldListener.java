package no.kh498.boxy3.tile;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import no.kh498.boxy3.Player;
import no.kh498.boxy3.Tile;

/**
 * @author karl henrik
 * @since 0.1.0
 */
public class WorldListener implements ContactListener {
    /**
     * Called when two fixtures begin to touch.
     *
     * @param contact
     */
    @Override
    public void beginContact(final Contact contact) {
        final Tile tile = (Tile) contact.getFixtureB().getUserData();
        if (tile != null) {
            final AbstractTile handler = tile.getHandler();
            if (handler != null) {
                final Object userData = contact.getFixtureA().getUserData();
                if (userData instanceof Player) {
                    handler.onPlayerTouch((Player) userData);
                }
            }
        }
    }
    /**
     * Called when two fixtures cease to touch.
     *
     * @param contact
     */
    @Override
    public void endContact(final Contact contact) {

    }
    @Override
    public void preSolve(final Contact contact, final Manifold oldManifold) {

    }
    @Override
    public void postSolve(final Contact contact, final ContactImpulse impulse) {

    }
}
