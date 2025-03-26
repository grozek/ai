//package utils;

/*
 * Utils
 * Mangae rotation of the map
 */
public class Utils {

    /*
     * rotateView
     * Rotates the view to the correct, neutral direction
     */
    public static char[][] rotateView(char[][] view, int direction) {
        int numberOfRotations = direction;
        for (int i = 0; i < numberOfRotations; i++) {
            view = rotateViewRight(view);
        }
        return view;
    }

    /*
     * rotateViewRight
     * rotates the 5x5 view
     */
    private static char[][] rotateViewRight(char[][] view) {
        int size = 5;

        char[][] output = new char[5][5];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                output[j][size - 1 - i] = view[i][j];
            }
        }
        return output;
    }
}
