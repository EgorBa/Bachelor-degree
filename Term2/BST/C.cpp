#include <iostream>
#include <map>
#include <string>
#include <unordered_map>

void quickSort(int array[], int low, int high, int a[]);

int** create_array2d(int a, int b);

void free(int** m, int a);

using namespace std;

int main(int argc, char** argv)
{
    unordered_map<int, string> map;
    unordered_map<int, int> map2;
    unordered_map<int, string> map1;
    int n;
    cin >> n;
    int** tree = create_array2d(5, n + 1);
    int** a = create_array2d(2, n);
    for (int i = 0; i < n + 1; i++)
    {
        for (int j = 0; j < 5; j++)
        {
            if ((j < 2) && (i < n))
            { a[j][i] = 0; }
            tree[j][i] = 0;
        }
    }
    for (int i = 0; i < n; i++)
    {
        int first;
        int second;
        cin >> first >> second;
        a[0][i] = first;
        a[1][i] = second;
        string s = to_string(i + 1);
        map[first] = s;
        map2[i + 1] = first;
    }
    quickSort(a[0], 0, n - 1, a[1]);
    tree[0][1] = a[0][0];
    tree[1][1] = a[1][0];
    for (int i = 2; i <= n; i++)
    {
        tree[0][i] = a[0][i - 1];
        tree[1][i] = a[1][i - 1];
        int j = i - 1;
        if (tree[1][i] > tree[1][i - 1])
        {
            tree[3][i - 1] = i;
            tree[4][i] = i - 1;
        } else
        {
            while (tree[1][i] < tree[1][j])
            {
                if (tree[4][j] == 0)
                {
                    break;
                }
                j = tree[4][j];
            }
            if ((tree[4][j] == 0) && (tree[1][i] < tree[1][j]))
            {
                tree[4][j] = i;
                tree[2][i] = j;
            } else
            {
                tree[4][tree[3][j]] = i;
                tree[2][i] = tree[3][j];
                tree[4][i] = j;
                tree[3][j] = i;
            }
        }
    }
    for (int i = 1; i <= n; i++)
    {
        string s = "";
        int j = 4;
        if (tree[j][i] == 0)
        {
            s.append("0 ");
        } else
        {
            s.append(map[tree[0][tree[j][i]]]);
            s.append(" ");
        }
        for (j = 2; j <= 3; j++)
        {
            if (tree[j][i] == 0)
            {
                s.append("0 ");
            } else
            {
                s.append(map[tree[0][tree[j][i]]]);
                s.append(" ");
            }
        }
        map1[tree[0][i]] = s;
    }
    cout << "YES" << "\n";
    for (int i = 1; i <= n; i++)
    {
        cout << map1[map2[i]] << "\n";
    }
    free(a, 2);
    free(tree, 5);
    return 0;
}

void quickSort(int arr[], int left, int right, int a[])
{
    int i = left, j = right;
    int tmp;
    int pivot = arr[(left + right) / 2];
    while (i <= j)
    {
        while (arr[i] < pivot)
            i++;
        while (arr[j] > pivot)
            j--;
        if (i <= j)
        {
            for (int k = 1; k <= 1; k++)
            {
                int temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
            tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
            i++;
            j--;
        }
    };
    if (left < j)
        quickSort(arr, left, j, a);
    if (i < right)
        quickSort(arr, i, right, a);

}

int** create_array2d(int a, int b)
{
    int** m = new int* [a];
    for (int i = 0; i < a; ++i)
        m[i] = new int[b];
    return m;
}

void free(int** m, int a)
{
    for (int i = 0; i < a; i++)
    {
        delete[] m[i];
    }
    delete[]m;
}