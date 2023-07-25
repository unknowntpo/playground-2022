#include <iostream>
#include <vector>

using namespace std;

/*
 *
 *
 *
 [1,2,3]

 1, 2, 3
 1, 3, 2

end: len(path) == n

*/

void showVec(vector<int> &vec) {
  for (const auto &e : vec) {
    cout << e << " ";
  }
  cout << endl;
}

void bt(int n, vector<int> &path, vector<int> &pickNums, int *out) {
  if (path.size() == n) {
    showVec(path);
    auto sum = [](const vector<int> &vec) {
      int result = 0;
      for (const auto &e : vec) {
        result += e;
      }
      return result;
    };
    cout << "sum of path: " << sum(path) << endl;
    if (sum(path) == (n * (n + 1) / 2 % 998244353)) {
      *out += 1;
    }
    return;
  }
  for (int i = 0; i < pickNums.size(); i++) {
    if (pickNums[i] == 0) {
      path.push_back(i + 1);
      // mark as picked
      pickNums[i] = 1;
      bt(n, path, pickNums, out);
      pickNums[i] = 0;
      path.pop_back();
    }
  }
}

int solution(int input) {
  vector<int> pickNums(input, 0);
  vector<int> path;
  int out = 0;
  bt(input, path, pickNums, &out);
  return out;
}

int main(int argc, char *argv[]) {
  int input;

  cin >> input;
  int out = solution(input);
  cout << out;

  return 0;
}
