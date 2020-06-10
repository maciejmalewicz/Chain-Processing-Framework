package executionChains.misc;

public class IdManager {
    private static int current = 0;

    public static int generate(){
        return current++;
    }
}
