package com.taiso.admin.car.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class AdminCarDAO {
	private Connection con = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private String sql = "";
	
	// 디비 연결해주는 메서드(커넥션풀)
		  private Connection getConnection() throws Exception {
			  
			  // 1. 드라이버 로드  // 2. 디비 연결
			  
			  // Context 객체 생성 (JNDI API)-자바네이밍디렉토리인터페이스
			  Context initCTX = new InitialContext();
			  // 디비연동정보 불러오기 (context.xml 파일정보)
			  DataSource ds= (DataSource)initCTX.lookup("java:comp/env/jdbc/Project");
			  
			  // 디비정보(연결) 불러오기
			  con = ds.getConnection();
			  
			  System.out.println(" DAO : 디비연결 성공(커넥션풀) ");
			  System.out.println(" DAO : con : " + con);
			  
			  return con;
		  }
		   
		   // 자원해제 메서드 - closeDB()
		   public void closeDB() {
			   System.out.println("DAO : 디비연결자원 해제");
			   
			   try {
				if(rs!= null) rs.close();
				if(pstmt!= null) pstmt.close();
				if(con!= null) con.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		   }
		   // 자원해제 메서드 - closeDB()
	
		// 상품등록메서드 - insertCar(DTO)
		  public void insertCar(CarDTO dto) {
			  int car_code = 0;
			  try {
				con = getConnection();
				sql="select max(car_code) from car";
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					car_code = rs.getInt(1)+1;
				}
				System.out.println(" DAO : car_code : " + car_code);
				
				// 2. 상품 등록
				sql = "insert into car(car_code,car_brand,car_name,car_location,car_price,car_op,car_category,car_file,car_year,car_fuel) "
						+ "value(?,?,?,?,?,?,?,?,?,?)";
				pstmt = con.prepareStatement(sql);
				
				// ???
				pstmt.setInt(1, car_code);
				pstmt.setString(2, dto.getCar_brand());
				pstmt.setString(3, dto.getCar_name());
				pstmt.setInt(4, dto.getCar_location());
				pstmt.setInt(5, dto.getCar_price());
				pstmt.setString(6, dto.getCar_op());
				pstmt.setString(7, dto.getCar_category());
				pstmt.setString(8, dto.getCar_file());
				pstmt.setInt(9, dto.getCar_year());
				pstmt.setString(10, dto.getCar_fuel());
				
				pstmt.executeUpdate();
				
				System.out.println(" DAO : 상품등록완료 ! ");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeDB();
			}
		  }
		// 상품등록메서드 - insertCar(DTO)
		  
		// 상품 리스트 - getAdminCarList()
		  public List getAdminCarList() {
			  List adminCarList = new ArrayList();
			  try {
				con = getConnection();
				sql = "select * from car";
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					// DB(테이블) -> DTO -> List
					CarDTO dto = new CarDTO();
					
					dto.setcar_brand(rs.getString("car_brand"));
					dto.setCar_category(rs.getString("car_category"));
					dto.setCar_code(rs.getInt("car_code"));
					dto.setCar_file(rs.getString("car_file"));
					dto.setCar_fuel(rs.getString("car_fuel"));
					dto.setCar_location(rs.getInt("car_location"));
					dto.setCar_name(rs.getString("car_name"));
					dto.setCar_op(rs.getString("car_op"));
					dto.setCar_price(rs.getInt("car_price"));
					dto.setCar_year(rs.getInt("car_year"));
					
					adminCarList.add(dto);
				} // while
					System.out.println(" DAO : 관리자 상품리스트 저장완료! ");
					System.out.println(" DAO : " + adminCarList.size());
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeDB();
			}
			  return adminCarList;
		  }
		// 상품 리스트 - getAdminCarList()
   
		// 상품 1개의 정보를 가져오기 - getAdminCar(car_code)
		  public CarDTO getAdminCar(int car_code) {
			  CarDTO dto = null;
			  try {
				con = getConnection();
				sql = "select * from car where car_code=?";
				pstmt = con.prepareStatement(sql);
				
				pstmt.setInt(1, car_code);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					dto = new CarDTO();
					dto.setcar_brand(rs.getString("car_brand"));
					dto.setCar_category(rs.getString("car_category"));
					dto.setCar_code(rs.getInt("car_code"));
					dto.setCar_file(rs.getString("car_file"));
					dto.setCar_fuel(rs.getString("car_fuel"));
					dto.setCar_location(rs.getInt("car_location"));
					dto.setCar_name(rs.getString("car_name"));
					dto.setCar_op(rs.getString("car_op"));
					dto.setCar_price(rs.getInt("car_price"));
					dto.setCar_year(rs.getInt("car_year"));
				} // if
				
				System.out.println(" DAO : 상품정보 가져오기 완료! ");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeDB();
			}
			  
			  return dto;
		  }
		// 상품 1개의 정보를 가져오기 - getAdminCar(car_code)
		  
		 // DAO - 상품정보 수정메서드 adminUpdateCar(DTO)
		  public void adminUpdateCar(CarDTO dto) {
			  try {
				con = getConnection();
				sql ="update car set car_category=?, car_name=?, car_brand=?, car_price=?, car_year=?, car_fuel=?, car_op=?, car_location=? "
						+ "where car_code=?";
				pstmt = con.prepareStatement(sql);
				
				pstmt.setString(1, dto.getCar_category());
				pstmt.setString(2, dto.getCar_name());
				pstmt.setString(3, dto.getCar_brand());
				pstmt.setInt(4, dto.getCar_price());
				pstmt.setInt(5, dto.getCar_year());
				pstmt.setString(6, dto.getCar_fuel());
				pstmt.setString(7, dto.getCar_op());
				pstmt.setInt(8, dto.getCar_location());
				pstmt.setInt(9, dto.getCar_code());
				
				pstmt.executeUpdate();
				
				System.out.println(" DAO : 관리자 상품정보 수정! ");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeDB();
			}
		  }
		// DAO - 상품정보 수정메서드 adminUpdateCar(DTO)
		 
		// 상품 삭제 - adminDeleteCar()
		  public void adminDeleteCar(int car_code) {
			  try {
				con = getConnection();
				sql ="delete from car where car_code=?";
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, car_code);
				pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		// 상품 삭제 - adminDeleteGood()
}
