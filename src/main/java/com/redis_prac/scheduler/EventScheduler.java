package com.redis_prac.scheduler;

import com.redis_prac.constant.Event;
import com.redis_prac.service.GifticonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class EventScheduler {
    private final GifticonService gifticonService;

    @Scheduled(fixedDelay = 1000)
    private void chickenEventScheduler() {
        if(gifticonService.validEnd()) {
            log.info("==========선착순 이벤트가 종료되었습니다. ==========");
            return;
        }

        gifticonService.publish(Event.CHICKEN);
        gifticonService.getOrder(Event.CHICKEN);
    }

}
