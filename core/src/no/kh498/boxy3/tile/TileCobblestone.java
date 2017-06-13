package no.kh498.boxy3.tile;

import no.kh498.boxy3.Player;
import no.kh498.boxy3.Tile;

/**
 * @author karl henrik
 * @since 0.1.0
 */
public class TileCobblestone extends AbstractTile {

    public TileCobblestone() {}

    @Override
    public void onPlayerTouch(final Player player) {
        player.setOnGround(true);
        player.setCanJump(true);
    }

    @Override
    public Tile getTile() {
        return Tile.COBBLESTONE;
    }
}
