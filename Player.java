public class Player {
    String playerName;
    Tile[] playerTiles;
    int numberOfTiles;

    public Player(String name) {
        setName(name);
        playerTiles = new Tile[15]; // there are at most 15 tiles a player owns at any time
        numberOfTiles = 0; // currently this player owns 0 tiles, will pick tiles at the beginning of the game
    }

    /*
     * This method calculates the longest chain per tile to be used when checking the win condition
     */
    public int[] calculateLongestChainPerTile() {
        // keep a separate copy of the tiles since findLongestChainOf sorts them
        Tile[] tilesCopy = new Tile[numberOfTiles];
        for (int i = 0; i < numberOfTiles; i++) {
            tilesCopy[i] = playerTiles[i];
        }

        // make the calculations
        int[] chainLengths = new int[numberOfTiles];
        for (int i = 0; i < numberOfTiles; i++) {
            chainLengths[i] = findLongestChainOf(tilesCopy[i]);
        }

        // revert the playerTiles to its original form
        for (int i = 0; i < numberOfTiles; i++) {
            playerTiles[i] = tilesCopy[i];
        }

        return chainLengths;
    }

    /*
     * TODO: finds and returns the longest chain of tiles that can be formed
     * using the given tile. a chain of tiles is either consecutive numbers
     * that have the same color or the same number with different colors
     * some chain examples are as follows:
     * 1B 2B 3B
     * 5Y 5B 5R 5K
     * 4Y 5Y 6Y 7Y 8Y
     * You can use canFormChainWith method in Tile class to check if two tiles can make a chain
     * based on color order and value order. Use sortTilesColorFirst() and sortTilesValueFirst()
     * methods to sort the tiles of this player then find the position of the given tile t.
     * check how many adjacent tiles there are starting from the tile position.
     * Note that if you start a chain with matching colors it should continue with the same type of match
     * and if you start a chain with matching values it should continue with the same type of match
     * use the different values canFormChainWith method returns.
     */
    public int findLongestChainOf(Tile t) {
        int tilePosition;

        // Color order
        sortTilesColorFirst();
        tilePosition = findPositionOfTile(t);
        int longestChainColorFirst = findLongestChainFromPosition(tilePosition, playerTiles);

        // Value order
        sortTilesValueFirst();
        tilePosition = findPositionOfTile(t);
        int longestChainValueFirst = findLongestChainFromPosition(tilePosition, playerTiles);

        return Math.max(longestChainColorFirst, longestChainValueFirst);
    }

    // Helper method to find the longest chain starting from a given position
    private int findLongestChainFromPosition(int position, Tile[] tiles) {
        int longestChain = 1;  // A single tile is a chain of length 1
        int currentChain = 1;

        // Check left
        for (int i = position - 1; i >= 0; i--) {
            if (tiles[i].canFormChainWith(tiles[i + 1]) > 0) {
                currentChain++;
                longestChain = Math.max(longestChain, currentChain);
            } else {
                break;
            }
        }

        // Check right
        currentChain = 1;  // Reset for the right side
        for (int i = position + 1; i < tiles.length; i++) {
            if (tiles[i].canFormChainWith(tiles[i - 1]) > 0) {
                currentChain++;
                longestChain = Math.max(longestChain, currentChain);
            } else {
                break;
            }
        }

        return longestChain;
    }

    /*
     * TODO: removes and returns the tile in given index
     */
    public Tile getAndRemoveTile(int index) {
        Tile removedTile = playerTiles[index];
        playerTiles[index] = null;
        for (int i = index; i < playerTiles.length - 1; i++) {
            playerTiles[i] = playerTiles[i + 1];
        }
        playerTiles[14] = null;
        numberOfTiles = numberOfTiles - 1;

        return removedTile;
    }

    /*
     * TODO: adds the given tile at the end of playerTiles array, should also
     * update numberOfTiles accordingly. Make sure the player does not try to
     * have more than 15 tiles at a time
     */
    public void addTile(Tile t) {
        if (numberOfTiles < 15) {
            playerTiles[numberOfTiles] = t;
            numberOfTiles++;
        }
    }

    /*
     * TODO: uses bubble sort to sort playerTiles in increasing color and value
     * value order: 1 < 2 < ... < 12 < 13
     * color order: Y < B < R < K
     * color is more important in this ordering, a sorted example:
     * 3Y 3Y 6Y 7Y 1B 2B 3B 3B 10R 11R 12R 2K 4K 5K
     * you can use compareToColorFirst method in Tile class for comparing
     * you are allowed to use Collections.sort method
     */
    public void sortTilesColorFirst() {
        bubbleSortValue();
        bubbleSortColor();
    }

    /*
     * TODO: uses bubble sort to sort playerTiles in increasing value and color
     * value order: 1 < 2 < ... < 12 < 13
     * color order: Y < B < R < K
     * value is more important in this ordering, a sorted example:
     * 1B 2B 2K 3Y 3Y 3B 3B 4K 5K 6Y 7Y 10R 11R 12R
     * you can use compareToValueFirst method in Tile class for comparing
     * you are allowed to use Collections.sort method
     */
    public void sortTilesValueFirst() {
        bubbleSortValue();
        bubbleSortColor();
    }

    private void bubbleSortValue() {
        int n = numberOfTiles;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (playerTiles[j - 1].getValue() > playerTiles[j].getValue()) {
                    // Swap
                    Tile temp = playerTiles[j - 1];
                    playerTiles[j - 1] = playerTiles[j];
                    playerTiles[j] = temp;
                }
            }
        }
    }

    private void bubbleSortColor() {
        int n = numberOfTiles;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (playerTiles[j - 1].compareToColorFirst(playerTiles[j]) > 0) {
                    // Swap
                    Tile temp = playerTiles[j - 1];
                    playerTiles[j - 1] = playerTiles[j];
                    playerTiles[j] = temp;
                }
            }
        }
    }

    public int findPositionOfTile(Tile t) {
        int tilePosition = -1;
        for (int i = 0; i < numberOfTiles; i++) {
            if (playerTiles[i].matchingTiles(t)) {
                tilePosition = i;
                break; // Exit loop early once the tile is found
            }
        }
        return tilePosition;
    }

    public void displayTiles() {
        System.out.println(playerName + "'s Tiles:");
        for (int i = 0; i < numberOfTiles; i++) {
            System.out.print(playerTiles[i].toString() + " ");
        }
        System.out.println();
    }

    public Tile[] getTiles() {
        return playerTiles;
    }

    public void setName(String name) {
        playerName = name;
    }

    public String getName() {
        return playerName;
    }
}
