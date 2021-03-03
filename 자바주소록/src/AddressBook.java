import java.io.*;
import java.util.ArrayList; // ArrayList를 사용하기 위한 import
import java.sql.*;

@SuppressWarnings("unused")

/*
 * 프로그램 이름 : AddressBook.java 프로그램 설명 : 클래스 AddressBook 정의 프로그램 작성일 : 2020-11-29
 * 작성자 : Jeong JiYeon
 */

public class AddressBook {

	private Connection conn = null;
	private Statement st;
	private ResultSet rs;

	// 생성자 함수
	public AddressBook() throws ClassNotFoundException, SQLException {

		Class.forName("org.mariadb.jdbc.Driver"); // JDBC 드라이버 연결
		conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/adressdb", "root", "swu");
		st = conn.createStatement();
	}

	// 등록된 사람 수 접근자
	public int getCount() throws SQLException {
		String getCountMsg = "select count(*)from adressinfo"; // table의 갯수를 세는 SQL문

		rs = st.executeQuery(getCountMsg);
		rs.next();// 커서 이동
		int perCnt = rs.getInt(1); // 갯수 읽기

		return perCnt;// 갯수 반환
	}

	// 동명이인 확인 메소드
	public boolean checkName(String name) throws Exception {
		String checkNameMsg = "select *from adressinfo"; // table 전체 출력 SQL문

		rs = st.executeQuery(checkNameMsg);
		while (rs.next()) {
			if (rs.getString("name").equals(name))// 일치하는 이름이 있다면
				return true;// true 반환
		}

		return false;// 일치하는 이름 없음
	}

	// 등록된 전화번호가 있는지 확인 메소드
	public boolean checkPhoneNum(String phoneNum) throws Exception {
		String checkNameMsg = "select *from adressinfo";// table 전체 출력 SQL문

		rs = st.executeQuery(checkNameMsg);
		while (rs.next()) {
			if (rs.getString("phoneNum").equals(phoneNum))// 일치하는 전화번호가 있다면
				return true;// true 반환
		}

		return false;// 일치하는 전화번호 없음
	}

	// 주소록 등록 메소드
	public void add(Person P) throws Exception {

		st.executeUpdate(// table 행 추가
				"insert into adressinfo (name,sex,phoneNum,adress,email) values ('" + P.getName() + "','" + P.getSex()
						+ "','" + P.getPhoneNum() + "','" + P.getAddress() + "','" + P.getEmail() + "');");

	}

	// 이름으로 주소록 번호 검색 메소드, 등록된 이름 없을 경우 익셉션
	public int searchName(String name) throws Exception {
		String searchNameMsg = "select *from adressinfo";// table 전체 출력 SQL문

		rs = st.executeQuery(searchNameMsg);
		while (rs.next()) {
			if (rs.getString("name").equals(name))// 일치하는 이름이 있으면
				return rs.getInt(name);// 해당 행을 Int로 반환
		}

		throw new Exception("등록된 name 없음");
	}

	// 전화번호로 주소록 번호 검색 메소드, 등록된 전화번호 없을 경우 익셉션
	public int searchPhoneNum(String phoneNum) throws Exception {
		String searchPhoneNumMsg = "select *from adressinfo";// table 전체 출력 SQL문

		rs = st.executeQuery(searchPhoneNumMsg);
		while (rs.next()) {
			if (rs.getString("phoneNum").equals(phoneNum))// 일치하는 전화번호가 있으면
				return rs.getInt(phoneNum);// 해당 행을 Int로 반환
		}

		throw new Exception("등록된 phoneNum 없음");
	}

	// 이름으로 주소록 번호 검색 메소드2, 입력한 문자를 포함하는 데이터를 검사하는 경우
	public Person searchNameWith(String name, int index) throws Exception {
		String searchWithMsg = "select * from adressinfo where name like '%" + name + "%';";

		rs = st.executeQuery(searchWithMsg);

		while (rs.next()) {
			if (rs.getRow() == index + 1) {
				String getName = rs.getString("name");
				String getSex = rs.getString("sex");
				String getphonNum = rs.getString("phoneNum");
				String getAdress = rs.getString("adress");
				String getEmail = rs.getString("email");

				// Person 객체 p를 생성해주어 문자 포함 데이터들의 행 값들을 넣어줌
				Person p = new Person(getName, getphonNum, getAdress, getEmail, getSex);

				return p;

			}
		}

		throw new Exception("searchNameWith 예외");
	}

	// 주소록 수정 메소드
	public void modify(String name, Person P) throws Exception {
		// table 정보 수정
		st.executeUpdate("update adressinfo set name='" + P.getName() + "', " + "sex='" + P.getSex() + "', "
				+ "phoneNum='" + P.getPhoneNum() + "', " + "adress='" + P.getAddress() + "', " + "email='"
				+ P.getEmail() + "'" + " where name='" + name + "';");
	}

	// 주소록 삭제 메소드
	public void delete(String name) throws SQLException {
		String deleteMsg = "delete from adressinfo where name ='" + name + "';";
		// table 행 삭제
		st.execute(deleteMsg);

	}

	// Person 객체 넘겨주는 메소드
	public Person getPerson(int index) throws Exception {
		String getCountMsg = "select *from adressinfo";// table 전체 정보 출력

		rs = st.executeQuery(getCountMsg);
		while (rs.next()) {
			if (rs.getRow() == index + 1) {
				String getName = rs.getString("name");
				String getSex = rs.getString("sex");
				String getphonNum = rs.getString("phoneNum");
				String getAdress = rs.getString("adress");
				String getEmail = rs.getString("email");

				// Person의 객체 p를 생성하여 모든 행들의 값들을 넣어줌
				Person p = new Person(getName, getphonNum, getAdress, getEmail, getSex);

				return p;
			}
		}

		throw new Exception("getPerson오류");

	}

	// DB를 닫아주는 메소드
	public void closeDB() throws Exception {
		rs.close();
		st.close();
		conn.close();
	}
}
