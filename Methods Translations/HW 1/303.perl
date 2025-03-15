use strict;
use Data::Dumper qw(Dumper);
my $o = "";
while (my $a = <STDIN>){
$a =~ s/(.*)(\n)/$1/;
$o = $o . $a;
}
my @num = ();
my @kek = split /</, $o;
foreach my $k(@kek){
if ($k =~ /.*a.*href=("|')((.*?):\/\/)?(.*?@)?(\w.*?)("|'|\/|:).*?>(.*)/) {
$k =~ s/.*a.*href=("|')((.*?):\/\/)?(.*?@)?(\w.*?)("|'|\/|:).*?>(.*)/$5/;
push(@num, $k);
}
}
@num = sort @num;
my @unique;
my %seen;
foreach my $arg (@num) {
  if (! $seen{$arg}) {
    push (@unique, $arg);
    $seen{$arg} = 1;
  }
}
foreach my $arg (@unique){
if ($arg =~ /(\w.*)/){
print "$arg\n";}
}