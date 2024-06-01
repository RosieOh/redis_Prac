package com.redis_prac.service;

import com.redis_prac.constant.Event;
import com.redis_prac.domain.EventCount;
import com.redis_prac.domain.Gifticon;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class GifticonService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final long FIRST_ELEMENT = 0;
    private static final long LAST_ELEMENT = -1;
    private static final long PUBLISH_SIZE = 10;
    private static final long LAST_INDEX = 1;
    private EventCount eventCount;

    public void setEventCount(Event event, int queue) {
        this.eventCount = new EventCount(event, queue);
    }

    public void addQueue(Event event) {
        final String people = Thread.currentThread().getName();
        final long now = System.currentTimeMillis();

        // 선착순 대기열 돌리는 로직
        redisTemplate.opsForZSet().add(event.toString(), people, (int) now);
        log.info("대기열에 추가 - {} ({}초)", people, now);
    }

    public void getOrder(Event event) {
        final long start = FIRST_ELEMENT;
        final long end = LAST_ELEMENT;

        Set<Object> queue = redisTemplate.opsForZSet().range(event.toString(), start, end);

        for (Object people : queue) {
            Long rank = redisTemplate.opsForZSet().rank(event.toString(), people);
            log.info("'{}'님의 현재 대기열은 {}명 남았습니다.", people, rank);
        }
    }

    public void publish(Event event) {
        final long start = FIRST_ELEMENT;
        final long end = PUBLISH_SIZE - LAST_INDEX;

        Set<Object> queue = redisTemplate.opsForZSet().range(event.toString(), start, end);
        for (Object people : queue) {
            final Gifticon gifticon = new Gifticon(event);
            log.info("'{}'님의 {} 기프티콘이 발급되었습니다. ({})", people, gifticon.getEvent().getName(), gifticon.getCode());
            redisTemplate.opsForZSet().remove(event.toString(), people);
            this.eventCount.decrease();
        }
    }

    public boolean validEnd() {
        return this.eventCount != null
                ? this.eventCount.end()
                : false;
    }

    public long getSize(Event event) {
        return redisTemplate.opsForZSet().size(event.toString());
    }
}
