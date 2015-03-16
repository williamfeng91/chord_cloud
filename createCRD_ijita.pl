#! /usr/bin/perl -w

while ($url = <>) {
	chomp $url;
	$start_score = 0;
	if ($url =~ /\/([^\/]+)$/) {
		$filename = $1 . ".crd";
	}

	open F, "wget -q -O- '$url'|" or die;
	open G, ">crds/$filename" or die;
	while ($line = <F>) {
		if (!$start_score) {
			next if $line !~ /<div class=\"main\">/;
			$start_score = 1;
		} else {
			last if $line =~ /<div id=\"key\">/;
		}
		if ($line =~ /title_begin -->(.*)<!--/) {
			$line = $1;
                	$line =~ s/<[^>]*>//g;
                	$line =~ s/&[^;]*;/  /g;
			print G "{title:$line}\n";
			next;
		} elsif ($line =~ /body_begin -->(.*)<!--/) {
                        $line = $1;
                        $line =~ s/<[^>]*>//g;
                        $line =~ s/&[^;]*;/  /g;
			print G "{subtitle:$line}\n";
			next;
		} elsif ($line =~ /line comment\"><strong>(.*)<\/strong>/) {
                        $line = $1;
                        $line =~ s/<[^>]*>//g;
                        $line =~ s/&[^;]*;/  /g;
			print G "{comment:$line}\n";
			next;
		} elsif ($line =~ /<br/) {
			print G "\n";
			next;
		} elsif ($line !~ /span class/) {
			next;
		} else {
			$line =~ s/<span class=\"chord[^>]*>([^<]*<\/span>)/[$1]/g;
			$line =~ s/<[^>]*>//g;
			$line =~ s/&[^;]*;/  /g;
			print G $line;
		}
	}
	close F;
	close G;
}
