#! /usr/bin/perl -w

$prefix = "ja.chordwiki.org";
$min = 1;
$max = 355;

foreach $list ($min .. $max) {
	$url = "ja.chordwiki.org/list/$list.html";
	$start_links = 0;

	open G, ">>links" or die;
	open F, "wget -q -O- '$url'|" or die;
	while ($line = <F>) {
		if (!$start_links) {
			next if $line !~ /<ul class=\"list note\">/;
			$start_links = 1;
		} else {
			last if $line =~ /<\/ul>/;
		}
		if ($line =~ /<li><a href=\"(.*)\">/) {
                	print G $prefix, $1, "\n";
        	}
	}
	close F;
	close G;
}
