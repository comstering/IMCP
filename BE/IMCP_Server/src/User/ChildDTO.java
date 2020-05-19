package User;

public class ChildDTO {
	private String childKey = null;    //  아이 식별값
	private String name;    //  아이 이름
	private String birth = null;    //  아이 만 나이
	private String imgRealname;    //  아이 사진파일 경로
	
	
	public String getChildKey() {
		return childKey;
	}
	public void setChildKey(String childKey) {
		this.childKey = childKey;
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
	public String getImgRealname() {
		return imgRealname;
	}
	public void setImgRealname(String imgRealname) {
		this.imgRealname = imgRealname;
	}
	
	public ChildDTO(String childKey, String name, String imgRealname) {
		super();
		this.childKey = childKey;
		this.name = name;
		this.imgRealname = imgRealname;
	}
	public ChildDTO(String childKey, String name, String birth, String imgRealname) {
		super();
		this.childKey = childKey;
		this.name = name;
		this.birth = birth;
		this.imgRealname = imgRealname;
	}
	
	
}
