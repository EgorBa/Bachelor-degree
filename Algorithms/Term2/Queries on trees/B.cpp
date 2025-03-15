#include <iostream>
#include <cmath>
#include <vector>
#include <unordered_set>

using namespace std;

int p[200010];
int d[200010];
int dp[200010][19];
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
    return p[v];
}

int main()
{
    cin >> n;
    vector1.assign(static_cast<const unsigned int>(n + 1), vector<int>(0));
    k = (int) (log(n) / log(2));
    for (int i = 2; i <= n; ++i)
    {
        cin >> p[i];;
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
    }
    int m;
    cin >> m;
    for (int i = 0; i < m; ++i)
    {
        int a, b;
        cin >> a >> b;
        cout << lca(a, b) << '\n';
    }
}