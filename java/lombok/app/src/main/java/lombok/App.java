/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package lombok;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
        User user = new User(3, "hello", "Hsinchu");
        System.out.println(user);
        System.out.println(user.getAddress());
    }
}
