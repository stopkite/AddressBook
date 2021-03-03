import java.io.*;
import java.util.ArrayList; // ArrayList�� ����ϱ� ���� import
import java.sql.*;

@SuppressWarnings("unused")

/*
 * ���α׷� �̸� : AddressBook.java ���α׷� ���� : Ŭ���� AddressBook ���� ���α׷� �ۼ��� : 2020-11-29
 * �ۼ��� : Jeong JiYeon
 */

public class AddressBook {

	private Connection conn = null;
	private Statement st;
	private ResultSet rs;

	// ������ �Լ�
	public AddressBook() throws ClassNotFoundException, SQLException {

		Class.forName("org.mariadb.jdbc.Driver"); // JDBC ����̹� ����
		conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/adressdb", "root", "swu");
		st = conn.createStatement();
	}

	// ��ϵ� ��� �� ������
	public int getCount() throws SQLException {
		String getCountMsg = "select count(*)from adressinfo"; // table�� ������ ���� SQL��

		rs = st.executeQuery(getCountMsg);
		rs.next();// Ŀ�� �̵�
		int perCnt = rs.getInt(1); // ���� �б�

		return perCnt;// ���� ��ȯ
	}

	// �������� Ȯ�� �޼ҵ�
	public boolean checkName(String name) throws Exception {
		String checkNameMsg = "select *from adressinfo"; // table ��ü ��� SQL��

		rs = st.executeQuery(checkNameMsg);
		while (rs.next()) {
			if (rs.getString("name").equals(name))// ��ġ�ϴ� �̸��� �ִٸ�
				return true;// true ��ȯ
		}

		return false;// ��ġ�ϴ� �̸� ����
	}

	// ��ϵ� ��ȭ��ȣ�� �ִ��� Ȯ�� �޼ҵ�
	public boolean checkPhoneNum(String phoneNum) throws Exception {
		String checkNameMsg = "select *from adressinfo";// table ��ü ��� SQL��

		rs = st.executeQuery(checkNameMsg);
		while (rs.next()) {
			if (rs.getString("phoneNum").equals(phoneNum))// ��ġ�ϴ� ��ȭ��ȣ�� �ִٸ�
				return true;// true ��ȯ
		}

		return false;// ��ġ�ϴ� ��ȭ��ȣ ����
	}

	// �ּҷ� ��� �޼ҵ�
	public void add(Person P) throws Exception {

		st.executeUpdate(// table �� �߰�
				"insert into adressinfo (name,sex,phoneNum,adress,email) values ('" + P.getName() + "','" + P.getSex()
						+ "','" + P.getPhoneNum() + "','" + P.getAddress() + "','" + P.getEmail() + "');");

	}

	// �̸����� �ּҷ� ��ȣ �˻� �޼ҵ�, ��ϵ� �̸� ���� ��� �ͼ���
	public int searchName(String name) throws Exception {
		String searchNameMsg = "select *from adressinfo";// table ��ü ��� SQL��

		rs = st.executeQuery(searchNameMsg);
		while (rs.next()) {
			if (rs.getString("name").equals(name))// ��ġ�ϴ� �̸��� ������
				return rs.getInt(name);// �ش� ���� Int�� ��ȯ
		}

		throw new Exception("��ϵ� name ����");
	}

	// ��ȭ��ȣ�� �ּҷ� ��ȣ �˻� �޼ҵ�, ��ϵ� ��ȭ��ȣ ���� ��� �ͼ���
	public int searchPhoneNum(String phoneNum) throws Exception {
		String searchPhoneNumMsg = "select *from adressinfo";// table ��ü ��� SQL��

		rs = st.executeQuery(searchPhoneNumMsg);
		while (rs.next()) {
			if (rs.getString("phoneNum").equals(phoneNum))// ��ġ�ϴ� ��ȭ��ȣ�� ������
				return rs.getInt(phoneNum);// �ش� ���� Int�� ��ȯ
		}

		throw new Exception("��ϵ� phoneNum ����");
	}

	// �̸����� �ּҷ� ��ȣ �˻� �޼ҵ�2, �Է��� ���ڸ� �����ϴ� �����͸� �˻��ϴ� ���
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

				// Person ��ü p�� �������־� ���� ���� �����͵��� �� ������ �־���
				Person p = new Person(getName, getphonNum, getAdress, getEmail, getSex);

				return p;

			}
		}

		throw new Exception("searchNameWith ����");
	}

	// �ּҷ� ���� �޼ҵ�
	public void modify(String name, Person P) throws Exception {
		// table ���� ����
		st.executeUpdate("update adressinfo set name='" + P.getName() + "', " + "sex='" + P.getSex() + "', "
				+ "phoneNum='" + P.getPhoneNum() + "', " + "adress='" + P.getAddress() + "', " + "email='"
				+ P.getEmail() + "'" + " where name='" + name + "';");
	}

	// �ּҷ� ���� �޼ҵ�
	public void delete(String name) throws SQLException {
		String deleteMsg = "delete from adressinfo where name ='" + name + "';";
		// table �� ����
		st.execute(deleteMsg);

	}

	// Person ��ü �Ѱ��ִ� �޼ҵ�
	public Person getPerson(int index) throws Exception {
		String getCountMsg = "select *from adressinfo";// table ��ü ���� ���

		rs = st.executeQuery(getCountMsg);
		while (rs.next()) {
			if (rs.getRow() == index + 1) {
				String getName = rs.getString("name");
				String getSex = rs.getString("sex");
				String getphonNum = rs.getString("phoneNum");
				String getAdress = rs.getString("adress");
				String getEmail = rs.getString("email");

				// Person�� ��ü p�� �����Ͽ� ��� ����� ������ �־���
				Person p = new Person(getName, getphonNum, getAdress, getEmail, getSex);

				return p;
			}
		}

		throw new Exception("getPerson����");

	}

	// DB�� �ݾ��ִ� �޼ҵ�
	public void closeDB() throws Exception {
		rs.close();
		st.close();
		conn.close();
	}
}
