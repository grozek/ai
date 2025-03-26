/*
 * MapEdges
 * used for printing
 */

public class MapEdges {
    int top;
    int bottom;
    int left;
    int right;

    /*
     * Finds the edges of each side of the map so that it can be printed
     */
    public MapEdges(int size, char[][] fullMap) {
        top = findTop(size, fullMap);
        bottom = findBottom(size, fullMap);
        left = findLeft(size, fullMap);
        right = findRight(size, fullMap);
    }

    private static int findLeft(int size, char[][] fullMap) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (fullMap[j][i] != '#') {
                    return i - 1;
                }
            }
        }
        return 0;
    }

    private static int findRight(int size, char[][] fullMap) {
        for (int j = size - 1; j >= 0; j--) {
            for (int i = 0; i < size; i++) {
                if (fullMap[i][j] != '#') {
                    return j + 1;
                }
            }
        }
        return size;
    }

    private static int findTop(int size, char[][] fullMap) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (fullMap[i][j] != '#') {
                    return i - 1;
                }
            }
        }
        return 0;
    }

    private static int findBottom(int size, char[][] fullMap) {
        for (int i = size - 1; i >= 0; i--) {
            for (int j = 0; j < size; j++) {
                if (fullMap[i][j] != '#') {
                    return i + 1;
                }
            }
        }
        return size;
    }
}
