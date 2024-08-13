package org.example.service.internal;

import org.example.config.SqlMaster;
import org.example.object.OriginObject;
import org.example.object.request.RequestListDto;
import org.example.object.response.ResponseListDto;
import org.example.utils.keys.ENV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Workspace extends OriginObject {

//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SqlMaster sm;

    public Object insertObject(String path, Object param){
        try{
            return this.sm.insert(path, param);
        }catch (Exception e){
            throw getErrString(e, param);
        }
    }

    public Object updateObject(String path, Object param){
        try{
            return this.sm.update(path, param);
        }catch (Exception e){
            throw getErrString(e, param);
        }
    }

    public Object deleteObject(String path, Object param){
        try{
            return this.sm.delete(path, param);
        }catch (Exception e){
            throw getErrString(e, param);
        }
    }

    public Object getItem(String path, Object param){
        try{
            return this.sm.selectOne(path, param);
        }catch (Exception e){
            throw getErrString(e, param);
        }
    }

    public List getList(String path, Object param){
        try{
            List list = (List)this.sm.selectList(path, param);
            return list == null ? new ArrayList() : list;
        }catch (Exception e){
            e.printStackTrace();
            throw getErrString(e, param);
        }
    }

    private RuntimeException getErrString(Exception e, Object param){
        Map rtn = new HashMap();
        rtn.put("result", "n");
        rtn.put("message", getMessage((Map)param));
        throw new RuntimeException(getJson(rtn));
    }
    // Database Settings end

    protected String getMessage(Map map){
        return getMessage(map, "900-000");
    }

    public String getMessage(Map map, String lang_code){
        map.put("lang_code", lang_code);
        try {
            return (String)getItem("comm.common.getMessage", map);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public <T extends RequestListDto> ResponseListDto fetchItemListByNextToken(T requestListDto, String listPath, String countPath, String id){
        ResponseListDto responseListDto = new ResponseListDto();
        Map param = map(requestListDto, HashMap.class);
        param.put("now", LocalDate.now());
        if(bePresent(requestListDto) && ENV.LIST_COUNT_DONE.equals(requestListDto.getNextToken())) {
            responseListDto.setNextToken(ENV.LIST_COUNT_DONE);
        }else {
            List<Map> items = getList(listPath, param);
            if(!bePresent(items)){
                responseListDto.setNextToken(ENV.LIST_COUNT_DONE);
            }else {
                String nextToken = String.valueOf(items.get(items.size() - 1).get(id));
                responseListDto.setNextToken(nextToken);
                responseListDto.setItems(items);
            }
        }
        Integer countAll = (Integer)getItem(countPath, param);
        responseListDto.setTotal(countAll);
        return responseListDto;
    }


//    protected <T> void toResponse(String to, T response){
//        messagingTemplate.convertAndSend(to, response);
//    }

}