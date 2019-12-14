package util;

import javax.servlet.http.HttpServletRequest;

public class Helper {
    public static int getIdFromRequest(HttpServletRequest request) {
        return getIdFromRequest(request,"id");
    }
    public static int getIdFromRequest(HttpServletRequest request,String fieldName) {
        String fieldPara = request.getParameter(fieldName);
        return Integer.parseInt(fieldPara);
    }

    public static String resolve(String logicView){
        return "/WEB-INF/pages/"+logicView+".jsp";
    }
    //为字串两端加引号
    public static String quote(String str){
        return "\"" + str +"\"";
    }

    public static void main(String[] args) {
        System.out.println(Helper.quote("schools:"));
    }
}
