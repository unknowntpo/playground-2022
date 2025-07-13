package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Example test class showing how to reuse the same RabbitMQ container
 * for testing different commands (Twitter notification system)
 */
class TwitterServiceTest {

    @BeforeAll
    static void setUpAll() {
        // Reuse the SAME container instance across all test classes!
        TestContainerConfig.getSharedContainer();
        System.out.println("🐦 Twitter tests using shared RabbitMQ at " + TestContainerConfig.getRabbitmqConnectionString());
    }

    @Test
    void shouldBroadcastTweetToAllFollowers() {
        // Given
        String rabbitmqHost = TestContainerConfig.getRabbitmqHost();
        int rabbitmqPort = TestContainerConfig.getRabbitmqPort();
        
        // TODO: Implement TwitterService similar to EcommerceService
        // TwitterService twitterService = new TwitterService(rabbitmqHost, rabbitmqPort);
        
        // When
        // twitterService.startFollowerNotifiers(4);
        // twitterService.publishTweet("@elonmusk", "Going to Mars!");
        
        // Then
        // All 4 followers should receive the same tweet
        assertThat(true).isTrue(); // Placeholder until TwitterService is implemented
        
        System.out.println("🐦 Twitter test completed using shared container");
    }

    @Test  
    void shouldHandleMultipleSimultaneousTweets() {
        // This test would also reuse the same container!
        assertThat(true).isTrue(); // Placeholder
    }
}