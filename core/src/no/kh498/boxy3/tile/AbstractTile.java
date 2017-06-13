package no.kh498.boxy3.tile;

import no.kh498.boxy3.Player;
import no.kh498.boxy3.Tile;

/**
 * @author karl henrik
 * @since 0.1.0
 */
public abstract class AbstractTile {
    
    public abstract void onPlayerTouch(Player player);

    public abstract Tile getTile();
}
