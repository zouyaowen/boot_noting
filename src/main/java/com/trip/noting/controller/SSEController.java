package com.trip.noting.controller;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SSEController {
    @GetMapping("/sse")
    @ResponseBody
    public void sse(HttpServletResponse response) throws IOException {
        response.setContentType("text/event-stream;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        for (int i = 0; i < 10; i++) {
            String data = "data: 消息 " + i + "\n\n";
            response.getWriter().write(data);
            response.getWriter().flush();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        response.getWriter().close();
    }


    public static class CruiseJourney {
        // 0 是海上巡游 > 0 是港口
        public Long portPoiId;
        public Integer theDay;
        public String startTime;
        public String arrivalTime;
    }

    @Test
    public void testJourney() {
        List<CruiseJourney> journeys = new ArrayList<>();
        journeys.add(new CruiseJourney() {{
            portPoiId = 1L;
            theDay = 1;
            arrivalTime = "";
            startTime = "";
        }});
        journeys.add(new CruiseJourney() {{
            portPoiId = 1L;
            theDay = 2;
            arrivalTime = "08:00:00";
            startTime = "";
        }});
        journeys.add(new CruiseJourney() {{
            portPoiId = 2L;
            theDay = 3;
            startTime = "10:00:00";
            arrivalTime = "08:00:00";
        }});
    }


    @Test
    public void testJourneyNormal2() {
        List<CruiseJourney> journeys = new ArrayList<>();
        journeys.add(new CruiseJourney() {{
            portPoiId = 1L;
            theDay = 1;
            arrivalTime = "";
            startTime = "";
        }});
        journeys.add(new CruiseJourney() {{
            portPoiId = 1L;
            theDay = 2;
            arrivalTime = "";
            startTime = "17:00:00";
        }});
        journeys.add(new CruiseJourney() {{
            portPoiId = 2L;
            theDay = 3;
            arrivalTime = "08:00:00";
            startTime = "17:00:00";
        }});
        journeys.add(new CruiseJourney() {{
            portPoiId = 3L;
            theDay = 4;
            arrivalTime = "08:00:00";
            startTime = "";
        }});
        boolean b = checkTime(journeys);
        System.out.println(b);
    }

    @Test
    public void testJourneyNormal1() {
        List<CruiseJourney> journeys = new ArrayList<>();
        journeys.add(new CruiseJourney() {{
            portPoiId = 1L;
            theDay = 1;
            arrivalTime = "";
            startTime = "17:00:00";
        }});
        journeys.add(new CruiseJourney() {{
            portPoiId = 2L;
            theDay = 2;
            arrivalTime = "08:00:00";
            startTime = "17:00:00";
        }});
        journeys.add(new CruiseJourney() {{
            portPoiId = 3L;
            theDay = 3;
            arrivalTime = "08:00:00";
            startTime = "";
        }});
        boolean b = checkTime(journeys);
        System.out.println(b);
    }

    @Test
    public void testJourneyError11() {
        List<CruiseJourney> journeys = new ArrayList<>();
        journeys.add(new CruiseJourney() {{
            portPoiId = 1L;
            theDay = 1;
            arrivalTime = "";
            startTime = "17:00:00";
        }});
        journeys.add(new CruiseJourney() {{
            portPoiId = 2L;
            theDay = 2;
            arrivalTime = "08:00:00";
            startTime = "";
        }});
        journeys.add(new CruiseJourney() {{
            portPoiId = 3L;
            theDay = 3;
            arrivalTime = "08:00:00";
            startTime = "";
        }});
        boolean b = checkTime(journeys);
        System.out.println(b);
    }

    @Test
    public void testJourneyNormal3() {
        List<CruiseJourney> journeys = new ArrayList<>();
        journeys.add(new CruiseJourney() {{
            portPoiId = 1L;
            theDay = 1;
            arrivalTime = "";
            startTime = "";
        }});
        journeys.add(new CruiseJourney() {{
            portPoiId = 1L;
            theDay = 2;
            arrivalTime = "";
            startTime = "17:00:00";
        }});
        journeys.add(new CruiseJourney() {{
            portPoiId = 2L;
            theDay = 3;
            arrivalTime = "08:00:00";
            startTime = "17:00:00";
        }});
        journeys.add(new CruiseJourney() {{
            portPoiId = 3L;
            theDay = 4;
            arrivalTime = "08:00:00";
            startTime = "";
        }});
        journeys.add(new CruiseJourney() {{
            portPoiId = 3L;
            theDay = 5;
            arrivalTime = "";
            startTime = "";
        }});
        boolean b = checkTime(journeys);
        System.out.println(b);
    }

    @Test
    public void testJourneyNormal4() {
        List<CruiseJourney> journeys = new ArrayList<>();
        journeys.add(new CruiseJourney() {{
            portPoiId = 1L;
            theDay = 1;
            arrivalTime = "";
            startTime = "";
        }});
        journeys.add(new CruiseJourney() {{
            portPoiId = 1L;
            theDay = 2;
            arrivalTime = "";
            startTime = "17:00:00";
        }});
        journeys.add(new CruiseJourney() {{
            portPoiId = 2L;
            theDay = 3;
            arrivalTime = "08:00:00";
            startTime = "";
        }});
        journeys.add(new CruiseJourney() {{
            portPoiId = 2L;
            theDay = 4;
            arrivalTime = "";
            startTime = "";
        }});
        journeys.add(new CruiseJourney() {{
            portPoiId = 2L;
            theDay = 5;
            arrivalTime = "";
            startTime = "17:00:00";
        }});
        journeys.add(new CruiseJourney() {{
            portPoiId = 3L;
            theDay = 6;
            arrivalTime = "08:00:00";
            startTime = "";
        }});
        journeys.add(new CruiseJourney() {{
            portPoiId = 3L;
            theDay = 7;
            arrivalTime = "";
            startTime = "";
        }});
        boolean b = checkTime(journeys);
        System.out.println(b);
    }

    @GetMapping("/checkTime")
    public boolean checkTime(List<CruiseJourney> journeyInfoList) {
        if (journeyInfoList == null || journeyInfoList.isEmpty()) {
            return false;
        }

        // 遍历行程检查时间
        for (int i = 0; i < journeyInfoList.size(); i++) {
            CruiseJourney current = journeyInfoList.get(i);
            // 如果是海上巡游，不需要检查时间
            if (current.portPoiId <= 0) {
                continue;
            }
            // 获取前一天和后一天的行程
            CruiseJourney prev = i > 0 ? journeyInfoList.get(i - 1) : null;
            CruiseJourney next = i < journeyInfoList.size() - 1 ? journeyInfoList.get(i + 1) : null;

            // 判断是否和前后是相同港口
            boolean sameAsPrev = prev != null && isSamePort(current, prev);
            boolean sameAsNext = next != null && isSamePort(current, next);

            // 如果是连续停靠的中间天数，不需要任何时间
            if (sameAsPrev && sameAsNext) {
                continue;
            } else if (!sameAsPrev && sameAsNext) {
                // 如果是同一港口的第一天，需要抵港时间（除非是行程第一天）
                if (i > 0 && StringUtils.isBlank(current.arrivalTime)) {
                    return false;
                }
            } else if (sameAsPrev) {
                // 如果是同一港口的最后一天，需要启航时间（除非是行程最后一天）
                if (i < journeyInfoList.size() - 1 && StringUtils.isBlank(current.startTime)) {
                    return false;
                }
            } else {
                // 对于单独一天的停靠（前后都不同） 第一天只需要启航时间
                if (i == 0) {
                    if (StringUtils.isBlank(current.startTime)) {
                        return false;
                    }
                }
                // 最后一天只需要抵港时间
                else if (i == journeyInfoList.size() - 1) {
                    if (StringUtils.isBlank(current.arrivalTime)) {
                        return false;
                    }
                }
                // 中间天数需要同时有抵港和启航时间
                else {
                    if (StringUtils.isBlank(current.arrivalTime) || StringUtils.isBlank(current.startTime)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isSamePort(CruiseJourney current, CruiseJourney prev) {
        if (current == null || prev == null) {
            return false;
        }
        // 可以根据实际业务补充判断逻辑，比如港口ID或者经纬度等
        return current.portPoiId != null && current.portPoiId.equals(prev.portPoiId);
    }
}
