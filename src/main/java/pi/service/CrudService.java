package pi.service;

public interface CrudService {

    public String get(String app_name, String sql);
    public String getJson(String app_name, String sql);
    public String list(String app_name, String sql);
    public String listJson(String app_name, String sql);
    public int execute(String app_name, String sql) throws Exception;
    

}
