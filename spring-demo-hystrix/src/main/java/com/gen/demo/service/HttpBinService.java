package com.gen.demo.service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;

@Service
public class HttpBinService {

	public Map map() {
//		long os = System.currentTimeMillis();
//		/* simulate performing network call to retrieve order */
//        try {
//            Thread.sleep((int) (Math.random() * 200) + 25);
//        } catch (InterruptedException e) {
//            // do nothing
//        }

        /* fail rarely ... but allow failure as this one has no fallback */
//        if (Math.random() > 0.5) {
            throw new RuntimeException("random failure loading order over network");
//        }

        /* latency spike 5% of the time */
//        if (Math.random() > 0.9) {
//            // random latency spike
//            try {
//                Thread.sleep((int) (Math.random() * 300) + 50);
//            } catch (InterruptedException e) {
//                // do nothing
//            }
//        }
//        long es = System.currentTimeMillis();
//        Map<String, String> fallback = new HashMap<>();
//        fallback.put("hello", "world, ts=" + (es - os));
//        /* success ... create Order with data "from" the remote service response */
//        return fallback;

	}

	public Supplier<Map> delaySuppplier() {
		return () -> this.map();
	}
}
