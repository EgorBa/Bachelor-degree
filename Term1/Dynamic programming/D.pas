type
  long = array [0..30] of integer;
  fig = array [0..9] of long;
 
var
  a, b: fig;
  i, n,e: integer;
  st:string;
 
function max(a, b: integer): integer;
begin
  if a > b then max := a else max := b; 
end;
 
procedure addlong(var a, b: long);
var
  i, z, cy: integer;
begin
  z := max(a[0], b[0]);
  cy := 0;
  for i := 1 to z + 1 do 
  begin
    inc(cy, a[i] + b[i]); 
    if cy > 9999 then begin a[i] := cy - 10000;cy := 1; end 
    else begin
      a[i] := cy;cy := 0; 
    end; 
  end; 
  if a[z + 1] > 0 then a[0] := z + 1 
  else a[0] := z;
end;
 
procedure xwrite(a: integer);
var
  s: string;
begin
  str(a, s); 
  while length(s) < 4 do 
    s := '0' + s;
  //write(s);
  st:=st+s;
end;
 
procedure writelong(var a: long);
var
  i: integer;
begin
  st:=st+a[a[0]];
  //write(a[a[0]]);
  for i := a[0] - 1 downto 1 do 
    xwrite(a[i]); 
end;
 
begin
  readln(n); 
  for i := 0 to 9 do 
  begin
    a[i][0] := 1; 
    a[i][1] := 1;
  end; 
  a[0][1] := 0;
  a[8][1] := 0;
  for i := 2 to n do   
  begin                 
    b[0] := a[4];
    addlong(b[0], a[6]);    
    b[1] := a[6]; 
    addlong(b[1], a[8]); 
    b[2] := a[7]; 
    addlong(b[2], a[9]); 
    b[3] := a[4];  
    addlong(b[3], a[8]); 
    b[4] := a[0]; 
    addlong(b[4], a[3]);
    addlong(b[4], a[9]);
    b[5][0] := 1; 
    b[5][1] := 0;
    b[6] := a[0]; 
    addlong(b[6], a[1]);
    addlong(b[6], a[7]);
    b[7] := a[2];  
    addlong(b[7], a[6]); 
    b[8] := a[1];
    addlong(b[8], a[3]);
    b[9] := a[2]; 
    addlong(b[9], a[4]);
    a := b;
  end;
  for i := 1 to 9 do
    addlong(a[0], a[i]);
  writelong(a[0]);
  st:=copy(st,length(st)-8,length(st));
  for i:=1 to length(st) do
  if (st[i]='0') and (e=0) then 
  else begin write(st[i]);e:=e+1; end;
end.