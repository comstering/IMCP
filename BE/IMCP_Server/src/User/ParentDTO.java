package User;

public class ParentDTO {
	private String img_realfile;
	private String name;
	private String childKey;
	private String birth;
	private String filePath;
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getImg_realfile() {
		return img_realfile;
	}
	public void setImg_realfile(String img_realfile) {
		this.img_realfile = img_realfile;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getChildKey() {
		return childKey;
	}
	public void setChildKey(String childKey) {
		this.childKey = childKey;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}

}
