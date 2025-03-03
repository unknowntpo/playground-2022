
class App {
public static void main(String[] args) {
  varArgs("Hello");
  varArgs("Hello", "World");
}    

public static void varArgs(String... names) {
    for (String name: names) {
  System.out.print(name);

    }
}
}


