package com.parse.starter;

/**
 * Created by williamfeng on 24/02/15.
 */
public class ChordPosition implements Comparable<ChordPosition> {
    private int fret;
    private int string;

    public ChordPosition() { this(-1, -1); }
    public ChordPosition(int fret, int string) {
        this.fret = fret;
        this.string = string;
    }

    public int getFret() { return fret; }
    public int getString() { return string; }
    public void setFret(int fret) { this.fret = fret; }
    public void setString(int string) { this.string = string; }
    public int compareTo(ChordPosition cp) {
        int result = Integer.valueOf(fret).compareTo(cp.fret);
        if (result == 0) {
            return Integer.valueOf(string).compareTo(cp.string);
        } else {
            return result;
        }
    }
    public static ChordPosition[] capoAt(ChordPosition[] positions, int capoFret) {
        for (int i = 0; i < positions.length; i++) {
            if (positions[i].getFret() != -1 && positions[i].getFret() <= capoFret) {
                return positions;
            }
        }
        for (int i = 0; i < positions.length; i++) {
            if (positions[i].getFret() != -1) {
                positions[i].setFret(positions[i].getFret() - capoFret);
            }
        }
        return positions;
    }
    public String toString() { return "" + fret + "(" + string + ")"; }
}
