package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.GraduateProjectType;
import cn.edu.sdjzu.xg.bysj.domain.GraduateProjectType;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public final class GraduateProjectTypeDao {
	private static GraduateProjectTypeDao graduateProjectTypeDao = new GraduateProjectTypeDao();
	
	private GraduateProjectTypeDao() {}
	public static GraduateProjectTypeDao getInstance(){
		return graduateProjectTypeDao;
	}
	
	private static Collection<GraduateProjectType> graduateProjectTypes;

public Collection<GraduateProjectType> findAll() throws SQLException {
	Set<GraduateProjectType> graduateProjectCategories = new HashSet<GraduateProjectType>();
	//获得连接对象
	Connection connection = JdbcHelper.getConn();
	Statement statement = connection.createStatement();
	ResultSet resultSet = statement.executeQuery("select * from projecttype");
	//若结果集仍然有下一条记录，则执行循环体
	while (resultSet.next()){
		//以当前记录中的id,description,no,remarks值为参数，创建GraduateProjectType对象
		GraduateProjectType GraduateProjectType = new GraduateProjectType(resultSet.getInt("id"),
				resultSet.getString("description"),
				resultSet.getString("no"),
				resultSet.getString("remarks"));
		//向GraduateProjectTypes集合中添加GraduateProjectType对象
		graduateProjectCategories.add(GraduateProjectType);
	}
	//关闭资源
	JdbcHelper.close(resultSet,statement,connection);
	return graduateProjectCategories;
}

	public GraduateProjectType find(Integer id) throws SQLException{
		GraduateProjectType GraduateProjectType = null;
		Connection connection = JdbcHelper.getConn();
		String deleteGraduateProjectType_sql = "SELECT * FROM projecttype WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteGraduateProjectType_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建GraduateProjectType对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			GraduateProjectType = new GraduateProjectType(resultSet.getInt("id"),
					resultSet.getString("description"),resultSet.getString("no"),
					resultSet.getString("remarks"));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return GraduateProjectType;
	}

	public boolean update(GraduateProjectType graduateProjectType) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String updategraduateProjectType_sql = " update projecttype set description=?,no=?,remarks=? where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(updategraduateProjectType_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,graduateProjectType.getDescription());
		preparedStatement.setString(2,graduateProjectType.getNo());
		preparedStatement.setString(3,graduateProjectType.getRemarks());
		preparedStatement.setInt(4,graduateProjectType.getId());
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}

	public boolean add(GraduateProjectType GraduateProjectType) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		String addGraduateProjectType_sql = "INSERT INTO projecttype (description,no,remarks) VALUES"+" (?,?,?)";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(addGraduateProjectType_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,GraduateProjectType.getDescription());
		preparedStatement.setString(2,GraduateProjectType.getNo());
		preparedStatement.setString(3,GraduateProjectType.getRemarks());
		//执行预编译语句，获取添加记录行数并赋值给affectedRowNum
		int affectedRowNum = preparedStatement.executeUpdate();
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}
	public boolean delete(int id) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String deleteDegree_sql = "DELETE FROM projecttype WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteDegree_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		//执行预编译语句，获取删除记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}

	public static void main(String[] args) throws SQLException {
		GraduateProjectTypeDao dao = new GraduateProjectTypeDao();
		Collection<GraduateProjectType> graduateProjectCategories = dao
				.findAll();
	}

}
