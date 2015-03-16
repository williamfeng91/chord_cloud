#! /usr/bin/perl -w

open MAP, ">>map" or die;
for $root (qw(C# D D# E F F# G G# A A# B)) {
	for $chord("", qw(m dim + sus2 sus4 Mb5 m#5 mbb5 sus4#5 sus2b5 sus2#5 7 m7 M7 mM7 dim7 +7 +M7 7b5 M7b5 m7b5 mM7b5 mM7bb5 m7#5 mM7#5 7b9 6 m6 6b5 6b9 6add9 m6add9 9 m9 M9 mM9 9b5 +9 9sus4 7#9 7#9b5 +M9 11 m11 M11 mM11 M#11 13 m13 M13 mM13 7sus2 M7sus2 7sus4 M7sus4 7sus2#5 7sus4#5 M7sus4#5 sus2sus4 7sus2sus4 M7sus2sus4 5 add9)) {
		for $bass (qw(C C# D D# E F F# G G# A A# B)) {
			if ($root eq $bass) {
				$chordname = "$root$chord";
			} else {
				$chordname = "${root}${chord}/${bass}";
			}
			print "$chordname\n";
			$chordUrlName = $chordname;
			$chordUrlName =~ s/#/%23/g;
			$chordUrlName =~ s/\+/%2B/g;
			$chordUrlName =~ s/\//%2F/g;
			$chordResName = $chordname;
			$chordResName =~ s/#/_sharp/g;
			$chordResName =~ s/\+/aug/g;
			$chordResName =~ s/\//_on_/g;
			print MAP "    <string-array name=\"$chordResName\">\n";
			$url = "http://jguitar.com/chordsearch?chordsearch=${chordUrlName}&labels=finger";
			open F, "wget -q -O- '$url'|" or die;
			while ($line = <F>) {
				next if $line !~ /images.*-(\w+%2C\w+%2C\w+%2C\w+%2C\w+%2C\w+).*png/;
				$position = $1;
				$position =~ s/%2C/,/ig;
				print MAP "        <item>$position</item>\n";
			}
			close F;
			print MAP "    </string-array>\n";
		}
	}
}
close MAP;
