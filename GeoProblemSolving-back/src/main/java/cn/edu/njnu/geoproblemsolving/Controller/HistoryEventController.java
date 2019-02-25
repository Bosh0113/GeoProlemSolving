package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.HistoryEvent.HistoryEventDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.HistoryEventEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/history")
public class HistoryEventController {

    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/save", produces = {"application/json;charset=UTF-8"},method = RequestMethod.POST)
    public String saveHistoryEvent(HistoryEventEntity historyEvent){
        HistoryEventDaoImpl historyEventDao=new HistoryEventDaoImpl(mongoTemplate);
        return historyEventDao.saveHistoryEvent(historyEvent);
    }

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public Object inquiryHistoryEvent(@RequestParam("scopeId") String scopeId){
        HistoryEventDaoImpl historyEventDao=new HistoryEventDaoImpl(mongoTemplate);
        return historyEventDao.inquiryHistoryEvent(scopeId);
    }
}
