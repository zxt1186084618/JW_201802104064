package cn.edu.sdjzu.xg.bysj.basic.login;

import cn.edu.sdjzu.xg.bysj.domain.User;
import cn.edu.sdjzu.xg.bysj.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login.ctl")
public class LoginController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从请求中获取json数据
        String user_json = JSONUtil.getJSON(request);
        //将json数据转换成对象
        User userFromReq = JSON.parseObject(user_json, User.class);
        //获取用户名和密码
        String username = userFromReq.getUsername();
        String password = userFromReq.getPassword();
        //创建JSON对象message，以便向前端响应信息
        JSONObject message = new JSONObject();
        try {
            User loggedUser = UserService.getInstance().login(username,password);
            if (loggedUser!=null){
                message.put("message","登陆成功");
                HttpSession session = request.getSession();
                //十分钟没有操作，使session失效
                session.setMaxInactiveInterval(10*60);
                session.setAttribute("currentUser",loggedUser);
                String teacher_json = JSON.toJSONString(loggedUser.getTeacher());
                response.getWriter().println(teacher_json);
                //此处应重定向到索引页（菜单页）
                return;
            }else {
                message.put("message","用户名或密码错误");
                response.getWriter().println(message);
            }
        } catch (SQLException e) {
            message.put("message","数据库操作异常");
            e.printStackTrace();
        } catch (Exception e){
            message.put("message","网络异常");
            e.printStackTrace();
        }
        //响应到message前端
        response.getWriter().println(message);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
