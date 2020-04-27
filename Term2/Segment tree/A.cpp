#include <iostream>
#include <string>

long long getSum(long i, long long sum[]);

using namespace std;

int main()
{
    int n, x, y, xy;
    cin >> n >> x >> y >> xy;
    long long a[n];
    long long sum[n];
    a[0] = xy;
    sum[0] = a[0];
    for (int i = 1; i < n; i++)
    {
        a[i] = (x * a[i - 1] + y) % (1 << 16);
        sum[i] = sum[i - 1] + a[i];
    }
    int m, z, t, zt;
    cin >> m >> z >> t >> zt;
    long c[3];
    long long b[3];
    b[0] = zt;
    c[0] = (int) (b[0] % n);
    long long result = 0;
    long long con = 1 << 30;
    for (int i = 0; i < m; i++)
    {
        if ((z * b[0] + t) < 0)
        {
            b[1] = con + t;
            c[1] = 0;
        } else
        {
            b[1] = (z * b[0] + t) % con;
            c[1] = b[1] % n;
        }
        if ((z * b[1] + t) < 0)
        {
            b[2] = con + t;
            c[2] = 0;
        } else
        {
            b[2] = (z * b[1] + t) % con;
            c[2] = b[2] % n;
        }
        long l = min(c[0], c[1]);
        long r = max(c[0], c[1]);
        result += getSum(r, sum) - getSum(l - 1, sum);
        b[0] = b[2];
        c[0] = c[2];
    }
    cout << result << endl;
    return 0;
}

long long getSum(long i, long long sum[])
{
    if (i >= 0)
    {
        return sum[(int) i];
    } else
    {
        return 0;
    }
}