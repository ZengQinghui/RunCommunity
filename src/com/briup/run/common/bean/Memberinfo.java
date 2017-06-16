package com.briup.run.common.bean;

import java.util.Date;

public class Memberinfo implements java.io.Serializable {

	private Long id;
	private Graderecord graderecord;
	private String nickname;
	private String password;
	private String gender;
	private Long age;
	private String email;
	private String provincecity;
	private String address;
	private String phone;
	private String passwordquestion;
	private String passwordanswer;
	private String recommender;
	private Long point;
	private Date registerdate;
	private Date latestdate;
	private Long status;
	private Long isonline;
	private Memberspace memberSpace;

	public Memberinfo() {
	}

	public Memberinfo(String nickname, String password, String gender, Long age, String email) {
		this.nickname = nickname;
		this.password = password;
		this.gender = gender;
		this.age = age;
		this.email = email;
	}

	public Memberinfo(Graderecord graderecord, String nickname, String password, String gender, Long age, String email,
			String provincecity, String address, String phone, String passwordquestion, String passwordanswer,
			String recommender, Long point, Date registerdate, Date latestdate, Long status, Long isonline,
			Memberspace memberSpace) {
		this.graderecord = graderecord;
		this.nickname = nickname;
		this.password = password;
		this.gender = gender;
		this.age = age;
		this.email = email;
		this.provincecity = provincecity;
		this.address = address;
		this.phone = phone;
		this.passwordquestion = passwordquestion;
		this.passwordanswer = passwordanswer;
		this.recommender = recommender;
		this.point = point;
		this.registerdate = registerdate;
		this.latestdate = latestdate;
		this.status = status;
		this.isonline = isonline;
		this.memberSpace = memberSpace;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Graderecord getGraderecord() {
		return this.graderecord;
	}

	public void setGraderecord(Graderecord graderecord) {
		this.graderecord = graderecord;
	}

	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Long getAge() {
		return this.age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProvincecity() {
		return this.provincecity;
	}

	public void setProvincecity(String provincecity) {
		this.provincecity = provincecity;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPasswordquestion() {
		return this.passwordquestion;
	}

	public void setPasswordquestion(String passwordquestion) {
		this.passwordquestion = passwordquestion;
	}

	public String getPasswordanswer() {
		return this.passwordanswer;
	}

	public void setPasswordanswer(String passwordanswer) {
		this.passwordanswer = passwordanswer;
	}

	public String getRecommender() {
		return this.recommender;
	}

	public void setRecommender(String recommender) {
		this.recommender = recommender;
	}

	public Long getPoint() {
		return this.point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	public Date getRegisterdate() {
		return this.registerdate;
	}

	public void setRegisterdate(Date registerdate) {
		this.registerdate = registerdate;
	}

	public Date getLatestdate() {
		return this.latestdate;
	}

	public void setLatestdate(Date latestdate) {
		this.latestdate = latestdate;
	}

	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getIsonline() {
		return this.isonline;
	}

	public void setIsonline(Long isonline) {
		this.isonline = isonline;
	}

	public Memberspace getMemberSpace() {
		return memberSpace;
	}

	public void setMemberSpace(Memberspace memberSpace) {
		this.memberSpace = memberSpace;
	}

	@Override
	public String toString() {
		return "Memberinfo [id=" + id + ", graderecord=" + graderecord + ", nickname=" + nickname + ", password="
				+ password + ", gender=" + gender + ", age=" + age + ", email=" + email + ", provincecity="
				+ provincecity + ", address=" + address + ", phone=" + phone + ", passwordquestion=" + passwordquestion
				+ ", passwordanswer=" + passwordanswer + ", recommender=" + recommender + ", point=" + point
				+ ", registerdate=" + registerdate + ", latestdate=" + latestdate + ", status=" + status + ", isonline="
				+ isonline + ", memberSpace=" + memberSpace + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((graderecord == null) ? 0 : graderecord.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isonline == null) ? 0 : isonline.hashCode());
		result = prime * result + ((latestdate == null) ? 0 : latestdate.hashCode());
		result = prime * result + ((memberSpace == null) ? 0 : memberSpace.hashCode());
		result = prime * result + ((nickname == null) ? 0 : nickname.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((passwordanswer == null) ? 0 : passwordanswer.hashCode());
		result = prime * result + ((passwordquestion == null) ? 0 : passwordquestion.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((point == null) ? 0 : point.hashCode());
		result = prime * result + ((provincecity == null) ? 0 : provincecity.hashCode());
		result = prime * result + ((recommender == null) ? 0 : recommender.hashCode());
		result = prime * result + ((registerdate == null) ? 0 : registerdate.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Memberinfo other = (Memberinfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nickname == null) {
			if (other.nickname != null)
				return false;
		} else if (!nickname.equals(other.nickname))
			return false;
		return true;
	}

}