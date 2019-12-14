package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.User;
import cn.edu.sdjzu.xg.bysj.service.*;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;


public final class UserDao {
	//本类的一个对象引用，保存自身对象
	private static UserDao userDao=new UserDao();
	//私有的构造方法，防止其它类创建它的对象
	private UserDao(){}
	//静态方法，返回本类的惟一对象
	public static UserDao getInstance(){
		return userDao;
	}
	//改密码
	public void changePassword(User user,String newPassword) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement pstmt = connection.prepareStatement(
				"UPDATE user SET password = ? where id = ?");
		pstmt.setString(1,newPassword);
		pstmt.setInt(2,user.getId());
		pstmt.executeUpdate();
		JdbcHelper.close(pstmt,connection);
	}
	//登录方法
	public User login(String username,String password) throws SQLException {
		//声明一个user
		User user = null;
		//连接对象
		Connection connection = JdbcHelper.getConn();
		//预编译语句
		PreparedStatement pstmt = connection.prepareStatement(
				"SELECT * FROM user where username = ? AND password = ?");
		pstmt.setString(1,username);
		pstmt.setString(2,password);
		//执行返回结果集
		ResultSet resultSet = pstmt.executeQuery();
		//判断结果集是否有东西
		if (resultSet.next()){
			user = new User(
					resultSet.getInt("id"),
					resultSet.getString("username"),
					resultSet.getString("password"),
					resultSet.getDate("loginTime"),
					TeacherService.getInstance().find(resultSet.getInt("teacher_id"))
			);
		}
		//关闭资源
		JdbcHelper.close(pstmt,connection);
		return user;
	}
	//找到全部的方法
	public Collection<User> findAll() throws SQLException {
		//集合
		Collection<User> users = new TreeSet<User>();
		//连接对象
		Connection connection = JdbcHelper.getConn();
		//编译对象
		Statement stmt = connection.createStatement();
		//执行返回结果集
		ResultSet resultSet = stmt.executeQuery("select * from user");
		//遍历结果集
		while(resultSet.next()){
			//创建对象
			User user = new User(
					resultSet.getInt("id"),
					resultSet.getString("username"),
					resultSet.getString("password"),
					resultSet.getDate("loginTime"),
					TeacherService.getInstance().find(resultSet.getInt("teacher_id"))
			);
			//增加到集合中
			users.add(user);
		}
		//关闭资源
		JdbcHelper.close(stmt,connection);
		return users;
	}
	//根据id找的方法
	public User find(Integer id) throws SQLException {
		//连接对象
		Connection connection = JdbcHelper.getConn();
		//预编译
		PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM user where id = ?");
		pstmt.setInt(1,id);//赋值
		//执行返回结果集
		ResultSet resultSet = pstmt.executeQuery();
		resultSet.next();//指针下移
		User user = new User(//创建对象
				resultSet.getInt("id"),
				resultSet.getString("username"),
				resultSet.getString("password"),
				resultSet.getDate("loginTime"),
				TeacherService.getInstance().find(resultSet.getInt("teacher_id"))
		);
		//关闭资源
		JdbcHelper.close(pstmt,connection);
		//返回对象
		return user;
	}
	//根据username找到的方法
	public User findByUsername(String username) throws SQLException {
		//连接对象
		Connection connection = JdbcHelper.getConn();
		//sql语句
		String findUser_sql = "SELECT * FROM user WHERE username = ?";
		//预编译
		PreparedStatement pstmt = connection.prepareStatement(findUser_sql);
		//赋值
		pstmt.setString(1,username);
		//执行返回结果集
		ResultSet resultSet = pstmt.executeQuery();
		//指针下移
		resultSet.next();
		//创建对象
		User user = new User(
				resultSet.getInt("id"),
				resultSet.getString("username"),
				resultSet.getString("password"),
				resultSet.getDate("loginTime"),
				TeacherService.getInstance().find(resultSet.getInt("teacher_id"))
		);
		return user;
	}
	//更新方法
	public void update(User user) throws SQLException {
		//连接对象
		Connection connection = JdbcHelper.getConn();
		PreparedStatement pstmt = connection.prepareStatement(//预编译
				"UPDATE user SET username = ?,password = ?,loginTime = ?,teacher_id = ? where id = ?");
		pstmt.setString(1,user.getUsername());//赋值
		pstmt.setString(2,user.getPassword());
		pstmt.setDate(3, (Date) user.getLoginTime());
		pstmt.setInt(4,user.getTeacher().getId());
		pstmt.setInt(5,user.getId());
		//执行
		pstmt.executeUpdate();
		//关闭资源
		JdbcHelper.close(pstmt,connection);
	}
	//增加
	public void add(User user) throws SQLException {
		//sql语句
		Connection connection = JdbcHelper.getConn();
		PreparedStatement pstmt = connection.prepareStatement("INSERT INTO user(username,password,loginTime,teacher_id) VALUES" +
				" (?,?,?,?)");
        //预编译
		pstmt.setString(1, user.getUsername());
		pstmt.setString(2,user.getPassword());
		pstmt.setDate(3, (Date) user.getLoginTime());
		pstmt.setInt(4,user.getTeacher().getId());
        //执行返回影响行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("添加了 " + affectedRowNum +" 行记录");
		//关闭
		JdbcHelper.close(pstmt,connection);
	}

	public void add(Connection connection,User user) throws SQLException {
		//sql语句
		String addUser_sql = "INSERT INTO user (username,password,loginTime,teacher_id) VALUES (?,?,?,?)";
		//预编译
		PreparedStatement pstmt = connection.prepareStatement(addUser_sql);
		//复制
		pstmt.setString(1, user.getUsername());
		pstmt.setString(2,user.getPassword());
		pstmt.setDate(3, (Date) user.getLoginTime());
		pstmt.setInt(4,user.getTeacher().getId());
		//执行返回影响行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("添加了 " + affectedRowNum +" 行记录");
		//关闭
		pstmt.close();
	}
	public void delete(Integer id) throws SQLException {
		User user = this.find(id);
		this.delete(user);
	}
	//删除方法根据对象
	public void delete(User user) throws SQLException {
		//连接对象
		Connection connection = JdbcHelper.getConn();
		//sql语句
		String deleteUser_sql = "DELETE FROM user WHERE id = ?";
		//预编译
		PreparedStatement pstmt = connection.prepareStatement(deleteUser_sql);
		//赋值
		pstmt.setInt(1,user.getId());
		//执行，返回影响行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("删除了 " + affectedRowNum +" 行记录");
		//关闭资源
		JdbcHelper.close(pstmt,connection);
	}
}
