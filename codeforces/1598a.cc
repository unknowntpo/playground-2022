
#include <format>
#include <iostream>
#include <vector>

using namespace std;

int solve(vector<vector<int>> &grid, int colsNum) {

  grid[1][1] = 1;

  for (int i = 1; i < 3; i++) {
    for (int j = 1; j <= colsNum; j++) {
      if (grid[i][j] == 1 && (i != 1 && j != 1)) {
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
  int colsNum = 0;
  cin >> colsNum;

  for (int t = 1; t <= tc; t++) {
    cout << "Case #" << t << ": ";
    vector<vector<int>> grid = vector(3, vector(colsNum + 1, 0));

    for (int i = 1; i < 3; i++) {
      string in;
      cin >> in;
      int j = 1;
      for (char c : in) {
        int num = c - '0'; // Convert char to int
        grid[i][j++] = num;
      }
    }
    solve(grid, colsNum);

    // cout << format("grid {}", grid);
    //     // display
    for (int i = 1; i < 3; i++) {
      for (int j = 1; j <= colsNum; j++) {
        cout << grid[i][j];
      }
      cout << endl;
    }
  }
}