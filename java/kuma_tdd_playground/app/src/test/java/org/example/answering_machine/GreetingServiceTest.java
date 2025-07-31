package org.example.answering_machine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Init: working
 * when working, call GreetingService.greeting() should get 'Hello'
 * when qk, call GreetingService.greeting() should get 'See you tomorrow'
 * when qk, call GreetingService.greeting() should get 'See you tomorrow'
 */
class GreetingServiceTest {
    @Test
    void working() {
        GreetingService service = new GreetingService();
        assertEquals("Hello", service.greeting());
        service.doSwitch(GreetingService.Status.QK);
        assertEquals("See you tomorrow", service.greeting());
        // reset, test working overtime
        service = new GreetingService();
        service.doSwitch(GreetingService.Status.WORK_OVERTIME);
        assertEquals("Working over time, you need to sleep", service.greeting());
    }

    @Test
    void qk() {
        GreetingService service = new GreetingService();
        assertEquals("Hello", service.greeting());
        service.doSwitch(GreetingService.Status.QK);
        assertEquals("See you tomorrow", service.greeting());

        // switch to daytime
        service.doSwitch(GreetingService.Status.DAYTIME);
        assertEquals("Hello", service.greeting());


        // test working overtime
        service = new GreetingService();
        assertEquals("Hello", service.greeting());
        service.doSwitch(GreetingService.Status.WORK_OVERTIME);
        assertEquals("Working over time, you need to sleep", service.greeting());
    }

    @Test
    void workOverTime() {
        GreetingService service = new GreetingService();
        assertEquals("Hello", service.greeting());
        service.doSwitch(GreetingService.Status.WORK_OVERTIME);
        assertEquals("Working over time, you need to sleep", service.greeting());

        var exception = assertThrows(IllegalStateException.class, () -> {
            service.doSwitch(GreetingService.Status.DAYTIME);
        } );
        String expectMsg = "Status.WORK_OVERTIME cannot switch to Status.DAYTIME, you need to QK first";
        assertEquals(expectMsg, exception.getMessage());

        // TODO: test can not switch to Daytime
        service.doSwitch(GreetingService.Status.QK);
        assertEquals("See you tomorrow", service.greeting());
    }
}
