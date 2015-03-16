package com.parse.starter;

import android.content.res.Resources;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by williamfeng on 22/02/15.
 */
public class ChordImgFragment extends Fragment {

    private static final int MUTED = -1;
    private static final int ORIGIN_X = 30;
    private static final int ORIGIN_Y = 3;
    private static final int FRET_SPACING = 50;
    private static final int STRING_SPACING = 50;

    private String packageName = BuildConfig.class.getPackage().getName();

    private String chordName;
    private String chordPosition;

    public static final ChordImgFragment newInstance(String chordName, String chordPosition) {

        ChordImgFragment f = new ChordImgFragment();
        f.chordName = chordName;
        f.chordPosition = chordPosition;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chord_viewpager, container, false);

        TextView title = (TextView) view.findViewById(R.id.chordTitle);
        title.setText(chordName);
        LayeredImageView chordImg = (LayeredImageView) view.findViewById(R.id.chordImg);
        createChordImg(view, chordImg, chordPosition);

        return view;
    }

    private void createChordImg(View view, LayeredImageView v, String chordPosition) {

        String[] positionArray = chordPosition.split(",");
        int lowest_fret = getMinFret(positionArray);
        int highest_fret = getMaxFret(positionArray);
        int fingers_needed = 0;
        ChordPosition[] chordPositionArray = new ChordPosition[positionArray.length];

        for (int i = 0; i < positionArray.length; i++) {
            chordPositionArray[i] = new ChordPosition();
            if (positionArray[i].matches("\\d+")) {
                int f = Integer.parseInt(positionArray[i]);
                if (f > 0) {
                    fingers_needed++;
                }
                chordPositionArray[i].setFret(f);
            } else {
                chordPositionArray[i].setFret(MUTED);
            }
            chordPositionArray[i].setString(i);
        }
        Arrays.sort(chordPositionArray);
        if (lowest_fret >= 3) {
            chordPositionArray = ChordPosition.capoAt(chordPositionArray, lowest_fret - 1);
        }

        Resources res = v.getResources();
        v.setImageResource(R.drawable.fretboard);
        Matrix m;

        int lastFret = MUTED;
        int finger;
        int lastFinger = 0;
        boolean isBarre = false;
        for (int i = 0, j = 0; i < chordPositionArray.length; i++) {
            int f = chordPositionArray[i].getFret();
            int s = chordPositionArray[i].getString();
            if (f == MUTED) {
                m = new Matrix();
                m.preTranslate(ORIGIN_X + STRING_SPACING * s,
                               ORIGIN_Y); // pixels to offset
                v.addLayer(res.getDrawable(R.drawable.cross), m);
            } else if (f == 0) {
                m = new Matrix();
                m.preTranslate(ORIGIN_X + STRING_SPACING * s,
                               ORIGIN_Y); // pixels to offset
                v.addLayer(res.getDrawable(R.drawable.circle), m);
            } else {
                if (f <= lastFinger) {
                    finger = lastFinger;
                } else {
                    finger = f;
                }
                if (lastFinger == 0) {
                    finger = 1;
                } else if (f == lastFret && isBarre) {
                    finger = lastFinger;
                } else if (f == lastFret && fingers_needed - j > 4 - lastFinger) {
                    finger = lastFinger;
                } else if (f == lastFret && fingers_needed - j <= 4 - lastFinger) {
                    finger = lastFinger + 1;
                } else if (f == lastFret + 1) {
                    finger = lastFinger + 1;
                } else if (f != lastFret && fingers_needed - j == 4 - lastFinger) {
                    finger = lastFinger + 1;
                } else if (f == lastFret + 2) {
                    finger = lastFinger + 2;
                }
                if (finger < 1) {
                    finger = 1;
                } else if (finger > 4) {
                    finger = 4;
                }
                int fingerID = res.getIdentifier("finger"+finger, "drawable", packageName);
                if (0 != fingerID) {
                    m = new Matrix();
                    m.preTranslate(ORIGIN_X + STRING_SPACING * s,
                                   ORIGIN_Y + FRET_SPACING * f); // pixels to offset
                    v.addLayer(res.getDrawable(fingerID), m);
                }
                j++;
                lastFret = f;
                if (finger == lastFinger) {
                    isBarre = true;
                } else {
                    isBarre = false;
                }
                lastFinger = finger;
            }
        }
        if (lowest_fret < 3) {
            m = new Matrix();
            m.preTranslate(ORIGIN_X + 15, ORIGIN_Y + 38); // pixels to offset
            v.addLayer(res.getDrawable(R.drawable.blackbar), m);
        }
        if (lowest_fret != 0) {
            TextView fretText = (TextView) view.findViewById(R.id.fret);

            RelativeLayout.LayoutParams position = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            position.leftMargin = 525;
            position.topMargin = 80;

            fretText.setLayoutParams(position);
            fretText.setText("Fret " + lowest_fret);
        }
    }

    private int getMinFret(String[] position) {
        int min = 99;
        for (int i = 0; i < position.length; i++) {
            if (position[i].matches("\\d+")) {
                int n = Integer.parseInt(position[i]);
                if (n < min) {
                    min = n;
                }
            }
        }
        return min;
    }

    private int getMaxFret(String[] position) {
        int max = 0;
        for (int i = 0; i < position.length; i++) {
            if (position[i].matches("\\d+")) {
                int n = Integer.parseInt(position[i]);
                if (n > max) {
                    max = n;
                }
            }
        }
        return max;
    }
}
