package Missing;

public class MissingDTO {
	private String childKey;
	private String img;
	private String name;
	private String birth;
	private String prentPhone;
	
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
	public String getAge() {
		return birth;
	}
	public void setAge(String birth) {
		this.birth = birth;
	}
	public String getPrentPhone() {
		return prentPhone;
	}
	public void setPrentPhone(String prentPhone) {
		this.prentPhone = prentPhone;
	}
	
	public MissingDTO(String childKey, String img, String name, String birth) {
		super();
		this.childKey = childKey;
		this.img = img;
		this.name = name;
		this.birth = birth;
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
