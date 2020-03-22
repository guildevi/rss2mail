package rss2mail.interfaces;


public interface Database {
	public Object find(Class<?> clazz, String id) throws Exception;
	public Object find(Class<?> clazz) throws Exception;
	public Object find(String classname) throws Exception;
	public Object find(String classname, String id) throws Exception;
	public void save(Object object) throws Exception;
	public void update(Object object) throws Exception;
	public void remove(Object object) throws Exception;
}
