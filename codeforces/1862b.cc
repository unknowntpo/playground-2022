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

vector<vector<int>> solution(vector<vector<int>> input) {
  return {{1}};
}

int main(int argc, char *argv[]) {
  int tCases;
  cin >> tCases;
  vector<vector<int>>vecs(tCases);
  
  for (int i = 0; i < tCases; i++) {
    int m; 
    cin >> m;
    vector<int> vec(m); 
    for (int j = 0; j < m; j++) {
      cin >> vec[j];
    }
    vecs.push_back(vec);
  }

  vector<vector<int>>out(tCases);
  out = solution(vecs);

  return 0;
}
