#include <iostream>
#include <vector>
#include <set>
#include <queue>

using namespace std;

int main()
{
    int n, m;
    while (cin >> n)
    {
        cin >> m;
        set<int> list[n];
        set<int> reverse_list[n];
        queue<int> queue;
        int d[n];
        int degree[n];
        bool used[n];
        for (int i = 0; i < n; ++i)
        {
            list[i].clear();
            reverse_list[i].clear();
            used[i] = false;
            degree[i] = 0;
            d[i] = -1;
        }
        for (int i = 0; i < m; i++)
        {
            int from, to;
            cin >> from >> to;
            list[from - 1].insert(to - 1);
            reverse_list[to - 1].insert(from - 1);
        }
        for (int v = 0; v < n; v++)
        {
            if (list[v].empty())
            {
                queue.push(v);
                d[v] = 0;
                used[v] = true;
            }
        }
        while (!queue.empty())
        {
            int v = queue.front();
            queue.pop();
            if (d[v] == 0)
            {
                for (int u : reverse_list[v])
                {
                    if (!used[u])
                    {
                        queue.push(u);
                        d[u] = 1;
                        used[u] = true;
                    }
                }
            } else
            {
                if (d[v] == 1)
                {
                    for (int u : reverse_list[v])
                    {
                        degree[u]++;
                        if (degree[u] == list[u].size() && !used[u])
                        {
                            d[u] = 0;
                            queue.push(u);
                            used[u] = true;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < n; i++)
        {
            if (d[i] == -1)
            {
                cout << "DRAW" << "\n";
            } else
            {
                cout << (d[i] == 0 ? "SECOND" : "FIRST") << "\n";
            }
        }
        cout << "\n";
    }
    return 0;
}