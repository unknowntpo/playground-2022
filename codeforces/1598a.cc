
#include <iostream>
#include <vector>

#define DEBUG 0

using namespace std;

int solve(vector<vector<int>> &grid, int colsNum) {

  grid[1][1] = 1;

  for (int i = 1; i < 3; i++) {
    for (int j = 1; j <= colsNum; j++) {
      // cout << "i: " << i << " j: " << j << endl;
      if (i == 1 && j == 1)
        continue;
      if (grid[i][j] == 1) {
        grid[i][j] = 0;
        continue;
      } else {
        grid[i][j] = grid[i - 1][j] + grid[i][j - 1] + grid[i - 1][j - 1];
      }
    }
  }
  return grid[2][colsNum];
}

int main() {
  ios_base::sync_with_stdio(0);
  cin.tie(0);
  cout.tie(0);
  int tc = 1;
  cin >> tc;
  for (int t = 1; t <= tc; t++) {
    cout << "Case #" << t << ": ";
    int colsNum = 0;
    cin >> colsNum;
    vector<vector<int>> grid(3, vector<int>(colsNum + 1, 0));

    for (int i = 1; i < 3; i++) {
      string in;
      cin >> in;
      int j = 1;
      for (char c : in) {
        int num = c - '0'; // Convert char to int
        grid[i][j++] = num;
      }
    }
    int res = solve(grid, colsNum);
    if (DEBUG) {
      cout << "result: " << res << endl;
    }
    string out = "";
    if (res > 0) {
      out = "YES";
    } else {
      out = "NO";
    }
    cout << out << endl;
    if (DEBUG) {
      // cout << format("grid {}", grid);
      //     // display
      cout << "--------DEBUG------" << endl;
      for (int i = 1; i < 3; i++) {
        for (int j = 1; j <= colsNum; j++) {
          cout << grid[i][j];
        }
        cout << endl;
      }
      cout << "--------DEBUG END------" << endl;
    }
  }
}