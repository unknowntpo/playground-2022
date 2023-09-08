
#include <iostream>
#include <vector>

using namespace std;

string solve(string in[], int numCols) {
  string ans = "YES";

  cout << numCols << endl;
  for (int j = 1; j < numCols; j++) {
    if (in[0][j] == '1' and in[1][j] == '1') {
      ans = "NO";
      return ans;
    }
  }
  return ans;
}

int main() {
  ios_base::sync_with_stdio(0);
  cin.tie(0);
  cout.tie(0);
  int tc = 1;
  cin >> tc;
  for (int t = 1; t <= tc; t++) {
    cout << "Case #" << t << ": " << endl;
    int numCols = 0;
    cin >> numCols;

    string in[2];
    for (int i = 0; i < 2; i++)
      cin >> in[i];

    string res = solve(in, numCols);
    cout << res << endl;
  }
}