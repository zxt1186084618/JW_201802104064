package cn.edu.sdjzu.xg.bysj.basic.project;
import cn.edu.sdjzu.xg.bysj.domain.GraduateProject;
import cn.edu.sdjzu.xg.bysj.service.GraduateProjectService;
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

@WebServlet("/graduateProject.ctl")
public class GraduateProjectController extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置请求字符编码为UTF-8
        request.setCharacterEncoding("UTF-8");
        //根据request对象，获得代表参数的JSON字串
        String teacher_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Teacher对象
        GraduateProject graduateProjectToAdd = JSON.parseObject(teacher_json, GraduateProject.class);
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //在数据库表中增加Teacher对象
        try {
            GraduateProjectService.getInstance().add(graduateProjectToAdd);
            message.put("message", "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
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

        //到数据库表中删除对应的教师
        try {
            GraduateProjectService.getInstance().delete(id);
            message.put("message", "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
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
        String graduateProject_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Teacher对象
        GraduateProject graduateProjectToAdd = JSON.parseObject(graduateProject_json, GraduateProject.class);
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表修改Teacher对象对应的记录
        try {
            GraduateProjectService.getInstance().update(graduateProjectToAdd);
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
            //如果id = null, 表示响应所有教师对象，否则响应id指定的教师对象
            if (id_str == null) {
                responseGraduateProjects(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseGraduateProject(id, response);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            //响应message到前端
            response.getWriter().println(message);
        } catch (Exception e) {
            e.printStackTrace();
            message.put("message", "网络异常");
            //响应message到前端
            response.getWriter().println(message);
        }
    }

    //响应一个教师对象
    private void responseGraduateProject(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找教师
        GraduateProject graduateProject = GraduateProjectService.getInstance().find(id);
        String graduateProject_json = JSON.toJSONString(graduateProject);
        //响应teacher_json到前端
        response.getWriter().println(graduateProject_json);
    }

    //响应所有教师对象
    private void responseGraduateProjects(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有教师
        Collection<GraduateProject> graduateProjects = GraduateProjectService.getInstance().findAll();
        String graduateProjects_json = JSON.toJSONString(graduateProjects, SerializerFeature.DisableCircularReferenceDetect);
        //响应teachers_json到前端
        response.getWriter().println(graduateProjects_json);
    }
}
