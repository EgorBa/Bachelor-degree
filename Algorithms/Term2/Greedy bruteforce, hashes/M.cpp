#include <iostream>
#include <vector>
#include <fstream>
#include <string>
#include <sstream>
#include <iterator>

using namespace std;

int MOD = 12345;

int hash1(const string& str)
{
    return hash<string>()(str) % MOD;
}

int main()
{
    vector<vector<pair<string, string>>> a;
    a.assign(static_cast<const unsigned int>(MOD + 1), vector<pair<string, string>>(0));
    string str;
    ifstream in("map.in");
    ofstream out("map.out");
    while (in >> str)
    {
        if (str == "put")
        {
            string key;
            string value;
            in >> key >> value;
            bool flag = false;
            for (int i = 0; i < a[hash1(key)].size(); ++i)
            {
                if (key == a[hash1(key)][i].first)
                {
                    a[hash1(key)][i].second = value;
                    flag = true;
                    break;
                }
            }
            if (!flag)
            {
                a[hash1(key)].emplace_back(key, value);
            }
        } else
        {
            if ("delete" == str)
            {
                string key;
                in >> key;
                for (int i = 0; i < a[hash1(key)].size(); ++i)
                {
                    if (key == a[hash1(key)][i].first)
                    {
                        for (int j = i; j < a[hash1(key)].size() - 1; ++j)
                        {
                            a[hash1(key)][j] = a[hash1(key)][j + 1];
                        }
                        a[hash1(key)].pop_back();
                        break;
                    }
                }
            } else
            {
                string ans;
                string key;
                in >> key;
                bool flag = false;
                for (int i = 0; i < a[hash1(key)].size(); ++i)
                {
                    if (key == a[hash1(key)][i].first)
                    {
                        ans = a[hash1(key)][i].second;
                        flag = true;
                        break;
                    }
                }
                if (flag)
                {
                    out << ans << '\n';
                } else
                {
                    out << "none" << '\n';
                }
            }
        }
    }
    out.close();
    in.close();
    return 0;
}