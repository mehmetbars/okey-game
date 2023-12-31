import java.util.Random;

public class OkeyGame {

    Player[] players;
    Tile[] tiles;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;

    public OkeyGame() {
        players = new Player[4];
    }

    public void createTiles() {
        tiles = new Tile[104];
        int currentTile = 0;

        // two copies of each color-value combination, no jokers
        for (int i = 1; i <= 13; i++) {
            for (int j = 0; j < 2; j++) {
                tiles[currentTile++] = new Tile(i,'Y');
                tiles[currentTile++] = new Tile(i,'B');
                tiles[currentTile++] = new Tile(i,'R');
                tiles[currentTile++] = new Tile(i,'K');
            }
        }
    }

    /*
     * TODO: distributes the starting tiles to the players
     * player at index 0 gets 15 tiles and starts first
     * other players get 14 tiles
     * this method assumes the tiles are already sorted
     */
    public void distributeTilesToPlayers() {
        int titleIndex = 0;

        for(int i = 0; i < 4; i++){
            if(i == 0){
                for( int j = 0; j < 15; j ++){
                    players[i].addTile(tiles[titleIndex++]);
                }
            }
            else{
                for(int j = 0; j < 14; j++){
                    players[i].addTile(tiles[titleIndex++]);
                }
            }
        }
    }

    /*
     * TODO: get the last discarded tile for the current player
     * (this simulates picking up the tile discarded by the previous player)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getLastDiscardedTile() {
        if(lastDiscardedTile == null){
            return lastDiscardedTile.toString();
        }
        else {
            return "No tile has been discarded yet.";
        }
        
    }

    /*
     * TODO: get the top tile from tiles array for the current player
     * that tile is no longer in the tiles array (this simulates picking up the top tile)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public Tile getTopTile() {
        if(tiles == null || tiles.length == 0 ){
            return null;
        }
        Tile topTile = this.tiles[tiles.length - 1];

        Tile[] newTiles = new Tile[tiles.length - 1];

        System.arraycopy(tiles, 0, newTiles, 0, tiles.length - 1);
        tiles = newTiles;
        return topTile;
    }

    /*
     * TODO: should randomly shuffle the tiles array before game starts
     */
    public void shuffleTiles() {
        Tile[] shufledTiles = new Tile[tiles.length];
        Random randomIndex = new Random();

        for(int i = 0; i < tiles.length; i++){
            int index = randomIndex.nextInt(tiles.length - 1);
            shufledTiles[i] = tiles[index];
            removeElement(tiles, index);
        }
    }
    //adding helper method forshuffleTiles method
    private static Tile[] removeElement(Tile[] original, int index) {
        if (original == null || index < 0 || index >= original.length) {
            return original;
        }
    
        Tile[] result = new Tile[original.length - 1];
        System.arraycopy(original, 0, result, 0, index);
        System.arraycopy(original, index + 1, result, index, original.length - index - 1);
    
        return result;
    }
    

    /*
     * TODO: check if game still continues, should return true if current player
     * finished the game. Use calculateLongestChainPerTile method to get the
     * longest chains per tile.
     * To win, you need one of the following cases to be true:
     * - 8 tiles have length >= 4 and remaining six tiles have length >= 3 the last one can be of any length
     * - 5 tiles have length >= 5 and remaining nine tiles have length >= 3 the last one can be of any length
     * These are assuming we check for the win condition before discarding a tile
     * The given cases do not cover all the winning hands based on the original
     * game and for some rare cases it may be erroneous but it will be enough
     * for this simplified version
     */
    public boolean didGameFinish() {
        int chainOf4 = 0;
        int chainOf5 = 0;
        int chainsOf3 = 0;
        int[] finishedChains = players[currentPlayerIndex].calculateLongestChainPerTile();

        for(int i = 0; i < finishedChains.length; i++){
            if (finishedChains[i] >= 4){
                chainOf4 ++;
            }
            if (finishedChains[i] >= 5){
                chainOf5 ++;
            }
            if (finishedChains[i] == 3){
                chainsOf3 ++;
            }
        }
        if (chainOf4 == 8 && chainsOf3 == 6 || chainOf5 == 5 && chainsOf3 == 9){
            return true;
        }
        else {
            return false;
        } // this part is done
        
    }

    /*
     * TODO: Pick a tile for the current computer player using one of the following:
     * - picking from the tiles array using getTopTile()
     * - picking from the lastDiscardedTile using getLastDiscardedTile()
     * You may choose randomly or consider if the discarded tile is useful for
     * the current status. Print whether computer picks from tiles or discarded ones.
     */
    public void pickTileForComputer() {
        Tile t = getTopTile();
        players[currentPlayerIndex].addTile(t);
        System.out.println(getCurrentPlayerName() + " picks " + t.toString() + " from tiles.");
    }

    /*
     * TODO: Current computer player will discard the least useful tile.
     * For this use the findLongestChainOf method in Player class to calculate
     * the longest chain length per tile of this player,
     * then choose the tile with the lowest chain length and discard it
     * this method should print what tile is discarded since it should be
     * known by other players
     */
    public void discardTileForComputer() {

    }

    /*
     * TODO: discards the current player's tile at given index
     * this should set lastDiscardedTile variable and remove that tile from
     * that player's tiles
     */
    public void discardTile(int tileIndex) {
        this.lastDiscardedTile = players[currentPlayerIndex].getAndRemoveTile(tileIndex);
    }

    public void currentPlayerSortTilesColorFirst() {
        players[currentPlayerIndex].sortTilesColorFirst();
    }

    public void currentPlayerSortTilesValueFirst() {
        players[currentPlayerIndex].sortTilesValueFirst();
    }

    public void displayDiscardInformation() {
        if(lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }

    public void displayCurrentPlayersTiles() {
        players[currentPlayerIndex].displayTiles();
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

      public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    public void passTurnToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }

    public void setPlayerName(int index, String name) {
        if(index >= 0 && index <= 3) {
            players[index] = new Player(name);
        }
    }

}
