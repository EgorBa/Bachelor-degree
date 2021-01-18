$c = "";
$flag = 0;
$empty = 1;
while ($a = <>) {
$a =~ s/ +/ /g ;
$a =~ s/^( )(.*)/$2/g;
$a =~ s/(.*)( )$/$1/g;
#print $a;
$a =~ s/^\s$//g;
$a =~ s/\n//g;
#print length($a);
#print "\"$a\"";
if (length($a) != 0 || $flag){
$flag = 1;
if ($empty || length($a) != 0){
$c = $c . $a . "\n";
if (length($a) == 0){$empty = 0;
} else{
$empty =1;}
}
}
}
$c =~ s/\n$//g;
print "$c";