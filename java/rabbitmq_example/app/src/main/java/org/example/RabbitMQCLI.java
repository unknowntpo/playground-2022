package org.example;

import org.example.command.EcommerceCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

@Command(
    name = "rabbitmq-demo",
    description = "RabbitMQ Use Cases Demonstration - Real-world examples with threaded producers and consumers",
    subcommands = {
        HelpCommand.class,
        EcommerceCommand.class
    },
    version = "1.0.0",
    mixinStandardHelpOptions = true,
    headerHeading = "%n",
    header = "🐰 RabbitMQ Use Cases Demo",
    descriptionHeading = "%nDescription:%n",
    parameterListHeading = "%nParameters:%n",
    optionListHeading = "%nOptions:%n",
    commandListHeading = "%nCommands:%n"
)
public class RabbitMQCLI implements Runnable {

    @Override
    public void run() {
        System.out.println("🐰 RabbitMQ Use Cases Demo");
        System.out.println();
        System.out.println("This application demonstrates real-world RabbitMQ patterns using threaded");
        System.out.println("producers and consumers. Each example simulates a concrete business scenario.");
        System.out.println();
        System.out.println("Available commands:");
        System.out.println("  ecommerce    - E-commerce Order Processing (Work Queues Pattern)");
        System.out.println("                 Simulates Amazon-style order distribution to warehouse workers");
        System.out.println();
        System.out.println("Use './gradlew run --args=\"<command> --help\"' for detailed command options.");
        System.out.println("Use './gradlew run --args=\"--help\"' to show this help with full command list.");
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new RabbitMQCLI()).execute(args);
        System.exit(exitCode);
    }
}