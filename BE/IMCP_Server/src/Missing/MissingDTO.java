package Missing;

public class MissingDTO {
	private String childKey;    //  아이 식별값
	private String img;    //  아이 사진파일 경로
	private String name;    //  아이 이름
	private String birth;    //  아이 생년월일
	private String prentPhone;    //  부모 핸드폰번호
	
	public String getChildKey() {
		return childKey;
	}
	public void setChildKey(String childKey) {
		this.childKey = childKey;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getPrentPhone() {
		return prentPhone;
	}
	public void setPrentPhone(String prentPhone) {
		this.prentPhone = prentPhone;
	}

	public MissingDTO(String childKey, String img, String name, String birth, String prentPhone) {
		super();
		this.childKey = childKey;
		this.img = img;
		this.name = name;
		this.birth = birth;
		this.prentPhone = prentPhone;
	}
}
