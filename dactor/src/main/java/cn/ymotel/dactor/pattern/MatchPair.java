package cn.ymotel.dactor.pattern;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchPair<T> {
    private List<String> matchPatterns;
    private String matchPattern;

    private  boolean completeMatch=false;

    public boolean isCompleteMatch() {
        return completeMatch;
    }

    public void setCompleteMatch(boolean completeMatch) {
        this.completeMatch = completeMatch;
    }

    public String getMatchPattern() {
        return matchPattern;
    }

    public void setMatchPattern(String matchPattern) {
        this.matchPattern = matchPattern;
    }

    private T bean;

    public List<String> getMatchPatterns() {
        return matchPatterns;
    }

    public void setMatchPatterns(List<String> matchPatterns) {
        this.matchPatterns = matchPatterns;
    }

    public T getBean() {
        return bean;
    }

    public void setBean(T bean) {
        this.bean = bean;
    }
    public Map convert2PatternMap(){
        Map rtnMap=new HashMap();
        if(matchPatterns ==null){
            return rtnMap;
        }
        for(int i = 0; i< matchPatterns.size(); i++){
            rtnMap.put(matchPatterns.get(i),bean);
        }
        return rtnMap;
    }

}
