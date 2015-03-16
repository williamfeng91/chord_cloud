package com.parse.starter;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by williamfeng on 22/02/15.
 */
public class ChordDialogFragment extends DialogFragment {

    private String chord;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chord_popup, container);

        ViewPager chordViewPager = (ViewPager) view.findViewById(R.id.chordViewPager);
        List fragments = getFragments(chord);
        ChordImgFragmentAdapter adapter = new ChordImgFragmentAdapter(getChildFragmentManager(), fragments);
        chordViewPager.setAdapter(adapter);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }

    private List getFragments(String chord){
        chord = parseChord(chord);
        String packageName = BuildConfig.class.getPackage().getName();
        int chordID = getResources().getIdentifier(chord, "array", packageName);
        if (0 == chordID) {
            return null;
        }
        String[] chordPositions = getResources().getStringArray(chordID);

        List fList = new ArrayList();
        for (int i = 0; i < chordPositions.length; i++) {
            fList.add(ChordImgFragment.newInstance(chord, chordPositions[i]));
        }
        return fList;
    }

    public void setChord(String chord) { this.chord = chord; }

    private String parseChord(String chord) {
        return chord.replaceAll("\\+", "aug")
                    .replaceAll("/", "_on_")
                    .replaceAll("-", "b")
                    .replaceAll("#", "_sharp");
    }
}
