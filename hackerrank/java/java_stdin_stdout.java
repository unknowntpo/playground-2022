// https://www.hackerrank.com/challenges/java-stdin-and-stdout-1/problem?isFullScreen=true
import java.util.*;

public class Solution {
  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);

    ArrayList<Integer> l = new ArrayList<Integer>();

    for (Integer i = 0; i < 3;i++) {
      l.add(scan.nextInt());
    }

    l.forEach(e -> System.out.println(e));
  }
}
