package no.kh498.boxy3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author karl henrik
 * @since 0.1.0
 */
public class Map {

    private final String levelName;
    private final TileEntity[][] tiles;

    Map(final String levelName) {
        this.levelName = "levels/" + levelName;
        this.tiles = load();
    }

    private TileEntity[][] load() {

        final HashMap<Integer, List> tempMap = new HashMap<Integer, List>();
        final FileHandle internal = Gdx.files.internal(this.levelName);
        final InputStream inputStream = internal.read();
        final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        final LineNumberReader lnr;
        final int nrOfLines;
        try {
            lnr = new LineNumberReader(new FileReader(internal.file()));
            lnr.skip(Long.MAX_VALUE);
            nrOfLines = lnr.getLineNumber() + 1;
            System.out.println("nrOfLines " + nrOfLines);
            lnr.close();
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }


        try {
            int i = 0;
            int maxArraySize = -1;
            while (br.ready()) {
                final String line = br.readLine();
                System.out.println("line " + i + ": " + line);
                final ArrayList<TileBody> tileEntities = new ArrayList<TileBody>();
                final char[] chars = line.toCharArray();
                int arraySize = 0;
                for (int j = 0; j < chars.length; j += 0) {
                    final char baseChar = chars[j];
                    int amount = 1;
                    while (j + amount < chars.length && baseChar == chars[j + amount]) {
                        amount++;
                    }
                    final TileBody tileEntity =
                        new TileBody(baseChar, j * BoxyMain.TILE_RESOLUTION, (nrOfLines - i) * BoxyMain.TILE_RESOLUTION,
                                     amount);
                    tileEntities.add(tileEntity);
                    arraySize++;
                    j += amount;
                    if (maxArraySize < arraySize) {
                        maxArraySize = arraySize;
                    }
                }

                tempMap.put(i, tileEntities);
                i++;
            }
            inputStream.close();
            br.close();

            final TileBody[][] returnArray = new TileBody[i][maxArraySize];
            for (final java.util.Map.Entry<Integer, List> collection : tempMap.entrySet()) {
                final int key = collection.getKey();
                //noinspection unchecked
                final ArrayList<TileBody> value = (ArrayList<TileBody>) collection.getValue();
                for (int j = 0; j < value.size(); j++) {
                    returnArray[key][j] = value.get(j);
                }
            }
            System.out.println(Arrays.deepToString(returnArray));
            return returnArray;
        } catch (final IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public void draw(final Batch batch) {
        if (this.tiles == null) {
            return;
        }
        for (final TileEntity[] tile : this.tiles) {
            for (final TileEntity aTile : tile) {
                if (aTile != null) {
                    aTile.draw(batch);
                }
            }
        }
    }

    static TileEntity[] getDefaultGround() {
        final TileEntity[] ground = new TileEntity[BoxyMain.WIDTH_RESOLUTION];
        for (int i = 0; i < BoxyMain.WIDTH_RESOLUTION; i++) {
            ground[i] = new TileEntity(Tile.COBBLESTONE, i * BoxyMain.TILE_RESOLUTION, BoxyMain.TILE_RESOLUTION);
        }
        return ground;
    }
}
