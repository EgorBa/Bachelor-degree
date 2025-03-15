#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

int MOD = 123456;

int main()
{
    vector <vector<int>> a;
    vector<char*> ans;
    a.assign(static_cast<const unsigned int>(MOD), vector<int>(0));
    ifstream in("set.in");
    ofstream out("set.out");
    int m;
    char chr;
    while (in >> chr >> chr >> chr >> chr >> chr >> chr >> m)
    {
        if ('t' == chr)
        {
            bool flag = false;
            for (int i = 0; i < a[abs(m) % MOD].size(); ++i)
            {
                if (m == a[abs(m) % MOD][i])
                {
                    flag = true;
                    break;
                }
            }
            if (!flag)
            {
                a[abs(m) % MOD].push_back(m);
            }
        } else
        {
            if ('e' == chr)
            {
                for (int i = 0; i < a[abs(m) % MOD].size(); ++i)
                {
                    if (m == a[abs(m) % MOD][i])
                    {
                        for (int j = i; j < a[abs(m) % MOD].size() - 1; ++j)
                        {
                            a[abs(m) % MOD][j] = a[abs(m) % MOD][j + 1];
                        }
                        a[abs(m) % MOD].pop_back();
                        break;
                    }
                }
            } else
            {
                bool flag = false;
                for (int i = 0; i < a[abs(m) % MOD].size(); ++i)
                {
                    if (m == a[abs(m) % MOD][i])
                    {
                        flag = true;
                        break;
                    }
                }
                if (flag)
                {
                    out << "true" << '\n';
                } else
                {
                    out << "false" << '\n';
                }
            }
        }
    }
    out.close();
    in.close();
    return 0;
}