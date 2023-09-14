
#include <iostream>
#include <vector>

using namespace std;

int solve(vector<string> in) {
  string ans = "YES";

  int numRows = in.size();
  int numCols = in[0].size();

  // very big number
  int minStep = 2000;

  for (int i = 0; i < numRows; i++) {
    int j = 0;
    while (in[i][j] == '*') {
      j++;
    }
    //
    if (in[i][j] == 'S') {
      // G can't find S
      return -1;
    }
    // found G, count minSteps
    int _minStep = 0;
    while (in[i][j] != 'S') {
      _minStep++;
      j++;
    }
    minStep = min(minStep, _minStep);
  }
  return (minStep == 2000) ? -1 : minStep;
}

/*
 *
 *
3 4
*G*S
G**S
*G*S
*/

int main() {
  ios_base::sync_with_stdio(0);
  cin.tie(0);
  cout.tie(0);
  int lines, cols;

  cin >> lines >> cols;

  cout << lines << " " << cols << endl;

  vector<string> colStrs(lines);
  for (int i = 0; i < lines; i++) {
    cin >> colStrs[i];
  }

  int res = solve(colStrs);

  cout << res;
}
