package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.HistoryEvent.HistoryEventDaoImpl;
import cn.edu.njnu.geoproblemsolving.Dao.Method.EncodeUtil;
import cn.edu.njnu.geoproblemsolving.Entity.HistoryEventEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/history")
public class HistoryEventController {

    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/save", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public String saveHistoryEvent(@RequestBody HistoryEventEntity historyEvent) {
        // decode scopeId
        String scopeId = historyEvent.getScopeId();
        if (scopeId.length() > 36) {
            String sid = new String(EncodeUtil.decode(scopeId));
            scopeId = sid.substring(0, sid.length() - 2);
        }
        historyEvent.setScopeId(scopeId);
        HistoryEventDaoImpl historyEventDao = new HistoryEventDaoImpl(mongoTemplate);
        return historyEventDao.saveHistoryEvent(historyEvent);
    }

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public Object inquiryHistoryEvent(@RequestParam("eventType") String eventType, @RequestParam("key") String key, @RequestParam("value") String value) {
        HistoryEventDaoImpl historyEventDao = new HistoryEventDaoImpl(mongoTemplate);
        if (key.equals("scopeId")&&value.length() > 36){
            String sid = new String(EncodeUtil.decode(value));
            value = sid.substring(0, sid.length() - 2);
        }
        return historyEventDao.inquiryHistoryEvent(eventType, key, value);
    }
}
