#include <iostream>
#include <cmath>

using namespace std;

int main()
{
    int n;
    cin >> n;
    int p[100010];
    int dp[100010][18];
    for (int i = 1; i <= n; i++)
    {
        cin >> p[i];
    }
    for (int i = 1; i <= n; i++)
    {
        dp[i][0] = p[i];
    }
    for (int j = 1; j <= (int) (log(n) / log(2)); j++)
    {
        for (int i = 1; i <= n; i++)
        {
            dp[i][j] = dp[dp[i][j - 1]][j - 1];
        }
    }
    for (int i = 1; i <= n; i++)
    {
        cout << i << ": ";
        for (int j = 0; j <= (int) (log(n) / log(2)); j++)
        {
            if (dp[i][j] == 0)
            {
                break;
            }
            cout << dp[i][j] << " ";
        }
        cout << '\n';
    }
}