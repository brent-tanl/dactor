package cn.ymotel.dactor.pattern;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PathMatcher;

import javax.servlet.DispatcherType;
import java.util.*;

public class PatternMatcher<T> {
    @Nullable
    private  String[] includePatterns;
    @Nullable
    private  String[] excludePatterns;
    @Nullable
    private PathMatcher pathMatcher=null;

    private  Set<String> dispatcherTypeSet= new HashSet<>();
    private   Set<Integer> httpStatusSet= new HashSet<>();
    private Set methods=new HashSet();
    private Set serverNames=new HashSet();
    private T bean;
    public PatternMatcher(@Nullable String[] includePatterns, @Nullable String[] excludePatterns,T bean) {
//        this.includePatterns = filter(includePatterns);
//        this.excludePatterns = filter(excludePatterns);
        this(includePatterns,excludePatterns,null,null,null,null,bean);
    }
    public PatternMatcher(@Nullable List includePatterns, @Nullable List excludePatterns,T bean) {

        this((String[])includePatterns.toArray(new String[0]),(String[])excludePatterns.toArray(new String[0]),null,null,null,null,bean);

    }

    public PatternMatcher(@Nullable String[] includePatterns, @Nullable String[] excludePatterns,@Nullable String[] methods,@Nullable String[] serverNames,String[] dispatcherTypes,Integer[] httpStatus, T bean) {
        this.includePatterns = filter(includePatterns);
        this.excludePatterns = filter(excludePatterns);
        if(dispatcherTypes!=null){
            for(int i=0;i<dispatcherTypes.length;i++){
                dispatcherTypeSet.add(dispatcherTypes[i]);
            }
        }
        if(dispatcherTypeSet.isEmpty()){
            dispatcherTypeSet.add(DispatcherType.REQUEST.name());
        }
        if(httpStatus!=null){
            for(int i=0;i<httpStatus.length;i++) {
                this.httpStatusSet.add(httpStatus[i]);
            }
        }
        this.bean = bean;
        if(methods!=null){
            for(int i=0;i<methods.length;i++){
                this.methods.add(methods[i]);
            }
        }
        if(serverNames!=null){
            for(int i=0;i<serverNames.length;i++){
                this.serverNames.add(serverNames[i]);
            }
        }
    }
//    public PatternMatcher(@Nullable List includePatterns, @Nullable List excludePatterns,List methods,List serverNames, T bean) {
//        this.includePatterns = filter((String[])includePatterns.toArray(new String[0]));
//        this.excludePatterns = filter((String[])excludePatterns.toArray(new String[0]));;
//        this.bean = bean;
//        if(methods!=null){
//            for(int i=0;i<methods.size();i++){
//                this.methods.add(methods.get(i));
//            }
//        }
//        if(serverNames!=null){
//            for(int i=0;i<serverNames.size();i++){
//                this.serverNames.add(serverNames.get(i));
//            }
//        }
//
//    }
    private String[] filter(String[] array){
        if(array==null){
            return null;
        }
        List ls=new ArrayList();
        for (int i = 0; i < array.length; i++) {
            String pattern = array[i];
            if (pattern == null || pattern.trim().equals("")) {
                continue;
            }

            ls.add(pattern.trim());
        }
        return (String[]) ls.toArray(new String[0]);
    }

    public  <T> T getBean(){
        return (T) bean;
    }


    public void setPathMatcher(@Nullable PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    /**
     * Determine a match for the given lookup path.
     * @param lookupPath the current request path
     * @return {@code true} if the interceptor applies to the given request path
     */
    public MatchPair matchePatterns(String lookupPath, PathMatcher pathMatcher) {
        return matchePatterns(lookupPath,pathMatcher,null,null,null,null,null);
//        PathMatcher pathMatcherToUse = (pathMatcher == null ? this.pathMatcher : pathMatcher);
//        if (!ObjectUtils.isEmpty(this.excludePatterns)) {
//            for (String pattern : this.excludePatterns) {
//                if (pathMatcherToUse.match(pattern, lookupPath)) {
//                    return null;
//                }
//            }
//        }
//        if (ObjectUtils.isEmpty(this.includePatterns)) {
//           return null;
//        }
//        MatchPair<T> pair=new MatchPair();
//
//        List rtnList=new ArrayList();
//        for (String pattern : this.includePatterns) {
//            if(pattern.equals(lookupPath)){
//                pair.setCompleteMatch(true);
//                rtnList.clear();
//                rtnList.add(pattern);
//                break;
//            }
//            if (pathMatcherToUse.match(pattern, lookupPath)) {
//                rtnList.add(pattern);
//            }
//        }
//        pair.setBean(this.bean);
//        pair.setMatchPatterns(rtnList);
//        return pair;
    }
    /**
     * Determine a match for the given lookup path.
     * @param lookupPath the current request path
     * @return {@code true} if the interceptor applies to the given request path
     */
    public MatchPair matchePatterns(String lookupPath, PathMatcher pathMatcher,String method,String serverName,String dispatcherType,Integer httpStatus,Comparator comparator) {
        if(serverName!=null&&(!this.serverNames.isEmpty())){
            if(serverNames.contains(serverName)){}else{
                return null;
            }
        }
        if(method!=null&&(!this.methods.isEmpty())){
            if(methods.contains(method)){}else{
                return null;
            }
        }
        if(dispatcherType!=null&&(!dispatcherTypeSet.isEmpty())){
            if(dispatcherTypeSet.contains(dispatcherType)){

            }else{
                return null;
            }
        }
        if(httpStatus!=null&&(!httpStatusSet.isEmpty())){
            if(httpStatusSet.contains(httpStatus)){

            }else{
                return null;
            }
        }

        PathMatcher pathMatcherToUse = (pathMatcher == null ? this.pathMatcher : pathMatcher);
        if (!ObjectUtils.isEmpty(this.excludePatterns)) {
            for (String pattern : this.excludePatterns) {
                if (pathMatcherToUse.match(pattern, lookupPath)) {
                    return null;
                }
            }
        }

        MatchPair<T> pair=new MatchPair();


        if(!DispatcherType.ERROR.name().equals(dispatcherType)){
            List rtnList=new ArrayList();
            if(this.includePatterns!=null) {
                for (String pattern : this.includePatterns) {
                    if (pathMatcherToUse.match(pattern, lookupPath)) {
                        rtnList.add(pattern);
                    }
                }
            }
            if (rtnList.isEmpty()) {
                return null;
            }
            if(comparator!=null) {
                if (rtnList.size() > 1) {
                    rtnList.sort(comparator);
                }
            }
            if(rtnList!=null&&rtnList.size()>0) {
                pair.setMatchPattern((String) rtnList.get(0));
            }
            pair.setMatchPatterns(rtnList);
        }
        if(!methods.isEmpty()){
            pair.setMethod(method);
        }
        if(!serverNames.isEmpty()){
            pair.setServerName(serverName);
        }
//        if(!dispatcherTypeSet.isEmpty()){
            pair.setDispatcherType(dispatcherType);
//        }
        if(!httpStatusSet.isEmpty()){
            pair.setHttpStatus(httpStatus);
        }
        pair.setBean(this.bean);

        return pair;
    }
}
