#include <iostream>
#include <cmath>
#include <vector>
#include <unordered_set>

using namespace std;

int p[200010];
int d[200010];
int dp[200010][19];
bool b[200010];
int near[200010];
int rang[200010];
int p1[200010];
int n;
int k;
vector <vector<int>> vector1;


void dfs(int u, int count)
{
    d[u] = count;
    for (auto v: vector1[u])
    {
        dfs(v, count + 1);
    }
}

int get(int x)
{
    if (x != p1[x])
    {
        p1[x] = get(p1[x]);
    }
    return p1[x];
}

void union1(int a, int b)
{
    int y;
    int cur_a = a;
    int cur_b = b;
    a = get(a);
    b = get(b);
    if (d[cur_a] < d[cur_b])
    {
        y = near[a];
    } else
    {
        y = near[b];
    }
    if (a != b)
    {
        if (rang[a] == rang[b])
        { rang[b]++; }
        if (rang[a] >= rang[b])
        {
            p1[b] = a;
            near[a] = y;
        } else
        {
            p1[a] = b;
            near[b] = y;
        }
    }
}

int lca(int v, int u)
{
    if (d[v] > d[u])
    { swap(v, u); }
    int h = d[u] - d[v];
    for (int i = k; i >= 0; --i)
    {
        if (h >= (1 << i))
        {
            u = dp[u][i];
            h -= (1 << i);
        }
    }
    if (v == u)
    {
        return v;
    }
    for (int i = k; i >= 0; --i)
    {
        if (dp[v][i] != dp[u][i])
        {
            v = dp[v][i];
            u = dp[u][i];
        }
    }
    int y = p[v];
    return near[get(p[v])];
}

int main()
{
    cin >> n;
    vector1.assign(static_cast<const unsigned int>(200010), vector<int>(0));
    for (int i = 0; i <= n; ++i)
    {
        p1[i] = i;
        near[i] = i;
    }
    k = (int) (log(n) / log(2));
    int l = 0;
    for (int i = 0; i < n; ++i)
    {
        char chr;
        cin >> chr;
        if (chr == '+')
        {
            l++;
            int a;
            cin >> a;
            a--;
            p[l] = a;
            vector1[a].push_back(l);
            d[l] = d[a] + 1;
            dp[l][0] = a;
            for (int j = 1; j <= k; ++j)
            {
                dp[l][j] = dp[dp[l][j - 1]][j - 1];
            }
        } else
        {
            if (chr == '?')
            {
                int a, b;
                cin >> a >> b;
                a--;
                b--;
                cout << lca(a, b) + 1 << '\n';
            } else
            {
                int a;
                cin >> a;
                a--;
                b[a] = true;
                near[a] = near[p[a]];
                if (b[p[a]])
                {
                    union1(a, p[a]);
                }
                for (auto v: vector1[a])
                {
                    if (b[v])
                    {
                        union1(v, a);
                    }
                }
            }
        }
    }
}