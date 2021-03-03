import java.io.*;
import java.util.*;

@SuppressWarnings("unused")

/* ���α׷� �̸� : Person.java
���α׷� ���� : Ŭ���� Person ���� ���α׷�
�ۼ��� : 2020-10-10
�ۼ��� :  Jeong JiYeon*/

public class Person { // Person Ŭ����
	
	private String name;		 // �̸� �ʵ�
	private String sex;			 // ���� �ʵ�
	private String phoneNum;	 // ��ȭ��ȣ �ʵ�
	private String address;	 	 // ���ּ� �ʵ�
	private String email;		 // �̸��� �ʵ�

	public Person(String name, String phoneNum,  String address, String email, String sex){ 
		this.name = name;
		this.phoneNum = phoneNum;
		this.sex = sex;
		this.address = address;
		this.email = email;
	}
	public Person() {
		this.name = null;
		this.sex = null;
		this.phoneNum = null;
		this.address = null;
		this.email = null;
	}
	
	public void setName(String name){ 		// �̸� ������
		this.name = name;
	}
	public void setSex(String sex){			// ���� ������
		this.sex = sex;
	}
	public void setPhoneNum(String phoneNum){ 	// ��ȭ��ȣ ������
		this.phoneNum = phoneNum;
	}
	public void setAddress(String address){ 	// ���ּ� ������
		this.address = address;
	}
	public void setEmail(String email){ 		// �̸��� ������
		this.email = email;
	}
	public String getName(){ 			// �̸� ������
		return name;
	}
	public String getSex(){ 			// ���� ������
		return sex;
	}
	public String getPhoneNum(){ 		// ��ȭ��ȣ ������
		return phoneNum;
	}
	public String getAddress(){ 		// ���ּ� ������
		return address;
	}
	public String getEmail(){ 			// �̸��� ������
		return email;
	}
	
}
