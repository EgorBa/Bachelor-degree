#include <iostream>
#include <vector>
#include <fstream>
#include <string>
#include <sstream>
#include <iterator>

using namespace std;

int MOD = 12300;
int MOD1 = 123;

int hash1(const string& str)
{
    return hash<string>()(str) % MOD;
}

int hash2(const string& str)
{
    return hash<string>()(str) % MOD1;
}


struct set
{
    vector <vector<string>> a;
    int size;

    set()
    {
        a.assign(static_cast<const unsigned int>(MOD1), vector<string>(0));
        size = 0;
    }

    void insert(const string& str)
    {
        int add = hash2(str);
        bool flag = false;
        for (const auto& i : a[add])
        {
            if (str == i)
            {
                flag = true;
                break;
            }
        }
        if (!flag)
        {
            a[add].push_back(str);
            size++;
        }
    }

    void delet(const string& str)
    {
        int add = hash2(str);
        for (int i = 0; i < a[add].size(); ++i)
        {
            if (str == a[add][i])
            {
                for (int j = i; j < a[add].size() - 1; ++j)
                {
                    a[add][j] = a[add][j + 1];
                }
                a[add].pop_back();
                size--;
                break;
            }
        }
    }

    bool contains(const string& str)
    {
        int add = hash2(str);
        bool flag = false;
        for (const auto& i : a[add])
        {
            if (str == i)
            {
                flag = true;
                break;
            }
        }
        return flag;
    }

    friend ostream& operator<<(std::ostream& out, const set& a)
    {
        for (const auto& i : a.a)
        {
            for (const auto& j : i)
            {
                out << j << " ";
            }
        }
        return out;
    }
};


int main()
{
    vector < vector < pair < string, set >> > a(static_cast<const unsigned int>(MOD));
    string str;
    string key;
    ifstream in("multimap.in");
    ofstream out("multimap.out");
    while (in >> str)
    {
        if (str == "put")
        {
            string value;
            in >> key >> value;
            bool flag = false;
            bool flag1 = false;
            int k = 0;
            int add = hash1(key);
            for (int i = 0; i < a[add].size(); ++i)
            {
                if (key == a[add][i].first)
                {
                    flag = a[add][i].second.contains(value);
                    k = i;
                    flag1 = true;
                    break;
                }
            }
            if (!flag1)
            {
                set t;
                t.insert(value);
                a[add].emplace_back(key, t);
            } else
            {
                if (!flag)
                {
                    a[add][k].second.insert(value);
                }
            }
        } else
        {
            if ("deleteall" == str)
            {
                in >> key;
                int add = hash1(key);
                for (int i = 0; i < a[add].size(); ++i)
                {
                    if (key == a[add][i].first)
                    {
                        for (int j = i; j < a[add].size() - 1; ++j)
                        {
                            a[add][j] = a[add][j + 1];
                        }
                        a[add].pop_back();
                        break;
                    }
                }
            } else
            {
                if ("delete" == str)
                {
                    string value;
                    in >> key >> value;
                    int add = hash1(key);
                    for (int i = 0; i < a[add].size(); ++i)
                    {
                        if (key == a[add][i].first)
                        {
                            a[add][i].second.delet(value);
                            if (a[add][i].second.size == 0)
                            {
                                for (int j = i; j < a[add].size() - 1; ++j)
                                {
                                    a[add][j] = a[add][j + 1];
                                }
                                a[add].pop_back();
                                break;
                            }
                            break;
                        }
                    }
                } else
                {
                    set ans;
                    in >> key;
                    int add = hash1(key);
                    bool flag = false;
                    for (auto& i : a[add])
                    {
                        if (key == i.first)
                        {
                            ans = i.second;
                            flag = true;
                            break;
                        }
                    }
                    if (flag)
                    {
                        out << ans.size << " " << ans << '\n';
                    } else
                    {
                        out << "0" << '\n';
                    }
                }
            }
        }
    }
    out.close();
    in.close();
    return 0;
}