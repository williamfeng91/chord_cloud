package com.parse.starter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by williamfeng on 20/02/15.
 */
public class ScoreDisplayActivity extends FragmentActivity {

    private LinearLayout chordScrollView;
    private LinearLayout scoreView;
    private TextView titleView;
    private TextView subtitleView;
    private ChordDialogFragment dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);

        chordScrollView = (LinearLayout) findViewById(R.id.chordScrollView);
        scoreView = (LinearLayout) findViewById(R.id.scoreView);
        titleView = (TextView) findViewById(R.id.scoreTitle);
        subtitleView = (TextView) findViewById(R.id.scoreSubtitle);

        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (getString(R.string.search).equals(item.getTitle())) {
            return onSearchRequested();
        } else {
            return false;
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String score = intent.getStringExtra("score");
        String title, subtitle;
        Scanner scanner = new Scanner(score);

        // Extract title
        if (!scanner.hasNextLine()) {
            return;
        }
        Pattern pattern = Pattern.compile("\\{title:(.*)\\}");
        Matcher matcher = pattern.matcher(scanner.nextLine());
        if (!matcher.find()) {
            return;
        }
        title = matcher.group(1);
        titleView.setText(title);

        // Extract other content
        Pattern pattern_subtitle = Pattern.compile("\\{subtitle:(.*)\\}");
        Pattern pattern_comment = Pattern.compile("\\{comment:(.*)\\}");
        Pattern pattern_empty_line = Pattern.compile("^\\s*$");
        pattern = Pattern.compile("\\[([\\w/#-]+)\\]");
        Matcher matcher_subtitle, matcher_comment, matcher_empty_line;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            matcher_subtitle = pattern_subtitle.matcher(line);
            matcher_comment = pattern_comment.matcher(line);
            matcher_empty_line = pattern_empty_line.matcher(line);
            if (matcher_subtitle.find()) {
                subtitle = matcher_subtitle.group(1);
                subtitleView.setText(subtitle + "\n");
            } else if (matcher_comment.find()) {
                String comment = matcher_comment.group(1);
                TextView commentView = new TextView(this);
                commentView.setText(comment);
                scoreView.addView(commentView);
            } else if (matcher_empty_line.find()) {
                TextView emptyView = new TextView(this);
                emptyView.setText("");
                scoreView.addView(emptyView);
            } else {
                FlowLayout lineView = new FlowLayout(this);
                FlowLayout.LayoutParams flowLP = new FlowLayout.LayoutParams(2, 2);
                scoreView.addView(lineView, params);
                LinearLayout segmentView;
                TextView chordTextView;
                TextView lyricsTextView = new TextView(this);
                String[] tokens = line.split("(?=\\[[\\w/#-]{1,10}\\])|(?<=\\[[\\w/#-]{1,10}\\])");
                boolean retrievedChord = false;
                for (String token : tokens) {
                    if (token.length() <= 0) {
                        continue;
                    }
                    matcher = pattern.matcher(token);
                    if (matcher.find()) {
                        retrievedChord = true;
                        segmentView = new LinearLayout(this);
                        segmentView.setOrientation(LinearLayout.VERTICAL);
                        segmentView.setPadding(20,0,0,0);
                        lineView.addView(segmentView, flowLP);
                        chordTextView = new TextView(this);
                        segmentView.addView(chordTextView);
                        lyricsTextView = new TextView(this);
                        segmentView.addView(lyricsTextView);
                        final String chord = matcher.group(1);
                        chordTextView.setText(chord);
                        chordTextView.setTextColor(Color.parseColor("#748A9D"));
                        chordTextView.setClickable(true);
                        chordTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog = new ChordDialogFragment();
                                dialog.setChord(chord);
                                dialog.show(getSupportFragmentManager(), "");
                            }
                        });
                    } else {
                        if (false == retrievedChord) {
                            segmentView = new LinearLayout(this);
                            segmentView.setOrientation(LinearLayout.VERTICAL);
                            segmentView.setPadding(20, 0, 0, 0);
                            lineView.addView(segmentView, flowLP);
                            chordTextView = new TextView(this);
                            segmentView.addView(chordTextView);
                            chordTextView.setText("");
                            lyricsTextView = new TextView(this);
                            segmentView.addView(lyricsTextView);
                        }
                        lyricsTextView.setText(token);
                        retrievedChord = false;
                    }
                }
            }
        }
        scanner.close();
    }

    public void addChordImg(int imgID) {

        final ImageView chordView = new ImageView(this);
        chordView.setImageResource(imgID);
        chordView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
        chordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chordScrollView.removeView(chordView);
            }
        });
        chordScrollView.addView(chordView);
    }
}
