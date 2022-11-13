package com.taiso.car.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;



public class CarDAO {
	private Connection con = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private String sql = "";

	// 디비 연결해주는 메서드(커넥션풀)
	private Connection getConnection() throws Exception {
		// 1. 드라이버 로드 // 2. 디비연결

		// Context 객체 생성 (JNDI API)
		Context initCTX = new InitialContext();
		// 디비연동정보 불러오기 (context.xml 파일정보)
		DataSource ds = (DataSource) initCTX.lookup("java:comp/env/jdbc/taiso");
		// 디비정보(연결) 불러오기
		con = ds.getConnection();

		System.out.println(" DAO : 디비연결 성공(커넥션풀) ");
		System.out.println(" DAO : con : " + con);

		return con;
	}// 커넥션풀 끝

	// 자원해제 메서드-closeDB()
	public void closeDB() {
		System.out.println(" DAO : 디비연결자원 해제");

		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (con != null)
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// 자원해제 메서드-closeDB()
	
	// 차정보 가져오는 메서드
	public List getCarList(String item) {
		List carsList = new ArrayList();
		StringBuffer SQL = new StringBuffer();

		CarDTO dto = null;
		try {
			con = getConnection();
			SQL.append("select * from car");
			
			if (item.equals("all")) {
				System.out.println(" DAO : all "+SQL);
			} else {
				SQL.append(" where car_category=?");
				System.out.println(" DAO : car_categroy "+SQL);
			}
			pstmt = con.prepareStatement(SQL+"");
			
			if(item.equals("all")) {
			}else {
				pstmt.setString(1, item);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
				dto = new CarDTO();

				dto.setCar_brand(rs.getString("car_brand"));
				dto.setCar_category(rs.getString("car_category"));
				dto.setCar_code(rs.getInt("car_code"));
				dto.setCar_file(rs.getString("car_file"));
				dto.setCar_fuel(rs.getString("car_fuel"));
				dto.setCar_location(rs.getInt("car_location"));
				dto.setCar_name(rs.getString("car_name"));
				dto.setCar_op(rs.getString("car_op"));
				dto.setCar_price(rs.getInt("car_price"));
				dto.setCar_year(rs.getString("car_year"));
				
//				dto.setBest(rs.getInt("best"));
//				dto.setCategory(rs.getString("category"));
//				dto.setColor(rs.getString("color"));
//				dto.setContent(rs.getString("content"));
//				dto.setDate(rs.getTimestamp("date"));
//				dto.setGno(rs.getInt("gno"));
//				dto.setImage(rs.getString("image"));
//				dto.setName(rs.getString("name"));
//				dto.setPrice(rs.getInt("price"));
//				dto.setSize(rs.getString("size"));

				carsList.add(dto);
			}
			System.out.println(" DAO : " + carsList.size());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		System.out.println(" DAO : 상품정보 조회 완료! ");
		return carsList;
	}// getCarList() 끝
	
	
}
