package org.example.answering_machine;

import javax.xml.transform.OutputKeys;

/**
 * 電話答錄機
 * <a href="https://www.youtube.com/watch?v=IDSujIxU-RA">...</a>
 */
public class GreetingService {
    public enum Status {
        DAYTIME,
        QK,
        WORK_OVERTIME,
    }

    Status status = Status.DAYTIME;

    public String greeting() {
        return switch (status) {
            case DAYTIME -> "Hello";
            case QK -> "See you tomorrow";
            case WORK_OVERTIME -> "Working over time, you need to sleep";
        };
    }

    public void doSwitch(Status expectStatus) {
        if (expectStatus == status) {
            // no change
            return;
        }
        switch (expectStatus) {
            case DAYTIME:
                // status should be QK
                if (status == Status.WORK_OVERTIME) {
                    throw new IllegalStateException("Status.WORK_OVERTIME cannot switch to Status.DAYTIME, you need to QK first");
                }
                status = Status.DAYTIME;
                break;
            case QK:
                // status should be DAYTIME, WORK_OVERTIME
                status = Status.QK;
                break;
            case WORK_OVERTIME:
                // status should be DAYTIME, QK
                status = Status.WORK_OVERTIME;
                break;
        }
    }
}
