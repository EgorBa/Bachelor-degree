#include <iostream>
#include <vector>
#include <string>
#include <fstream>

using namespace std;

int main()
{
    ifstream in("jurassic.in");
    int n;
    in >> n;
    vector<int> list(n);
    for (int i = 0; i < n; ++i)
    {
        string str;
        in >> str;
        list[i] = 0;
        for (char j : str)
        {
            list[i] += (1 << (j - 'A'));
        }
    }
    int ans = -1;
    for (int i = 0; i < (1 << n); ++i)
    {
        int count = 0;
        int mask = 0;
        for (int j = 1; j < (1 << n); j = (1 << count))
        {
            if ((j & i) != 0)
            {
                mask = mask ^ list[count];
            }
            count++;
        }
        if (mask == 0 && i > ans)
        {
            ans = i;
        }
    }
    in.close();
    ofstream out("jurassic.out");
    int count = 0;
    int y = 0;
    for (int j = 1; j < (1 << n); j = (1 << count))
    {
        if ((j & ans) != 0)
        {
            y++;
        }
        count++;
    }
    count = 0;
    out << y << " " << '\n';
    for (int j = 1; j < (1 << n); j = (1 << count))
    {
        if ((j & ans) != 0)
        {
            out << (count + 1) << " ";
        }
        count++;
    }
    out.close();
}
