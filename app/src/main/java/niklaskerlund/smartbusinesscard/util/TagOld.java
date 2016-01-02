package niklaskerlund.smartbusinesscard.util;

/**
 * Created by Niklas on 2015-12-21.
 */
public class TagOld implements Comparable<TagOld> {

    private String name;
    private int resourceID; //If icons.

    public TagOld(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(TagOld t) {
        return name.compareTo(t.name);
    }

    public String toString() {
        return name;
    }
}
