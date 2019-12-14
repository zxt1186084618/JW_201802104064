package cn.edu.sdjzu.xg.bysj.basic.projecttype;

import cn.edu.sdjzu.xg.bysj.domain.GraduateProjectType;
import cn.edu.sdjzu.xg.bysj.service.GraduateProjectTypeService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/graduateProjectType.ctl")
public class GraduateProjectTypeController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置请求字符编码为UTF-8
        request.setCharacterEncoding("UTF-8");
        //根据request对象，获得代表参数的JSON字串
        String GraduateProjectType_json = JSONUtil.getJSON(request);

        //将JSON字串解析为GraduateProjectType对象
        GraduateProjectType GraduateProjectTypeToAdd =
                JSON.parseObject(GraduateProjectType_json, GraduateProjectType.class);

        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //在数据库表中增加GraduateProjectType对象
        try {
            GraduateProjectTypeService.getInstance().add(GraduateProjectTypeToAdd);
            message.put("message", "增加成功");
        } catch (Exception e) {
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();

        //到数据库表中删除对应的学位
        try {
            GraduateProjectTypeService.getInstance().delete(id);
            message.put("message", "删除成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
        } catch (Exception e) {
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置请求字符编码为UTF-8
        request.setCharacterEncoding("UTF-8");
        String GraduateProjectType_json = JSONUtil.getJSON(request);
        //将JSON字串解析为GraduateProjectType对象
        GraduateProjectType GraduateProjectTypeToAdd = JSON.parseObject(GraduateProjectType_json, GraduateProjectType.class);
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表修改GraduateProjectType对象对应的记录
        try {
            GraduateProjectTypeService.getInstance().update(GraduateProjectTypeToAdd);
            message.put("message", "修改成功");
        } catch (Exception e) {
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //读取参数id
        String id_str = request.getParameter("id");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
            if (id_str == null) {
                responseGraduateProjectTypes(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseGraduateProjectType(id, response);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            //响应message到前端
            response.getWriter().println(message);
        } catch (Exception e) {
            message.put("message", "网络异常");
            //响应message到前端
            response.getWriter().println(message);
        }
    }
    //响应一个学位对象
    private void responseGraduateProjectType(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学位
        GraduateProjectType GraduateProjectType = GraduateProjectTypeService.getInstance().find(id);
        String GraduateProjectType_json = JSON.toJSONString(GraduateProjectType);
        //响应GraduateProjectType_json到前端
        response.getWriter().println(GraduateProjectType_json);
    }

    //响应所有学位对象
    private void responseGraduateProjectTypes(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有学位
        Collection<GraduateProjectType> GraduateProjectTypes = GraduateProjectTypeService.getInstance().findAll();
        String GraduateProjectTypes_json = JSON.toJSONString(GraduateProjectTypes, SerializerFeature.DisableCircularReferenceDetect);
        //响应GraduateProjectTypes_json到前端
        response.getWriter().println(GraduateProjectTypes_json);
    }
}
