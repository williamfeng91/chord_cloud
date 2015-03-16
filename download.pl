#! /usr/bin/perl -w

use LWP::Simple;

$prefix = "http://jguitar.com";
$png_path = "Chords/";
$m4a_path = "Audios/";
$chordname = "";
$i = 0;

open MAP, ">>map" or die;
for $chord (qw/C%23 C%23m C%23m7 C%23M7 C%23M7b5 C%23m7b5 C%23m7%235 C%23M7sus2 C%23M7sus4 C%23M7sus4%235 C%23M7sus2sus4 C%23mM7 C%23mM7b5 C%23mM7bb5 C%23mM7%235 C%23mM9 C%23mM11 C%23mM13 C%23m6 C%23m6add9 C%23m9 C%23M9 C%23m11 C%23M11 C%23m13 C%23M13 C%23dim C%23dim7 C%23%2B C%23%2B7 C%23%2BM7 C%23%2B9 C%23%2BM9 C%23sus2 C%23sus4 C%23sus4%235 C%23sus2b5 C%23sus2%235 C%23sus2sus4 C%237 C%237b5 C%237b9 C%237%239 C%237%239b5 C%237sus2 C%237sus4 C%237sus2%235 C%237sus4%235 C%237sus2sus4 C%23%2FF C%23%2FG%23 C%23m%2FE C%23m%2FG%23 C%23m7%2FE C%23m7%2FG%23 C%23m7%2FB C%23M7%2FF C%23M7%2FG%23 C%23M7%2FC C%23M7b5%2FF C%23M7b5%2FG C%23M7b5%2FC C%23m7b5%2FE C%23m7b5%2FG C%23m7b5%2FB C%23m7%235%2FE C%23m7%235%2FA C%23m7%235%2FB C%23M7sus2%2FD%23 C%23M7sus2%2FG%23 C%23M7sus2%2FC C%23M7sus4%2FF%23 C%23M7sus4%2FG%23 C%23M7sus4%2FC C%23M7sus4%235%2FF%23 C%23M7sus4%235%2FA C%23M7sus4%235%2FC C%23M7sus2sus4%2FD%23 C%23M7sus2sus4%2FF%23 C%23M7sus2sus4%2FG%23 C%23M7sus2sus4%2FC C%23mM7%2FE C%23mM7%2FG%23 C%23mM7%2FC C%23mM7b5%2FE C%23mM7b5%2FG C%23mM7b5%2FC C%23mM7bb5%2FE C%23mM7bb5%2FF%23 C%23mM7bb5%2FC C%23mM7%235%2FE C%23mM7%235%2FA C%23mM7%235%2FC C%23mM9%2FE C%23mM9%2FG%23 C%23mM9%2FC C%23mM11%2FE C%23mM11%2FG%23 C%23mM11%2FC C%23mM13%2FE C%23mM13%2FG%23 C%23mM13%2FC C%23m6%2FE C%23m6%2FG%23 C%23m6%2FA%23 C%23m6add9%2FE C%23m6add9%2FG%23 C%23m6add9%2FA%23 C%23m9%2FE C%23m9%2FG%23 C%23m9%2FB C%23M9%2FF C%23M9%2FG%23 C%23M9%2FC C%23m11%2FE C%23m11%2FG%23 C%23m11%2FB C%23M11%2FF C%23M11%2FG%23 C%23M11%2FC C%23m13%2FE C%23m13%2FG%23 C%23m13%2FB C%23M13%2FF C%23M13%2FG%23 C%23M13%2FC C%23dim%2FE C%23dim%2FG C%23dim7%2FE C%23dim7%2FG C%23dim7%2FA%23 C%23%2B%2FF C%23%2B%2FA C%23%2B7%2FF C%23%2B7%2FA C%23%2B7%2FB C%23%2BM7%2FF C%23%2BM7%2FA C%23%2BM7%2FC C%23%2B9%2FF C%23%2B9%2FA C%23%2B9%2FB C%23sus2%2FD%23 C%23sus2%2FG%23 C%23sus4%2FF%23 C%23sus4%2FG%23 C%23sus4%235%2FF%23 C%23sus4%235%2FA C%23sus2b5%2FD%23 C%23sus2b5%2FG C%23sus2%235%2FD%23 C%23sus2%235%2FA C%23sus2sus4%2FD%23 C%23sus2sus4%2FF%23 C%23sus2sus4%2FG%23 C%237%2FF C%237%2FG%23 C%237%2FB C%237b5%2FF C%237b5%2FG C%237b5%2FB C%237b9%2FF C%237b9%2FG%23 C%237b9%2FB C%237%239%2FF C%237%239%2FG%23 C%237%239%2FB C%237%239b5%2FF C%237%239b5%2FG C%237%239b5%2FB C%237sus2%2FD%23 C%237sus2%2FG%23 C%237sus2%2FB C%237sus4%2FF%23 C%237sus4%2FG%23 C%237sus4%2FB C%237sus2%235%2FD%23 C%237sus2%235%2FA C%237sus2%235%2FB C%237sus4%235%2FF%23 C%237sus4%235%2FA C%237sus4%235%2FB C%237sus2sus4%2FD%23 C%237sus2sus4%2FF%23 C%237sus2sus4%2FG%23 C%237sus2sus4%2FB/) {
	$n = $i - 1;
	print MAP "map.put($chordname, $n);\n";
	$i = 1;
	$j = 1;
	$k = 1;
	$url = "http://jguitar.com/chordsearch?chordsearch=${chord}&labels=finger";
	open F, "wget -q -O- '$url'|" or die;
	while ($line = <F>) {
		next if $line !~ /chordshape/;
		if ($line =~ /\/images.*(\.png)/) {
			$link = $prefix.$&;
			$type = $1;
			$filename = "${png_path}${chord}_${i}${type}";
			$i++;
		} elsif ($line =~ /\/audio.*-8(\.m4a)/) {
			$link = $prefix.$&;
			$type = $1;
			$filename = "${m4a_path}${chord}_${j}_arpeggiated${type}";
			$j++;
		} elsif ($line =~ /\/audio.*(\.m4a)/) {
			$link = $prefix.$&;
			$type = $1;
			$filename = "${m4a_path}${chord}_${k}_strummed${type}";
			$k++;
		}
		if ($filename =~ /png/) {
			$filename =~ s/M/maj/;
			$filename =~ s/%23/_sharp/g;
			$filename =~ s/%2B/aug/;
			$filename =~ s/%2F/_on_/;
			$filename = lc($filename);
			$chordname = $filename;
			$chordname =~ s/^chords\///;
			$chordname =~ s/_\d\.png$//;
		}
		next if -e $filename;
		$status = getstore($link, $filename);
		if (is_success($status)) {
  		#	print "file downloaded correctly\n";
		} else {
			print "$filename not downloaded\n";
  		#	print "error downloading file: $status\n";
		}
	}
	close F;
}
$n = $i - 1;
print MAP "map.put($chordname, $n);\n";
close MAP;
