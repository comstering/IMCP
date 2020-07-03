package Child;

public class ChildDTO {
	private String key;    //  아이 고유키
	private String name;    //  아이 이름
	private String birth;    //  아이 생년월일
	private String img;    //  이미지 저장 경로
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
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
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	
	public ChildDTO(String key, String name, String birth, String img) {
		super();
		this.key = key;
		this.name = name;
		this.birth = birth;
		this.img = img;
	}	
}
