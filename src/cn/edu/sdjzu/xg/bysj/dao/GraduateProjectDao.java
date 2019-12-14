package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.*;
import cn.edu.sdjzu.xg.bysj.service.*;
import util.JdbcHelper;

import java.sql.*;
import java.util.*;

public final class GraduateProjectDao {
	private static GraduateProjectDao graduateProjectDao = new GraduateProjectDao();
	static Set<GraduateProject> projects;

	private GraduateProjectDao() {
	}

	public static GraduateProjectDao getInstance() {
		return graduateProjectDao;
	}


	public Collection<GraduateProject> findAll()throws SQLException {
		Collection<GraduateProject> graduateProjects = new TreeSet<GraduateProject>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
		ResultSet resultSet = statement.executeQuery("select * from project");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			//GraduateProject，根据遍历结果中的id,description,no,remarks,school_id值
			GraduateProjectCategory graduateProjectCategory = GraduateProjectCategoryDao.getInstance().find(resultSet.getInt("ProjectCategory_id"));
			GraduateProjectType graduateProjectType = GraduateProjectTypeDao.getInstance().find(resultSet.getInt("ProjectType_id"));
			Teacher teacher = TeacherDao.getInstance().find(resultSet.getInt("Teacher_id"));
			GraduateProject graduateProject = new GraduateProject(resultSet.getInt("id"),
					resultSet.getString("title"),
					graduateProjectCategory,
					graduateProjectType,
					teacher
			);
			//向graduateProjects集合中添加GraduateProject对象
			//graduateProjectCategories.add(graduateProjectCategory);
			graduateProjects.add(graduateProject);
		}
		//关闭资源
		JdbcHelper.close(resultSet,statement,connection);
		return graduateProjects;
	}
	public boolean addGraduateProject(GraduateProject project) throws SQLException {
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//添加预编译语句
		PreparedStatement preparedStatement = connection.prepareStatement(
				"INSERT INTO project (title, ProjectCategory_id, ProjectType_id,Teacher_id) VALUES (?,?,?,?)");
		preparedStatement.setString(1,project.getTitle());
		preparedStatement.setInt(2,project.getGraduateProjectCategory().getId());
		preparedStatement.setInt(3,project.getGraduateProjectType().getId());
		preparedStatement.setInt(4,project.getTeacher().getId());
		//执行预编译对象的executeUpdate()方法，获得删除行数
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println("添加了"+ affectedRowNum + "条记录");
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum > 0;
	}

	public boolean update(GraduateProject graduateProject) throws SQLException,ClassNotFoundException{
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(
				"UPDATE project SET title=? WHERE id=?");
		//为预编译参数赋值
		preparedStatement.setString(1,graduateProject.getTitle());
		preparedStatement.setInt(2,graduateProject.getId());
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println("修改了"+ affectedRowNum + "条记录");
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum > 0;
	}

	public GraduateProject find(Integer id) throws SQLException {
		GraduateProject graduateProject = null;
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(
				"SELECT * FROM project WHERE id=?");
		//为预编译参数赋值
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks,school值为参数，创建对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()) {
			graduateProject = new GraduateProject(resultSet.getInt("id"),
					resultSet.getString("title"),
					GraduateProjectCategoryService.getInstance().find(resultSet.getInt("id")),
					GraduateProjectTypeService.getInstance().find(resultSet.getInt("id")),
					TeacherService.getInstance().find(resultSet.getInt("id")));
			//关闭资源
		}
			JdbcHelper.close(resultSet, preparedStatement, connection);
			return graduateProject;
	}

		public boolean delete (Integer id) throws SQLException {
			Connection connection = JdbcHelper.getConn();
			//在该连接上创建预编译语句对象
			PreparedStatement preparedStatement = connection.prepareStatement(
					"DELETE FROM project WHERE id=?");
			//为预编译参数赋值
			preparedStatement.setInt(1, id);
			//执行预编译语句，获取删除记录行数并赋值给affectedRowNum
			int affectedRows = preparedStatement.executeUpdate();
			System.out.println("删除了" + affectedRows + "条记录");
			//关闭资源
			JdbcHelper.close(preparedStatement, connection);
			return affectedRows > 0;
		}
	}