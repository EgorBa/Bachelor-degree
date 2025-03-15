#include <iostream>
#include <fstream>
#include <cmath>
#include <vector>
#include <unordered_set>

using namespace std;

int p[200010];
int d[200010];
int w[200010][19];
int dp[200010][19];
int n;
int k, res;
vector <vector<int>> vector1;

void dfs(int u, int count)
{
    d[u] = count;
    for (auto v: vector1[u])
    {
        dfs(v, count + 1);
    }
}

int min(int a, int b)
{
    if (a > b)
    {
        return b;
    } else
    {
        return a;
    }
}

int lca(int v, int u)
{
    res = static_cast<int>(INFINITY);
    if (d[v] > d[u])
    { swap(v, u); }
    int h = d[u] - d[v];
    for (int i = k; i >= 0; --i)
    {
        if (h >= (1 << i))
        {
            res = min(res, w[u][i]);
            u = dp[u][i];
            h -= (1 << i);
        }
    }
    if (v == u)
    {
        return res;
    }
    for (int i = k; i >= 0; --i)
    {
        if (dp[v][i] != dp[u][i])
        {
            res = min(res, min(w[u][i], w[v][i]));
            v = dp[v][i];
            u = dp[u][i];
        }
    }

    return min(res, min(w[u][0], w[v][0]));
}

int main()
{
    ifstream in("minonpath.in");
    ofstream out("minonpath.out");
    in >> n;
    vector1.assign(static_cast<const unsigned int>(n + 1), vector<int>(0));
    for (int i = 0; i < 200010; ++i)
    {
        for (int j = 0; j < 19; ++j)
        {
            w[i][j] = static_cast<int>(INFINITY);
        }
    }
    k = (int) (log(n) / log(2));
    for (int i = 2; i <= n; ++i)
    {
        int weight;
        in >> p[i] >> weight;
        w[i][0] = weight;
        vector1[p[i]].push_back(i);
    }
    dfs(1, 0);
    for (int i = 1; i <= n; ++i)
    {
        dp[i][0] = p[i];
    }
    for (int j = 1; j <= k; ++j)
    {
        for (int i = 1; i <= n; ++i)
        {
            dp[i][j] = dp[dp[i][j - 1]][j - 1];
        }
        for (int i = 1; i <= n; ++i)
        {
            w[i][j] = min(w[dp[i][j - 1]][j - 1], w[i][j - 1]);
        }

    }
    int m;
    in >> m;
    for (int i = 0; i < m; ++i)
    {
        int a, b;
        in >> a >> b;
        out << lca(a, b) << '\n';
    }
    in.close();
    out.close();
}