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


struct map
{
    vector <vector<pair < string, string>>>
    a;
    int size;

    map()
    {
        a.assign(static_cast<const unsigned int>(MOD), vector < pair < string, string >> (0));
        size = 0;
    }

    void put(string key, string value)
    {
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
            size++;
        }
    }

    void delet(const string& key)
    {
        for (int i = 0; i < a[hash1(key)].size(); ++i)
        {
            if (key == a[hash1(key)][i].first)
            {
                for (int j = i; j < a[hash1(key)].size() - 1; ++j)
                {
                    a[hash1(key)][j] = a[hash1(key)][j + 1];
                }
                a[hash1(key)].pop_back();
                size--;
                break;
            }
        }
    }

    string get(const string& key)
    {
        string ans;
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
            return ans;
        } else
        {
            return "none";
        }
    }

    bool contains(const string& key)
    {
        bool flag = false;
        for (int i = 0; i < a[hash1(key)].size(); ++i)
        {
            if (key == a[hash1(key)][i].first)
            {
                flag = true;
                break;
            }
        }
        return flag;
    }
};

int main()
{
    map a;
    map prev;
    map next;
    string str;
    string prev_key = "";
    ifstream in("linkedmap.in");
    ofstream out("linkedmap.out");
    while (in >> str)
    {
        if (str == "put")
        {
            string key;
            string value;
            in >> key >> value;
            if (a.contains(key))
            {
                a.put(key, value);
            } else
            {
                a.put(key, value);
                prev.put(key, prev_key);
                next.put(prev_key, key);
                prev_key = key;

            }
        } else
        {
            if ("delete" == str)
            {
                string key;
                in >> key;
                if (key == prev_key)
                {
                    prev_key = prev.get(key);
                }
                a.delet(key);
                next.put(prev.get(key), next.get(key));
                prev.put(next.get(key), prev.get(key));
                next.delet(key);
                prev.delet(key);
            } else
            {
                if (str == "get")
                {
                    string ans;
                    string key;
                    in >> key;
                    out << a.get(key) << '\n';
                } else
                {
                    if (str == "prev")
                    {
                        string key;
                        in >> key;
                        out << a.get(prev.get(key)) << '\n';
                    } else
                    {
                        string key;
                        in >> key;
                        out << a.get(next.get(key)) << '\n';
                    }
                }
            }
        }
    }
    out.close();
    in.close();
    return 0;
}